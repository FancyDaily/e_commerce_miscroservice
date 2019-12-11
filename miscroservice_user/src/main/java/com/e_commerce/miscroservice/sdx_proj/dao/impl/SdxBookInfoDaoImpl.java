package com.e_commerce.miscroservice.sdx_proj.dao.impl;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookInfoDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
* 书籍信息的dao层
*
*/

@Repository
public class SdxBookInfoDaoImpl implements SdxBookInfoDao {
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
    public List<TSdxBookInfoPo> findTSdxBookInfoByAll(TSdxBookInfoPo tSdxBookInfoPo, Integer page, Integer size, Integer sortType, Integer filterType) {
    	final Integer FILTER_TYPE_NEW = 1;	//新书上架
    	final Integer FILTER_TYPE_DOUBAN_OVER = 2;	//豆瓣8.5
		final Integer FILTER_TYPE_NEW_THAT_YOU_CAN_NOT_AFFORD = 3;	//买不起的新书
        MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookInfoPo.class);
        build.eq(TSdxBookInfoPo::getDeletedFlag, Boolean.FALSE);
        final Long dayCnt = 7L;
        final Double overScores = 8.5;
        final Double overPrice = 45d;
        if(filterType != null) {
        	if(filterType.equals(FILTER_TYPE_NEW) || filterType.equals(FILTER_TYPE_NEW_THAT_YOU_CAN_NOT_AFFORD)) {
				long mills = System.currentTimeMillis() - DateUtil.interval * dayCnt;
				build.gte(TSdxBookInfoPo::getCreateTime, new Timestamp(mills).toString());
				if(filterType.equals(FILTER_TYPE_NEW_THAT_YOU_CAN_NOT_AFFORD)) {
					build.gte(TSdxBookInfoPo::getPrice, overPrice);
				}
			} else if(filterType.equals(FILTER_TYPE_DOUBAN_OVER)) {
				build.gte(TSdxBookInfoPo::getScoreDouban, overScores);
			}
		}

        if (tSdxBookInfoPo.getId() == null) {
            if (StringUtils.isNotEmpty(tSdxBookInfoPo.getName())) {
                build.like(TSdxBookInfoPo::getName,tSdxBookInfoPo.getName());
            }
            if (StringUtils.isNotEmpty(tSdxBookInfoPo.getPublisher())) {
                build.like(TSdxBookInfoPo::getPublisher,tSdxBookInfoPo.getPublisher());
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
            if (tSdxBookInfoPo.getTagId()!=null ) {
                build.eq(TSdxBookInfoPo::getTagId,tSdxBookInfoPo.getTagId());
            }
            if (tSdxBookInfoPo.getScoreDouban()!=null ) {
                build.eq(TSdxBookInfoPo::getScoreDouban,tSdxBookInfoPo.getScoreDouban());
            }
            if (StringUtils.isNotEmpty(tSdxBookInfoPo.getBindingStyle())) {
                build.like(TSdxBookInfoPo::getBindingStyle,tSdxBookInfoPo.getBindingStyle());
            }
            if (StringUtils.isNotEmpty(tSdxBookInfoPo.getTag())) {
                build.like(TSdxBookInfoPo::getTag,tSdxBookInfoPo.getTag());
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
            if (tSdxBookInfoPo.getTag() != null) {
            	build.eq(TSdxBookInfoPo::getTag, tSdxBookInfoPo.getTag());
			}
            if (tSdxBookInfoPo.getTagId() != null) {
            	build.eq(TSdxBookInfoPo::getTagId, tSdxBookInfoPo.getTagId());
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
			.eq(TSdxBookInfoPo::getId, id)
		);
	}

	@Override
	public TSdxBookInfoPo selectByName(String name) {
		return MybatisPlus.getInstance().findOne(new TSdxBookInfoPo(), baseBuild()
			.eq(TSdxBookInfoPo::getName, name)
		);
	}

	@Override
	public List<TSdxBookInfoPo> selectInIds(Long... bookInfoIds) {
		return MybatisPlus.getInstance().findAll(new TSdxBookInfoPo(), baseBuild()
			.in(TSdxBookInfoPo::getId, bookInfoIds)
		);
	}

	@Override
	public List<TSdxBookInfoPo> selectInIds(List<Long> bookInfoIds) {
		return MybatisPlus.getInstance().findAll(new TSdxBookInfoPo(), baseBuild()
			.in(TSdxBookInfoPo::getId, bookInfoIds)
		);
	}

	@Override
	public List<TSdxBookInfoPo> selectInServiceIds(List<Long> serviceIds) {
		return MybatisPlus.getInstance().findAll(new TSdxBookInfoPo(), baseBuild()
			.in(TSdxBookInfoPo::getServiceId, serviceIds)
		);
	}

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TSdxBookInfoPo.class)
			.eq(TSdxBookInfoPo::getDeletedFlag, Boolean.FALSE);
	}
}
