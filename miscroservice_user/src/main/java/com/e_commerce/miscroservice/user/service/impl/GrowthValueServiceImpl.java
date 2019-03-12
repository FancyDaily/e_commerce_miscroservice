package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TTypeRecord;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.enums.application.GrowthValueEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.user.dao.TypeRecordDao;
import com.e_commerce.miscroservice.user.service.GrowthValueService;
import com.e_commerce.miscroservice.user.vo.SingleGrowthValueView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GrowthValueServiceImpl implements GrowthValueService {

    @Autowired
    private TypeRecordDao typeRecordDao;

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
     * 增加成长值记录
     * @param inviter
     * @param code
     * @return
     */
    @Override
    public TUser addGrowthValue(TUser inviter, int code) {
        return null;
    }
}
