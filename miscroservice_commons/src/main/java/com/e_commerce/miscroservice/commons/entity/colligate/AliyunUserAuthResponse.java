package com.e_commerce.miscroservice.commons.entity.colligate;

/**
 * 阿里云身份认证返回类型
 * @Author: FangyiXu
 * @Date: 2019-07-23 10:34
 */
public class AliyunUserAuthResponse {
	/**
	 * 姓名
	 */
	String name;

	/**
	 * 身份证号
	 */
	String idNo;

	/**
	 * 返回信息
	 */
	String respMessage;

	/**
	 * 返回码
	 */
	String respCode;

	/**
	 * 省份
	 */
	String province;

	/**
	 * 城市
	 */
	String city;

	/**
	 * 乡/县
	 */
	String county;

	/**
	 * 生日
	 */
	String birthday;

	/**
	 * 性别
	 */
	String sex;

	/**
	 * 年龄
	 */
	String age;

	public void setName(String name) {
		this.name = name;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public void setRespMessage(String respMessage) {
		this.respMessage = respMessage;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public String getIdNo() {
		return idNo;
	}

	public String getRespMessage() {
		return respMessage;
	}

	public String getRespCode() {
		return respCode;
	}

	public String getProvince() {
		return province;
	}

	public String getCity() {
		return city;
	}

	public String getCounty() {
		return county;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getSex() {
		return sex;
	}

	public String getAge() {
		return age;
	}

}
