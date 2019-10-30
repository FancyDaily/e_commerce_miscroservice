package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookTicketDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookTicketPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 预定书券的dao层
*
*/

@Repository
public class SdxBookTicketDaoImpl implements SdxBookTicketDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxBookTicktIfNotExist(TSdxBookTicketPo tSdxBookTicketPo) {
        return tSdxBookTicketPo.save();
    }
    @Override
    public int modTSdxBookTickt(TSdxBookTicketPo tSdxBookTicketPo) {
        return tSdxBookTicketPo.update(tSdxBookTicketPo.build().eq(TSdxBookTicketPo::getId, tSdxBookTicketPo.getId()));
    }
    @Override
    public int delTSdxBookTicktByIds(Long... ids) {
        TSdxBookTicketPo tSdxBookTicketPo = TSdxBookTicketPo.builder().build();
        tSdxBookTicketPo.setDeletedFlag(Boolean.TRUE);
        return tSdxBookTicketPo.update(tSdxBookTicketPo.build().in(TSdxBookTicketPo::getId, ids));
    }
    @Override
    public TSdxBookTicketPo findTSdxBookTicktById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookTicketPo.class);
        build.eq(TSdxBookTicketPo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxBookTicketPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxBookTicketPo.builder().build(), build);
    }
    @Override
    public List<TSdxBookTicketPo> findTSdxBookTicktByAll(TSdxBookTicketPo tSdxBookTicketPo, Integer page, Integer size) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookTicketPo.class);
        build.eq(TSdxBookTicketPo::getDeletedFlag, Boolean.FALSE);
        if (tSdxBookTicketPo.getId() == null) {
            if (tSdxBookTicketPo.getExpire()!=null ) {
                build.eq(TSdxBookTicketPo::getExpire, tSdxBookTicketPo.getExpire());
            }
            if (tSdxBookTicketPo.getUserId()!=null ) {
                build.eq(TSdxBookTicketPo::getUserId, tSdxBookTicketPo.getUserId());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxBookTicketPo::getId, tSdxBookTicketPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxBookTicketPo.builder().build()    , build);
    }

	@Override
	public List<TSdxBookTicketPo> selectByUserId(Long userId) {
		return MybatisPlus.getInstance().findAll(new TSdxBookTicketPo(), new MybatisPlusBuild(TSdxBookTicketPo.class)
			.gt(TSdxBookTicketPo::getExpire, System.currentTimeMillis())
			.eq(TSdxBookTicketPo::getUserId, userId)
		);
	}
}
