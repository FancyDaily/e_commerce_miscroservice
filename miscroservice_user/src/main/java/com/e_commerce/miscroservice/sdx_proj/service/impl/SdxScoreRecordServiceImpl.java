package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookInfoDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxScoreRecordDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxUserDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookOrderEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxScoreRecordEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxScoreRecordService;
import com.e_commerce.miscroservice.sdx_proj.vo.SdxScoreRecordVo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxScoreRecordVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
* 积分流水的service层
*
*/

@Service
@Log
public class SdxScoreRecordServiceImpl implements SdxScoreRecordService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private SdxScoreRecordDao sdxScoreRecordDao;
    @Autowired
	private SdxUserDao sdxUserDao;
	@Autowired
    private SdxBookInfoDao sdxBookInfoDao;
    @Override
    public long modTSdxScoreRecord(TSdxScoreRecordPo tSdxScoreRecordPo) {
        if (tSdxScoreRecordPo == null) {
            log.warn("操作积分流水参数为空");
            return ERROR_LONG;
        }
        if (tSdxScoreRecordPo.getId() == null) {
            log.info("start添加积分流水={}", tSdxScoreRecordPo);
            int result = sdxScoreRecordDao.saveTSdxScoreRecordIfNotExist(tSdxScoreRecordPo);
            return result != 0 ? tSdxScoreRecordPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改积分流水={}", tSdxScoreRecordPo.getId());
            return sdxScoreRecordDao.modTSdxScoreRecord(tSdxScoreRecordPo);
        }
    }
    @Override
    public int delTSdxScoreRecordByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除积分流水,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},积分流水", Arrays.asList(ids));
        return sdxScoreRecordDao.delTSdxScoreRecordByIds(ids);
    }
    @Override
    public TSdxScoreRecordVo findTSdxScoreRecordById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找积分流水,所传Id不符合规范");
            return new TSdxScoreRecordVo();
        }
        log.info("start根据Id={}查找积分流水", id);
        TSdxScoreRecordPo tSdxScoreRecordPo = sdxScoreRecordDao.findTSdxScoreRecordById(id);
        return tSdxScoreRecordPo==null?new TSdxScoreRecordVo():tSdxScoreRecordPo.copyTSdxScoreRecordVo() ;
    }
    @Override
    public List<TSdxScoreRecordVo> findTSdxScoreRecordByAll(TSdxScoreRecordPo tSdxScoreRecordPo,Integer page, Integer size) {
        List    <TSdxScoreRecordVo> tSdxScoreRecordVos = new ArrayList<>();
        if (tSdxScoreRecordPo == null) {
            log.warn("根据条件查找积分流水,参数不对");
            return tSdxScoreRecordVos;
        }
        log.info("start根据条件查找积分流水={}", tSdxScoreRecordPo);
        List        <TSdxScoreRecordPo> tSdxScoreRecordPos = sdxScoreRecordDao.findTSdxScoreRecordByAll(            tSdxScoreRecordPo,page,size);
        for (TSdxScoreRecordPo po : tSdxScoreRecordPos) {
            tSdxScoreRecordVos.add(po.copyTSdxScoreRecordVo());
        }
        return tSdxScoreRecordVos;
    }

    @Override
	public void dealWithScoreOutTypeBARN(TCsqUser csqUser, Integer forSaleScore) {
		//消耗积分 -> 使用消耗积分逻辑，涉及积分消耗等                                                                                                                                                                                                                                                                                                                               插入流水等。
		csqUser.setSdxScores(csqUser.getSdxScores() - forSaleScore);
		sdxUserDao.update(csqUser);
		//支出流水
		sdxScoreRecordDao.saveTSdxScoreRecordIfNotExist(TSdxScoreRecordPo.builder()
			.userId(csqUser.getId())
			.inOrOut(SdxScoreRecordEnum.IN_OUT_OUT.getCode())
			.amount(forSaleScore)
			.type(SdxScoreRecordEnum.TYPE_AFTER_READING_NOTE.getCode())
			.build()
		);
	}

	@Override
	public QueryResult list(Long userId, Integer pageNum, Integer pageSize, Integer option) {
		final int OPTION_ALL = -1;
		final int OPTION_IN = SdxScoreRecordEnum.IN_OUT_IN.getCode();
		final int OPTION_OUT = SdxScoreRecordEnum.IN_OUT_OUT.getCode();

		List<SdxScoreRecordVo> vos;
		List<TSdxScoreRecordPo> sdxScoreRecordPos = sdxScoreRecordDao.selectByUserIdAndInOutPage(userId, option == -1? null: option, pageNum, pageSize);
		vos = sdxScoreRecordPos.stream()
			.map(a -> {
				String bookInfoIds = a.getBookInfoIds();
				String description = a.getDescription();
				if(bookInfoIds != null) {
					List<Long> bookInfoIdList = Arrays.asList(StringUtil.splitString(bookInfoIds, ",")).stream().map(Long::valueOf).collect(Collectors.toList());
					List<TSdxBookInfoPo> bookInfoPos = sdxBookInfoDao.selectInIds(bookInfoIdList);
					String bookInfoNames = bookInfoPos.stream().map(TSdxBookInfoPo::getName).reduce("", (b, c) -> b + "," + c).substring(1);
					String act = SdxScoreRecordEnum.IN_OUT_IN.getCode() == a.getInOrOut()? "捐赠了 ": "购买了" + bookInfoNames;
					description = StringUtil.isEmpty(description)? act: description;
				}
				SdxScoreRecordVo vo = a.copySdxScoreRecordVo();
				String dateStr = a.getCreateTime().toString();
				vo.setDate(formatDateToMdHm(dateStr));    //日期
				vo.setDescription(description);	  //TODO 描述部分
				return vo;
			}).collect(Collectors.toList());
		//排序
		vos = vos.stream()
			.sorted(Comparator.comparing(SdxScoreRecordVo::getTimeStamp).reversed()).collect(Collectors.toList());
		return PageUtil.buildQueryResult(vos);
	}

	@Override
	public void dealWithScoreOut(TSdxBookOrderPo order) {
		//积分消耗 ——> 积分减少(订单未成功在之后再考虑返还)
		Long userId = order.getUserId();
		TCsqUser csqUser = sdxUserDao.selectByPrimaryKey(userId);
		int surplusScores = csqUser.getSdxScores() - order.getScoreDiscount();
		csqUser.setSdxScores(surplusScores);
		sdxUserDao.update(csqUser);
		//插入流水
		//TODO
		sdxScoreRecordDao.saveTSdxScoreRecordIfNotExist(TSdxScoreRecordPo.builder()
			.userId(csqUser.getId())
			.inOrOut(SdxScoreRecordEnum.IN_OUT_OUT.getCode())
			.amount(order.getScoreDiscount())
			.money(order.getTotalPrice())
			.type(SdxScoreRecordEnum.TYPE_AFTER_READING_NOTE.getCode())
			.bookInfoIds(order.getBookIfIs())
			.orderId(order.getId())
			.build()
		);
	}

	@Override
	public void earnScores(Long userId, TSdxBookOrderPo order) {
		Integer type = order.getType();
		checkTypeIn(type);
		//余额变动
		TCsqUser user = sdxUserDao.selectByPrimaryKey(userId);
		Integer sdxScores = user.getSdxScores();
		Integer scores = order.getExpectedTotalScores();
		//获取书本总价，一比一兑换成积分
		String bookIfIs = order.getBookIfIs();
		List<Long> bookInfoIds = StringUtil.splitToArray(bookIfIs);
		List<TSdxBookInfoPo> sdxBookInfoPos = sdxBookInfoDao.selectInIds(bookInfoIds);
		scores = sdxBookInfoPos.stream()
			.map(TSdxBookInfoPo::getPrice)
			.reduce(0d, Double::sum).intValue();
		user.setSdxScores(sdxScores + scores);
		sdxUserDao.update(user);
		// 插入流水 -> 1条 用户获取积分流水
		String description = "捐赠书籍";
		TSdxScoreRecordPo build = TSdxScoreRecordPo.builder()
			.userId(userId)
			.amount(scores)
			.orderId(order.getId())
			.type(type)
			.inOrOut(SdxScoreRecordEnum.IN_OUT_IN.getCode())
			.bookInfoIds(bookIfIs)
			.bookIds(order.getBookIds())
//			.bookAfterReadingNoteId()	//TODO 读后感编号??
			.description(description)		//描述
			.build();
//			.date()		//trasient
		sdxScoreRecordDao.save(build);
	}

	private void checkTypeIn(Integer type) {
		if(SdxBookOrderEnum.TYPE_PURCHASE.getCode() == type) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "错误的赚取类型！");
	}

	private static String formatDateToMdHm(String dateStr) {
		return dateStr.substring(5, dateStr.length() - 3).replace("-", "/");
	}

}
