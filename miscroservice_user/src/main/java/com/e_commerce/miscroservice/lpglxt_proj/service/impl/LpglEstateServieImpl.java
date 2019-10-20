package com.e_commerce.miscroservice.lpglxt_proj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlus;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisPlusBuild;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.utils.PageUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglEstateDao;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglHouseDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglEstate;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglEstateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:05
 */
@Service
public class LpglEstateServieImpl implements LpglEstateService {

	@Autowired
	private LpglEstateDao lpglEstateDao;

	@Autowired
	private LpglHouseDao lpglHouseDao;

	@Override
	public QueryResult list(Integer pageNum, Integer pageSize) {
		List<TLpglEstate> tLpglEstates = lpglEstateDao.selectAllPage(pageNum, pageSize);
		long total = IdUtil.getTotal();

		return PageUtil.buildQueryResult(tLpglEstates, total);
	}

	@Override
	public Map<String, Map<Integer, Map<Double, Map<Integer, List<TLpglHouse>>>>> houseMap(Long estateId, Integer buildingNum) {
		List<TLpglHouse> tLpglHouses =
			estateId == null ? lpglHouseDao.selectAll() :
				buildingNum == null ? lpglHouseDao.selectByEstateId(estateId) :
					lpglHouseDao.selectByEstateIdAndBuildingNum(estateId, buildingNum);

		if(tLpglHouses == null) {
			return new HashMap<>();
		}
		//按楼号分组
		return tLpglHouses.stream()
			.collect(groupingBy(TLpglHouse::getGroupName,
				groupingBy(TLpglHouse::getBuildingNum,
					groupingBy(TLpglHouse::getBuildingArea,
						groupingBy(TLpglHouse::getFloorNum)
					)
				)
				)
			);
	}

	@Override
	public void save(String name) {
		TLpglEstate build = TLpglEstate.builder()
			.name(name).build();
		lpglEstateDao.insert(build);
	}

	@Override
	public void modify(Long estateId, String name, String isValid) {
		TLpglEstate build = TLpglEstate.builder().name(name).build();
		build.setId(estateId);
		build.setIsValid(isValid);
		lpglEstateDao.update(build);
	}

	@Override
	public AjaxResult houseTopic(Long estateId, Integer buildingNum) {
		AjaxResult ajaxResult = new AjaxResult();
		List<TLpglHouse> list = MybatisPlus.getInstance().findAll(new TLpglHouse(),new MybatisPlusBuild(TLpglHouse.class)
			.eq(TLpglHouse::getEstateId,estateId)
			.eq(TLpglHouse::getBuildingNum,buildingNum)
		);
		List<Double> result = new LinkedList<>();
		if (list!=null&&list.size()>0){
			list.forEach(tLpglHouse -> {
					result.add(tLpglHouse.getBuildingArea());
				}
			);
		}
		ajaxResult.setData(result);
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	/*
	list: [

                {
                    louceng: "7号楼",

                    danyuan: [{

                        num: 1,

                        headerList:  [
                            20,20,20,20,20,20,20,20
                        ],


                        list: [
                            {
                                index: 1,
                                room: [
                                    {num: 101},
                                    {num: 102},
                                    {num: 103},
                                    {num: 104},
                                    {num: 105},
                                    {num: 106},
                                    {num: 107},
                                    {num: 108}
                                ]
                            },
                            {
                                index: 2,
                                room: [
                                    {num: 101},
                                    {num: 102},
                                    {num: 103},
                                    {num: 104},
                                    {num: 105},
                                    {num: 106},
                                    {num: 107},
                                    {num: 108}
                                ]
                            },
                        ]


                    }]
                },


                {
                    louceng: "8号楼",

                    danyuan: [{

                        num: 1,

                        headerList:  [
                            20,20,20,20,20,20,20,20
                        ],


                        list: [
                            {
                                index: 1,
                                room: [
                                    {num: 101},
                                    {num: 102},
                                    {num: 103},
                                    {num: 104},
                                    {num: 105},
                                    {num: 106},
                                    {num: 107},
                                    {num: 108}
                                ]
                            },
                        ]


                    }]
                }

            ],
	 */
	@Override
	public AjaxResult houseContent(Long estateId, String buildingNum) {
		AjaxResult ajaxResult = new AjaxResult();
		List result = new LinkedList();
		List<TLpglHouse> allHouse = new ArrayList<>();
		String[] strs = buildingNum.split(",");
		List<Integer> numberList = new ArrayList<>();
		if (StringUtils.isNotEmpty(buildingNum) &&strs.length>0){
			for (String str : strs) {
				Integer number = Integer.valueOf(str);
				numberList.add(number);
			}
		}
		if (numberList!=null&&numberList.size()>0){
			for (Integer itg : numberList) {
				List<TLpglHouse> lis = MybatisPlus.getInstance().findAll(new TLpglHouse(),new MybatisPlusBuild(TLpglHouse.class)
					.eq(TLpglHouse::getEstateId,estateId)
					.eq(TLpglHouse::getBuildingNum,itg)
				);
				if (lis!=null&&lis.size()>0){
					allHouse.addAll(lis);
				}
			}
//			allHouse = MybatisPlus.getInstance().findAll(new TLpglHouse(),new MybatisPlusBuild(TLpglHouse.class)
//				.eq(TLpglHouse::getEstateId,estateId)
//				.eq(TLpglHouse::getBuildingNum,buildingNum)
//			);
		}else {
			allHouse = MybatisPlus.getInstance().findAll(new TLpglHouse(),new MybatisPlusBuild(TLpglHouse.class)
				.eq(TLpglHouse::getEstateId,estateId)
			);
		}
		//楼号列表
		List<Integer> buildingNumList = new LinkedList<>();
		if (allHouse!=null&&allHouse.size()>0){
			allHouse.forEach(tLpglHouse -> {
					if (!buildingNumList.contains(tLpglHouse.getBuildingNum())){
						Integer num = tLpglHouse.getBuildingNum();
						JSONObject lou = new JSONObject();
						buildingNumList.add(tLpglHouse.getBuildingNum());
						List<TLpglHouse> group = MybatisPlus.getInstance().findAll(new TLpglHouse(),new MybatisPlusBuild(TLpglHouse.class)
							.eq(TLpglHouse::getEstateId,estateId)
							.eq(TLpglHouse::getBuildingNum,num)
							.orderBy(MybatisPlusBuild.OrderBuild.buildAsc(TLpglHouse::getBuildingNum))
						);
						lou.put("louceng",num+"号楼");

						List groupNameList = new LinkedList<>();
						List danyuanResult = new LinkedList<>();
						//单元

						if (group!=null&&group.size()>0){
							group.forEach(tLpglHouse2 -> {
									if (!groupNameList.contains(tLpglHouse.getGroupName())){
										String groupName = tLpglHouse.getGroupName();
										JSONObject danyuan = new JSONObject();
										groupNameList.add( tLpglHouse.getGroupName());
										danyuan.put("num",groupName);
										List headerList = new LinkedList();
										List<TLpglHouse> danyuanList = MybatisPlus.getInstance().findAll(new TLpglHouse(),new MybatisPlusBuild(TLpglHouse.class)
											.eq(TLpglHouse::getEstateId,estateId)
											.eq(TLpglHouse::getBuildingNum,num)
											.eq(TLpglHouse::getGroupName,groupName)
											.orderBy(MybatisPlusBuild.OrderBuild.buildAsc(TLpglHouse::getGroupName))

										);
										List floorNumList = new LinkedList();
										List list = new LinkedList();
										danyuanList.forEach(danyuanName->{
											if (!floorNumList.contains(danyuanName.getFloorNum())){
												JSONObject floor = new JSONObject();
												floor.put("index",danyuanName.getFloorNum());
												floorNumList.add(danyuanName.getFloorNum());
												List numList = new LinkedList();
												List<TLpglHouse> floorList = MybatisPlus.getInstance().findAll(new TLpglHouse(),new MybatisPlusBuild(TLpglHouse.class)
													.eq(TLpglHouse::getEstateId,estateId)
													.eq(TLpglHouse::getBuildingNum,num)
													.eq(TLpglHouse::getGroupName,groupName)
													.eq(TLpglHouse::getFloorNum,danyuanName.getFloorNum())
													.orderBy(MybatisPlusBuild.OrderBuild.buildAsc(TLpglHouse::getFloorNum))

												);
												floorList.forEach(numObj->{
													JSONObject obj = new JSONObject();
//													String str = numObj.getHouseNum()+","+numObj.getId()+","+numObj.getStatus();
//													if (numObj.getSaleManId()!=null){
//														str=str+","+numObj.getSaleManId();
//													}
													obj.put("num",numObj.getHouseNum());
													obj.put("roomId",numObj.getId());
													obj.put("status",numObj.getStatus());
													obj.put("saleManId",numObj.getSaleManId());
													numList.add(obj);
													if (headerList.size()<floorList.size()){
														headerList.add(numObj.getBuildingArea());
													}

												});
												floor.put("room",numList);
												list.add(floor);

											}
										});
										danyuan.put("headerList",headerList);
										danyuan.put("list",list);
										danyuanResult.add(danyuan);
									}
								}
							);
						}
						lou.put("danyuan",danyuanResult);
						result.add(lou);
					}
				}
			);
		}



//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("index",)


		ajaxResult.setData(result);
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}
}
