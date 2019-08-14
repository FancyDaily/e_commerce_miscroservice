package com.e_commerce.miscroservice.csq_proj.po;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Data;

import java.io.Serializable;

@Table
public class TOldUser implements Serializable {

	@Id
    private String id;

    private String adder;

    private String addtime;

    private String editor;

    private String edittime;

    private String status;

    private String sort;

    private String username;

    private String nickname;

    private String account;

    private String password;

    private String sex;

    private String birthday;

    private String idtype;

    private String idnumber;

    private String education;

    private String nation;

    private String nativecode;

    private String nativedetailaddress;

    private String birthcode;

    private String birthdetailaddress;

    private String email;

    private String qq;

    private String addresscode;

    private String detailaddress;

    private String politicalstatus;

    private String userno;

    private String point;

    private String sertime;

    private String pointlevel;

    private String sertimelevel;

    private String pointlevelname;

    private String sertimelevelname;

    private String fromuserid;

    private String fromorgid;

    private String fromteamid;

    private String addressfivelevelcode;

    private String memo;

    private String parentid;

    private String registersource;

    private String areacode8;

    private String areaname8;

    private String areacode10;

    private String areaname10;

    private String workunit;

    private String unitaddress;

    private String identificationphoto;

    private String isgroupuser;

    private String zyhuserid;

    private String openid;

	public String getId() {
		return id;
	}

	public String getAdder() {
		return adder;
	}

	public String getAddtime() {
		return addtime;
	}

	public String getEditor() {
		return editor;
	}

	public String getEdittime() {
		return edittime;
	}

	public String getStatus() {
		return status;
	}

	public String getSort() {
		return sort;
	}

	public String getUsername() {
		return username;
	}

	public String getNickname() {
		return nickname;
	}

	public String getAccount() {
		return account;
	}

	public String getPassword() {
		return password;
	}

	public String getSex() {
		return sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getIdtype() {
		return idtype;
	}

	public String getIdnumber() {
		return idnumber;
	}

	public String getEducation() {
		return education;
	}

	public String getNation() {
		return nation;
	}

	public String getNativecode() {
		return nativecode;
	}

	public String getNativedetailaddress() {
		return nativedetailaddress;
	}

	public String getBirthcode() {
		return birthcode;
	}

	public String getBirthdetailaddress() {
		return birthdetailaddress;
	}

	public String getEmail() {
		return email;
	}

	public String getQq() {
		return qq;
	}

	public String getAddresscode() {
		return addresscode;
	}

	public String getDetailaddress() {
		return detailaddress;
	}

	public String getPoliticalstatus() {
		return politicalstatus;
	}

	public String getUserno() {
		return userno;
	}

	public String getPoint() {
		return point;
	}

	public String getSertime() {
		return sertime;
	}

	public String getPointlevel() {
		return pointlevel;
	}

	public String getSertimelevel() {
		return sertimelevel;
	}

	public String getPointlevelname() {
		return pointlevelname;
	}

	public String getSertimelevelname() {
		return sertimelevelname;
	}

	public String getFromuserid() {
		return fromuserid;
	}

	public String getFromorgid() {
		return fromorgid;
	}

	public String getFromteamid() {
		return fromteamid;
	}

	public String getAddressfivelevelcode() {
		return addressfivelevelcode;
	}

	public String getMemo() {
		return memo;
	}

	public String getParentid() {
		return parentid;
	}

	public String getRegistersource() {
		return registersource;
	}

	public String getAreacode8() {
		return areacode8;
	}

	public String getAreaname8() {
		return areaname8;
	}

	public String getAreacode10() {
		return areacode10;
	}

	public String getAreaname10() {
		return areaname10;
	}

	public String getWorkunit() {
		return workunit;
	}

	public String getUnitaddress() {
		return unitaddress;
	}

	public String getIdentificationphoto() {
		return identificationphoto;
	}

	public String getIsgroupuser() {
		return isgroupuser;
	}

	public String getZyhuserid() {
		return zyhuserid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAdder(String adder) {
		this.adder = adder;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public void setEdittime(String edittime) {
		this.edittime = edittime;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public void setNativecode(String nativecode) {
		this.nativecode = nativecode;
	}

	public void setNativedetailaddress(String nativedetailaddress) {
		this.nativedetailaddress = nativedetailaddress;
	}

	public void setBirthcode(String birthcode) {
		this.birthcode = birthcode;
	}

	public void setBirthdetailaddress(String birthdetailaddress) {
		this.birthdetailaddress = birthdetailaddress;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public void setAddresscode(String addresscode) {
		this.addresscode = addresscode;
	}

	public void setDetailaddress(String detailaddress) {
		this.detailaddress = detailaddress;
	}

	public void setPoliticalstatus(String politicalstatus) {
		this.politicalstatus = politicalstatus;
	}

	public void setUserno(String userno) {
		this.userno = userno;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public void setSertime(String sertime) {
		this.sertime = sertime;
	}

	public void setPointlevel(String pointlevel) {
		this.pointlevel = pointlevel;
	}

	public void setSertimelevel(String sertimelevel) {
		this.sertimelevel = sertimelevel;
	}

	public void setPointlevelname(String pointlevelname) {
		this.pointlevelname = pointlevelname;
	}

	public void setSertimelevelname(String sertimelevelname) {
		this.sertimelevelname = sertimelevelname;
	}

	public void setFromuserid(String fromuserid) {
		this.fromuserid = fromuserid;
	}

	public void setFromorgid(String fromorgid) {
		this.fromorgid = fromorgid;
	}

	public void setFromteamid(String fromteamid) {
		this.fromteamid = fromteamid;
	}

	public void setAddressfivelevelcode(String addressfivelevelcode) {
		this.addressfivelevelcode = addressfivelevelcode;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public void setRegistersource(String registersource) {
		this.registersource = registersource;
	}

	public void setAreacode8(String areacode8) {
		this.areacode8 = areacode8;
	}

	public void setAreaname8(String areaname8) {
		this.areaname8 = areaname8;
	}

	public void setAreacode10(String areacode10) {
		this.areacode10 = areacode10;
	}

	public void setAreaname10(String areaname10) {
		this.areaname10 = areaname10;
	}

	public void setWorkunit(String workunit) {
		this.workunit = workunit;
	}

	public void setUnitaddress(String unitaddress) {
		this.unitaddress = unitaddress;
	}

	public void setIdentificationphoto(String identificationphoto) {
		this.identificationphoto = identificationphoto;
	}

	public void setIsgroupuser(String isgroupuser) {
		this.isgroupuser = isgroupuser;
	}

	public void setZyhuserid(String zyhuserid) {
		this.zyhuserid = zyhuserid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
