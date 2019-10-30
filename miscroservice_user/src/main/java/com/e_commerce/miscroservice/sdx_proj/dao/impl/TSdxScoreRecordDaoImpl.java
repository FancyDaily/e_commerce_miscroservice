package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxScoreRecordDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 积分流水的dao层
*
*/

@Repository
public class TSdxScoreRecordDaoImpl implements TSdxScoreRecordDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxScoreRecordIfNotExist(TSdxScoreRecordPo tSdxScoreRecordPo) {
        return tSdxScoreRecordPo.save();
    }
    @Override
    public int modTSdxScoreRecord(TSdxScoreRecordPo tSdxScoreRecordPo) {
        return tSdxScoreRecordPo.update(tSdxScoreRecordPo.build().eq(TSdxScoreRecordPo::getId, tSdxScoreRecordPo.getId()));
    }
    @Override
    public int delTSdxScoreRecordByIds(Long... ids) {
        TSdxScoreRecordPo tSdxScoreRecordPo = TSdxScoreRecordPo.builder().build();
        tSdxScoreRecordPo.setDeletedFlag(Boolean.TRUE);
        return tSdxScoreRecordPo.update(tSdxScoreRecordPo.build().in(TSdxScoreRecordPo::getId, ids));
    }
    @Override
    public TSdxScoreRecordPo findTSdxScoreRecordById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxScoreRecordPo.class);
        build.eq(TSdxScoreRecordPo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxScoreRecordPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxScoreRecordPo.builder().build(), build);
    }
    @Override
    public List<TSdxScoreRecordPo> findTSdxScoreRecordByAll(TSdxScoreRecordPo tSdxScoreRecordPo, Integer page, Integer size) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxScoreRecordPo.class);
        build.eq(TSdxScoreRecordPo::getDeletedFlag, Boolean.FALSE);
        if (tSdxScoreRecordPo.getId() == null) {
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxScoreRecordPo::getId, tSdxScoreRecordPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxScoreRecordPo.builder().build()    , build);
    }
}
