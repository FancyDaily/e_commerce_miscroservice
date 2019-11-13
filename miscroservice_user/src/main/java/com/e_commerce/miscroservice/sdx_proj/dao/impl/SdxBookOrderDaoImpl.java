package com.e_commerce.miscroservice.sdx_proj.dao.impl;

import com.alipay.api.domain.MyBkAccountVO;
import com.e_commerce.miscroservice.commons.entity.colligate.Page;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxBookOrderDao;
import com.e_commerce.miscroservice.sdx_proj.enums.SdxBookOrderEnum;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookInfoPo;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxBookOrderPo;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单的dao层
 */

@Repository
public class SdxBookOrderDaoImpl implements SdxBookOrderDao {
	private final int PAGE_SIZE = 10;

	@Override
	public int saveTSdxBookOrderIfNotExist(TSdxBookOrderPo tSdxBookOrderPo) {
		return tSdxBookOrderPo.save();
	}

	@Override
	public int modTSdxBookOrder(TSdxBookOrderPo tSdxBookOrderPo) {
		return tSdxBookOrderPo.update(tSdxBookOrderPo.build().eq(TSdxBookOrderPo::getId, tSdxBookOrderPo.getId()));
	}

	@Override
	public int delTSdxBookOrderByIds(Long... ids) {
		TSdxBookOrderPo tSdxBookOrderPo = TSdxBookOrderPo.builder().build();
		tSdxBookOrderPo.setDeletedFlag(Boolean.TRUE);
		return tSdxBookOrderPo.update(tSdxBookOrderPo.build().in(TSdxBookOrderPo::getId, ids));
	}

	@Override
	public TSdxBookOrderPo findTSdxBookOrderById(Long id) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookOrderPo.class);
		build.eq(TSdxBookOrderPo::getDeletedFlag, Boolean.FALSE);
		build.eq(TSdxBookOrderPo::getId, id);
		return MybatisPlus.getInstance().findOne(TSdxBookOrderPo.builder().build(), build);
	}

	@Override
	public List<TSdxBookOrderPo> findTSdxBookOrderByAll(TSdxBookOrderPo tSdxBookOrderPo, Integer page, Integer size) {
		MybatisPlusBuild build = new MybatisPlusBuild(TSdxBookOrderPo.class);
		build.eq(TSdxBookOrderPo::getDeletedFlag, Boolean.FALSE);
		if (tSdxBookOrderPo.getId() == null) {
			if (tSdxBookOrderPo.getType() != null) {
				build.eq(TSdxBookOrderPo::getType, tSdxBookOrderPo.getType());
			}
			if (tSdxBookOrderPo.getPrice() != null) {
				build.eq(TSdxBookOrderPo::getPrice, tSdxBookOrderPo.getPrice());
			}
			if (tSdxBookOrderPo.getStatus() != null) {
				build.eq(TSdxBookOrderPo::getStatus, tSdxBookOrderPo.getStatus());
			}
			if (StringUtils.isNotEmpty(tSdxBookOrderPo.getBookIds())) {
				build.like(TSdxBookOrderPo::getBookIds, tSdxBookOrderPo.getBookIds());
			}
			if (tSdxBookOrderPo.getShipType() != null) {
				build.eq(TSdxBookOrderPo::getShipType, tSdxBookOrderPo.getShipType());
			}
			if (tSdxBookOrderPo.getBookPrice() != null) {
				build.eq(TSdxBookOrderPo::getBookPrice, tSdxBookOrderPo.getBookPrice());
			}
			if (tSdxBookOrderPo.getShipPirce() != null) {
				build.eq(TSdxBookOrderPo::getShipPirce, tSdxBookOrderPo.getShipPirce());
			}
			if (tSdxBookOrderPo.getTotalPrice() != null) {
				build.eq(TSdxBookOrderPo::getTotalPrice, tSdxBookOrderPo.getTotalPrice());
			}
			if (tSdxBookOrderPo.getBookIfIs() != null) {
				build.eq(TSdxBookOrderPo::getBookIfIs, tSdxBookOrderPo.getBookIfIs());
			}
			if (tSdxBookOrderPo.getBookStationId() != null) {
				build.eq(TSdxBookOrderPo::getBookStationId, tSdxBookOrderPo.getBookStationId());
			}
			if (tSdxBookOrderPo.getScoreDiscount() != null) {
				build.eq(TSdxBookOrderPo::getScoreDiscount, tSdxBookOrderPo.getScoreDiscount());
			}
			if (tSdxBookOrderPo.getExactTotalScores() != null) {
				build.eq(TSdxBookOrderPo::getExactTotalScores, tSdxBookOrderPo.getExactTotalScores());
			}
			if (tSdxBookOrderPo.getShippingAddressId() != null) {
				build.eq(TSdxBookOrderPo::getShippingAddressId, tSdxBookOrderPo.getShippingAddressId());
			}
			if (tSdxBookOrderPo.getExpectedTotalScores() != null) {
				build.eq(TSdxBookOrderPo::getExpectedTotalScores, tSdxBookOrderPo.getExpectedTotalScores());
			}
			if (page == null) {
				page = 1;
			}
			IdUtil.setTotal(build);
			build.page(page, size == null ? PAGE_SIZE : size);
		} else {
			build.eq(TSdxBookOrderPo::getId, tSdxBookOrderPo.getId());
		}
		return MybatisPlus.getInstance().findAll(TSdxBookOrderPo.builder().build(), build);
	}

	@Override
	public List<TSdxBookOrderPo> selectByTypeAndStatus(int type, int status) {
		return MybatisPlus.getInstance().findAll(new TSdxBookOrderPo(), byTypeAndStatusBuild(type, status)
		);
	}

	private MybatisPlusBuild byTypeAndStatusBuild(int type, int status) {
		return baseBuild()
			.eq(TSdxBookOrderPo::getType, type)
			.eq(TSdxBookOrderPo::getStatus, status);
	}

	@Override
	public List<TSdxBookOrderPo> selectByBookInfoIdAndTypeAndStatus(Long bookInfoId, int type, int status) {
		return MybatisPlus.getInstance().findAll(new TSdxBookOrderPo(), buildContains4BookInfos(byTypeAndStatusBuild(type, status), bookInfoId)
		);
	}

	@Override
	public List<TSdxBookOrderPo> selectByBookInfoIdAndTypeAndStatus(Long bookInfoId, int type, int status, Page page) {
		MybatisPlusBuild build = buildContains4BookInfos(byTypeAndStatusBuild(type, status), bookInfoId);
		build = PageUtil.dealWithPage(build, page);
		return MybatisPlus.getInstance().findAll(new TSdxBookOrderPo(), build
		);
	}

	@Override
	public List<TSdxBookOrderPo> selectByBookInfoIdAndTypeAndStatus(Long bookInfoId, int type, int status, Page page, MybatisPlusBuild.OrderBuild... orderBuild) {
		MybatisPlusBuild build = buildContains4BookInfos(byTypeAndStatusBuild(type, status), bookInfoId);
		orderBuild = orderBuild == null ? new MybatisPlusBuild.OrderBuild[]{MybatisPlusBuild.OrderBuild.buildDesc(TSdxBookOrderPo::getCreateTime)} : orderBuild;
		build.orderBy(orderBuild);
		build = PageUtil.dealWithPage(build, page);
		return MybatisPlus.getInstance().findAll(new TSdxBookOrderPo(), build
		);
	}

	@Override
	public TSdxBookOrderPo selectByShippingAddressIdAndBookInfoIdsAndBookFeeAndUserIdAndShipFee(Long shippingAddressId, String bookInfoIds, Double bookFee, Long userId, Double shipFee, int code, int type) {
		return MybatisPlus.getInstance().findOne(new TSdxBookOrderPo(), baseBuild()
			.eq(TSdxBookOrderPo::getShippingAddressId, shippingAddressId)
			.eq(TSdxBookOrderPo::getBookIfIs, bookInfoIds)
			.eq(TSdxBookOrderPo::getBookPrice, bookFee)
			.eq(TSdxBookOrderPo::getUserId, userId)
			.eq(TSdxBookOrderPo::getShipPirce, shipFee)
			.eq(TSdxBookOrderPo::getStatus, code)
			.eq(TSdxBookOrderPo::getType, type)
		);
	}

	@Override
	public TSdxBookOrderPo selectByOrderNo(String out_trade_no) {
		return MybatisPlus.getInstance().findOne(new TSdxBookOrderPo(), baseBuild()
			.eq(TSdxBookOrderPo::getOrderNo, out_trade_no)
		);
	}

	@Override
	public TSdxBookOrderPo selectByPrimaryKey(Long orderId) {
		return MybatisPlus.getInstance().findOne(new TSdxBookOrderPo(), baseBuild()
			.eq(TSdxBookOrderPo::getId, orderId)
		);
	}

	@Override
	public List<TSdxBookOrderPo> purchaseList(Long userIds, Integer option, Integer pageNum, Integer pageSize) {
		Integer OPTION_ALL = -2;	//全部
		MybatisPlusBuild eq = listAllTypeBuild(userIds, option, OPTION_ALL);
		eq.eq(TSdxBookOrderPo::getType, SdxBookOrderEnum.TYPE_PURCHASE.getCode());
		return MybatisPlus.getInstance().findAll(new TSdxBookOrderPo(), eq.page(pageNum, pageSize));
	}

	@Override
	public List<TSdxBookOrderPo> donateList(Long userIds, Integer option, Integer pageNum, Integer pageSize) {
		Integer OPTION_ALL = -2;	//全部
		MybatisPlusBuild eq = listAllTypeBuild(userIds, option, OPTION_ALL);
		eq.eq(TSdxBookOrderPo::getType, SdxBookOrderEnum.TYPE_DONATE.getCode());
		return MybatisPlus.getInstance().findAll(new TSdxBookOrderPo(), eq.page(pageNum, pageSize));
	}

	private MybatisPlusBuild listAllTypeBuild(Long userIds, Integer option, Integer OPTION_ALL) {
		MybatisPlusBuild eq = baseBuild().eq(TSdxBookOrderPo::getUserId, userIds);
		eq = option == null || option.equals(OPTION_ALL) ? eq : eq.eq(TSdxBookOrderPo::getStatus, option);
		return eq;
	}

	private static MybatisPlusBuild buildContains4BookInfos(MybatisPlusBuild build, Long param) {
		String LIKE = "%";
		String like1 = LIKE + param + LIKE;
		String like2 = param + LIKE;
		String like3 = LIKE + param;
		return build.and()
			.groupBefore()
			.like(TSdxBookOrderPo::getBookIfIs, like1)
			.or()
			.like(TSdxBookOrderPo::getBookIfIs, like2)
			.or()
			.like(TSdxBookOrderPo::getBookIfIs, like3)
			.groupAfter();
	}

	public static void main(String[] args) {
//		String build = buildContains4BookInfos(baseBuild(), 13123L).build();
		MybatisPlusBuild build = baseBuild()
			.and()
			.like(TSdxBookOrderPo::getBookIfIs, "123");
		System.out.println(build.build());
	}

	private static MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TSdxBookInfoPo.class)
			.eq(TSdxBookOrderPo::getDeletedFlag, Boolean.FALSE);
	}
}
