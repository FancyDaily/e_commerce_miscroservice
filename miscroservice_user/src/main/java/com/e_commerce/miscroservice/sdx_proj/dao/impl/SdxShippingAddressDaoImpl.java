package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxShippingAddressDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShippingAddressPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
* 配送地址的dao层
*
*/

@Repository
public class SdxShippingAddressDaoImpl implements SdxShippingAddressDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxShippingAddressIfNotExist(TSdxShippingAddressPo tSdxShippingAddressPo) {
        return tSdxShippingAddressPo.save();
    }
    @Override
    public int modTSdxShippingAddress(TSdxShippingAddressPo tSdxShippingAddressPo) {
        return tSdxShippingAddressPo.update(tSdxShippingAddressPo.build().eq(TSdxShippingAddressPo::getId, tSdxShippingAddressPo.getId()));
    }
    @Override
    public int delTSdxShippingAddressByIds(Long... ids) {
        TSdxShippingAddressPo tSdxShippingAddressPo = TSdxShippingAddressPo.builder().build();
        tSdxShippingAddressPo.setDeletedFlag(Boolean.TRUE);
        return tSdxShippingAddressPo.update(tSdxShippingAddressPo.build().in(TSdxShippingAddressPo::getId, ids));
    }
    @Override
    public TSdxShippingAddressPo findTSdxShippingAddressById(Long id) {
		MybatisPlusBuild build = baseBuild();
		build.eq(TSdxShippingAddressPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxShippingAddressPo.builder().build(), build);
    }

	private MybatisPlusBuild baseBuild() {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxShippingAddressPo.class);
		build.eq(TSdxShippingAddressPo::getDeletedFlag, Boolean.FALSE);
		return build;
	}

	@Override
    public List<TSdxShippingAddressPo> findTSdxShippingAddressByAll(TSdxShippingAddressPo tSdxShippingAddressPo, Integer page, Integer size) {
		MybatisPlusBuild build = baseBuild();
		if (tSdxShippingAddressPo.getId() == null) {
            if (StringUtils.isNotEmpty(tSdxShippingAddressPo.getCity())) {
                build.like(TSdxShippingAddressPo::getCity,tSdxShippingAddressPo.getCity());
            }
            if (StringUtils.isNotEmpty(tSdxShippingAddressPo.getName())) {
                build.like(TSdxShippingAddressPo::getName,tSdxShippingAddressPo.getName());
            }
            if (StringUtils.isNotEmpty(tSdxShippingAddressPo.getCounty())) {
                build.like(TSdxShippingAddressPo::getCounty,tSdxShippingAddressPo.getCounty());
            }
            if (StringUtils.isNotEmpty(tSdxShippingAddressPo.getStreet())) {
                build.like(TSdxShippingAddressPo::getStreet,tSdxShippingAddressPo.getStreet());
            }
            if (tSdxShippingAddressPo.getUserId()!=null ) {
                build.eq(TSdxShippingAddressPo::getUserId,tSdxShippingAddressPo.getUserId());
            }
            if (StringUtils.isNotEmpty(tSdxShippingAddressPo.getUserTel())) {
                build.like(TSdxShippingAddressPo::getUserTel,tSdxShippingAddressPo.getUserTel());
            }
            if (StringUtils.isNotEmpty(tSdxShippingAddressPo.getProvince())) {
                build.like(TSdxShippingAddressPo::getProvince,tSdxShippingAddressPo.getProvince());
            }
            if (StringUtils.isNotEmpty(tSdxShippingAddressPo.getDetailAddress())) {
                build.like(TSdxShippingAddressPo::getDetailAddress,tSdxShippingAddressPo.getDetailAddress());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxShippingAddressPo::getId, tSdxShippingAddressPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxShippingAddressPo.builder().build()    , build);
    }

	@Override
	public List<TSdxShippingAddressPo> selectByUserId(Long id, boolean isPage, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild eq = baseBuild()
			.eq(TSdxShippingAddressPo::getUserId, id);
		return MybatisPlus.getInstance().findAll(new TSdxShippingAddressPo(), isPage? PageUtil.pageBuild(eq, pageNum, pageSize) : eq
		);
	}
}
