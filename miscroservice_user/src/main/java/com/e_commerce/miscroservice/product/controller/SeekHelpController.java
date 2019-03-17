package com.e_commerce.miscroservice.product.controller;

import com.e_commerce.miscroservice.commons.constant.colligate.AppMessageConstant;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.product.util.AutoAnalysisWord;
import com.e_commerce.miscroservice.product.util.DateUtil;
import com.e_commerce.miscroservice.product.vo.AnalysisAudioView;
import com.e_commerce.miscroservice.product.vo.DetailProductView;
import com.e_commerce.miscroservice.product.vo.PageMineReturnView;
import com.e_commerce.miscroservice.product.vo.ServiceParamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * 求助模块
 * 包含发布求助  下架求助  删除求助  上架求助
 */
@RestController
@RequestMapping("/api/v2/seekHelp")
public class SeekHelpController extends BaseController {

	Log logger = Log.getInstance(SeekHelpController.class);

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 我发布的求助
	 *
	 * @param token    当前用户token
	 * @param pageNum  页数
	 * @param pageSize 每页数量
	 *                 <p>
	 *                 {
	 *                 "success": 是否成功,
	 *                 "msg": "成功或失败消息",
	 *                 "data": {
	 *                 "resultList": [
	 *                 {
	 *                 "service": {
	 *                 "id": 商品ID,
	 *                 "serviceName": "名称",
	 *                 "servicePlace": 线上或线下,
	 *                 "servicePersonnel": 需要人数,
	 *                 "status":状态  1、待审核  2、上架中   3/4、已下架  6、审核未通过
	 *                 "startTime": 开始时间毫秒值（单次显示开始时间使用此字段）,
	 *                 "endTime": 结束时间毫秒值(单词显示结束时间使用此字段),
	 *                 "timeType": 是否重复 0、不重复 1、重复性,
	 *                 "collectType": 收取分类, 1、互助时 2、公益时
	 *                 "collectTime": 收取时长,
	 *                 "nameAudioUrl": "音频地址",
	 *                 "dateWeekNumber": "5,6",
	 *                 "startDateS": 开始日期字符串  例："20190308",
	 *                 "endDateS": 结束日期字符串  例："20190310",
	 *                 "startTimeS": 开始时间字符串  例："1320",
	 *                 "endTimeS": 结束时间字符串   例："1340",
	 *                 "dateWeek":显示周X的字符串
	 *                 },
	 *                 "imgUrl": "封面图",
	 *                 "status": "显示的状态"
	 *                 }
	 *                 ],
	 *                 "totalCount": 总条数
	 *                 }
	 *                 }
	 * @return
	 */
	@RequestMapping("/pageMine")
	public Object pageMine(String token, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		try {
			QueryResult<PageMineReturnView> list = productService.pageMine(user, pageNum, pageSize, ProductEnum.TYPE_SEEK_HELP.getValue());
			result.setData(list);
			result.setSuccess(true);
			result.setMsg(AppMessageConstant.PRODUCT_QUERY_SUCCESS);
		} catch (MessageException e) {
			logger.warn(AppMessageConstant.PRODUCT_QUERY_ERROR + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg(AppMessageConstant.PRODUCT_QUERY_ERROR + e.getMessage());
		} catch (Exception e) {
			logger.error(AppMessageConstant.PRODUCT_QUERY_ERROR + errInfo(e), e);
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg(AppMessageConstant.PRODUCT_QUERY_ERROR);
		}
		return result;
	}

	/**
	 * @param token     用户token
	 * @param serviceId 商品ID
	 *                  {
	 *                  "success": 是否成功,
	 *                  "msg": "查询成功",
	 *                  "data": {
	 *                  "service": {
	 *                  "id": 103460827004141568,
	 *                  "type": 1、求助 2、服务,
	 *                  "status": 状态：1、待审核  2、上架中（可以选择上架） 3/4 已下架 （可以选择上架或删除）,
	 *                  "source": 来源 1、个人 2、组织,
	 *                  "serviceName": "标题",
	 *                  "servicePlace": 1、线上 2、线下,
	 *                  "labels": 标签,
	 *                  "servicePersonnel": 需要人数,
	 *                  "startTime": 开始时间毫秒值,
	 *                  "endTime": 结束时间毫秒值,
	 *                  "timeType": 0、固定时间 1、可重复,
	 *                  "dateWeekNumber": 周  字符串用逗号隔开  1、周一 2、周二  7、周日,
	 *                  "startDateS":  开始日期，"20190308",
	 *                  "endDateS": 结束日期 "20190330",
	 *                  "startTimeS": 开始时间 "0920",
	 *                  "endTimeS": 结束时间 "0940",
	 *                  "addressName": "地址名称",
	 *                  "longitude": "经度",
	 *                  "latitude": "纬度",
	 *                  "collectType": 收取类型  1、互助时 2、公益时,
	 *                  "collectTime": 收取时长,
	 *                  "nameAudioUrl": "音频地址",
	 *                  },
	 *                  "desc": [
	 *                  {
	 *                  "sort": 排序序号,
	 *                  "depict": "详情",
	 *                  "url": "图片地址",
	 *                  "isCover": "0",
	 *                  }
	 *                  ]
	 *                  }
	 *                  }
	 * @return
	 */
	@PostMapping("/productDetail")
	public Object detail(String token, Long serviceId) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		try {
			DetailProductView detailProductView = productService.detail(user, serviceId);
			result.setData(detailProductView);
			result.setSuccess(true);
			result.setMsg(AppMessageConstant.PRODUCT_QUERY_SUCCESS);
			return result;
		} catch (MessageException e) {
			logger.warn(AppMessageConstant.PRODUCT_QUERY_ERROR + e.getMessage());
			result.setSuccess(false);
			result.setMsg(AppMessageConstant.PRODUCT_QUERY_ERROR + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error(AppMessageConstant.PRODUCT_QUERY_ERROR + errInfo(e), e);
			result.setSuccess(false);
			result.setMsg(AppMessageConstant.PRODUCT_QUERY_ERROR);
			return result;
		}
	}


	/**
	 * 上架求助服务
	 *
	 * @param token     当前用户token
	 * @param productId 商品ID
	 *                  <p>
	 *                  {
	 *                  "success": 是否成功,
	 *                  "msg": "成功或错误消息"
	 *                  }
	 * @return
	 */
	@RequestMapping("/upperFrame")
	public Object upperFrame(String token, Long productId) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		try {
			productService.upperFrame(user, productId);
			result.setSuccess(true);
			result.setMsg(AppMessageConstant.PRODUCT_UPPERFRAME_SUCCESS);
			return result;
		} catch (MessageException e) {
			logger.warn(AppMessageConstant.PRODUCT_UPPERFRAME_ERROR + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg(AppMessageConstant.PRODUCT_UPPERFRAME_ERROR + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error(AppMessageConstant.PRODUCT_UPPERFRAME_ERROR + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg(AppMessageConstant.PRODUCT_UPPERFRAME_ERROR);
			return result;
		}
	}


	/**
	 * 删除求助
	 *
	 * @param token     当前用户token
	 * @param productId 商品ID
	 *                  <p>
	 *                  {
	 *                  "success": 是否成功,
	 *                  "msg": "成功或错误消息"
	 *                  }
	 * @return
	 */
	@RequestMapping("/delSeekHelp")
	public Object delSeekHelp(String token, Long productId) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		try {
			productService.del(user, productId);
			result.setSuccess(true);
			result.setMsg(AppMessageConstant.PRODUCT_DEL_SUCCESS);
			return result;
		} catch (MessageException e) {
			logger.warn(AppMessageConstant.PRODUCT_DEL_ERROR + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg(AppMessageConstant.PRODUCT_DEL_ERROR + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error(AppMessageConstant.PRODUCT_DEL_ERROR + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg(AppMessageConstant.PRODUCT_DEL_ERROR);
			return result;
		}
	}

	/**
	 * 下架求助
	 *
	 * @param token     用户token
	 * @param productId 商品ID
	 *                  <p>
	 *                  {
	 *                  "success": 是否成功,
	 *                  "msg": "成功或失败的消息"
	 *                  }
	 * @return
	 */
	@PostMapping("/lowerFrameSeekHelp")
	public Object lowerFrameSeekHelp(String token, Long productId) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		try {
			productService.lowerFrame(user, productId);
			result.setSuccess(true);
			result.setMsg(AppMessageConstant.PRODUCT_LOWERFRAME_SUCCESS);
			return result;
		} catch (MessageException e) {
			logger.warn(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR);
			return result;
		}
	}


	/**
	 * 发布
	 *
	 * @param request
	 * @param param
	 * @param token
	 * @return
	 */
	@PostMapping("/submit")
	public Object submitSeekHelp(HttpServletRequest request, @RequestBody ServiceParamView param, String token) {
		AjaxResult result = new AjaxResult();
		//从拦截器中获取参数的String
/*		String paramString = (String) request.getAttribute("paramString");
		request.removeAttribute("paramString");
		ServiceParamView param = JsonUtil.parseFromJson(paramString, ServiceParamView.class);
		String token = param.getToken();*/
		if (param.getService().getTimeType().equals(ProductEnum.TIME_TYPE_REPEAT.getValue())) {
			// 如果重复的，把前端传的固定毫秒值置位0
			param.getService().setStartTime(0L);
			param.getService().setEndTime(0L);
		}
		TUser user = (TUser) redisUtil.get(token);
		//这一层可判断出是求助，手动设置type参数
//		param.getService().setType(ProductEnum.TYPE_SEEK_HELP.getValue());
		try {
			if (Objects.equals(param.getService().getType(), ProductEnum.TYPE_SEEK_HELP.getValue())) {
				productService.submitSeekHelp(user, param, token);
				result.setSuccess(true);
			} else if (Objects.equals(param.getService().getType(), ProductEnum.TYPE_SERVICE.getValue())) {
				productService.submitService(user, param, token);
				result.setSuccess(true);
			} else {
				result.setSuccess(false);
				result.setMsg("请选择发布类型");
			}
			result.setMsg(AppMessageConstant.SEEKHELP_SUBMIT_SUCCESS);
			return result;
		} catch (MessageException e) {
			logger.warn(AppMessageConstant.SEEKHELP_SUBMIT_ERROR + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg(AppMessageConstant.SEEKHELP_SUBMIT_ERROR + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error(AppMessageConstant.SEEKHELP_SUBMIT_ERROR + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg(AppMessageConstant.SEEKHELP_SUBMIT_ERROR);
			return result;
		}
	}

	/**
	 * 发布求助文本解析
	 *
	 * @param text 解析后的文本内容
	 * @param city 左上角的地址
	 *             {
	 *             "serviceName": ",标题",
	 *             "startDateS": "开始日期： 如 20190315",
	 *             "endDateS": " 结束日期 如 20190316",
	 *             "startTimeS": "开始时间  如 1442",
	 *             "endTimeS": "结束时间 1100",
	 *             "longitude": 经度  101.777813,
	 *             "latitude": 纬度  36.630599,
	 *             "collectTime": 收取时间  10,
	 *             "addressName": "地址",
	 *             "type": 类型，1求助  2、服务,
	 *             "personCount": "人数",
	 *             "timeType": 重复周期  0、固定时间  1、可重复,
	 *             "dateWeekNumber": "周几"  如1,2,3,4,
	 *             "startTime": 具体的开始时间  固定时间时 传的开始时间 1552632120000,
	 *             "endTime": 具体的结束时间    1552705200000
	 *             }
	 * @return
	 */
	@PostMapping("/analysisWord")
	public AnalysisAudioView analysisWord(String text, String city) {
		AnalysisAudioView resultView = new AnalysisAudioView();
		AutoAnalysisWord analysisWord = new AutoAnalysisWord();
		Map<String, Object> map = analysisWord.parse(text, city);
		System.out.println(map);
		// 地址名称
		resultView.setAddressName((String) map.get("location"));
		//标题
		resultView.setServiceName((String) map.get("work"));
		//开始日期
		resultView.setStartDateS((String) map.get("startDate"));
		// 结束日期
		resultView.setEndDateS((String) map.get("endDate"));
		// 开始时间
		resultView.setStartTimeS((String) map.get("startTime"));
		// 结束时间
		resultView.setEndTimeS((String) map.get("endTime"));
		resultView.setDateWeekNumber((String) map.get("weekDay"));
		// 经度
		try {
			resultView.setLongitude((Double) map.get("longitude"));
		} catch (Exception e) {
		}
		// 纬度
		try {
			resultView.setLatitude((Double) map.get("latitude"));
		} catch (Exception e) {
		}
		if (resultView.getLongitude() == null || resultView.getLatitude() == null) {
			resultView.setServicePlace(0);
		} else {
			resultView.setServicePlace(ProductEnum.PLACE_UNDERLINE.getValue());
		}
		// 收取时间
		try {
			resultView.setCollectTime((Integer) map.get("payCount"));
		} catch (Exception e) {
		}
		// 发布类型 1、求助 2、服务
		try {
			resultView.setType((Integer) map.get("pushType"));
		} catch (Exception e) {
		}
		// 需要的人数
		try {
			resultView.setServicePersonnel((Integer) map.get("personCount"));
		} catch (Exception e) {
			resultView.setServicePersonnel(1);
		}

		if (StringUtil.isEmpty(resultView.getEndDateS())) { // 如果结束时间没有传，则往后加一天
			Long startDateMill = DateUtil.commonParse(resultView.getStartDateS(), "yyyy-MM-dd");
			Long nextDayStartMill = DateUtil.addDays(startDateMill, 1);
			resultView.setEndDateS(DateUtil.commonFormat(nextDayStartMill, "yyyy-MM-dd"));
		}

		if (StringUtil.isEmpty(resultView.getEndTimeS())) {
			resultView.setEndTimeS(resultView.getStartTimeS());
		}

		//如果weekDay为null 则是单次  若weekDay不为null，则是重复性
		if (resultView.getDateWeekNumber() == null || Objects.equals(resultView.getDateWeekNumber(), "null")) {
			resultView.setTimeType(ProductEnum.TIME_TYPE_FIXED.getValue());
		} else {
			resultView.setTimeType(ProductEnum.TIME_TYPE_REPEAT.getValue());
		}

		//如果是单次的，开始和结束时间都不为null或“” 就将开始时间和结束时间传回去
//		if (StringUtil.isNotEmpty(resultView.getStartDateS())) {
////			resultView.setStartDateS(resultView.getStartDateS().replaceAll("-", ""));
//		} else {
//			resultView.setStartDateS(DateUtil.format(System.currentTimeMillis()));
//		}
//		if (StringUtil.isNotEmpty(resultView.getEndDateS())) {
////			resultView.setEndDateS(resultView.getEndDateS().replaceAll("-", ""));
//		}
//		if (StringUtil.isNotEmpty(resultView.getStartTimeS())) {
//			resultView.setStartTimeS(resultView.getStartTimeS().replaceAll(":", ""));
//		}
//		if (StringUtil.isNotEmpty(resultView.getEndTimeS())) {
//			resultView.setEndTimeS(resultView.getEndTimeS().replaceAll(":", ""));
//		}
		if (resultView.getTimeType().equals(ProductEnum.TIME_TYPE_FIXED.getValue())) { //单次的求助服务
			if (StringUtil.isNotEmpty(resultView.getStartTimeS()) && StringUtil.isNotEmpty(resultView.getStartTimeS())) {
				resultView.setStartTime(DateUtil.commonParse(resultView.getStartDateS() + resultView.getStartTimeS(), "yyyy-MM-ddHH:mm"));
			}
			if (StringUtil.isNotEmpty(resultView.getEndDateS()) && StringUtil.isNotEmpty(resultView.getEndTimeS())) {
				resultView.setEndTime(DateUtil.commonParse(resultView.getEndDateS() + resultView.getEndTimeS(), "yyyy-MM-ddHH:mm"));
			}
		}
		return resultView;
	}
}
