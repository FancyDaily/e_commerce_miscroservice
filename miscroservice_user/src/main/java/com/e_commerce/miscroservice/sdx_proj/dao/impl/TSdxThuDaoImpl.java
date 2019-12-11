package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxThuDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxThuPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 动态点赞的dao层
*
*/

@Repository
public class TSdxThuDaoImpl implements TSdxThuDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxThuIfNotExist(TSdxThuPo tSdxThuPo) {
        return tSdxThuPo.save();
    }
    @Override
    public int modTSdxThu(TSdxThuPo tSdxThuPo) {
        return tSdxThuPo.update(tSdxThuPo.build().eq(TSdxThuPo::getId, tSdxThuPo.getId()));
    }
    @Override
    public int delTSdxThuByIds(Long... ids) {
        TSdxThuPo tSdxThuPo = TSdxThuPo.builder().build();
        tSdxThuPo.setDeletedFlag(Boolean.TRUE);
        return tSdxThuPo.update(tSdxThuPo.build().in(TSdxThuPo::getId, ids));
    }
    @Override
    public TSdxThuPo findTSdxThuById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxThuPo.class);
        build.eq(TSdxThuPo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxThuPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxThuPo.builder().build(), build);
    }
    @Override
    public List<TSdxThuPo> findTSdxThuByAll(TSdxThuPo tSdxThuPo, Integer page, Integer size) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxThuPo.class);
        build.eq(TSdxThuPo::getDeletedFlag, Boolean.FALSE);
        if (tSdxThuPo.getId() == null) {
            if (tSdxThuPo.getType()!=null ) {
                build.eq(TSdxThuPo::getType,tSdxThuPo.getType());
            }
            if (tSdxThuPo.getUserId()!=null ) {
                build.eq(TSdxThuPo::getUserId,tSdxThuPo.getUserId());
            }
            if (tSdxThuPo.getTrendsId()!=null ) {
                build.eq(TSdxThuPo::getTrendsId,tSdxThuPo.getTrendsId());
            }
            if (StringUtils.isNotEmpty(tSdxThuPo.getTypeName())) {
                build.like(TSdxThuPo::getTypeName,tSdxThuPo.getTypeName());
            }
            if (StringUtils.isNotEmpty(tSdxThuPo.getUserName())) {
                build.like(TSdxThuPo::getUserName,tSdxThuPo.getUserName());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxThuPo::getId, tSdxThuPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxThuPo.builder().build()    , build);
    }
}
