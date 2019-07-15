package com.e_commerce.miscroservice.csq_proj.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.csq_proj.service.CsqCollectionService;
import com.e_commerce.miscroservice.csq_proj.vo.CsqCollectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 从善桥收藏Controller
 *
 * @Author: FangyiXu
 * @Date: 2019-06-11 15:35
 */
@RestController
@Log
@RequestMapping("csq/collection")
public class CsqCollectionController {

	@Autowired
	private CsqCollectionService csqCollectionService;

	/**
	 * 点击收藏 取消收藏
	 *
	 * @param serviceId 服务id
	 *                  <p>
	 *                  {"success":true,"errorCode":"","msg":"","data":""}
	 * @return
	 */
	@RequestMapping({"click", "click/" + TokenUtil.AUTH_SUFFIX})
	public AjaxResult clickCollection(Long serviceId) {
		AjaxResult result = new AjaxResult();
//		Integer userId = IdUtil.getId();
		Integer userId = UserUtil.getTestId().intValue();
		log.info("收藏={},userId={}", serviceId, userId);
		try {
			csqCollectionService.clickCollection(serviceId, userId);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn(e.getMessage());

			result.setSuccess(false);
			result.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("点击收藏 取消收藏", e);
			result.setSuccess(false);
		}
		return result;
	}


	/**
	 * 收藏列表
	 *
	 * @param pageNum  页码
	 * @param pageSize 大小
	 *                 <p>
	 *                 {
	 *                 "resultList":[
	 *                 {
	 *                 "purpose":"", //项目内容
	 *                 "name":"发布一个项目",	//项目名
	 *                 "detailPic":"",	//描述图
	 *                 "desc":"你认真的样子好像天桥底下贴膜的",	//项目描述
	 *                 "recordId":"",	//备案id
	 *                 "serviceId":2,	//项目id
	 *                 "sumTotalIn":0,	//累积筹到金额
	 *                 "surplusAmount":0	//剩余金额
	 *                 }
	 *                 ],
	 *                 "totalCount":1
	 *                 }
	 * @return
	 */
	@RequestMapping({"list", "list/" + TokenUtil.AUTH_SUFFIX})
	public AjaxResult collectionList(Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
//		Integer userId = IdUtil.getId();
		Long userId = UserUtil.getTestId();
		log.info("收藏列表id={},Num={},size={}", userId, pageNum, pageSize);
		try {
			QueryResult<CsqCollectionVo> list = csqCollectionService.collectionList(pageNum, pageSize, userId.intValue());
			result.setData(list);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn(e.getMessage());

			result.setSuccess(false);
			result.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("收藏列表", e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 是否收藏
	 *
	 * @param serviceId 项目编号
	 *                  <p>
	 *                  {"success":true,"errorCode":"","msg":"","data":""}
	 * @return
	 */
	@RequestMapping({"isCollection", "isCollection/" + TokenUtil.AUTH_SUFFIX})
	public AjaxResult isCollection(Long serviceId) {
		AjaxResult result = new AjaxResult();
//		Integer userId = IdUtil.getId();
		Long userId = UserUtil.getTestId();
		log.info("是否收藏={},userId={},serviceId={}", userId, serviceId);
		try {
			boolean isCollection = csqCollectionService.isCollection(userId, serviceId);
			result.setData(isCollection);
			result.setSuccess(true);
		} catch (MessageException e) {
			log.warn(e.getMessage());
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("是否收藏", e);
			result.setSuccess(false);
		}
		return result;
	}


}
