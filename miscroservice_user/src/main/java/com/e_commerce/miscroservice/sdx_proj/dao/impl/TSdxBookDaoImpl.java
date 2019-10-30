package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 书袋熊书籍的dao层
*
*/

@Repository
public class TSdxBookDaoImpl implements TSdxBookDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxBookIfNotExist(TSdxBookPo tSdxBookPo) {
        return tSdxBookPo.save();
    }
    @Override
    public int modTSdxBook(TSdxBookPo tSdxBookPo) {
        return tSdxBookPo.update(tSdxBookPo.build().eq(TSdxBookPo::getId, tSdxBookPo.getId()));
    }
    @Override
    public int delTSdxBookByIds(Long... ids) {
        TSdxBookPo tSdxBookPo = TSdxBookPo.builder().build();
        tSdxBookPo.setDeletedFlag(Boolean.TRUE);
        return tSdxBookPo.update(tSdxBookPo.build().in(TSdxBookPo::getId, ids));
    }
    @Override
    public TSdxBookPo findTSdxBookById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookPo.class);
        build.eq(TSdxBookPo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxBookPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxBookPo.builder().build(), build);
    }
    @Override
    public List<TSdxBookPo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size) {
    	return findTSdxBookByAll(tSdxBookPo, page, size, null);
    }

	@Override
	public List<TSdxBookPo> findTSdxBookByAll(TSdxBookPo tSdxBookPo, Integer page, Integer size, Integer sortType) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookPo.class);
		build.eq(TSdxBookPo::getDeletedFlag, Boolean.FALSE);
		if (tSdxBookPo.getId() == null) {
			if (tSdxBookPo.getStatus()!=null ) {
				build.eq(TSdxBookPo::getStatus,tSdxBookPo.getStatus());
			}
			if (tSdxBookPo.getDonaterId()!=null ) {
				build.eq(TSdxBookPo::getDonaterId,tSdxBookPo.getDonaterId());
			}
			if (tSdxBookPo.getServiceId()!=null ) {
				build.eq(TSdxBookPo::getServiceId,tSdxBookPo.getServiceId());
			}
			if (tSdxBookPo.getBookInfoId()!=null ) {
				build.eq(TSdxBookPo::getBookInfoId,tSdxBookPo.getBookInfoId());
			}
			if (tSdxBookPo.getExactPrice()!=null ) {
				build.eq(TSdxBookPo::getExactPrice,tSdxBookPo.getExactPrice());
			}
			if (tSdxBookPo.getExactScore()!=null ) {
				build.eq(TSdxBookPo::getExactScore,tSdxBookPo.getExactScore());
			}
			if (tSdxBookPo.getExpectedScore()!=null ) {
				build.eq(TSdxBookPo::getExpectedScore,tSdxBookPo.getExpectedScore());
			}
			if (tSdxBookPo.getCurrentOwnerId()!=null ) {
				build.eq(TSdxBookPo::getCurrentOwnerId,tSdxBookPo.getCurrentOwnerId());
			}
			if (page == null) {
				page = 1;
			}
			//排序
			if(sortType != null) {

			}



			IdUtil.setTotal(build);
			build.page(page, size==null?PAGE_SIZE:size);
		}
		else {
			build.eq(TSdxBookPo::getId, tSdxBookPo.getId());
		}
		return MybatisPlus.getInstance().findAll( TSdxBookPo.builder().build()    , build);
	}

	@Override
	public List<TSdxBookPo> selectByBookInfoIdAndStatus(long bookInfoId, int status) {
		return MybatisPlus.getInstance().findAll(new TSdxBookPo(), baseBuild()
			.eq(TSdxBookPo::getBookInfoId, bookInfoId)
			.eq(TSdxBookPo::getStatus, status)
		);
	}

	@Override
	public List<TSdxBookPo> selectByBookInfoIdInStatus(long bookInfoId, List<Integer> status) {
		return MybatisPlus.getInstance().findAll(new TSdxBookPo(), baseBuild()
			.eq(TSdxBookPo::getBookInfoId, bookInfoId)
			.in(TSdxBookPo::getStatus, status)
		);
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TSdxBookPo.class)
			.eq(TSdxBookPo::getDeletedFlag, Boolean.FALSE);
	}
}
