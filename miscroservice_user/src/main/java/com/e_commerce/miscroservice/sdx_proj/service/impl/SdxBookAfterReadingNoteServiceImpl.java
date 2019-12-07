package com.e_commerce.miscroservice.sdx_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.*;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookAfterReadingNoteUserEnum;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookTransRecordEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNotePo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNoteUserPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTransRecordPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookAfterReadingNoteService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 书籍读后感的service层
 */

@Service
@Log
public class SdxBookAfterReadingNoteServiceImpl implements SdxBookAfterReadingNoteService {
	private final long ERROR_LONG = 0L;
	private final int ERROR_INT = 0;
	@Autowired
	private SdxBookAfterReadingNoteDao sdxBookAfterReadingNoteDao;
	@Autowired
	private SdxBookAfterReadingNoteUserDao sdxBookAfterReadingNoteUserDao;
	@Autowired
	private SdxUserDao sdxUserDao;
	@Autowired
	private SdxScoreRecordDao sdxScoreRecordDao;
	@Autowired
	private SdxScoreRecordServiceImpl tSdxScoreRecordService;
	@Autowired
	private SdxBookTransRecordDao sdxBookTransRecordDao;
	@Override
	public long modTSdxBookAfterReadingNote(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo) {
		if (tSdxBookAfterReadingNotePo == null) {
			log.warn("操作书籍读后感参数为空");
			return ERROR_LONG;
		}
		if (tSdxBookAfterReadingNotePo.getId() == null) {
			log.info("start添加书籍读后感={}", tSdxBookAfterReadingNotePo);
			//加入一条漂流日记
			//确认我是第几任主人
			List<TSdxBookTransRecordPo> transRecordPos = sdxBookTransRecordDao.selectByBookInfoIdAndType(tSdxBookAfterReadingNotePo.getBookInfoId(), SdxBookTransRecordEnum.TYPE_BECOME_OWNER.getCode());
			int num = transRecordPos == null? 1 : transRecordPos.size() + 1;
			int result = sdxBookAfterReadingNoteDao.saveTSdxBookAfterReadingNoteIfNotExist(tSdxBookAfterReadingNotePo);

			TSdxBookTransRecordPo po = TSdxBookTransRecordPo.builder()
				.type(SdxBookTransRecordEnum.TYPE_AFTER_READING_NOTE.getCode())
				.bookAfterReadingNoteId(tSdxBookAfterReadingNotePo.getId())
				.userId(tSdxBookAfterReadingNotePo.getUserId())
				.bookInfoId(tSdxBookAfterReadingNotePo.getBookInfoId())
				.bookId(tSdxBookAfterReadingNotePo.getBookId())
				.description("发表了读后感")
				.build();
			sdxBookTransRecordDao.saveTSdxBookTransRecordIfNotExist(po);
			return result != 0 ? tSdxBookAfterReadingNotePo.getId() : ERROR_LONG;
		} else {
			log.info("start修改书籍读后感={}", tSdxBookAfterReadingNotePo.getId());
			return sdxBookAfterReadingNoteDao.modTSdxBookAfterReadingNote(tSdxBookAfterReadingNotePo);
		}
	}

	@Override
	public int delTSdxBookAfterReadingNoteByIds(Long... ids) {
		if (ids == null || ids.length == 0) {
			log.warn("删除书籍读后感,ids集合为空");
			return ERROR_INT;
		}
		log.warn("start删除Id集合={},书籍读后感", Arrays.asList(ids));
		return sdxBookAfterReadingNoteDao.delTSdxBookAfterReadingNoteByIds(ids);
	}

	@Override
	public TSdxBookAfterReadingNoteVo findTSdxBookAfterReadingNoteById(Long id) {
		if (id == null || id <= 0L) {
			log.warn("根据Id查找书籍读后感,所传Id不符合规范");
			return new TSdxBookAfterReadingNoteVo();
		}
		log.info("start根据Id={}查找书籍读后感", id);
		TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo = sdxBookAfterReadingNoteDao.findTSdxBookAfterReadingNoteById(id);
		TSdxBookAfterReadingNoteVo vo = tSdxBookAfterReadingNotePo == null ? null : tSdxBookAfterReadingNotePo.copyTSdxBookAfterReadingNoteVo();
		if (vo == null) return new TSdxBookAfterReadingNoteVo();
		Long userId = vo.getUserId();
		if (userId != null) {
			TCsqUser csqUser = sdxUserDao.selectByPrimaryKey(userId);
			if (csqUser != null) {
				vo.setUserName(csqUser.getName());
				vo.setHeadPic(csqUser.getUserHeadPortraitPath());
			}
		}
		//查看是否点赞或者点踩 -> 查找点赞关系
		TSdxBookAfterReadingNoteUserPo bookAfterReadingNoteUserPo = sdxBookAfterReadingNoteUserDao.selectByBookAfrnIdAndUserIdAndIsThumbAndType(id, userId, SdxBookAfterReadingNoteUserEnum.IS_THUMB_YES.getCode(), SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode());
		if (bookAfterReadingNoteUserPo != null) {
			vo.setIsThumb(bookAfterReadingNoteUserPo.getIsThumb());
			vo.setThumbType(bookAfterReadingNoteUserPo.getThumbType());
		}

		vo.setDate(DateUtil.timeStamp2Date(tSdxBookAfterReadingNotePo.getCreateTime().getTime()));

		return vo;
	}

	@Override
	public List<TSdxBookAfterReadingNoteVo> findTSdxBookAfterReadingNoteByAll(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo, Integer page, Integer size) {
		List<TSdxBookAfterReadingNoteVo> tSdxBookAfterReadingNoteVos = new ArrayList<>();
		if (tSdxBookAfterReadingNotePo == null) {
			log.warn("根据条件查找书籍读后感,参数不对");
			return tSdxBookAfterReadingNoteVos;
		}
		log.info("start根据条件查找书籍读后感={}", tSdxBookAfterReadingNotePo);
		Long currentUserId = tSdxBookAfterReadingNotePo.getUserId();
		tSdxBookAfterReadingNotePo.setUserId(null);
		List<TSdxBookAfterReadingNotePo> tSdxBookAfterReadingNotePos = sdxBookAfterReadingNoteDao.findTSdxBookAfterReadingNoteByAll(tSdxBookAfterReadingNotePo, page, size);
		List<Long> userIds = tSdxBookAfterReadingNotePos.stream()
			.map(TSdxBookAfterReadingNotePo::getUserId).collect(Collectors.toList());
		Map<Long, List<TCsqUser>> idUserMap = sdxUserDao.selectInIds(userIds).stream().collect(Collectors.groupingBy(TCsqUser::getId));
		//查询购买状态
		for (TSdxBookAfterReadingNotePo po : tSdxBookAfterReadingNotePos) {
			List<TSdxBookAfterReadingNoteUserPo> tSdxBookAfterReadingNoteUserPos = sdxBookAfterReadingNoteUserDao.selectByBookAfrnIdAndUserIdAndTypeAndIsPurchase(po.getId(), currentUserId, SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode(), SdxBookAfterReadingNoteUserEnum.IS_PURCHASE_YES.getCode());
			if (!tSdxBookAfterReadingNoteUserPos.isEmpty()) po.setNoNeedBuy(true);
			TSdxBookAfterReadingNoteVo tSdxBookAfterReadingNoteVo = po.copyTSdxBookAfterReadingNoteVo();
			Long userId = po.getUserId();
			List<TCsqUser> tCsqUsers = idUserMap.get(userId);
			if (tCsqUsers != null) {
				TCsqUser csqUser = tCsqUsers.get(0);
				tSdxBookAfterReadingNoteVo.setHeadPic(csqUser.getUserHeadPortraitPath());
				tSdxBookAfterReadingNoteVo.setUserName(csqUser.getName());
			}

			//查看是否点赞或者点踩 -> 查找点赞关系
			TSdxBookAfterReadingNoteUserPo bookAfterReadingNoteUserPo = sdxBookAfterReadingNoteUserDao.selectByBookAfrnIdAndUserIdAndIsThumbAndType(po.getId(), userId, SdxBookAfterReadingNoteUserEnum.IS_THUMB_YES.getCode(), SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode());
			if (bookAfterReadingNoteUserPo != null) {
				tSdxBookAfterReadingNoteVo.setIsThumb(bookAfterReadingNoteUserPo.getIsThumb());
				tSdxBookAfterReadingNoteVo.setThumbType(bookAfterReadingNoteUserPo.getThumbType());
			}
			tSdxBookAfterReadingNoteVo.setDate(DateUtil.timeStamp2Date(po.getCreateTime().getTime()));

			tSdxBookAfterReadingNoteVos.add(tSdxBookAfterReadingNoteVo);
		}
		return tSdxBookAfterReadingNoteVos;
	}

	@Override
	public void buy(Long bookArnId, Long userId) {
		TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo = sdxBookAfterReadingNoteDao.selectByPrimaryKey(bookArnId);
		if (tSdxBookAfterReadingNotePo == null) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "读后感不存在！");
		TCsqUser csqUser = sdxUserDao.selectByPrimaryKey(userId);
		Integer sdxScores = csqUser.getSdxScores();
		Integer forSaleScore = tSdxBookAfterReadingNotePo.getForSaleScore();
		forSaleScore = forSaleScore == null ? 10 : forSaleScore;
		if ((sdxScores = (sdxScores - forSaleScore)) < 0)
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "剩余积分不足，无法购买！");

		//检查是否已购买、点赞、或点踩
		int type = SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode();
		TSdxBookAfterReadingNoteUserPo afterReadingNoteUserPo = sdxBookAfterReadingNoteUserDao.selectByBookAfrnIdAndUserIdAndType(bookArnId, userId, type);
		if (afterReadingNoteUserPo != null) {
			if (SdxBookAfterReadingNoteUserEnum.IS_PURCHASE_YES.getCode() == afterReadingNoteUserPo.getIsPurchase())
				return;
			afterReadingNoteUserPo.setIsPurchase(SdxBookAfterReadingNoteUserEnum.IS_PURCHASE_YES.getCode());
			sdxBookAfterReadingNoteUserDao.update(afterReadingNoteUserPo);
		} else {
			afterReadingNoteUserPo = TSdxBookAfterReadingNoteUserPo.builder()
				.bookAfterReadingNoteId(bookArnId)
				.userId(userId)
				.bookId(tSdxBookAfterReadingNotePo.getBookId())
				.bookInfoId(tSdxBookAfterReadingNotePo.getBookInfoId())
				.type(SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode())
				.isPurchase(SdxBookAfterReadingNoteUserEnum.IS_PURCHASE_YES.getCode()).build();
			sdxBookAfterReadingNoteUserDao.insert(afterReadingNoteUserPo);
		}
		//消耗积分 -> 使用消耗积分逻辑，涉及积分消耗等
		tSdxScoreRecordService.dealWithScoreOutTypeBARN(csqUser, forSaleScore);
	}

	@Override
	public void thumb(Long bookAfterReadingId, Long userId, Integer option) {
		//检查option
		checkThumbOption(option);

		//检查目标关联是否存在 -> 点赞和购买将共用同一条记录，且对于同一条读后感，不存在二次购买
		TSdxBookAfterReadingNoteUserPo sdxBookAfterReadingNoteUserPo = sdxBookAfterReadingNoteUserDao.selectByBookAfrnIdAndUserIdAndType(bookAfterReadingId, userId, SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode());
		if (sdxBookAfterReadingNoteUserPo == null) {    //创建一条
			TSdxBookAfterReadingNoteUserPo po = TSdxBookAfterReadingNoteUserPo.builder()
				.thumbType(option)
				.isThumb(SdxBookAfterReadingNoteUserEnum.IS_THUMB_YES.getCode())
				.bookAfterReadingNoteId(bookAfterReadingId)
				.userId(userId)
				.type(SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode())
//				.bookId()
				.bookInfoId(findBookId(bookAfterReadingId))
				.build();

			TSdxBookAfterReadingNotePo sdxBookAfterReadingNotePo = sdxBookAfterReadingNoteDao.selectByPrimaryKey(bookAfterReadingId);
			if (SdxBookAfterReadingNoteUserEnum.THUMB_TYPE_UP.getCode() == option) {
				//赞数增加
				Integer thumbUpNum = sdxBookAfterReadingNotePo.getThumbUpNum();
				sdxBookAfterReadingNotePo.setThumbUpNum(++thumbUpNum);
			} else {
				//踩数增加
				Integer thumbDownNum = sdxBookAfterReadingNotePo.getThumbDownNum();
				sdxBookAfterReadingNotePo.setThumbDownNum(++thumbDownNum);
			}

			sdxBookAfterReadingNoteUserDao.saveTSdxBookAfterReadingNoteUserIfNotExist(po);
			return;
		}
		//修改关联的值 -> upside down
		Integer isThumb = sdxBookAfterReadingNoteUserPo.getIsThumb();
		Integer thumbType = sdxBookAfterReadingNoteUserPo.getThumbType();
		boolean isTransTypeRelated = !option.equals(thumbType);    //发生点赞到点踩 或者 点踩到点赞类型变化
		boolean isThumbUp = SdxBookAfterReadingNoteUserEnum.IS_THUMB_NO.getCode() == isThumb;    //原本是没有点赞或点踩
		sdxBookAfterReadingNoteUserPo.setThumbType(option);
		sdxBookAfterReadingNoteUserPo.setIsThumb(isThumbUp ? SdxBookAfterReadingNoteUserEnum.IS_THUMB_YES.getCode() : isTransTypeRelated ? SdxBookAfterReadingNoteUserEnum.IS_THUMB_YES.getCode() : SdxBookAfterReadingNoteUserEnum.IS_THUMB_NO.getCode());

		TSdxBookAfterReadingNotePo sdxBookAfterReadingNotePo = sdxBookAfterReadingNoteDao.selectByPrimaryKey(bookAfterReadingId);
		Integer thumbDownNum = sdxBookAfterReadingNotePo.getThumbDownNum();
		Integer thumbUpNum = sdxBookAfterReadingNotePo.getThumbUpNum();
		//赞数增加 -> 原本没有 赞或踩
		if (thumbType == null) {    //原本没有踩或赞 或
			if (SdxBookAfterReadingNoteUserEnum.THUMB_TYPE_UP.getCode() == option)
				sdxBookAfterReadingNotePo.setThumbUpNum(isThumbUp ? ++thumbUpNum : thumbUpNum > 0 ? --thumbUpNum : 0);
			else
				sdxBookAfterReadingNotePo.setThumbDownNum(isThumbUp ? ++thumbDownNum : thumbDownNum > 0 ? --thumbDownNum : 0);
		}
		else if (isTransTypeRelated) {
			//赞数增加, 踩数减少 -> 原本是点踩,现在要点赞
			if (SdxBookAfterReadingNoteUserEnum.THUMB_TYPE_DOWN.getCode() == thumbType) {
//				sdxBookAfterReadingNotePo.setThumbUpNum(isThumbUp ? ++thumbUpNum : thumbUpNum > 0 ? --thumbUpNum : 0);
				sdxBookAfterReadingNotePo.setThumbUpNum(++thumbUpNum);
				sdxBookAfterReadingNotePo.setThumbDownNum(thumbDownNum > 0 ? --thumbDownNum : 0);
			}
			//踩数增加，赞数减少 -> 原本是点赞 类型改为点踩
			if (SdxBookAfterReadingNoteUserEnum.THUMB_TYPE_UP.getCode() == thumbType) {
//				sdxBookAfterReadingNotePo.setThumbDownNum(isThumbUp ? ++thumbDownNum : thumbDownNum > 0 ? --thumbDownNum : 0);
				sdxBookAfterReadingNotePo.setThumbDownNum(++thumbDownNum);
				sdxBookAfterReadingNotePo.setThumbUpNum(thumbUpNum > 0 ? --thumbUpNum : 0);
			}
		} else {
			//没有发生类型变化
			if (SdxBookAfterReadingNoteUserEnum.THUMB_TYPE_DOWN.getCode() == thumbType) {
				sdxBookAfterReadingNotePo.setThumbDownNum(isThumbUp ? ++thumbDownNum : thumbDownNum > 0 ? --thumbDownNum : 0);
			}
			if (SdxBookAfterReadingNoteUserEnum.THUMB_TYPE_UP.getCode() == thumbType) {
				sdxBookAfterReadingNotePo.setThumbUpNum(isThumbUp ? ++thumbUpNum : thumbUpNum > 0 ? --thumbUpNum : 0);
			}
		}

		sdxBookAfterReadingNoteDao.modTSdxBookAfterReadingNote(sdxBookAfterReadingNotePo);
		sdxBookAfterReadingNoteUserDao.update(sdxBookAfterReadingNoteUserPo);
	}

	@Override
	public Integer getIndex(TSdxBookAfterReadingNotePo copyTSdxBookAfterReadingNotePo) {
		Long userId = copyTSdxBookAfterReadingNotePo.getUserId();
		Long bookInfoId = copyTSdxBookAfterReadingNotePo.getBookInfoId();
		List<TSdxBookTransRecordPo> transRecordPos = sdxBookTransRecordDao.selectByBookInfoIdAndUserId(bookInfoId, userId);

		return transRecordPos == null? 1: transRecordPos.size() + 1;
	}

	private void checkThumbOption(Integer option) {
		if (option == null || SdxBookAfterReadingNoteUserEnum.getType(option) == null)
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "操作不合法！");
	}

	private Long findBookId(Long bfrnId) {
		TSdxBookAfterReadingNotePo po;
		return (po = sdxBookAfterReadingNoteDao.selectByPrimaryKey(bfrnId)) == null ? null : po.getBookInfoId();
	}

}
