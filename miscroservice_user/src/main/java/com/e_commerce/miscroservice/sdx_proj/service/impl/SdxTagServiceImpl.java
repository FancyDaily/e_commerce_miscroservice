package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxTagDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTagPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxTagService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxTagVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 书本类型的service层
*
*/

@Service
@Log
public class SdxTagServiceImpl implements SdxTagService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private SdxTagDao sdxTagDao;
    @Override
    public long modTSdxTag(TSdxTagPo tSdxTagPo) {
        if (tSdxTagPo == null) {
            log.warn("操作书本类型参数为空");
            return ERROR_LONG;
        }
        if (tSdxTagPo.getId() == null) {
            log.info("start添加书本类型={}", tSdxTagPo);
            int result = sdxTagDao.saveTSdxTagIfNotExist(tSdxTagPo);
            return result != 0 ? tSdxTagPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改书本类型={}", tSdxTagPo.getId());
            return sdxTagDao.modTSdxTag(tSdxTagPo);
        }
    }
    @Override
    public int delTSdxTagByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除书本类型,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},书本类型", Arrays.asList(ids));
        return sdxTagDao.delTSdxTagByIds(ids);
    }
    @Override
    public TSdxTagVo findTSdxTagById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找书本类型,所传Id不符合规范");
            return new TSdxTagVo();
        }
        log.info("start根据Id={}查找书本类型", id);
        TSdxTagPo tSdxTagPo = sdxTagDao.findTSdxTagById(id);
        return tSdxTagPo==null?new TSdxTagVo():tSdxTagPo.copyTSdxTagVo() ;
    }
    @Override
    public List<TSdxTagVo> findTSdxTagByAll(TSdxTagPo tSdxTagPo,Integer page, Integer size) {
        List    <TSdxTagVo> tSdxTagVos = new ArrayList<>();
        if (tSdxTagPo == null) {
            log.warn("根据条件查找书本类型,参数不对");
            return tSdxTagVos;
        }
        log.info("start根据条件查找书本类型={}", tSdxTagPo);
        List        <TSdxTagPo> tSdxTagPos = sdxTagDao.findTSdxTagByAll(            tSdxTagPo,page,size);
        for (TSdxTagPo po : tSdxTagPos) {
            tSdxTagVos.add(po.copyTSdxTagVo());
        }
        return tSdxTagVos;
    }
}
