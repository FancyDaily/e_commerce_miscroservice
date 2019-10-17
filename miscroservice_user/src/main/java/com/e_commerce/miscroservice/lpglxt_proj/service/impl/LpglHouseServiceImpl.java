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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		List<TLpglUser> tLpglUsers = lpglUserDao.selectByGroupId(groupId);
		List<Long> userIds = tLpglUsers.stream()
			.map(TLpglUser::getId).collect(Collectors.toList());
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
			InputStream inputStream = new FileInputStream(new File("/Users/xufangyi/Downloads/楼盘管理导入demo.xlsx"));
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
			List<TLpglHouse> lpglHouses = lpglHouseDao.selectWithListWhereEstateIdAndBuildingNumAndHouseNumCondition(todoList);
			Map<Integer, List<TLpglHouse>> idHouseMap = lpglHouses.stream().collect(Collectors.groupingBy(TLpglHouse::getHouseNum));

			Map<Boolean, List<TLpglHouse>> conditonMap = todoList.stream()
				.collect(Collectors.partitioningBy(a -> idHouseMap.get(a.getHouseNum()) != null));	//新增/修改 推断分区
			List<TLpglHouse> modifyList = conditonMap.get(Boolean.TRUE);
			List<TLpglHouse> addList = conditonMap.get(Boolean.FALSE);
			lpglHouseDao.insert(addList, true);

			if(!skipModify) {
				//处理待修改的数据,赋予id
				modifyList = modifyList.stream()
					.map(a -> {
						List<TLpglHouse> tLpglHouses = idHouseMap.get(a.getHouseNum());
						a.setId(tLpglHouses == null? null: tLpglHouses.get(0).getId());
						return a;
					}).collect(Collectors.toList());
				modifyList = modifyList.stream()
					.filter(a -> a.getId() != null).collect(Collectors.toList());

				lpglHouseDao.modify(modifyList, true);	//TODO 批量更新不可用
			}

		} catch (IOException e) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		String path = "/Users/xufangyi/Downloads/导入模版.xlsx";
		try {
			List<String[]> strings = POIUtil.readFromPath(path);
			strings.stream()
				.forEach(a -> {
					Arrays.stream(a).forEach(System.out::println);
				});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int save(TLpglHouse tLpglHouse) {
		int i = MybatisPlus.getInstance().save(tLpglHouse);
		return i;
	}
}
