package com.e_commerce.miscroservice.order.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.order.vo.PageOrderReturnView;
import com.e_commerce.miscroservice.order.vo.PageServiceParamView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 功能描述:订单Controller
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2019年3月2日 下午3:46:01
 */
@RestController
@RequestMapping("/api/v1/order")
public class OrderController extends BaseController {

	/**
	 * 列出求助服务订单列表
	 * @param type 类型 1、求助 2、服务
	 * @param serviceTypeId 类型ID 如：温暖公益的ID 用于筛选 全部的id为1000000
	 * @param longitude 经度
	 * @param latitude 纬度
	 * @param pageNum 分页页数
	 * @param pageSize 每页数量
	 * @param condition 查询条件
	 * @param token 当前用户token
	 * @return
	 */
	@PostMapping("/list")
	@Consume(PageServiceParamView.class)
	public Object list(Integer type, Integer serviceTypeId, double longitude, double latitude, Integer pageNum,
					   Integer pageSize, String condition, String token) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		PageServiceParamView param = (PageServiceParamView)ConsumeHelper.getObj();
		try {
			QueryResult<PageOrderReturnView> list = orderService.list(param, user);
			result.setData(list);
			result.setSuccess(true);
			result.setMsg("查询成功");
		} catch (MessageException e) {
			logger.warn("查询服务列表失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败," + e.getMessage());
		} catch (Exception e) {
			logger.error("查询服务列表失败" + errInfo(e), e);
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败");
		}
		return result;
	}

	/**
	 *
	 * 功能描述:服务详情
	 * 作者:马晓晨
	 * 创建时间:2018年11月3日 下午4:49:24
	 * @return
	 */
	@RequestMapping("/detail")
	public Object detail(Long serviceId, String token) {
		TUser user = (TUser) redisUtil.get(token);
		AjaxResult result = new AjaxResult();
		try {
//			DetailServiceReturnView data = productService.serviceDetail(serviceId, user);
			result.setSuccess(true);
//			result.setData(data);
			result.setMsg("获取详情成功");
		} catch (MessageException e) {
			logger.warn("获取详情失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("获取详情失败," + e.getMessage());
		} catch (Exception e) {
			logger.error("获取详情失败" + errInfo(e), e);
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("获取详情失败");
		}
		return result;
	}


}
