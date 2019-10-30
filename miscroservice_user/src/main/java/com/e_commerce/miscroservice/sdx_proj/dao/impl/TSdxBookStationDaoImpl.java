package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookStationDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookStationPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 书籍回收驿站的dao层
*
*/

@Repository
public class TSdxBookStationDaoImpl implements TSdxBookStationDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxBookStationIfNotExist(TSdxBookStationPo tSdxBookStationPo) {
        return tSdxBookStationPo.save();
    }
    @Override
    public int modTSdxBookStation(TSdxBookStationPo tSdxBookStationPo) {
        return tSdxBookStationPo.update(tSdxBookStationPo.build().eq(TSdxBookStationPo::getId, tSdxBookStationPo.getId()));
    }
    @Override
    public int delTSdxBookStationByIds(Long... ids) {
        TSdxBookStationPo tSdxBookStationPo = TSdxBookStationPo.builder().build();
        tSdxBookStationPo.setDeletedFlag(Boolean.TRUE);
        return tSdxBookStationPo.update(tSdxBookStationPo.build().in(TSdxBookStationPo::getId, ids));
    }
    @Override
    public TSdxBookStationPo findTSdxBookStationById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookStationPo.class);
        build.eq(TSdxBookStationPo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxBookStationPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxBookStationPo.builder().build(), build);
    }
    @Override
    public List<TSdxBookStationPo> findTSdxBookStationByAll(TSdxBookStationPo tSdxBookStationPo, Integer page, Integer size) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookStationPo.class);
        build.eq(TSdxBookStationPo::getDeletedFlag, Boolean.FALSE);
        if (tSdxBookStationPo.getId() == null) {
            if (StringUtils.isNotEmpty(tSdxBookStationPo.getName())) {
                build.like(TSdxBookStationPo::getName,tSdxBookStationPo.getName());
            }
            if (StringUtils.isNotEmpty(tSdxBookStationPo.getAddress())) {
                build.like(TSdxBookStationPo::getAddress,tSdxBookStationPo.getAddress());
            }
            if (tSdxBookStationPo.getLatitude()!=null ) {
                build.eq(TSdxBookStationPo::getLatitude,tSdxBookStationPo.getLatitude());
            }
            if (tSdxBookStationPo.getLongitude()!=null ) {
                build.eq(TSdxBookStationPo::getLongitude,tSdxBookStationPo.getLongitude());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxBookStationPo::getId, tSdxBookStationPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxBookStationPo.builder().build()    , build);
    }
}
