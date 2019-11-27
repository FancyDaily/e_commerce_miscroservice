package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxTagDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxTagPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* 书本类型的dao层
*
*/

@Repository
public class SdxTagDaoImpl implements SdxTagDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxTagIfNotExist(TSdxTagPo tSdxTagPo) {
        return tSdxTagPo.save();
    }
    @Override
    public int modTSdxTag(TSdxTagPo tSdxTagPo) {
        return tSdxTagPo.update(tSdxTagPo.build().eq(TSdxTagPo::getId, tSdxTagPo.getId()));
    }
    @Override
    public int delTSdxTagByIds(Long... ids) {
        TSdxTagPo tSdxTagPo = TSdxTagPo.builder().build();
        tSdxTagPo.setDeletedFlag(Boolean.TRUE);
        return tSdxTagPo.update(tSdxTagPo.build().in(TSdxTagPo::getId, ids));
    }
    @Override
    public TSdxTagPo findTSdxTagById(Long id) {
		MybatisPlusBuild build = baseBuild();
		build.eq(TSdxTagPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxTagPo.builder().build(), build);
    }

    @Override
    public List<TSdxTagPo> findTSdxTagByAll(TSdxTagPo tSdxTagPo, Integer page, Integer size) {
		MybatisPlusBuild build = baseBuild();
		if (tSdxTagPo.getId() == null) {
            if (StringUtils.isNotEmpty(tSdxTagPo.getName())) {
                build.like(TSdxTagPo::getName,tSdxTagPo.getName());
            }
            if (tSdxTagPo.getType()!=null ) {
                build.eq(TSdxTagPo::getType,tSdxTagPo.getType());
            }
            if (page == null) {
                page = 1;
            }
            IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxTagPo::getId, tSdxTagPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxTagPo.builder().build()    , build);
    }

	private MybatisPlusBuild baseBuild() {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxTagPo.class);
		build.eq(TSdxTagPo::getDeletedFlag, Boolean.FALSE);
		return build;
	}

	@Override
	public List<TSdxTagPo> selectInNames(String... tagNames) {
		//处理其中为null的元素
		List<String> tags = Arrays.stream(tagNames)
			.filter(StringUtil::isNotEmpty).collect(Collectors.toList());
		return MybatisPlus.getInstance().findAll(new TSdxTagPo(), baseBuild()
			.in(TSdxTagPo::getName, tags)
		);
	}
}
