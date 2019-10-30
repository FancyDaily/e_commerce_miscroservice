package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookInfoReserveDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoReservePo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 书籍预定信息的dao层
*
*/

@Repository
public class TSdxBookInfoReserveDaoImpl implements TSdxBookInfoReserveDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxBookInfoReserveIfNotExist(TSdxBookInfoReservePo tSdxBookInfoReservePo) {
        return tSdxBookInfoReservePo.save();
    }
    @Override
    public int modTSdxBookInfoReserve(TSdxBookInfoReservePo tSdxBookInfoReservePo) {
        return tSdxBookInfoReservePo.update(tSdxBookInfoReservePo.build().eq(TSdxBookInfoReservePo::getId, tSdxBookInfoReservePo.getId()));
    }
    @Override
    public int delTSdxBookInfoReserveByIds(Long... ids) {
        TSdxBookInfoReservePo tSdxBookInfoReservePo = TSdxBookInfoReservePo.builder().build();
        tSdxBookInfoReservePo.setDeletedFlag(Boolean.TRUE);
        return tSdxBookInfoReservePo.update(tSdxBookInfoReservePo.build().in(TSdxBookInfoReservePo::getId, ids));
    }
    @Override
    public TSdxBookInfoReservePo findTSdxBookInfoReserveById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookInfoReservePo.class);
        build.eq(TSdxBookInfoReservePo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxBookInfoReservePo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxBookInfoReservePo.builder().build(), build);
    }
    @Override
    public List<TSdxBookInfoReservePo> findTSdxBookInfoReserveByAll(TSdxBookInfoReservePo tSdxBookInfoReservePo, Integer page, Integer size) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookInfoReservePo.class);
        build.eq(TSdxBookInfoReservePo::getDeletedFlag, Boolean.FALSE);
        if (tSdxBookInfoReservePo.getId() == null) {
            if (tSdxBookInfoReservePo.getUserId()!=null ) {
                build.eq(TSdxBookInfoReservePo::getUserId,tSdxBookInfoReservePo.getUserId());
            }
            if (tSdxBookInfoReservePo.getBookInfoId()!=null ) {
                build.eq(TSdxBookInfoReservePo::getBookInfoId,tSdxBookInfoReservePo.getBookInfoId());
            }
            if (tSdxBookInfoReservePo.getBookTicketId()!=null ) {
                build.eq(TSdxBookInfoReservePo::getBookTicketId,tSdxBookInfoReservePo.getBookTicketId());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxBookInfoReservePo::getId, tSdxBookInfoReservePo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxBookInfoReservePo.builder().build()    , build);
    }
}
