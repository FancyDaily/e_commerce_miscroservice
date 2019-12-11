package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxCommonsDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxCommonsPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 用户评论的dao层
*
*/

@Repository
public class TSdxCommonsDaoImpl implements TSdxCommonsDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxCommonsIfNotExist(TSdxCommonsPo tSdxCommonsPo) {
        return tSdxCommonsPo.save();
    }
    @Override
    public int modTSdxCommons(TSdxCommonsPo tSdxCommonsPo) {
        return tSdxCommonsPo.update(tSdxCommonsPo.build().eq(TSdxCommonsPo::getId, tSdxCommonsPo.getId()));
    }
    @Override
    public int delTSdxCommonsByIds(Long... ids) {
        TSdxCommonsPo tSdxCommonsPo = TSdxCommonsPo.builder().build();
        tSdxCommonsPo.setDeletedFlag(Boolean.TRUE);
        return tSdxCommonsPo.update(tSdxCommonsPo.build().in(TSdxCommonsPo::getId, ids));
    }
    @Override
    public TSdxCommonsPo findTSdxCommonsById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxCommonsPo.class);
        build.eq(TSdxCommonsPo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxCommonsPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxCommonsPo.builder().build(), build);
    }
    @Override
    public List<TSdxCommonsPo> findTSdxCommonsByAll(TSdxCommonsPo tSdxCommonsPo, Integer page, Integer size) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxCommonsPo.class);
        build.eq(TSdxCommonsPo::getDeletedFlag, Boolean.FALSE);
        if (tSdxCommonsPo.getId() == null) {
            if (tSdxCommonsPo.getUserId()!=null ) {
                build.eq(TSdxCommonsPo::getUserId,tSdxCommonsPo.getUserId());
            }
            if (tSdxCommonsPo.getFriendId()!=null ) {
                build.eq(TSdxCommonsPo::getFriendId,tSdxCommonsPo.getFriendId());
            }
            if (tSdxCommonsPo.getTrendsId()!=null ) {
                build.eq(TSdxCommonsPo::getTrendsId,tSdxCommonsPo.getTrendsId());
            }
            if (StringUtils.isNotEmpty(tSdxCommonsPo.getContentInfo())) {
                build.like(TSdxCommonsPo::getContentInfo,tSdxCommonsPo.getContentInfo());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxCommonsPo::getId, tSdxCommonsPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxCommonsPo.builder().build()    , build);
    }
}
