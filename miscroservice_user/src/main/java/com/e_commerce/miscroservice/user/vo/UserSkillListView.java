package com.e_commerce.miscroservice.user.vo;
/**
 * 功能描述:
 * 模块:
 * 项目:timebank
 * 版本号:1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:许方毅
 * 邮箱:519029909@qq.com
 * 创建时间:2019年2月15日 下午2:36:42
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */

import java.io.Serializable;
import java.util.List;


public class UserSkillListView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer skillCnt;

	private List<UserSkillView> userSkills;

	public Integer getSkillCnt() {
		return skillCnt;
	}

	public void setSkillCnt(Integer skillCnt) {
		this.skillCnt = skillCnt;
	}

	public List<UserSkillView> getUserSkills() {
		return userSkills;
	}

	public void setUserSkills(List<UserSkillView> userSkills) {
		this.userSkills = userSkills;
	}
}
