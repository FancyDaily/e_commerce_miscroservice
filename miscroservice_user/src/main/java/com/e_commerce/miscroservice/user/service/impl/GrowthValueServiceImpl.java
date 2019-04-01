package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TTypeRecord;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.application.GrowthValueEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.SnowflakeIdWorker;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.user.dao.TypeRecordDao;
import com.e_commerce.miscroservice.user.dao.UserDao;
import com.e_commerce.miscroservice.user.service.GrowthValueService;
import com.e_commerce.miscroservice.user.service.UserService;
import com.e_commerce.miscroservice.user.vo.SingleGrowthValueView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GrowthValueServiceImpl implements GrowthValueService {

    @Autowired
    private TypeRecordDao typeRecordDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private SnowflakeIdWorker idGenerator = new SnowflakeIdWorker();

    /**
     * 成长值记录明细
     * @param user
     * @param ymString
     * @param option
     * @return
     */
    @Override
    public Map<String, Object> scoreList(TUser user, String ymString, String option) {
        // id
        Long id = user.getId();

        // 结果
        List<SingleGrowthValueView> resultList = new ArrayList<SingleGrowthValueView>();

        // 判空
        if (StringUtil.isEmpty(ymString)) {
            ymString = DateUtil.timeStamp2Date(System.currentTimeMillis());
        }

        if (!StringUtil.isEmpty(ymString) && !ymString.contains("-")) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "日期参数格式不正确!");
        }

        // 当前月份
        String[] split = ymString.split("-");
        String month = split[1].toString();

        // 处理请求参数 ymString
        Map<String, Object> map = DateUtil.ym2BetweenStamp(ymString);
        String beginStr = (String) map.get("begin");
        String endStr = (String) map.get("end");
        Long begin = Long.valueOf(beginStr);
        Long end = Long.valueOf(endStr);

        // 返回结果
        List<TTypeRecord> totalList = typeRecordDao.selectByUserIdBetween(id,begin,end);


        // inList
        List<Integer> in = new ArrayList<Integer>();
        // outList
        List<Integer> out = new ArrayList<Integer>();

        for (GrowthValueEnum d : GrowthValueEnum.values()) {
            if (d.getInOut() == 1) {
                in.add(d.getCode());
            }
            if (d.getInOut() == 0) {
                out.add(d.getCode());
            }
        }

        // 计算月度总计，并分组：收入、支出
        List<SingleGrowthValueView> inList = new ArrayList<SingleGrowthValueView>();
        List<SingleGrowthValueView> outList = new ArrayList<SingleGrowthValueView>();
        Long totalIn = 0L;
        Long totalOut = 0L;

        // 筛选数据、统计总和
        for (TTypeRecord record : totalList) {
            SingleGrowthValueView view = BeanUtil.copy(record, SingleGrowthValueView.class);
            view.setIdString(String.valueOf(view.getId()));	//String化

            view.setDate(DateUtil.timeStamp2Date(record.getCreateTime()));
            Integer type = record.getType();
            for (GrowthValueEnum payType : GrowthValueEnum.values()) {
                if (type.equals(payType.getCode())) {
                    if (in.contains(type)) { // 收入
                        resultList.add(view);
                        totalIn += record.getNum();
                        inList.add(view);
                    }
                    if (out.contains(type)) { // 支出
                        view.setNum(-view.getNum());
                        resultList.add(view);
                        totalOut += record.getNum();
                        outList.add(view);
                    }
                    /* view.setTitle(payType.getMessage()); //根据type计算title */
                    break;
                }
            }
        }

        if (StringUtil.equals(AppConstant.PAYMENTS_OPTION_IN, option)) { // 收入
            resultList = inList;
        }

        if (StringUtil.equals(AppConstant.PAYMENTS_OPTION_OUT, option)) { // 收入
            resultList = outList;
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        Long growthValue = user.getGrowthValue();

        //排序
        Collections.sort(resultList, new Comparator<SingleGrowthValueView>() {

            @Override
            public int compare(SingleGrowthValueView o1, SingleGrowthValueView o2) {
                return (int) (o2.getCreateTime() - o1.getCreateTime());
            }

        });

        resultMap.put("total", growthValue);
        resultMap.put("month", month);
        resultMap.put("monthTotalIn", totalIn);
        resultMap.put("monthTotalOut", totalOut);
        resultMap.put("monthList", resultList);

        return resultMap;
        // END------------
    }

    /**
     * 增加成长值记录 //TODO 由于权益不确定是否保留，生日加倍板块待定
     * @param user
     * @param type
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public TUser addGrowthValue(TUser user, int type) {
        // 判空
        if (user == null) {
            throw new MessageException(AppErrorConstant.INCOMPLETE_PARAM, "用户为空!");
        }

        // 日期信息
        String today = DateUtil.timeStamp2Date(System.currentTimeMillis());
        Map<String, Object> ym2BetweenStamp = DateUtil.ym2BetweenStamp(today);
        Long beginStamp = Long.valueOf((String)ym2BetweenStamp.get("begin"));
        Long endStamp = Long.valueOf((String) ym2BetweenStamp.get("end"));

       /*
       //判断是否为生日
        Long birth = user.getBirthday();
        String birthDay = "";
        if(birth!=null) {
            String birthStr = String.valueOf(birth);
            birthDay = birthStr.substring(0,4) + "-" + birthStr.substring(4,6) + "-" + birthStr.substring(6,8); //生日格式
        }
        boolean eaquals = today.equals(birthDay);
        */

        // 成长值明细标题
        String title = "";

        // 获取的成长值
        long growthNum = 0L;
        Integer inOut = 0; // 扣减
        Integer rate = AppConstant.GROWTH_VALUE_RATE_BIRTHDAY; // 倍率

        // 获取当日成长值收入记录
        List<TTypeRecord> records = typeRecordDao.selectIncomeByUserIdBetween(user.getId(),beginStamp,endStamp);
        Long dayTotal = 0l;

        //统计当日各类型总收入
        for(TTypeRecord record:records) {
            if(record.getType().equals(type)) {
                dayTotal += record.getNum();
            }
        }

        // 根据type获取得到的成长值
        for (GrowthValueEnum growthEnum : GrowthValueEnum.values()) {
            if (growthEnum.getCode() == type) {
                Integer maxIn = growthEnum.getMaxIn();

                /*
                //判断是否加倍
                if(eaquals) {
                    maxIn = maxIn * rate;
                }
                */

                // 判断是否达到上限
                if(dayTotal > maxIn) {
                    return user;	//结束
                }
                growthNum = growthEnum.getPrice();
                inOut = growthEnum.getInOut();
                title = growthEnum.getMessage();
            }
        }

        /*
        // 生日成长值加倍
        if (eaquals) {
            growthNum = growthNum * rate;
        }
        */

        // 计算成长值
        Long growthValue = user.getGrowthValue();
        Long result = growthValue;
        if (inOut.equals(0)) {
            growthNum = -growthNum;
        }

        result = growthValue + growthNum;
        user.setGrowthValue(result);
//        user = userService.levelUp(user);	//升级并更新 LEVEL_UP REMARK
        //TODO 更新数据库
        userDao.updateByPrimaryKey(user);

        // 插入成长值明细
        TTypeRecord record = new TTypeRecord();
        record.setUserId(user.getId());
        record.setType(type);
        record.setSubType(0);
        record.setTitle(title);
        record.setContent(title);
        record.setNum(growthNum);
        // updater & creater
        record.setCreateTime(System.currentTimeMillis());
        record.setCreateUser(user.getId());
        record.setCreateUserName(user.getName());
        record.setUpdateTime(System.currentTimeMillis());
        record.setUpdateUser(user.getId());
        record.setUpdateUserName(user.getName());
        record.setSubType(0);

        record.setIsValid(AppConstant.IS_VALID_YES);
        typeRecordDao.insert(record);

        return user;
    }

    /**
     * 查找日常成长值记录
     * @param id
     */
    @Override
    public List<TTypeRecord> findOnesDailyGrowthRecords(Long id) {
        return typeRecordDao.selectDailyGrowthRecords(id);
    }

    /**
     * 查找所有成长流水
     * @param id
     * @return
     */
    @Override
    public List<TTypeRecord> findOnesGrowthRecords(Long id) {
        return typeRecordDao.selectGrowthRecords(id);
    }

}
