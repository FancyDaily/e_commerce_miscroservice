package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookAfterReadingNoteDao;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookAfterReadingNoteUserDao;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxScoreRecordDao;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxUserDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookAfterReadingNoteUserEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNotePo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNoteUserPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxBookAfterReadingNoteService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookAfterReadingNoteVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import io.netty.handler.codec.MessageAggregationException;
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
public class TSdxBookAfterReadingNoteServiceImpl implements TSdxBookAfterReadingNoteService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxBookAfterReadingNoteDao tSdxBookAfterReadingNoteDao;
	@Autowired
    private TSdxBookAfterReadingNoteUserDao tSdxBookAfterReadingNoteUserDao;
	@Autowired
	private TSdxUserDao tSdxUserDao;
	@Autowired
	private TSdxScoreRecordDao tSdxScoreRecordDao;
    @Override
    public long modTSdxBookAfterReadingNote(TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo) {
        if (tSdxBookAfterReadingNotePo == null) {
            log.warn("操作书籍读后感参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookAfterReadingNotePo.getId() == null) {
            log.info("start添加书籍读后感={}", tSdxBookAfterReadingNotePo);
            int result = tSdxBookAfterReadingNoteDao.saveTSdxBookAfterReadingNoteIfNotExist(tSdxBookAfterReadingNotePo);
            return result != 0 ? tSdxBookAfterReadingNotePo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改书籍读后感={}", tSdxBookAfterReadingNotePo.getId());
            return tSdxBookAfterReadingNoteDao.modTSdxBookAfterReadingNote(tSdxBookAfterReadingNotePo);
        }
    }
    @Override
    public int delTSdxBookAfterReadingNoteByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除书籍读后感,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},书籍读后感", Arrays.asList(ids));
        return tSdxBookAfterReadingNoteDao.delTSdxBookAfterReadingNoteByIds(ids);
    }
    @Override
    public TSdxBookAfterReadingNoteVo findTSdxBookAfterReadingNoteById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找书籍读后感,所传Id不符合规范");
            return new TSdxBookAfterReadingNoteVo();
        }
        log.info("start根据Id={}查找书籍读后感", id);
        TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo = tSdxBookAfterReadingNoteDao.findTSdxBookAfterReadingNoteById(id);
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
        List        <TSdxBookAfterReadingNotePo> tSdxBookAfterReadingNotePos = tSdxBookAfterReadingNoteDao.findTSdxBookAfterReadingNoteByAll(            tSdxBookAfterReadingNotePo,page,size);
        for (TSdxBookAfterReadingNotePo po : tSdxBookAfterReadingNotePos) {
            tSdxBookAfterReadingNoteVos.add(po.copyTSdxBookAfterReadingNoteVo());
        }
        return tSdxBookAfterReadingNoteVos;
    }

	@Override
	public void buy(Long bookArnId, Long userId) {
		TSdxBookAfterReadingNotePo tSdxBookAfterReadingNotePo = tSdxBookAfterReadingNoteDao.selectByPrimaryKey(bookArnId);
		if(tSdxBookAfterReadingNotePo == null) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "读后感不存在！");
		TCsqUser csqUser = tSdxUserDao.selectByPrimaryKey(userId);
		Integer sdxScores = csqUser.getSdxScores();
		Integer forSaleScore = tSdxBookAfterReadingNotePo.getForSaleScore();
		if((sdxScores = (sdxScores - forSaleScore)) < 0) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "剩余积分不足，无法购买！");

		//检查是否已购买、点赞、或点踩
		int type = SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode();
		TSdxBookAfterReadingNoteUserPo afterReadingNoteUserPo = tSdxBookAfterReadingNoteUserDao.selectByBookAfrnIdAndUserIdAndType(bookArnId, userId, type);
		if(afterReadingNoteUserPo != null) {
			if(SdxBookAfterReadingNoteUserEnum.IS_PURCHASE_YES.getCode() == afterReadingNoteUserPo.getIsPurchase()) return;
			afterReadingNoteUserPo.setIsPurchase(SdxBookAfterReadingNoteUserEnum.IS_PURCHASE_YES.getCode());
			tSdxBookAfterReadingNoteUserDao.update(afterReadingNoteUserPo);
		} else {
			afterReadingNoteUserPo = TSdxBookAfterReadingNoteUserPo.builder()
				.bookAfterReadingNoteId(bookArnId)
				.userId(userId)
				.bookId(tSdxBookAfterReadingNotePo.getBookId())
				.bookInfoId(tSdxBookAfterReadingNotePo.getBookInfoId())
				.type(SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode())
				.isPurchase(SdxBookAfterReadingNoteUserEnum.IS_PURCHASE_YES.getCode()).build();
			tSdxBookAfterReadingNoteUserDao.insert(afterReadingNoteUserPo);
		}
		//消耗积分 -> 使用消耗积分逻辑，涉及积分消耗等
		dealWithScoreOut(csqUser, sdxScores);
	}

	private void dealWithScoreOut(TCsqUser csqUser, Integer sdxScores) {
		//消耗积分 -> 使用消耗积分逻辑，涉及积分消耗等                                                                                                                                                                                                                                                                                                                               插入流水等。
		csqUser.setSdxScores(sdxScores);
		tSdxUserDao.update(csqUser);
		//支出流水
		//TODO
//		tSdxScoreRecordDao.saveTSdxScoreRecordIfNotExist(TSdxScoreRecordPo.builder()
//		)
	}
}
