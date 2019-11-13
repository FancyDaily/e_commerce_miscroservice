package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxWishListDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxWishListPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 心愿单的dao层
*
*/

@Repository
public class SdxWishListDaoImpl implements SdxWishListDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxWishListIfNotExist(TSdxWishListPo tSdxWishListPo) {
        return tSdxWishListPo.save();
    }
    @Override
    public int modTSdxWishList(TSdxWishListPo tSdxWishListPo) {
        return tSdxWishListPo.update(tSdxWishListPo.build().eq(TSdxWishListPo::getId, tSdxWishListPo.getId()));
    }
    @Override
    public int delTSdxWishListByIds(Long... ids) {
        TSdxWishListPo tSdxWishListPo = TSdxWishListPo.builder().build();
        tSdxWishListPo.setDeletedFlag(Boolean.TRUE);
        return tSdxWishListPo.update(tSdxWishListPo.build().in(TSdxWishListPo::getId, ids));
    }
    @Override
    public TSdxWishListPo findTSdxWishListById(Long id) {
		MybatisPlusBuild build = baseBuild();
		build.eq(TSdxWishListPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxWishListPo.builder().build(), build);
    }
    @Override
    public List<TSdxWishListPo> findTSdxWishListByAll(TSdxWishListPo tSdxWishListPo, Integer page, Integer size) {
		MybatisPlusBuild build = baseBuild();
		if (tSdxWishListPo.getId() == null) {
            if (tSdxWishListPo.getBookInfoId()!=null ) {
                build.eq(TSdxWishListPo::getBookInfoId,tSdxWishListPo.getBookInfoId());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxWishListPo::getId, tSdxWishListPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxWishListPo.builder().build()    , build);
    }

	private MybatisPlusBuild baseBuild() {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxWishListPo.class);
		build.eq(TSdxWishListPo::getDeletedFlag, Boolean.FALSE);
		return build;
	}

	@Override
	public List<TSdxWishListPo> selectByUserId(Long userId, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild eq = baseBuild()
			.eq(TSdxWishListPo::getUserId, userId);
		return MybatisPlus.getInstance().findAll(new TSdxWishListPo(), eq.page(pageNum, pageSize)
		);
	}
}
