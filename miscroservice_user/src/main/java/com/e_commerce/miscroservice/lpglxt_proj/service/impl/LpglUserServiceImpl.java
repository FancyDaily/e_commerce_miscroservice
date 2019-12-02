package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.service.Token;
import com.e_commerce.miscroservice.commons.enums.colligate.ApplicationEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglPositionDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglUserDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglUserPositionDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.*;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglCertService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglPositionService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglRoleService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglUserService;
import com.e_commerce.miscroservice.lpglxt_proj.utils.WxUtil;
import com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;
import java.util.stream.Collectors;

import static com.e_commerce.miscroservice.user.rpc.AuthorizeRpcService.DEFAULT_PASS;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:05
 */
@Service
@Log
public class LpglUserServiceImpl implements LpglUserService {

	@Autowired
	private LpglRoleService lpglRoleService;

	@Autowired
	private LpglPositionService lpglPositionService;

	@Autowired
	private LpglPositionDao lpglPositionDao;

	@Autowired
	private LpglUserDao lpglUserDao;

	@Autowired
	private AuthorizeRpcService authorizeRpcService;

	@Autowired
	LpglCertService lpglCertService;

	@Autowired
	LpglUserPositionDao lpglUserPositionDao;


	@Override
	public TLpglUser findOne(Long id) {
		TLpglUser tLpglUser = MybatisPlus.getInstance().findOne(new TLpglUser(), new MybatisPlusBuild(TLpglUser.class)
			.eq(TLpglUser::getId, id)
			.eq(TLpglUser::getDeletedFlag, 0)
		);
		return tLpglUser;
	}

	@Override
	public AjaxResult login(String username, String password, HttpServletRequest request, HttpServletResponse response, String openid) {
		AjaxResult ajaxResult = new AjaxResult();
		TLpglUser user = MybatisPlus.getInstance().findOne(new TLpglUser(), new MybatisPlusBuild(TLpglUser.class)
			.eq(TLpglUser::getUserAccount, username)
			.eq(TLpglUser::getPassword, password)
		);

		if (user == null) {
			log.warn("用户名或密码错误");
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg("用户名或密码错误");
			return ajaxResult;
		}
		if (!StringUtil.isEmpty(openid) && !openid.equals(user.getVxOpenId())) {
			user.setVxOpenId(openid);
			MybatisPlus.getInstance().update(user, new MybatisPlusBuild(TLpglUser.class)
				.eq(TLpglUser::getId, user.getId())
			);
		}

		user = UserUtil.loginTLpg(user, ApplicationEnum.LPGL_APPLICATION.toCode(), authorizeRpcService, response);

		List<TLpglUserPosistion> userPosition = lpglRoleService.findUserPosition(user.getId());
		List<Long> positionIds = userPosition.stream()
			.map(TLpglUserPosistion::getPosistionId)
			.distinct()
			.collect(Collectors.toList());
		List<TLpglPosistion> posistions = lpglPositionDao.selectInIds(positionIds);
		List<TLpglRole> roles = lpglRoleService.findAllPosistionRole(positionIds);
		Map<Long, List<TLpglRole>> positionIdRoles = roles.stream()
			.collect(Collectors.groupingBy(TLpglRole::getPositionId));
		List<TLpglPosistion> tLpglPosistions = new ArrayList<>();
		posistions
			.forEach(a -> {
				Long positionId = a.getId();
				List<TLpglRole> roles1 = positionIdRoles.get(positionId);
				a.setRoles(roles1);
				tLpglPosistions.add(a);
			});

		HashMap<String, Object> resMap = new HashMap<>();
		resMap.put("user", user);
		resMap.put("positions", tLpglPosistions);
		ajaxResult.setData(resMap);
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	@Override
	public AjaxResult register(String username, String password, Long posistionId, HttpServletResponse response, HttpServletRequest request, String name) {

		AjaxResult ajaxResult = new AjaxResult();

		TLpglUser req = new TLpglUser();
		TLpglUser user = MybatisPlus.getInstance().findOne(new TLpglUser(), new MybatisPlusBuild(TLpglUser.class).eq(TLpglUser::getUserAccount, username));

		TLpglPosistion tLpglPosistion = lpglRoleService.findAllPosistionById(posistionId);
		if (tLpglPosistion == null) {
			log.warn("职位不存在");
			ajaxResult.setSuccess(false);
			ajaxResult.setMsg("用户已注册");
			return ajaxResult;
		}
		req.setUserAccount(username);
		req.setPassword(password);
		req.setName(name);
		req.setUserTel(username);
		if (user != null) {
			log.warn("用户已注册 更新职位");
			ajaxResult.setSuccess(true);
			int i = lpglRoleService.updatePosistionRole(posistionId, user.getId());

			return ajaxResult;
		}

		MybatisPlus.getInstance().save(req);
		TLpglUser result = MybatisPlus.getInstance().findOne(new TLpglUser(), new MybatisPlusBuild(TLpglUser.class).eq(TLpglUser::getUserAccount, username));

		//注册到认证中心
		String namePrefix = UserUtil.getApplicationNamePrefix(ApplicationEnum.CONGSHANQIAO_APPLICATION.toCode());
		Token token = authorizeRpcService.reg(namePrefix + result.getId(), DEFAULT_PASS, result.getId().toString(), username, Boolean.FALSE);


		log.info("认证中心获取的token为={},msg={}", token != null ? token.getToken() : "", token != null ? token.getMsg() : "");
		if (token != null && token.getToken() != null) {
			response.addHeader("token", token.getToken());
		}


		int i = lpglRoleService.updatePosistionRole(posistionId, result.getId());
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	@Override
	public AjaxResult getOpenid(String code) {

		AjaxResult result = new AjaxResult();
		String openId = WxUtil.code2PublicOpenId(code);
		HashMap<String, Object> map = new HashMap<>();
		map.put("openid", openId);
		result.setData(map);
		return result;
	}

	@Override
	public AjaxResult userList(String name, String userAccount, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		MybatisPlusBuild mybatisPlusBuild = new MybatisPlusBuild(TLpglUser.class);

		mybatisPlusBuild.eq(TLpglUser::getIsValid, AppConstant.IS_VALID_YES);
		mybatisPlusBuild.eq(TLpglUser::getDeletedFlag, false);
		mybatisPlusBuild.like(TLpglUser::getName, buildLikeString(name));
		mybatisPlusBuild.like(TLpglUser::getUserAccount, buildLikeString(userAccount));

		IdUtil.setTotal(mybatisPlusBuild);
		result.setData(PageUtil.buildQueryResult(MybatisPlus.getInstance().findAll(new TLpglUser(), mybatisPlusBuild.page(pageNum, pageSize)), IdUtil.getTotal()));
		result.setSuccess(true);
		return result;
	}

	@Override
	public AjaxResult authorities(Long id) {
		List<TLpglUserPosistion> userPosition = lpglRoleService.findUserPosition(id);
		List<Long> positionIds = userPosition.stream()
			.map(TLpglUserPosistion::getPosistionId).collect(Collectors.toList());
		List<TLpglRole> allPosistionRole = lpglRoleService.findAllPosistionRole(positionIds);
		List<Long> roleIds = allPosistionRole.stream()
			.map(TLpglRole::getId).collect(Collectors.toList());
		AjaxResult result = new AjaxResult();
		result.setData(lpglRoleService.findAllRoleAuthority(roleIds));
		result.setSuccess(true);
		return result;
	}

	@Override
	public List<TLpglUser> getBoss(Long id) {
		//销售主管 -> 显示 销售经理 总经理 总经办
		TLpglUser user = lpglUserDao.selectByPrimaryKey(id);
		TLpglPosistion position = lpglPositionService.getPosition(id);
		if(position == null) return new ArrayList<>();
		String posisitionName = position.getPosisitionName();
		List<String> positionNames = new ArrayList<>();
		if("销售主管".equals(posisitionName)) {
			positionNames = Arrays.asList("销售经理", "总经理", "总经办");
		}
		//市场经理 -> 显示 总经理 总经办
		if("市场经理".equals(posisitionName)) {
			positionNames = Arrays.asList("总经理", "总经办");
		}

		//获得相应用户信息
		return lpglPositionService.findUsersByPositionNames(positionNames);
	}

	@Override
	public void handOver(Long userId, Long certId) {
		TLpglUser tLpglUser = lpglUserDao.selectByPrimaryKey(userId);
		if(tLpglUser == null) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "用户不存在！");
		String vxOpenId = tLpglUser.getVxOpenId();
		if(vxOpenId == null) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "发送通知对象所需openid为空！");
		//发送通知
		lpglCertService.handOverMessage(certId, vxOpenId);
	}

	@Override
	public void delUser(Long userIds) {
		TLpglUser build = TLpglUser.builder()
			.build();
		build.setId(userIds);
		build.setIsValid(AppConstant.IS_VALID_YES);
		build.setDeletedFlag(true);
		lpglUserDao.update(build);
	}

	@Override
	public AjaxResult delUserAuthorities(Long id, Long authId) {
		AjaxResult result = new AjaxResult();
		return result;
	}

	@Override
	public List<TLpglUserPosistion> findUserPosition(Long userIds) {
		List<TLpglUserPosistion> userPosistions = lpglUserPositionDao.selectByUserId(userIds);
		if(userPosistions.isEmpty()) return new ArrayList<>();
		List<Long> positionIds = userPosistions.stream()
			.map(TLpglUserPosistion::getId).collect(Collectors.toList());
		List<TLpglPosistion> posistions = lpglPositionDao.selectInIds(positionIds);
		Map<Long, List<TLpglPosistion>> idPositionMap = posistions.stream()
			.collect(Collectors.groupingBy(TLpglPosistion::getId));
		return userPosistions.stream()
			.map(a -> {
				List<TLpglPosistion> tLpglPosistions = idPositionMap.get(a.getPosistionId());
				TLpglPosistion tLpglPosistion = tLpglPosistions.get(0);
				a.setName(tLpglPosistion.getPosisitionName());
				return a;
			}).collect(Collectors.toList());
	}

	private String buildLikeString(String str) {
		return '%' + str + '%';
	}

	private String preStringParam(String str) {
		return str == null ? "" : str;
	}
}
