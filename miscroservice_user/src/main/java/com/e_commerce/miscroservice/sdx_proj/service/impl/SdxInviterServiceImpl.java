package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxInviterDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxInviterPo;
import com.e_commerce.miscroservice.sdx_proj.service.SdxInviterService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxInviterVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 邀请人信息的service层
*
*/

@Service
@Log
public class SdxInviterServiceImpl implements SdxInviterService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private SdxInviterDao sdxInviterDao;
    @Override
    public long modTSdxInviter(TSdxInviterPo tSdxInviterPo) {
        if (tSdxInviterPo == null) {
            log.warn("操作邀请人信息参数为空");
            return ERROR_LONG;
        }
        if (tSdxInviterPo.getId() == null) {
            log.info("start添加邀请人信息={}", tSdxInviterPo);
            int result = sdxInviterDao.saveTSdxInviterIfNotExist(tSdxInviterPo);
            return result != 0 ? tSdxInviterPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改邀请人信息={}", tSdxInviterPo.getId());
            return sdxInviterDao.modTSdxInviter(tSdxInviterPo);
        }
    }
    @Override
    public int delTSdxInviterByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除邀请人信息,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},邀请人信息", Arrays.asList(ids));
        return sdxInviterDao.delTSdxInviterByIds(ids);
    }
    @Override
    public TSdxInviterVo findTSdxInviterById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找邀请人信息,所传Id不符合规范");
            return new TSdxInviterVo();
        }
        log.info("start根据Id={}查找邀请人信息", id);
        TSdxInviterPo tSdxInviterPo = sdxInviterDao.findTSdxInviterById(id);
        return tSdxInviterPo==null?new TSdxInviterVo():tSdxInviterPo.copyTSdxInviterVo() ;
    }
    @Override
    public List<TSdxInviterVo> findTSdxInviterByAll(TSdxInviterPo tSdxInviterPo,Integer page, Integer size) {
        List    <TSdxInviterVo> tSdxInviterVos = new ArrayList<>();
        if (tSdxInviterPo == null) {
            log.warn("根据条件查找邀请人信息,参数不对");
            return tSdxInviterVos;
        }
        log.info("start根据条件查找邀请人信息={}", tSdxInviterPo);
        List        <TSdxInviterPo> tSdxInviterPos = sdxInviterDao.findTSdxInviterByAll(            tSdxInviterPo,page,size);
        for (TSdxInviterPo po : tSdxInviterPos) {
            tSdxInviterVos.add(po.copyTSdxInviterVo());
        }
        return tSdxInviterVos;
    }
}
