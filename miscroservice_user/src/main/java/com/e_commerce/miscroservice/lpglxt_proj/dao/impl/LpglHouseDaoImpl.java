package com.e_commerce.miscroservice.lpglxt_proj.dao.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppConstant;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglHouseDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-12 10:47
 */
@Component
public class LpglHouseDaoImpl implements LpglHouseDao {

	private MybatisPlusBuild baseBuild() {
		return new MybatisPlusBuild(TLpglHouse.class)
			.eq(TLpglHouse::getIsValid, AppConstant.IS_VALID_YES);
	}

	@Override
	public List<TLpglHouse> selectByEstateId(Long estateId) {
		return MybatisPlus.getInstance().findAll(new TLpglHouse(), byEstateBuild(estateId)
		);
	}

	private MybatisPlusBuild byEstateBuild(Long estateId) {
		return baseBuild()
			.eq(TLpglHouse::getEstateId, estateId);
	}

	@Override
	public List<TLpglHouse> selectByEstateIdPage(Long estateId, Integer pageNum, Integer pageSize) {
		MybatisPlusBuild mybatisPlusBuild = byEstateBuild(estateId);
		IdUtil.setTotal(mybatisPlusBuild);

		return MybatisPlus.getInstance().findAll(new TLpglHouse(), mybatisPlusBuild.page(pageNum, pageSize)
		);
	}

	@Override
	public List<TLpglHouse> selectAll() {
		return MybatisPlus.getInstance().findAll(new TLpglHouse(), baseBuild()
		);
	}

	@Override
	public List<TLpglHouse> selectByEstateIdAndBuildingNum(Long estateId, Integer buildingNum) {
		return MybatisPlus.getInstance().findAll(new TLpglHouse(), baseBuild()
			.eq(TLpglHouse::getEstateId, estateId)
			.eq(TLpglHouse::getBuildingNum, buildingNum)
		);
	}

	@Override
	public TLpglHouse selectByPrimaryKey(Long houseId) {
		return MybatisPlus.getInstance().findOne(new TLpglHouse(), idBuild(houseId)
		);
	}

	@Override
	public int update(TLpglHouse obj) {
		return MybatisPlus.getInstance().update(obj, idBuild(obj.getId())
		);
	}

	private MybatisPlusBuild idBuild(Long id) {
		return baseBuild()
			.eq(TLpglHouse::getId, id);
	}

	@Override
	public int insert(TLpglHouse obj) {
		obj.setIsValid(AppConstant.IS_VALID_YES);
		return MybatisPlus.getInstance().save(obj);
	}

	@Override
	public int remove(Long houseId) {
		TLpglHouse tLpglHouse = new TLpglHouse();
		tLpglHouse.setId(houseId);
		tLpglHouse.setIsValid(AppConstant.IS_VALID_NO);
		return MybatisPlus.getInstance()
			.update(tLpglHouse, idBuild(houseId));
	}

	@Override
	public List<TLpglHouse> selectInIds(List<Long> houseIds) {
		return MybatisPlus.getInstance().findAll(new TLpglHouse(), baseBuild()
			.in(TLpglHouse::getId, houseIds)
		);
	}

	@Override
	public int insert(List<TLpglHouse> addList) {
		return insert(addList, false);
	}

	@Override
	public int insert(List<TLpglHouse> addList, boolean isMultiple) {
		MybatisPlus instance = MybatisPlus.getInstance();
		int saveNum = 0;
		if (isMultiple) {
			//如果数量超过piece，进行分割
			int offset = 0;
			int piece = 200;
			//分片插入
			while (addList.size() > offset) {
				List<TLpglHouse> dealList = addList.stream()
					.skip(offset)
					.limit(piece)
					.collect(Collectors.toList());
				saveNum += instance.save(dealList);
				offset += dealList.size();
			}
		} else {
			for (TLpglHouse element : addList) {
				saveNum = instance.save(element);
			}
		}
		return saveNum;
	}

	@Override
	public int modify(List<TLpglHouse> modifyList) {
		return modify(modifyList, false);
	}

	@Override
	public int modify(List<TLpglHouse> modifyList, boolean isMultiple) {
		MybatisPlus instance = MybatisPlus.getInstance();
		int modifyNum = 0;
		if (isMultiple) {
			//如果数量超过piece，进行分割
			int offset = 0;
			int piece = 50;
			//分片插入
			while (modifyList.size() > offset) {
				List<TLpglHouse> dealList = modifyList.stream()
					.skip(offset)
					.limit(piece)
					.collect(Collectors.toList());
				List<Long> ids = dealList.stream()
					.map(TLpglHouse::getId).collect(Collectors.toList());
				modifyNum += instance.update(dealList, baseBuild()
					.in(TLpglHouse::getId, ids));
				offset += dealList.size();
			}
		} else {
			for (TLpglHouse theModify : modifyList) {
				modifyNum += instance.update(theModify, baseBuild()
					.eq(TLpglHouse::getId, theModify.getId()));
			}
		}
		return modifyNum;
	}

	@Override
	public List<TLpglHouse> selectWithListWhereEstateIdAndBuildingNumAndHouseNumCondition(List<TLpglHouse> todoList) {
		MybatisPlusBuild baseBuild = baseBuild();
		int cnt = 0;
		for (TLpglHouse element : todoList) {
			Long estateId = element.getEstateId();
			Integer buildingNum = element.getBuildingNum();
			Integer houseNum = element.getHouseNum();

			baseBuild.eq(TLpglHouse::getEstateId, estateId)
				.eq(TLpglHouse::getBuildingNum, buildingNum)
				.eq(TLpglHouse::getHouseNum, houseNum);
			if (cnt != 0) {
				baseBuild.groupAfter();
			}

			if (cnt != todoList.size() - 1) {
				baseBuild.or()
					.groupBefore();
			}
			cnt++;
		}
		return MybatisPlus.getInstance().findAll(new TLpglHouse(), baseBuild
		);
	}

	@Override
	public List<TLpglHouse> selectByEstateIdInSalesManIdsPage(Long estateId, Integer pageNum, Integer pageSize, List<Long> salesManIds) {
		MybatisPlusBuild mybatisPlusBuild = byEstateBuild(estateId);
		mybatisPlusBuild = salesManIds == null || salesManIds.isEmpty() ? mybatisPlusBuild.in(TLpglHouse::getSaleManId) : mybatisPlusBuild;
		return MybatisPlus.getInstance().findAll(new TLpglHouse(), mybatisPlusBuild
		);
	}


	public static void main(String[] args) {
		MybatisPlusBuild baseBuild = new MybatisPlusBuild(TLpglHouse.class)
			.eq(TLpglHouse::getIsValid, AppConstant.IS_VALID_YES);
		;
		ArrayList<TLpglHouse> todoList = new ArrayList<>();
		for (int i = 0; i < 4000; i++) {
			TLpglHouse tLpglHouse = new TLpglHouse();
			tLpglHouse.setEstateId(Long.valueOf(i));
			tLpglHouse.setBuildingNum(i);
			tLpglHouse.setHouseNum(i);
			todoList.add(tLpglHouse);
		}

		int cnt = 0;
		for (TLpglHouse element : todoList) {
			Long estateId = element.getEstateId();
			Integer buildingNum = element.getBuildingNum();
			Integer houseNum = element.getHouseNum();

			baseBuild.eq(TLpglHouse::getEstateId, estateId)
				.eq(TLpglHouse::getBuildingNum, buildingNum)
				.eq(TLpglHouse::getHouseNum, houseNum);
			if (cnt != 0) {
				baseBuild.groupAfter();
			}

			if (cnt != todoList.size() - 1) {
				baseBuild.or()
					.groupBefore();
			}
			cnt++;
		}

		String build = baseBuild.build();
		System.out.println(build);
	}

	/*public static void main(String[] args) {
		List<Integer> addList = IntStream.rangeClosed(0, 55).boxed().collect(Collectors.toList());
		System.out.println("TOTAL NUMS <=> " + addList);
		//如果数量超过200，进行分割
		int offset = 0;
		int piece = 200;
		//分片插入
		while (addList.size() > offset) {
			List<Integer> nums = addList.stream()
				.skip(offset)
				.limit(piece)
				.collect(Collectors.toList());
			System.out.println("nums of this piece <=> " + nums);
			offset += nums.size();
		}
	}*/
}
