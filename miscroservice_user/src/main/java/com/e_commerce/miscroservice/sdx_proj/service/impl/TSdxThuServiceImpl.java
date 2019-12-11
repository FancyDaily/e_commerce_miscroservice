package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxThuDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxThuPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxThuService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxThuVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 动态点赞的service层
*
*/

@Service
@Log
public class TSdxThuServiceImpl implements TSdxThuService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxThuDao tSdxThuDao;
    @Override
    public long modTSdxThu(TSdxThuPo tSdxThuPo) {
        if (tSdxThuPo == null) {
            log.warn("操作动态点赞参数为空");
            return ERROR_LONG;
        }
        if (tSdxThuPo.getId() == null) {
            log.info("start添加动态点赞={}", tSdxThuPo);
            int result = tSdxThuDao.saveTSdxThuIfNotExist(tSdxThuPo);
            return result != 0 ? tSdxThuPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改动态点赞={}", tSdxThuPo.getId());
            return tSdxThuDao.modTSdxThu(tSdxThuPo);
        }
    }
    @Override
    public int delTSdxThuByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除动态点赞,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},动态点赞", Arrays.asList(ids));
        return tSdxThuDao.delTSdxThuByIds(ids);
    }
    @Override
    public TSdxThuVo findTSdxThuById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找动态点赞,所传Id不符合规范");
            return new TSdxThuVo();
        }
        log.info("start根据Id={}查找动态点赞", id);
        TSdxThuPo tSdxThuPo = tSdxThuDao.findTSdxThuById(id);
        return tSdxThuPo==null?new TSdxThuVo():tSdxThuPo.copyTSdxThuVo() ;
    }
    @Override
    public List<TSdxThuVo> findTSdxThuByAll(TSdxThuPo tSdxThuPo,Integer page, Integer size) {
        List    <TSdxThuVo> tSdxThuVos = new ArrayList<>();
        if (tSdxThuPo == null) {
            log.warn("根据条件查找动态点赞,参数不对");
            return tSdxThuVos;
        }
        log.info("start根据条件查找动态点赞={}", tSdxThuPo);
        List        <TSdxThuPo> tSdxThuPos = tSdxThuDao.findTSdxThuByAll(            tSdxThuPo,page,size);
        for (TSdxThuPo po : tSdxThuPos) {
            tSdxThuVos.add(po.copyTSdxThuVo());
        }
        return tSdxThuVos;
    }
}
