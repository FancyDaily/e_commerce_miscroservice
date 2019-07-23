package com.e_commerce.miscroservice.user.vo;

import java.io.Serializable;

/**
 * 功能描述:
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2018年11月29日 下午2:06:52
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class ShareServiceView implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private DetailServiceReturnView detailServiceReturnView;
	
	public DetailServiceReturnView getDetailServiceReturnView() {
		return detailServiceReturnView;
	}

	public void setDetailServiceReturnView(DetailServiceReturnView detailServiceReturnView) {
		this.detailServiceReturnView = detailServiceReturnView;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
