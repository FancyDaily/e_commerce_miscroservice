package com.e_commerce.miscroservice.xiaoshi_proj.product.vo;


import com.e_commerce.miscroservice.commons.entity.application.TService;
import lombok.Data;

/**
 * 我发布的返回值view
 * @author 马晓晨
 * @date 2019/3/8
 */
@Data
public class PageMineReturnView {
	//商品信息
	private TService service;
	//封面图
	private String imgUrl;
	//要显示的状态
	private String status;
}
