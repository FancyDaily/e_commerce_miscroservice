package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.sdx_proj.dao.TSdxBookInfoDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.function.Function;

/**
* 书籍信息的dao层
*
*/

@Repository
public class TSdxBookInfoDaoImpl implements TSdxBookInfoDao {
        private final int PAGE_SIZE = 10;
    @Override
    public int saveTSdxBookInfoIfNotExist(TSdxBookInfoPo tSdxBookInfoPo) {
        return tSdxBookInfoPo.save();
    }
    @Override
    public int modTSdxBookInfo(TSdxBookInfoPo tSdxBookInfoPo) {
        return tSdxBookInfoPo.update(tSdxBookInfoPo.build().eq(TSdxBookInfoPo::getId, tSdxBookInfoPo.getId()));
    }
    @Override
    public int delTSdxBookInfoByIds(Long... ids) {
        TSdxBookInfoPo tSdxBookInfoPo = TSdxBookInfoPo.builder().build();
        tSdxBookInfoPo.setDeletedFlag(Boolean.TRUE);
        return tSdxBookInfoPo.update(tSdxBookInfoPo.build().in(TSdxBookInfoPo::getId, ids));
    }
    @Override
    public TSdxBookInfoPo findTSdxBookInfoById(Long id) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookInfoPo.class);
        build.eq(TSdxBookInfoPo::getDeletedFlag, Boolean.FALSE);
        build.eq(TSdxBookInfoPo::getId, id);
        return MybatisPlus.getInstance().findOne( TSdxBookInfoPo.builder().build(), build);
    }
    @Override
    public List<TSdxBookInfoPo> findTSdxBookInfoByAll(TSdxBookInfoPo tSdxBookInfoPo, Integer page, Integer size, Integer sortType) {
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookInfoPo.class);
        build.eq(TSdxBookInfoPo::getDeletedFlag, Boolean.FALSE);
        if (tSdxBookInfoPo.getId() == null) {
            if (StringUtils.isNotEmpty(tSdxBookInfoPo.getName())) {
                build.like(TSdxBookInfoPo::getName,tSdxBookInfoPo.getName());
            }
            if (StringUtils.isNotEmpty(tSdxBookInfoPo.getPress())) {
                build.like(TSdxBookInfoPo::getPress,tSdxBookInfoPo.getPress());
            }
            if (tSdxBookInfoPo.getPrice()!=null ) {
                build.eq(TSdxBookInfoPo::getPrice,tSdxBookInfoPo.getPrice());
            }
            if (StringUtils.isNotEmpty(tSdxBookInfoPo.getAuthor())) {
                build.like(TSdxBookInfoPo::getAuthor,tSdxBookInfoPo.getAuthor());
            }
            if (StringUtils.isNotEmpty(tSdxBookInfoPo.getCatalog())) {
                build.like(TSdxBookInfoPo::getCatalog,tSdxBookInfoPo.getCatalog());
            }
            if (tSdxBookInfoPo.getCategoryId()!=null ) {
                build.eq(TSdxBookInfoPo::getCategoryId,tSdxBookInfoPo.getCategoryId());
            }
            if (tSdxBookInfoPo.getScoreDouban()!=null ) {
                build.eq(TSdxBookInfoPo::getScoreDouban,tSdxBookInfoPo.getScoreDouban());
            }
            if (StringUtils.isNotEmpty(tSdxBookInfoPo.getBindingStyle())) {
                build.like(TSdxBookInfoPo::getBindingStyle,tSdxBookInfoPo.getBindingStyle());
            }
            if (StringUtils.isNotEmpty(tSdxBookInfoPo.getCategoryName())) {
                build.like(TSdxBookInfoPo::getCategoryName,tSdxBookInfoPo.getCategoryName());
            }
            if (StringUtils.isNotEmpty(tSdxBookInfoPo.getIntroduction())) {
                build.like(TSdxBookInfoPo::getIntroduction,tSdxBookInfoPo.getIntroduction());
            }
            if (tSdxBookInfoPo.getMaximumReserve()!=null ) {
                build.eq(TSdxBookInfoPo::getMaximumReserve,tSdxBookInfoPo.getMaximumReserve());
            }
            if (tSdxBookInfoPo.getMaximumDiscount()!=null ) {
                build.eq(TSdxBookInfoPo::getMaximumDiscount,tSdxBookInfoPo.getMaximumDiscount());
            }
            if (page == null) {
                page = 1;
            }

			//排序
			Integer COMPLEX_SORT = 0;
			Integer SOLDNUM_SORT = 1;
			Integer SCORE_SORT = 2;
			Integer CHEAP_SORT = 3;

			if(sortType == null || COMPLEX_SORT.equals(sortType)) {	//综合
				build.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TSdxBookInfoPo::getSoldNum),
					MybatisPlusBuild.OrderBuild.buildDesc(TSdxBookInfoPo::getScoreDouban),
					MybatisPlusBuild.OrderBuild.buildDesc(TSdxBookInfoPo::getPrice));
			} else if (SOLDNUM_SORT.equals(sortType)) {	//销量优先
				build.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TSdxBookInfoPo::getSoldNum));
			} else if (SCORE_SORT.equals(sortType)) {	//评分优先
				build.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TSdxBookInfoPo::getScoreDouban));
			} else if(CHEAP_SORT.equals(sortType)){		//价格优先
				build.orderBy(MybatisPlusBuild.OrderBuild.buildDesc(TSdxBookInfoPo::getPrice));
			}

			IdUtil.setTotal(build);
            build.page(page, size==null?PAGE_SIZE:size);
        }
        else {
            build.eq(TSdxBookInfoPo::getId, tSdxBookInfoPo.getId());
        }
        return MybatisPlus.getInstance().findAll( TSdxBookInfoPo.builder().build()    , build);
    }

	@Override
	public TSdxBookInfoPo selectByPrimaryKey(Long id) {
		return MybatisPlus.getInstance().findOne(new TSdxBookInfoPo(), baseBuild()
		);
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TSdxBookInfoPo.class)
			.eq(TSdxBookInfoPo::getDeletedFlag, Boolean.FALSE);
	}
}
