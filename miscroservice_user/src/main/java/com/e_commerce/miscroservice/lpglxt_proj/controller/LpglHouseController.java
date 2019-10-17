package com.e_commerce.miscroservice.lpglxt_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.dao.LpglGroupDao;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglCertService;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 房源
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:59
 */
@RequestMapping("lpgl/house")
@RestController
@Log
public class LpglHouseController {

	@Autowired
	private LpglHouseService lpglHouseService;

	@Autowired
	private LpglCertService lpglCertService;

	@Autowired
	private LpglGroupDao lpglGroupDao;

	/**
	 * 商品房列表
	 * @param estateId 楼盘编号
	 * @param buildingNum 楼号
	 * @param pageNum 页码
	 * @param pageSize 大小
	 * @param groupId 分组编号
	 * @return
	 */
	@RequestMapping("list")
	public Object houseList(Long estateId, Integer buildingNum, Integer pageNum, Integer pageSize, Long groupId) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("商品房列表, userId, estateId={}, buildingNum={}, pageNum={}, pageSize={}, groupId={}", userId, estateId, buildingNum, pageNum, pageSize, groupId);
			QueryResult list = lpglHouseService.list(userId, estateId, pageNum, pageSize, groupId);
			result.setData(list);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "商品房列表", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("商品房列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 商品房详情
	 * @param houseId 商品房编号
	 * @return
	 */
	@RequestMapping("detail")
	public Object houseDetail(Long houseId) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("商品房详情, houseId={}", houseId);
			TLpglHouse res = lpglHouseService.detail(houseId);
			result.setData(res);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "商品房详情", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("商品房详情", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 商品房信息修改
	 * @param id 商品房编号
	 * @param houseNum 房号
	 * @param name 名字
	 * @param houseType 户型
	 * @param structType 结构
	 * @param buildingNum 楼号
	 * @param groupNum 单元号
	 * @param floorNum 楼层号
	 * @param direction 朝向
	 * @param buildingArea 建筑面积
	 * @param insideArea 室内面积
	 * @param buildingPrice 建筑单价
	 * @param insidePrice 室内单价
	 * @return
	 */
	@RequestMapping("modify")
	@Consume(TLpglHouse.class)
	public Object houseModify(@RequestParam Long id, Integer houseNum, String name, String houseType,
							  String structType, Integer buildingNum, Integer groupNum, Integer floorNum,
							  String direction, Double buildingArea, Double insideArea, Double buildingPrice, Double insidePrice) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		TLpglHouse obj = (TLpglHouse) ConsumeHelper.getObj();
		try {
			log.info("商品房信息修改, id={}, houseNum={}, name={}, houseType={}, structType={}, buildingNum={}, " +
				"groupNum={}, floorNum={}, direction={}, buildingArea={}, insideArea={}, buildingPrice={}, insidePrice={}", id, houseNum, name, houseType, structType,
				buildingNum, groupNum, floorNum, direction, buildingArea, insideArea, buildingPrice, insidePrice);
			lpglHouseService.modify(obj);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "商品房信息修改", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("商品房信息修改", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 商品房信息添加
	 * @param houseNum 房号
	 * @param name 名字
	 * @param houseType 户型
	 * @param structType 结构
	 * @param buildingNum 楼号
	 * @param groupNum 单元号
	 * @param floorNum 楼层号
	 * @param direction 朝向
	 * @param buildingArea 建筑面积
	 * @param insideArea 室内面积
	 * @param buildingPrice 建筑单价
	 * @param insidePrice 室内单价
	 * @param status 状态
	 * @param isValid 有效性0无效1有效
	 * @return
	 */
	@RequestMapping("add")
	@Consume(TLpglHouse.class)
	public Object houseAdd(Integer houseNum, String name, String houseType,
							  String structType, Integer buildingNum, Integer groupNum, Integer floorNum,
							  String direction, Double buildingArea, Double insideArea, Double buildingPrice, Double insidePrice, Integer status, Integer isValid) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		TLpglHouse obj = (TLpglHouse) ConsumeHelper.getObj();
		try {
			log.info("商品房信息添加, houseNum={}, name={}, houseType={}, structType={}, buildingNum={}, " +
					"groupNum={}, floorNum={}, direction={}, buildingArea={}, insideArea={}, buildingPrice={}, insidePrice={}, status={}, isValid={}", houseNum, name, houseType, structType,
				buildingNum, groupNum, floorNum, direction, buildingArea, insideArea, buildingPrice, insidePrice, status, isValid);
			lpglHouseService.add(obj);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "商品房信息添加", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("商品房信息添加", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 商品房移除
	 * @param houseId 商品房编号
	 * @return
	 */
	@RequestMapping("remove")
	public Object houseRemove(Long houseId) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("商品房移除, houseId={}", houseId);
			lpglHouseService.remove(houseId);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "商品房移除", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("商品房移除", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 商品房信息导入
	 * @param estateId 楼盘编号
	 * @param file 多部件文件
	 * @param skipModify 跳过覆写
	 * @return
	 */
	@RequestMapping("record/in")
	public Object houseRecordIn(Long estateId, MultipartFile file, boolean skipModify) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("商品房信息导入, estateId={}, file={}, skipModify={}", estateId, file, skipModify);
			lpglHouseService.recordIn(estateId, file, skipModify);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn("====方法描述: {}, Message: {}====", "商品房信息导入", e.getMessage());
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("商品房信息导入", e);
			result.setSuccess(false);
		}
		return result;
	}



	/**
	 * 添加房源
	 * @param estateId
	 * @param houseNum
	 * @param name
	 * @param houseType
	 * @param structType
	 * @param buildingNum
	 * @param groupNum
	 * @param floorNum
	 * @param directionNum
	 * @param buildingArea
	 * @param insideArea
	 * @param buildingPrice
	 * @param insidePrice
	 * @param status
	 * @return
	 */
	@Consume(TLpglHouse.class)
	@RequestMapping("house/insert")
	public Object houseInsert(	Long estateId,Integer houseNum,String name, String houseType,String structType, Integer buildingNum,Integer groupNum, Integer floorNum,Integer directionNum, Double buildingArea,Double insideArea, Double buildingPrice,Double insidePrice, Integer status){
		AjaxResult result = new AjaxResult();
		TLpglHouse tLpglHouse =  (TLpglHouse)ConsumeHelper.getObj();
		int i = lpglHouseService.save(tLpglHouse);
		log.info("房源录入");
		result.setSuccess(true);
		return result;
	}
}
