package com.e_commerce.miscroservice.order.vo;

import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import com.e_commerce.miscroservice.order.po.TOrder;
import com.e_commerce.miscroservice.product.vo.BaseUserView;
import lombok.Data;

import java.util.List;


/**
 * 功能描述:服务求助详情返回view
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月3日 下午6:18:50
 */
@Data
public class DetailOrderReturnView {
//	/**
//	 * 不显示关注
//	 */
//	public final int CARE_STATUS_NOT_SHOW = 0;
//	/**
//	 * 显示关注
//	 */
//	public final int CARE_STATUS_SHOW_CARE = 1;
//	/**
//	 * 显示已关注
//	 */
//	public final int CARE_STATUS_SHOW_ALREADY_CARE = 2;
//
//	/**
//	 * 收藏状态为0未收藏
//	 */
//	public final int COLLECT_STATUS_NOT_COLLECT = 0;
//	/**
//	 * 收藏状态为1已收藏
//	 */
//	public final int COLLECT_STATUS_ALREADY_COLLECT = 1;
//
//	/**
//	 * 不显示帮助他
//	 */
//	public final int SHOW_HELP_STATUS_NOT_SHOW = 1;
//	/**
//	 * 显示帮助他
//	 */
//	public final int SHOW_HELP_STATUS_SHOW_HELP = 2;
//	/**
//	 * 显示已帮助
//	 */
//	public final int SHOW_HELP_STATUS_SHOW_HELPED = 3;
//	/**
//	 * 显示进行中
//	 */
//	public final int SHOW_HELP_STATUS_RUNNING = 4;
//	/**
//	 * 显示已完成
//	 */
//	public final int SHOW_HELP_STATUS_COMPLETED = 5;
//	/**
//	 * 显示已下架
//	 */
//	public final int SHOW_HELP_STATUS_SHOW_LOWERFRAME = 8;
	
	/**
	 * 服务求助
	 */
	private TOrder order;
	/**
	 * 服务求助发布者
	 */
	private BaseUserView user;
	/**
	 * 服务求助详细描述
	 */
	private List<TServiceDescribe> listServiceDescribe;
	/**
	 * 服务求助类型（key-value 中的value）
	 */
	private String serviceType;
	/**
	 * 关注状态 ： 0、用户自己看自己求助详情，不显示关注按钮  1、用户未登录或未关注  显示关注按钮  2、已经关注过该用户，显示已关注
	 */
//	private Integer careStatus;
	/**
	 * 收藏状态：0、未收藏  1、已收藏
	 */
//	private Integer collectStatus;
	/**
	 * 展示帮助他状态 1、不显示帮助他  2、显示帮助他   3、显示已帮助
	 */
//	private Integer showHelpStatus = 2;
	
	public String getServiceIdString() {
		if (order != null) {
			return order.getId() + "";
		}
		return "";
	}
	/**
	 * 
	 * 功能描述:发布者id
	 * 作者:马晓晨
	 * 创建时间:2018年11月25日 下午11:53:01
	 * @return
	 */
	public String getServiceUserIdString() {
		if (order != null) {
			return order.getCreateUser() + "";
		}
		return "";
	}

}
