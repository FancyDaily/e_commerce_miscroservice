package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookTransRecordDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTransRecordPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 书籍漂流记录的dao层
*
*/

@Repository
public class TSdxBookTransRecordDaoImpl implements TSdxBookTransRecordDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxBookTransRecordIfNotExist(TSdxBookTransRecordPo tSdxBookTransRecordPo) {
        return tSdxBookTransRecordPo.save();
    }
    @Override
    public int modTSdxBookTransRecord(TSdxBookTransRecordPo tSdxBookTransRecordPo) {
        return tSdxBookTransRecordPo.update(tSdxBookTransRecordPo.build().eq(TSdxBookTransRecordPo::getId, tSdxBookTransRecordPo.getId()));
    }
    @Override
    public int delTSdxBookTransRecordByIds(Long... ids) {
        TSdxBookTransRecordPo tSdxBookTransRecordPo = TSdxBookTransRecordPo.builder().build();
        tSdxBookTransRecordPo.setDeletedFlag(Boolean.TRUE);
        return tSdxBookTransRecordPo.update(tSdxBookTransRecordPo.build().in(TSdxBookTransRecordPo::getId, ids));
    }
    @Override
    public TSdxBookTransRecordPo findTSdxBookTransRecordById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookTransRecordPo.class);
        build.eq(TSdxBookTransRecordPo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxBookTransRecordPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxBookTransRecordPo.builder().build(), build);
    }
    @Override
    public List<TSdxBookTransRecordPo> findTSdxBookTransRecordByAll(TSdxBookTransRecordPo tSdxBookTransRecordPo, Integer page, Integer size) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookTransRecordPo.class);
        build.eq(TSdxBookTransRecordPo::getDeletedFlag, Boolean.FALSE);
        if (tSdxBookTransRecordPo.getId() == null) {
            if (tSdxBookTransRecordPo.getType()!=null ) {
                build.eq(TSdxBookTransRecordPo::getType,tSdxBookTransRecordPo.getType());
            }
            if (tSdxBookTransRecordPo.getBookId()!=null ) {
                build.eq(TSdxBookTransRecordPo::getBookId,tSdxBookTransRecordPo.getBookId());
            }
            if (tSdxBookTransRecordPo.getUserId()!=null ) {
                build.eq(TSdxBookTransRecordPo::getUserId,tSdxBookTransRecordPo.getUserId());
            }
            if (tSdxBookTransRecordPo.getBookInfoId()!=null ) {
                build.eq(TSdxBookTransRecordPo::getBookInfoId,tSdxBookTransRecordPo.getBookInfoId());
            }
            if (StringUtils.isNotEmpty(tSdxBookTransRecordPo.getDescription())) {
                build.like(TSdxBookTransRecordPo::getDescription,tSdxBookTransRecordPo.getDescription());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxBookTransRecordPo::getId, tSdxBookTransRecordPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxBookTransRecordPo.builder().build()    , build);
    }
}
