package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.csq_proj.po.TCsqUser;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookAfterReadingNoteDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookAfterReadingNoteUserDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookTransRecordDao;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxUserDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookAfterReadingNoteUserEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNotePo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNoteUserPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTransRecordPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxBookTransRecordService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookTransRecordVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 书籍漂流记录的service层
*
*/

@Service
@Log
public class SdxBookTransRecordServiceImpl implements SdxBookTransRecordService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private SdxBookTransRecordDao sdxBookTransRecordDao;
	@Autowired
    private SdxBookAfterReadingNoteDao sdxBookAfterReadingNoteDao;
	@Autowired
	private SdxBookAfterReadingNoteUserDao sdxBookAfterReadingNoteUserDao;
	@Autowired
	private SdxUserDao sdxUserDao;
    @Override
    public long modTSdxBookTransRecord(TSdxBookTransRecordPo tSdxBookTransRecordPo) {
        if (tSdxBookTransRecordPo == null) {
            log.warn("操作书籍漂流记录参数为空");
            return ERROR_LONG;
        }
        if (tSdxBookTransRecordPo.getId() == null) {
            log.info("start添加书籍漂流记录={}", tSdxBookTransRecordPo);
            int result = sdxBookTransRecordDao.saveTSdxBookTransRecordIfNotExist(tSdxBookTransRecordPo);
            return result != 0 ? tSdxBookTransRecordPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改书籍漂流记录={}", tSdxBookTransRecordPo.getId());
            return sdxBookTransRecordDao.modTSdxBookTransRecord(tSdxBookTransRecordPo);
        }
    }
    @Override
    public int delTSdxBookTransRecordByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除书籍漂流记录,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},书籍漂流记录", Arrays.asList(ids));
        return sdxBookTransRecordDao.delTSdxBookTransRecordByIds(ids);
    }
    @Override
    public TSdxBookTransRecordVo findTSdxBookTransRecordById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找书籍漂流记录,所传Id不符合规范");
            return new TSdxBookTransRecordVo();
        }
        log.info("start根据Id={}查找书籍漂流记录", id);
        TSdxBookTransRecordPo tSdxBookTransRecordPo = sdxBookTransRecordDao.findTSdxBookTransRecordById(id);
        return tSdxBookTransRecordPo==null?new TSdxBookTransRecordVo():tSdxBookTransRecordPo.copyTSdxBookTransRecordVo() ;
    }
    @Override
    public List<TSdxBookTransRecordVo> findTSdxBookTransRecordByAll(TSdxBookTransRecordPo tSdxBookTransRecordPo,Integer page, Integer size) {
        List    <TSdxBookTransRecordVo> tSdxBookTransRecordVos = new ArrayList<>();
        if (tSdxBookTransRecordPo == null) {
            log.warn("根据条件查找书籍漂流记录,参数不对");
            return tSdxBookTransRecordVos;
        }
        log.info("start根据条件查找书籍漂流记录={}", tSdxBookTransRecordPo);
        List        <TSdxBookTransRecordPo> tSdxBookTransRecordPos = sdxBookTransRecordDao.findTSdxBookTransRecordByAll(            tSdxBookTransRecordPo,page,size);
        for (TSdxBookTransRecordPo po : tSdxBookTransRecordPos) {
			TSdxBookTransRecordVo tSdxBookTransRecordVo = po.copyTSdxBookTransRecordVo();
			Long afterReadingNoteId = tSdxBookTransRecordVo.getBookAfterReadingNoteId();
			Long userId = tSdxBookTransRecordPo.getUserId();
			if(userId != null) {
				TCsqUser csqUser = sdxUserDao.selectByPrimaryKey(userId);
				tSdxBookTransRecordVo.setUserName(csqUser.getName());
				tSdxBookTransRecordVo.setHeadPic(csqUser.getUserHeadPortraitPath());
			}
			if(afterReadingNoteId != null) {
				TSdxBookAfterReadingNotePo afterReadingNotePo = sdxBookAfterReadingNoteDao.selectByPrimaryKey(afterReadingNoteId);
				//确认点赞状态、类型,购买状态
				Long afterReadingNotePoId = afterReadingNotePo.getId();
				List<TSdxBookAfterReadingNoteUserPo> sdxBookAfterReadingNoteUserPos = sdxBookAfterReadingNoteUserDao.selectByBookAfrnIdAndUserIdAndIsThumb(afterReadingNotePoId, userId, SdxBookAfterReadingNoteUserEnum.IS_THUMB_YES.getCode());
				if(!sdxBookAfterReadingNoteUserPos.isEmpty()) {
					TSdxBookAfterReadingNoteUserPo readingNoteUserPo = sdxBookAfterReadingNoteUserPos.get(0);
					afterReadingNotePo.setIsThumb(readingNoteUserPo.getIsThumb());
					afterReadingNotePo.setThumbType(readingNoteUserPo.getThumbType());
				}
				List<TSdxBookAfterReadingNoteUserPo> bookAfterReadingNoteUserPos = sdxBookAfterReadingNoteUserDao.selectByBookAfrnIdAndUserIdAndTypeAndIsPurchase(afterReadingNotePoId, userId, SdxBookAfterReadingNoteUserEnum.TYPE_THUMB_OR_PURCHASE.getCode(), SdxBookAfterReadingNoteUserEnum.IS_PURCHASE_YES.getCode());
				if(!bookAfterReadingNoteUserPos.isEmpty()) {
					afterReadingNotePo.setNoNeedBuy(true);
				}
				tSdxBookTransRecordVo.setAfterReadingNote(afterReadingNotePo);
			}
			tSdxBookTransRecordVos.add(tSdxBookTransRecordVo);
        }
        return tSdxBookTransRecordVos;
    }
}
