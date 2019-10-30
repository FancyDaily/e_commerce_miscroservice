package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxInviterDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxInviterPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 邀请人信息的dao层
*
*/

@Repository
public class TSdxInviterDaoImpl implements TSdxInviterDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxInviterIfNotExist(TSdxInviterPo tSdxInviterPo) {
        return tSdxInviterPo.save();
    }
    @Override
    public int modTSdxInviter(TSdxInviterPo tSdxInviterPo) {
        return tSdxInviterPo.update(tSdxInviterPo.build().eq(TSdxInviterPo::getId, tSdxInviterPo.getId()));
    }
    @Override
    public int delTSdxInviterByIds(Long... ids) {
        TSdxInviterPo tSdxInviterPo = TSdxInviterPo.builder().build();
        tSdxInviterPo.setDeletedFlag(Boolean.TRUE);
        return tSdxInviterPo.update(tSdxInviterPo.build().in(TSdxInviterPo::getId, ids));
    }
    @Override
    public TSdxInviterPo findTSdxInviterById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxInviterPo.class);
        build.eq(TSdxInviterPo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxInviterPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxInviterPo.builder().build(), build);
    }
    @Override
    public List<TSdxInviterPo> findTSdxInviterByAll(TSdxInviterPo tSdxInviterPo, Integer page, Integer size) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxInviterPo.class);
        build.eq(TSdxInviterPo::getDeletedFlag, Boolean.FALSE);
        if (tSdxInviterPo.getId() == null) {
            if (tSdxInviterPo.getUserId()!=null ) {
                build.eq(TSdxInviterPo::getUserId,tSdxInviterPo.getUserId());
            }
            if (tSdxInviterPo.getInviterId()!=null ) {
                build.eq(TSdxInviterPo::getInviterId,tSdxInviterPo.getInviterId());
            }
            if (tSdxInviterPo.getBookInfoId()!=null ) {
                build.eq(TSdxInviterPo::getBookInfoId,tSdxInviterPo.getBookInfoId());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxInviterPo::getId, tSdxInviterPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxInviterPo.builder().build()    , build);
    }
}
