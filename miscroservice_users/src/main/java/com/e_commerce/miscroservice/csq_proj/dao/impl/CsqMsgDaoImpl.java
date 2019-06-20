package com.e_commerce.miscroservice.csq_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.csq_proj.dao.CsqMsgDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqSysMsg;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-19 15:14
 */
@Component
public class CsqMsgDaoImpl implements CsqMsgDao {

	private MybatisPlusBuild baseWhereBuild() {
		return new MybatisPlusBuild(TCsqSysMsg.class)
			.eq(TCsqSysMsg::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TCsqSysMsg> selectByUserId(Long userId) {
		return MybatisPlus.getInstance().finAll(new TCsqSysMsg(), baseWhereBuild()
			.eq(TCsqSysMsg::getUserId, userId));
	}

	@Override
	public List<TCsqSysMsg> selectByUserIdAndIsRead(Long userId, int isRead) {
		return MybatisPlus.getInstance().finAll(new TCsqSysMsg(), baseWhereBuild()
			.eq(TCsqSysMsg::getUserId, userId)
			.eq(TCsqSysMsg::getIsRead, isRead));
	}

	@Override
	public int update(List<TCsqSysMsg> toUpdater) {
		List<Long> toUpdaterIds = toUpdater.stream().map(TCsqSysMsg::getId).collect(Collectors.toList());
		return MybatisPlus.getInstance().update(toUpdater, baseWhereBuild()
			.in(TCsqSysMsg::getId, toUpdaterIds));
	}

	@Override
	public int insert(TCsqSysMsg... csqSysMsg) {
		List<TCsqSysMsg> csqSysMsgs = Arrays.stream(csqSysMsg).map(a -> {
			if (a.getId() != null) {
				a.setId(null);
			}
			return a;
		}).collect(Collectors.toList());
		return MybatisPlus.getInstance().save(csqSysMsgs);
	}
}
