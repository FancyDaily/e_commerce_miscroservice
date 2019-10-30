package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookAfterReadingNoteUserDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookAfterReadingNoteUserPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 书籍读后感用户相关的dao层
*
*/

@Repository
public class TSdxBookAfterReadingNoteUserDaoImpl implements TSdxBookAfterReadingNoteUserDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxBookAfterReadingNoteUserIfNotExist(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo) {
        return tSdxBookAfterReadingNoteUserPo.save();
    }
    @Override
    public int modTSdxBookAfterReadingNoteUser(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo) {
        return tSdxBookAfterReadingNoteUserPo.update(tSdxBookAfterReadingNoteUserPo.build().eq(TSdxBookAfterReadingNoteUserPo::getId, tSdxBookAfterReadingNoteUserPo.getId()));
    }
    @Override
    public int delTSdxBookAfterReadingNoteUserByIds(Long... ids) {
        TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo = TSdxBookAfterReadingNoteUserPo.builder().build();
        tSdxBookAfterReadingNoteUserPo.setDeletedFlag(Boolean.TRUE);
        return tSdxBookAfterReadingNoteUserPo.update(tSdxBookAfterReadingNoteUserPo.build().in(TSdxBookAfterReadingNoteUserPo::getId, ids));
    }
    @Override
    public TSdxBookAfterReadingNoteUserPo findTSdxBookAfterReadingNoteUserById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookAfterReadingNoteUserPo.class);
        build.eq(TSdxBookAfterReadingNoteUserPo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxBookAfterReadingNoteUserPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxBookAfterReadingNoteUserPo.builder().build(), build);
    }
    @Override
    public List<TSdxBookAfterReadingNoteUserPo> findTSdxBookAfterReadingNoteUserByAll(TSdxBookAfterReadingNoteUserPo tSdxBookAfterReadingNoteUserPo, Integer page, Integer size) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookAfterReadingNoteUserPo.class);
        build.eq(TSdxBookAfterReadingNoteUserPo::getDeletedFlag, Boolean.FALSE);
        if (tSdxBookAfterReadingNoteUserPo.getId() == null) {
            if (tSdxBookAfterReadingNoteUserPo.getType()!=null ) {
                build.eq(TSdxBookAfterReadingNoteUserPo::getType,tSdxBookAfterReadingNoteUserPo.getType());
            }
            if (tSdxBookAfterReadingNoteUserPo.getBookId()!=null ) {
                build.eq(TSdxBookAfterReadingNoteUserPo::getBookId,tSdxBookAfterReadingNoteUserPo.getBookId());
            }
            if (tSdxBookAfterReadingNoteUserPo.getUserId()!=null ) {
                build.eq(TSdxBookAfterReadingNoteUserPo::getUserId,tSdxBookAfterReadingNoteUserPo.getUserId());
            }
            if (tSdxBookAfterReadingNoteUserPo.getIsThumb()!=null ) {
                build.eq(TSdxBookAfterReadingNoteUserPo::getIsThumb,tSdxBookAfterReadingNoteUserPo.getIsThumb());
            }
            if (tSdxBookAfterReadingNoteUserPo.getBookInfoId()!=null ) {
                build.eq(TSdxBookAfterReadingNoteUserPo::getBookInfoId,tSdxBookAfterReadingNoteUserPo.getBookInfoId());
            }
            if (tSdxBookAfterReadingNoteUserPo.getIsPurchase()!=null ) {
                build.eq(TSdxBookAfterReadingNoteUserPo::getIsPurchase,tSdxBookAfterReadingNoteUserPo.getIsPurchase());
            }
            if (tSdxBookAfterReadingNoteUserPo.getBookAfterReadingNoteId()!=null ) {
                build.eq(TSdxBookAfterReadingNoteUserPo::getBookAfterReadingNoteId,tSdxBookAfterReadingNoteUserPo.getBookAfterReadingNoteId());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxBookAfterReadingNoteUserPo::getId, tSdxBookAfterReadingNoteUserPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxBookAfterReadingNoteUserPo.builder().build()    , build);
    }
}
