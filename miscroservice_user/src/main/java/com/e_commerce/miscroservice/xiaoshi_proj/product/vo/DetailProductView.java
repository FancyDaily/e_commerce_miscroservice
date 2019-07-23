package com.e_commerce.miscroservice.xiaoshi_proj.product.vo;

import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import lombok.Data;

import java.util.List;

/**
 * @author 马晓晨
 * @date 2019/3/14
 */
@Data
public class DetailProductView {
	private TService service;
	private List<TServiceDescribe> desc;
	private String imgUrl;
}
