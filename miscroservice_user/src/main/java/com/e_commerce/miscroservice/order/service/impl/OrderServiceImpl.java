package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.order.service.OrderService;
import com.e_commerce.miscroservice.order.vo.PageOrderReturnView;
import com.e_commerce.miscroservice.order.vo.PageOrderParamView;
import com.e_commerce.miscroservice.product.controller.ProductCommonController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class OrderServiceImpl extends BaseService implements OrderService {

	@Autowired
	ProductCommonController productService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public int saveOrder(TOrder order) {
		return orderDao.saveOneOrder(order);
	}

	@Override
	public QueryResult<PageOrderReturnView> list(PageOrderParamView param, TUser user) {
		QueryResult<PageOrderReturnView> result = new QueryResult<>();
		List<PageOrderReturnView> listReturn = new ArrayList<>();
		// 为了兼容PC端组织发布的权限显示问题，根据当前用户的组织查看当前用户是否有权限访问
		List<Long> companyIds = new ArrayList<>();
		if (user != null) { // 如果当前用户是登录状态，则查看当前用户加入了哪些组织
			//TODO 调用用户模块
//			companyIds = userCompanyDao.selectCompanyIdByUser(user.getId());
//			param.setCurrentUserId(user.getId());
		}
		if (companyIds.size() == 0) {
			// 如果用户没有登录或者用户没有加入组织，则为了数据库查询的in语句不出错，加入一个1值。但是会出现效率问题
			companyIds.add(1L);
		}
		param.setUserCompanyIds(companyIds);
		//搜索条件
		String condition = param.getCondition();
		//特殊字符替换
		if (StringUtil.isNotEmpty(condition)) {
			//英文括号需要转义
			String replaceCondition = condition.replaceAll(" ", "").replaceAll("\\(", "").replaceAll("\\)", "")
					.replaceAll("（", "").replaceAll("）", "");
			param.setCondition(replaceCondition);
		}
		Page<TService> page = PageHelper.startPage(param.getPageNum(), param.getPageSize());
		// 根据条件分页所有求助服务的订单
		List<TOrder> listOrder = orderDao.pageOrder(param);
		// 装载要查询的商品ID
		List<Long> serviceIds = new ArrayList<>();
		// 装载所有需要查询的用户id
		List<Long> userIds = new ArrayList<>();
		for (int i = 0; i < listOrder.size(); i++) {
			userIds.add(listOrder.get(i).getCreateUser());
			serviceIds.add(listOrder.get(i).getServiceId());
		}
		if (userIds.size() == 0 || serviceIds.size() == 0) {
			result.setResultList(listReturn);
			result.setTotalCount(0L);
			return result;
		}
		// 获取商品封面图
		Map<Long, String> productCoverPic = productService.getProductCoverPic(serviceIds);
		// TODO 调用用户模块 根据ID获取类型名称
//		Map<String, String> serviceTypeMap = getServiceTypeMap();
		// 遍历列表order，组装返回view
		for (int i = 0; i < listOrder.size(); i++) {
			PageOrderReturnView returnView = new PageOrderReturnView();
			TOrder order = listOrder.get(i);
			returnView.setOrder(order);
//			String serviceType = serviceTypeMap.get(service.getServiceTypeId() + "");
//			if (serviceType == null) {
//				returnView.setServiceType("");
//			} else {
//				returnView.setServiceType(serviceType);
//			}
			//设置封面图
			returnView.setImgUrl(productCoverPic.get(order.getServiceId()));
			//TODO 获取发布者的信息
//			TUser tUser = usersInfo.get(service.getUserId());
//			// 进行部分字段映射
//			PageServiceUserView userView = BeanUtil.copy(tUser, PageServiceUserView.class);
//			returnView.setUser(userView);
			listReturn.add(returnView);
		}
		result.setResultList(listReturn);
		result.setTotalCount(page.getTotal());
		return result;
	}
}
