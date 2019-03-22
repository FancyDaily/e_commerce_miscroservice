package com.e_commerce.miscroservice.user.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.application.TCompany;
import com.e_commerce.miscroservice.commons.entity.application.TGroup;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.application.TUserCompany;
import com.e_commerce.miscroservice.commons.enums.application.GroupEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.exception.colligate.NoAuthChangeException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.BeanUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.user.dao.CompanyDao;
import com.e_commerce.miscroservice.user.dao.GroupDao;
import com.e_commerce.miscroservice.user.dao.UserCompanyDao;
import com.e_commerce.miscroservice.user.service.GroupService;
import com.e_commerce.miscroservice.user.vo.BaseGroupView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GroupServiceImpl implements GroupService {

	Log logger = Log.getInstance(GroupServiceImpl.class);

	@Autowired
	private GroupDao groupDao;
	@Autowired
	private UserCompanyDao userCompanyDao;
	@Autowired
	private CompanyDao companyDao;

	/**
	 * 功能描述: 获取组织账号对应组织编号
	 * 作者: 许方毅
	 * 创建时间: 2019年1月14日 下午1:45:04
	 * @param userId 组织用户ID
	 * @return
	 */
	protected Long getOwnCompanyId(Long userId) {
//		TUserCompanyExample userCompanyExample = new TUserCompanyExample();
//		TUserCompanyExample.Criteria criteria = userCompanyExample.createCriteria();
//		criteria.andUserIdEqualTo(id);
//		criteria.andCompanyJobEqualTo(AppConstant.JOB_COMPANY_CREATER);
//		criteria.andIsValidEqualTo(AppConstant.IS_VALID_YES);
//		List<TUserCompany> userCompanies = userCompanyDao.selectByExample(userCompanyExample);
		TUserCompany userCompany = userCompanyDao.getOwnCompanyIdByUser(userId);
		if(userCompany == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM,"没有对应组织！");
		}
		return userCompany.getCompanyId();
	}

	@Override
	public List<BaseGroupView> listGroup(TUser user) {

		// 从当前的组织用户中获取组织的id号
		Long companyId = null;
		try {
			companyId = getOwnCompanyId(user.getId());
		} catch (MessageException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

//		TGroupExample groupExample = new TGroupExample();
//		TGroupExample.Criteria groupCriteria = groupExample.createCriteria();
//		groupCriteria.andCompanyIdEqualTo(companyId).andIsValidEqualTo(IS_VALID_YES);
//		List<TGroup> listGroup = groupDao.selectByExample(groupExample);
		// 将组织id 及 组织人数放到map中，为了提升下面的查询效率
		List<TGroup> listGroup = groupDao.listGroup(companyId);
		Map<Long, Integer> groupPersonNumMap = new HashMap<>();
		for (int i = 0; i < listGroup.size(); i++) {
			TGroup group = listGroup.get(i);
			// 初始人数都为0
			groupPersonNumMap.put(group.getId(), 0);
		}
		// 因为页面需要显示每个组织的人数，所以需要将组织封装view加入每个组织的人数
		// 查询出该组织下面的所有人
		List<TUserCompany> allCompanyPerson = userCompanyDao.listCompanyUser(companyId);
//		TUserCompanyExample userCompanyExample = new TUserCompanyExample();
//		TUserCompanyExample.Criteria createCriteria = userCompanyExample.createCriteria();
//		// 状态为2的用户才会审核通过
//		createCriteria.andIsValidEqualTo(IS_VALID_YES).andStateEqualTo(2).andGroupIdIsNotNull().andCompanyIdEqualTo(companyId);
//		List<TUserCompany> allCompanyPerson = userCompanyDao.selectByExample(userCompanyExample);
		// 匹配对应的groupId 在map中进行人数的+1操作
		for (TUserCompany userCompany : allCompanyPerson) {
			Integer groupPersonNum = groupPersonNumMap.get(userCompany.getGroupId());
			groupPersonNum++;
			groupPersonNumMap.put(userCompany.getGroupId(), groupPersonNum);
		}
		// 进行view的转换 并将分组人数加入
		List<BaseGroupView> listGroupView = new ArrayList<BaseGroupView>();
		for (TGroup group : listGroup) {
			BaseGroupView groupView = BeanUtil.copy(group, BaseGroupView.class);
			Integer groupPersonNum = groupPersonNumMap.get(group.getId());
			groupView.setPersonNum(groupPersonNum);
			listGroupView.add(groupView);
		}
		return listGroupView;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insert(TGroup group, TUser user) {
		// 从当前的组织用户中获取组织的id号
		Long companyId = null;
		try {
			companyId = getOwnCompanyId(user.getId());
		} catch (MessageException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		group.setCompanyId(companyId);
		if (group.getCompanyId() == null || StringUtil.isEmpty(group.getGroupName())) {
			logger.error("新建分组时没有加入组织ID或者组织名字是空的");
			throw new IllegalArgumentException("参数不正确,请刷新重试");
		}
		// 校验传来的组织ID在数据库中是否有效
		TCompany company = companyDao.selectByPrimaryKey(group.getCompanyId());
		// 错误的组织ID传递进来
		boolean inaccurateCompanyId = (company == null || company.getIsValid().equals(AppConstant.IS_VALID_NO)
				|| !Objects.equals(1, company.getStatus()));
		if (inaccurateCompanyId) {
			logger.error("新建分组时传递的组织参数不正确 组织ID为{}", group.getCompanyId());
			throw new IllegalArgumentException("参数不正确,请刷新重试");
		}

		Long currentTime = System.currentTimeMillis();
		// 这个分组是用户创建的
//		group.setId(snowflakeIdWorker.nextId());
		group.setIsValid(AppConstant.IS_VALID_YES);
		group.setAuth(GroupEnum.AUTH_CREATED.getValue());
		group.setCreateTime(currentTime);
		group.setCreateUser(user.getId());
		group.setCreateUserName(user.getName());
		group.setUpdateUser(user.getId());
		group.setUpdateTime(currentTime);
		group.setUpdateUserName(user.getName());
		groupDao.insert(group);
		logger.info("组织新建分组id为{}，名称为{}", group.getId(), group.getGroupName());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateGroup(TGroup group, TUser user) throws NoAuthChangeException {
		Long currentTime = System.currentTimeMillis();
		if (StringUtil.isEmpty(group.getGroupName()) || group.getId() == null) {
			throw new IllegalArgumentException("参数不正确");
		}
		TGroup updateGroup = groupDao.selectByPrimaryKey(group.getId());
		if (updateGroup.getAuth().equals(GroupEnum.AUTH_DEFAULT.getValue())) {
			throw new NoAuthChangeException("默认分组无法修改");
		}
		if (updateGroup.getIsValid().equals(AppConstant.IS_VALID_NO)) {
			throw new IllegalArgumentException("参数不正确");
		}
		// 旧的分组名称，为了记录在日志中
		String oldGroupName = updateGroup.getGroupName();
		updateGroup.setGroupName(group.getGroupName());
		updateGroup.setUpdateTime(currentTime);
		updateGroup.setUpdateUser(user.getId());
		updateGroup.setUpdateUserName(user.getName());
		groupDao.updateByPrimaryKeySelective(updateGroup);
		logger.info("组织修改分组id为{}，名称从{}改为{}", group.getId(), oldGroupName, group.getGroupName());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteGroup(Long groupId, TUser user) throws NoAuthChangeException {
		Long currentTime = System.currentTimeMillis();
		if (groupId == null) {
			throw new IllegalArgumentException("参数不正确");
		}
		TGroup delGroup = groupDao.selectByPrimaryKey(groupId);
		if (delGroup.getAuth().equals(GroupEnum.AUTH_DEFAULT.getValue())) {
			throw new NoAuthChangeException("默认分组无法删除");
		}
		if (delGroup.getIsValid().equals(AppConstant.IS_VALID_NO)) {
			throw new IllegalArgumentException("参数不正确");
		}
		// 查看该分组下面是否有用户，如果有用户，则不能进行删除
		long countUser = userCompanyDao.countGroupUser(groupId);
//		TUserCompanyExample userCompanyExample = new TUserCompanyExample();
//		TUserCompanyExample.Criteria createCriteria = userCompanyExample.createCriteria();
//		// 状态为2的用户才会在这个分组里面
//		createCriteria.andIsValidEqualTo(IS_VALID_YES).andGroupIdEqualTo(groupId).andStateEqualTo(2);
//		long countUser = userCompanyDao.countByExample(userCompanyExample);
		if (countUser > 0) {
			throw new NoAuthChangeException("该分组下还有用户，无法删除，请将用户移动到其他分组再进行删除");
		}
		delGroup.setIsValid(AppConstant.IS_VALID_NO);
		delGroup.setUpdateTime(currentTime);
		delGroup.setUpdateUser(user.getId());
		delGroup.setUpdateUserName(user.getName());
		groupDao.updateByPrimaryKeySelective(delGroup);
		logger.info("组织删除分组id为{}，名称为{}的分组", groupId, delGroup.getGroupName());
	}
}
