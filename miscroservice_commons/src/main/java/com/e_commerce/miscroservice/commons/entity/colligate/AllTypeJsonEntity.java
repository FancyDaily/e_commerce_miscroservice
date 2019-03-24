package com.e_commerce.miscroservice.commons.entity.colligate;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述:用于键为allType的key value表中的json value的映射
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月16日 下午4:23:09
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class AllTypeJsonEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	private String id;
	/**
	 * 包含类目
	 */
	private List<String> classification;
	/**
	 * title
	 */
	private String title;
	/**
	 * 图片地址
	 */
	private String imgUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public List<String> getClassification() {
		return classification;
	}
	public void setClassification(List<String> classification) {
		this.classification = classification;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
}
