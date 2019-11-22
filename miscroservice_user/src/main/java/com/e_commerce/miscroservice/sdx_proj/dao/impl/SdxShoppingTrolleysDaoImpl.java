package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxShoppingTrolleysDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShoppingTrolleysPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
* 购物车的dao层
*
*/

@Repository
public class SdxShoppingTrolleysDaoImpl implements SdxShoppingTrolleysDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxShoppingTrolleysIfNotExist(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo) {
        return tSdxShoppingTrolleysPo.save();
    }
    @Override
    public int modTSdxShoppingTrolleys(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo) {
        return tSdxShoppingTrolleysPo.update(tSdxShoppingTrolleysPo.build().eq(TSdxShoppingTrolleysPo::getId, tSdxShoppingTrolleysPo.getId()));
    }
    @Override
    public int delTSdxShoppingTrolleysByIds(Long... ids) {
		return delTSdxShoppingTrolleysByIds(Arrays.asList(ids));
    }

	@Override
	public int delTSdxShoppingTrolleysByIds(List<Long> ids) {
		TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo = TSdxShoppingTrolleysPo.builder().build();
		tSdxShoppingTrolleysPo.setDeletedFlag(Boolean.TRUE);
		return tSdxShoppingTrolleysPo.update(tSdxShoppingTrolleysPo.build().in(TSdxShoppingTrolleysPo::getId, ids));
	}

    @Override
    public TSdxShoppingTrolleysPo findTSdxShoppingTrolleysById(Long id) {
		MybatisPlusBuild build = baseBuild();
		build.eq(TSdxShoppingTrolleysPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxShoppingTrolleysPo.builder().build(), build);
    }

	private MybatisPlusBuild baseBuild() {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxShoppingTrolleysPo.class);
		build.eq(TSdxShoppingTrolleysPo::getDeletedFlag, Boolean.FALSE);
		return build;
	}

	@Override
    public List<TSdxShoppingTrolleysPo> findTSdxShoppingTrolleysByAll(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo, Integer page, Integer size) {
		MybatisPlusBuild build = baseBuild();
		if (tSdxShoppingTrolleysPo.getId() == null) {
            if (tSdxShoppingTrolleysPo.getUserId()!=null ) {
                build.eq(TSdxShoppingTrolleysPo::getUserId,tSdxShoppingTrolleysPo.getUserId());
            }
            if (tSdxShoppingTrolleysPo.getBookInfoId()!=null ) {
                build.eq(TSdxShoppingTrolleysPo::getBookInfoId,tSdxShoppingTrolleysPo.getBookInfoId());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxShoppingTrolleysPo::getId, tSdxShoppingTrolleysPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxShoppingTrolleysPo.builder().build()    , build);
    }

	@Override
	public TSdxShoppingTrolleysPo selectByPo(TSdxShoppingTrolleysPo tSdxShoppingTrolleysPo) {
		Long userId = tSdxShoppingTrolleysPo.getUserId();
		Long bookInfoId = tSdxShoppingTrolleysPo.getBookInfoId();
		return MybatisPlus.getInstance().findOne(new TSdxShoppingTrolleysPo(), byUserIdBuild(userId)
			.eq(TSdxShoppingTrolleysPo::getBookInfoId, bookInfoId)
		);
	}

	@Override
	public List<TSdxShoppingTrolleysPo> selectByUserId(Long id) {
		return MybatisPlus.getInstance().findAll(new TSdxShoppingTrolleysPo(), byUserIdBuild(id)
		);
	}

	private MybatisPlusBuild byUserIdBuild(Long id) {
		return baseBuild()
			.eq(TSdxShoppingTrolleysPo::getUserId, id);
	}

	@Override
	public List<TSdxShoppingTrolleysPo> selectByUserIdPage(Long id, Integer pageNum, Integer pageSize) {
		return MybatisPlus.getInstance().findAll(new TSdxShoppingTrolleysPo(), PageUtil.pageBuild(byUserIdBuild(id), pageNum, pageSize));
	}

	@Override
	public List<TSdxShoppingTrolleysPo> selectByUserIdInBookInfoIds(Long userId, List<Long> bookInfoIdList) {
		return MybatisPlus.getInstance().findAll(new TSdxShoppingTrolleysPo(), baseBuild()
			.eq(TSdxShoppingTrolleysPo::getUserId, userId)
			.in(TSdxShoppingTrolleysPo::getBookInfoId, bookInfoIdList)
		);
	}
}
