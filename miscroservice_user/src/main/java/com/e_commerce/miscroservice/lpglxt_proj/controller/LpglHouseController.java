package com.e_commerce.miscroservice.lpglxt_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.lpglxt_proj.po.TLpglHouse;
import com.e_commerce.miscroservice.lpglxt_proj.service.LpglHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: FangyiXu
 * @Date: 2019-10-14 16:59
 */
@RequestMapping("lpgl/house")
@RestController
@Log
public class LpglHouseController {

	@Autowired
	private LpglHouseService lpglHouseService;

	/**
	 * 商品房列表
	 * @return
	 */
	@RequestMapping("house/list")
	public Object houseList(Long estateId, Integer buildingNum, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		Long userId = IdUtil.getId();
		try {
			log.info("商品房列表, estateId={}, buildingNum={}, pageNum={}, pageSize={}", estateId, buildingNum, pageNum, pageSize);
			QueryResult list = lpglHouseService.list(estateId, pageNum, pageSize);
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
