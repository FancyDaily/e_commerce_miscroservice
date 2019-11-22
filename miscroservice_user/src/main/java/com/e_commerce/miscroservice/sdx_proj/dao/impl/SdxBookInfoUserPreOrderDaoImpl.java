package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookInfoUserPreOrderDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoUserPreOrderPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;

/**
* 书籍信息预定(书籍信息用户关系)的dao层
*
*/

@Repository
public class SdxBookInfoUserPreOrderDaoImpl implements SdxBookInfoUserPreOrderDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxBookInfoUserPreOrderIfNotExist(TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo) {
        return tSdxBookInfoUserPreOrderPo.save();
    }
    @Override
    public int modTSdxBookInfoUserPreOrder(TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo) {
        return tSdxBookInfoUserPreOrderPo.update(tSdxBookInfoUserPreOrderPo.build().eq(TSdxBookInfoUserPreOrderPo::getId, tSdxBookInfoUserPreOrderPo.getId()));
    }
    @Override
    public int delTSdxBookInfoUserPreOrderByIds(Long... ids) {
        TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo = TSdxBookInfoUserPreOrderPo.builder().build();
        tSdxBookInfoUserPreOrderPo.setDeletedFlag(Boolean.TRUE);
        return tSdxBookInfoUserPreOrderPo.update(tSdxBookInfoUserPreOrderPo.build().in(TSdxBookInfoUserPreOrderPo::getId, ids));
    }
    @Override
    public TSdxBookInfoUserPreOrderPo findTSdxBookInfoUserPreOrderById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookInfoUserPreOrderPo.class);
        build.eq(TSdxBookInfoUserPreOrderPo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxBookInfoUserPreOrderPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxBookInfoUserPreOrderPo.builder().build(), build);
    }
    @Override
    public List<TSdxBookInfoUserPreOrderPo> findTSdxBookInfoUserPreOrderByAll(TSdxBookInfoUserPreOrderPo tSdxBookInfoUserPreOrderPo, Integer page, Integer size) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookInfoUserPreOrderPo.class);
        build.eq(TSdxBookInfoUserPreOrderPo::getDeletedFlag, Boolean.FALSE);
        if (tSdxBookInfoUserPreOrderPo.getId() == null) {
            if (tSdxBookInfoUserPreOrderPo.getUserId()!=null ) {
                build.eq(TSdxBookInfoUserPreOrderPo::getUserId,tSdxBookInfoUserPreOrderPo.getUserId());
            }
            if (tSdxBookInfoUserPreOrderPo.getBookInfoId()!=null ) {
                build.eq(TSdxBookInfoUserPreOrderPo::getBookInfoId,tSdxBookInfoUserPreOrderPo.getBookInfoId());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxBookInfoUserPreOrderPo::getId, tSdxBookInfoUserPreOrderPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxBookInfoUserPreOrderPo.builder().build()    , build);
    }

	@Override
	public List<TSdxBookInfoUserPreOrderPo> selectByBookInfoId(Long id) {
		return MybatisPlus.getInstance().findAll(new TSdxBookInfoUserPreOrderPo(), baseBuild()
			.eq(TSdxBookInfoUserPreOrderPo::getBookInfoId, id)
		);
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TSdxBookInfoUserPreOrderPo.class)
			.eq(TSdxBookInfoUserPreOrderPo::getDeletedFlag, Boolean.FALSE);
	}

	@Override
	public List<TSdxBookInfoUserPreOrderPo> selectByUserIdAndBookInfoId(Long userId, Long bookInfoId) {
		return MybatisPlus.getInstance().findAll(new TSdxBookInfoUserPreOrderPo(), baseBuild()
			.eq(TSdxBookInfoUserPreOrderPo::getUserId, userId)
			.eq(TSdxBookInfoUserPreOrderPo::getBookInfoId, bookInfoId)
		);
	}

	@Override
	public int update(List<TSdxBookInfoUserPreOrderPo> toUpdater) {
		List<Long> toUpdateIds = toUpdater.stream()
			.map(TSdxBookInfoUserPreOrderPo::getId).collect(Collectors.toList());
		return MybatisPlus.getInstance().update(toUpdater, baseBuild()
			.in(TSdxBookInfoUserPreOrderPo::getId, toUpdateIds)
		);
	}
}
