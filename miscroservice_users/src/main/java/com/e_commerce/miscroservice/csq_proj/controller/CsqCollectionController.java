package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.csq_proj.po.TCsqService;
import com.e_commerce.miscroservice.csq_proj.service.CsqCollectionService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqCollectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 从善桥收藏Controller
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:35
 */
@RestController
@Log
@RequestMapping("collection")
public class CsqCollectionController {

	@Autowired
	private CsqCollectionService csqCollectionService;

	/**
	 * 点击收藏 取消收藏
	 * @param serviceId 服务id
	 * @return
	 */
	@RequestMapping("click/auth")
	public Object clickCollection(Long serviceId){
		AjaxResult result = new AjaxResult();
		Integer userId = IdUtil.getId();
		log.info("收藏={},userId={}",serviceId,userId);
		try {
			csqCollectionService.clickCollection(serviceId,userId);
		}catch (MessageException e){
			log.warn(e.getMessage());

			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}catch (Exception e){

		}
		return result;
	}


	/**
	 * 收藏列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("list/auth")
	public Object collectionList(Integer pageNum, Integer pageSize){
		AjaxResult result = new AjaxResult();
		Integer userId = IdUtil.getId();
		log.info("收藏列表id={},Num={},size={}",userId,pageNum,pageSize);
		try {
			QueryResult<CsqCollectionVo> list = csqCollectionService.collectionList(pageNum,pageSize,userId);
			result.setData(list);
			result.setSuccess(true);
		}catch (MessageException e){
			log.warn(e.getMessage());

			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}catch (Exception e){

		}


		return result;
	}


}
