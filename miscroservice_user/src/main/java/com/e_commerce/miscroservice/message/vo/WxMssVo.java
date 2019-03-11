package com.xiaoshitimebank.app.wechat.entity;

import java.util.Map;


/**
 * 功能描述:小程序推送所需数据
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱: 747052172@qq.com
 * 创建时间:2018年11月21日 下午2:39:24
 */
/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱:747052172@qq.com
 * 创建时间:2018年11月22日 下午6:10:03
 */
public class WxMssVo {
	private String touser;//用户openid
	private String template_id;//模版id
	private String page = "/pages/help/help";//默认跳到小程序首页 
	private String form_id;//收集到的用户formid
	private String emphasis_keyword = "";//放大那个推送字段 keyword1.DATA
	private Map<String, TemplateData> data;//推送文字
	
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getForm_id() {
		return form_id;
	}
	public void setForm_id(String form_id) {
		this.form_id = form_id;
	}
	public String getEmphasis_keyword() {
		return emphasis_keyword;
	}
	public void setEmphasis_keyword(String emphasis_keyword) {
		this.emphasis_keyword = emphasis_keyword;
	}
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public Map<String, TemplateData> getData() {
		return data;
	}
	public void setData(Map<String, TemplateData> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "WxMssVo [touser=" + touser + ", template_id=" + template_id + ", page=" + page + ", form_id=" + form_id
				+ ", emphasis_keyword=" + emphasis_keyword + ", data=" + data + "]";
	}
	
   
   
}
