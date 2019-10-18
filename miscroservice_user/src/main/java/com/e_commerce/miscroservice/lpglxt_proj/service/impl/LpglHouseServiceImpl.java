package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.util.colligate.POIUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglEstateDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglHouseDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglUserDao;
import com.e_commerce.miscroservice.lpglxt_proj.enums.TlpglHouseEnum;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglEstate;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglUser;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:40
 */
@Service
public class LpglHouseServiceImpl implements LpglHouseService {

	@Autowired
	private LpglHouseDao lpglHouseDao;

	@Autowired
	private LpglEstateDao lpglEstateDao;

	@Autowired
	private LpglUserDao lpglUserDao;

	@Override
	public QueryResult list(Long userId, Long estateId, Integer pageNum, Integer pageSize, Long groupId) {
		List<Long> userIds = new ArrayList<>();
		if(groupId != null) {
			List<TLpglUser> tLpglUsers = lpglUserDao.selectByGroupId(groupId);
			userIds = tLpglUsers.stream()
				.map(TLpglUser::getId).collect(Collectors.toList());
		} else {
			userIds.add(userId);
		}
		List<TLpglHouse> tLpglHouses = lpglHouseDao.selectByEstateIdInSalesManIdsPage(estateId, pageNum, pageSize, userIds);
		long total = IdUtil.getTotal();

		return PageUtil.buildQueryResult(tLpglHouses, total);
	}

	@Override
	public TLpglHouse detail(Long houseId) {
		return lpglHouseDao.selectByPrimaryKey(houseId);
	}

	@Override
	public int modify(TLpglHouse obj) {
		return lpglHouseDao.update(obj);
	}

	@Override
	public int add(TLpglHouse obj) {
		return lpglHouseDao.insert(obj);
	}

	@Override
	public void remove(Long houseId) {
		lpglHouseDao.remove(houseId);
	}

	@Override
	public List<String> recordIn(Long estateId, MultipartFile file, boolean skipModify) {
		try {
//			POIUtil.checkFile(file);
			//确认楼盘是否存在
			TLpglEstate tLpglEstate = lpglEstateDao.selectByPrimaryKey(estateId);
			if(tLpglEstate == null) throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "该楼盘不存在！");
			//收集表格数据
//			InputStream inputStream = file.getInputStream();
			InputStream inputStream = file != null? file.getInputStream() : new FileInputStream(new File("/Users/xufangyi/Downloads/楼盘管理导入demo.xlsx"));
			List<Map<String, Object>> maps = POIUtil.estateDemo(inputStream);
			List<TLpglHouse> todoList = maps.stream()
				.map(
					a -> {
						Integer houseNum = (Integer) a.get("houseNum");
						Integer floorNum = (Integer) a.get("floorNum");
						Double area = (Double) a.get("area");
						Integer buildingNum = (Integer) a.get("buildingNum");
						String groupName = (String) a.get("groupName");
						Boolean haveColor = (Boolean) a.get("haveColor");
						return TLpglHouse.builder()
							.estateId(estateId)
							.houseNum(houseNum)
							.floorNum(floorNum)
							.buildingArea(area)
							.buildingNum(buildingNum)
							.status(haveColor? TlpglHouseEnum.STATUS_SOLDOUT.getCode() : TlpglHouseEnum.STATUS_INITAIL.getCode())
							.groupName(groupName).build();
					}
				).collect(Collectors.toList());
			//插入数据(楼盘号、楼号、房间号已存在则进行修改
			dealWithImportDatas(skipModify, todoList);

		} catch (IOException e) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, e.getMessage());
		}
		return null;
	}

	private void dealWithImportDatas(boolean skipModify, List<TLpglHouse> todoList) {
		List<TLpglHouse> lpglHouses = lpglHouseDao.selectWithListWhereEstateIdAndBuildingNumAndHouseNumCondition(todoList);
		Map<Integer, Map<Integer, List<TLpglHouse>>> idHouseMap = lpglHouses.stream().collect(Collectors.groupingBy(TLpglHouse::getBuildingNum, Collectors.groupingBy(TLpglHouse::getHouseNum)));

		Map<Boolean, List<TLpglHouse>> conditonMap = todoList.stream()
			.collect(Collectors.partitioningBy(a -> idHouseMap.get(a.getBuildingNum()) == null || idHouseMap.get(a.getBuildingNum()).get(a.getHouseNum()) == null));	//新增/修改 推断分区
		List<TLpglHouse> modifyList = conditonMap.get(Boolean.FALSE);
		List<TLpglHouse> addList = conditonMap.get(Boolean.TRUE);
		lpglHouseDao.insert(addList, true);

		if(!skipModify) {
			//处理待修改的数据,赋予id
			modifyList = modifyList.stream()
				.map(a -> {
					List<TLpglHouse> tLpglHouses = idHouseMap.get(a.getBuildingNum()).get(a.getHouseNum());
					a.setId(tLpglHouses == null? null: tLpglHouses.get(0).getId());
					return a;
				}).collect(Collectors.toList());
			modifyList = modifyList.stream()
				.filter(a -> a.getId() != null).collect(Collectors.toList());

			lpglHouseDao.modify(modifyList, false);	//TODO 批量更新不可用
		}
	}

	@Override
	public void recordInDetail(Long estateId, MultipartFile file, boolean skipModify) throws Exception {
		InputStream inputStream = file != null? file.getInputStream() : new FileInputStream(new File("/Users/xufangyi/Downloads/导入商品房数据.xls"));
		List<String[]> strings = POIUtil.readExcel(inputStream);
		List<List<String>> todoList = strings.stream().map(Arrays::asList).collect(Collectors.toList());
		List<String> titleString = todoList.get(0);
		String title = titleString.get(0);
		String buildingNum = title.substring(0, title.length() - 5);
		ArrayList<TLpglHouse> houses = new ArrayList<>();
		for(int i = 2; i < todoList.size(); i++) {
			List<String> listString = todoList.get(i);
			TLpglHouse build = TLpglHouse.builder()
				.estateId(estateId)
				.buildingNum(Integer.valueOf(buildingNum))
				.houseNum(Integer.valueOf(listString.get(0)))
				.buildingArea(Double.valueOf(listString.get(1)))
				.buildingPrice(Double.valueOf(listString.get(2)))
				.totalPrice(Double.valueOf(listString.get(3)))
				.bicycleNum(listString.get(4))
				.bicyclePrice(listString.get(5))
				.carPrice(listString.get(6))
				.build();
			houses.add(build);
		}
		houses
			.stream()
			.forEach(System.out::println);
		//插入或者修改
		dealWithImportDatas(skipModify, houses);
	}

	@Override
	public int save(TLpglHouse tLpglHouse) {
		int i = MybatisPlus.getInstance().save(tLpglHouse);
		return i;
	}
}
