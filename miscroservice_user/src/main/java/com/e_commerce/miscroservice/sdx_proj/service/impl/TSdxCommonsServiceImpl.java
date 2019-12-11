package com.e_commerce.miscroservice.sdx_proj.service.impl;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxCommonsDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxCommonsPo;
import com.e_commerce.miscroservice.sdx_proj.service.TSdxCommonsService;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxCommonsVo;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
* 用户评论的service层
*
*/

@Service
@Log
public class TSdxCommonsServiceImpl implements TSdxCommonsService {
    private final long ERROR_LONG = 0L;
    private final int ERROR_INT = 0;
    @Autowired
    private TSdxCommonsDao tSdxCommonsDao;
    @Override
    public long modTSdxCommons(TSdxCommonsPo tSdxCommonsPo) {
        if (tSdxCommonsPo == null) {
            log.warn("操作用户评论参数为空");
            return ERROR_LONG;
        }
        if (tSdxCommonsPo.getId() == null) {
            log.info("start添加用户评论={}", tSdxCommonsPo);
            int result = tSdxCommonsDao.saveTSdxCommonsIfNotExist(tSdxCommonsPo);
            return result != 0 ? tSdxCommonsPo.getId() : ERROR_LONG;
        }
        else {
            log.info("start修改用户评论={}", tSdxCommonsPo.getId());
            return tSdxCommonsDao.modTSdxCommons(tSdxCommonsPo);
        }
    }
    @Override
    public int delTSdxCommonsByIds(Long... ids) {
        if (ids == null || ids.length == 0) {
            log.warn("删除用户评论,ids集合为空");
            return ERROR_INT;
        }
        log.warn("start删除Id集合={},用户评论", Arrays.asList(ids));
        return tSdxCommonsDao.delTSdxCommonsByIds(ids);
    }
    @Override
    public TSdxCommonsVo findTSdxCommonsById(Long id) {
        if (id == null||id<=0L) {
            log.warn("根据Id查找用户评论,所传Id不符合规范");
            return new TSdxCommonsVo();
        }
        log.info("start根据Id={}查找用户评论", id);
        TSdxCommonsPo tSdxCommonsPo = tSdxCommonsDao.findTSdxCommonsById(id);
        return tSdxCommonsPo==null?new TSdxCommonsVo():tSdxCommonsPo.copyTSdxCommonsVo() ;
    }
    @Override
    public List<TSdxCommonsVo> findTSdxCommonsByAll(TSdxCommonsPo tSdxCommonsPo,Integer page, Integer size) {
        List    <TSdxCommonsVo> tSdxCommonsVos = new ArrayList<>();
        if (tSdxCommonsPo == null) {
            log.warn("根据条件查找用户评论,参数不对");
            return tSdxCommonsVos;
        }
        log.info("start根据条件查找用户评论={}", tSdxCommonsPo);
        List        <TSdxCommonsPo> tSdxCommonsPos = tSdxCommonsDao.findTSdxCommonsByAll(            tSdxCommonsPo,page,size);
        for (TSdxCommonsPo po : tSdxCommonsPos) {
            tSdxCommonsVos.add(po.copyTSdxCommonsVo());
        }
        return tSdxCommonsVos;
    }
}
