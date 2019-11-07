package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxScoreRecordDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxScoreRecordPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 积分流水的dao层
*
*/

@Repository
public class SdxScoreRecordDaoImpl implements SdxScoreRecordDao {
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

	@Override
	public List<TSdxScoreRecordPo> selectByUserIdPage(Long userId, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild eq = byUserIdBuild(userId);
		eq = PageUtil.pageBuild(eq, pageNum, pageSize);
		return MybatisPlus.getInstance().findAll(new TSdxScoreRecordPo(), eq);
	}

	@Override
	public List<TSdxScoreRecordPo> selectByUserId(Long userId) {
		return selectByUserId(userId, null, null, false);
	}

	@Override
	public List<TSdxScoreRecordPo> selectByUserId(Long userId, Integer pageNum, Integer pageSize, boolean isPage) {
		MybatisPlusBuild eq = byUserIdBuild(userId);
		eq = isPage? PageUtil.pageBuild(eq, pageNum, pageSize) : eq;
		return MybatisPlus.getInstance().findAll(new TSdxScoreRecordPo(), eq);
	}

	private MybatisPlusBuild byUserIdBuild(Long userId) {
		return baseBuild()
			.eq(TSdxScoreRecordPo::getUserId, userId);
	}

	@Override
	public List<TSdxScoreRecordPo> selectByUserIdAndInOutPage(Long userId, Integer inOut, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild eq = byUserIdBuild(userId);
		eq = inOut == null? eq: eq.eq(TSdxScoreRecordPo::getInOut, inOut);
		return MybatisPlus.getInstance().findAll(new TSdxScoreRecordPo(), PageUtil.pageBuild(eq, pageNum, pageSize));
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TSdxScoreRecordPo.class)
			.eq(TSdxScoreRecordPo::getDeletedFlag, Boolean.FALSE);
	}
}
