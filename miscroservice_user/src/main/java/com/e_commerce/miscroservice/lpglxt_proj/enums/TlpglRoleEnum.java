package com.e_commerce.miscroservice.lpglxt_proj.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 20:55
 */
public enum TlpglRoleEnum {

	LEVEL_0(0, new String[]{"系统管理员"}),
	LEVEL_1(1, new String[]{"总经办", "总经理"}),
	LEVEL_2(2, new String[]{"项目管理员"}),
	LEVEL_3(3, new String[]{"销售经理", "市场经理", "财务经理"}),
	LEVEL_4(4, new String[]{"销售主管", "销售人员", "市场专员", "出纳", "会计", "前台"});

	private int level;
	private String[] names;

	public static TlpglRoleEnum getType(Integer type) {
		for(TlpglRoleEnum tEnum: TlpglRoleEnum.values()) {
			if(tEnum.getLevel() == type) {
				return tEnum;
			}
		}
		return null;
	}

	public static List<String> getNames(Integer type) {
		for(TlpglRoleEnum tEnum: TlpglRoleEnum.values()) {
			if(tEnum.getLevel() == type) {
				return Arrays.asList(tEnum.getNames());
			}
		}
		return null;
	}

	public int getLevel() {
		return level;
	}

	public String[] getNames() {
		return names;
	}

	TlpglRoleEnum(int level, String[] names) {
		this.level = level;
		this.names = names;
	}

}
