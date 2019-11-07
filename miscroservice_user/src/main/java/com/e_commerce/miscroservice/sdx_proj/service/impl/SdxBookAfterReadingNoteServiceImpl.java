package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookAfterReadingNoteDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookAfterReadingNoteUserDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxScoreRecordDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxUserDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookAfterReadingNoteUserEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNotePo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNoteUserPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookAfterReadingNoteService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 书籍读后感的service层
*
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
    @Override
    public long modTSdxBookAfterReadingNote(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo) {
        if (tSdxBookAfterReadingNotePo == null) {
            log.warn("操作书籍读后感参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookAfterReadingNotePo.getId() == null) {
            log.info("start添加书籍读后感={}", tSdxBookAfterReadingNotePo);
            int result = sdxBookAfterReadingNoteDao.saveTSdxBookAfterReadingNoteIfNotExist(tSdxBookAfterReadingNotePo);
            return result != 0 ? tSdxBookAfterReadingNotePo.getId() : ERROR_LONG;
        }
        else {
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
        if (id == null||id<=0L) {
            log.warn("根据Id查找书籍读后感,所传Id不符合规范");
            return new TSdxBookAfterReadingNoteVo();
        }
        log.info("start根据Id={}查找书籍读后感", id);
        TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo = sdxBookAfterReadingNoteDao.findTSdxBookAfterReadingNoteById(id);
        return tSdxBookAfterReadingNotePo==null?new TSdxBookAfterReadingNoteVo():tSdxBookAfterReadingNotePo.copyTSdxBookAfterReadingNoteVo() ;
    }
    @Override
    public List<TSdxBookAfterReadingNoteVo> findTSdxBookAfterReadingNoteByAll(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo,Integer page, Integer size) {
        List    <TSdxBookAfterReadingNoteVo> tSdxBookAfterReadingNoteVos = new ArrayList<>();
        if (tSdxBookAfterReadingNotePo == null) {
            log.warn("根据条件查找书籍读后感,参数不对");
            return tSdxBookAfterReadingNoteVos;
        }
        log.info("start根据条件查找书籍读后感={}", tSdxBookAfterReadingNotePo);
        List        <TSdxBookAfterReadingNotePo> tSdxBookAfterReadingNotePos = sdxBookAfterReadingNoteDao.findTSdxBookAfterReadingNoteByAll(            tSdxBookAfterReadingNotePo,page,size);
        for (TSdxBookAfterReadingNotePo po : tSdxBookAfterReadingNotePos) {
            tSdxBookAfterReadingNoteVos.add(po.copyTSdxBookAfterReadingNoteVo());
        }
        return tSdxBookAfterReadingNoteVos;
    }

	@Override
	public void buy(Long bookArnId, Long userId) {
		TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo = sdxBookAfterReadingNoteDao.selectByPrimaryKey(bookArnId);
		if(tSdxBookAfterReadingNotePo == null) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "读后感不存在！");
		TCsqUser csqUser = sdxUserDao.selectByPrimaryKey(userId);
		Integer sdxScores = csqUser.getSdxScores();
		Integer forSaleScore = tSdxBookAfterReadingNotePo.getForSaleScore();
		if((sdxScores = (sdxScores - forSaleScore)) < 0) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "剩余积分不足，无法购买！");

		//检查是否已购买、点赞、或点踩
		int type = SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode();
		TSdxBookAfterReadingNoteUserPo afterReadingNoteUserPo = sdxBookAfterReadingNoteUserDao.selectByBookAfrnIdAndUserIdAndType(bookArnId, userId, type);
		if(afterReadingNoteUserPo != null) {
			if(SdxBookAfterReadingNoteUserEnum.IS_PURCHASE_YES.getCode() == afterReadingNoteUserPo.getIsPurchase()) return;
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
		if(sdxBookAfterReadingNoteUserPo == null) {	//创建一条
			TSdxBookAfterReadingNoteUserPo po = TSdxBookAfterReadingNoteUserPo.builder()
				.thumbType(option)
				.isThumb(SdxBookAfterReadingNoteUserEnum.IS_THUMB_YES.getCode())
				.bookAfterReadingNoteId(bookAfterReadingId)
				.userId(userId)
				.type(SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode())
//				.bookId()
				.bookInfoId(findBookId(bookAfterReadingId))
				.build();
			sdxBookAfterReadingNoteUserDao.saveTSdxBookAfterReadingNoteUserIfNotExist(po);
			return;
		}
		//修改关联的值 -> upside down
		sdxBookAfterReadingNoteUserPo.setThumbType(option);
		sdxBookAfterReadingNoteUserPo.setIsThumb(SdxBookAfterReadingNoteUserEnum.IS_THUMB_YES.getCode());
		sdxBookAfterReadingNoteUserDao.update(sdxBookAfterReadingNoteUserPo);
	}

	private void checkThumbOption(Integer option) {
    	if(option == null || SdxBookAfterReadingNoteUserEnum.getType(option) == null)
    		throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "操作不合法！");
	}

	private Long findBookId(Long bfrnId) {
		TSdxBookAfterReadingNotePo po;
		return (po = sdxBookAfterReadingNoteDao.selectByPrimaryKey(bfrnId)) == null? null : po.getBookInfoId();
	}

}
