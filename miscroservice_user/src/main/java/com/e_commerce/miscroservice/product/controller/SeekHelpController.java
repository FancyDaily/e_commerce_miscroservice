package com.e_commerce.miscroservice.product.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.product.vo.PageMineReturnView;
import com.e_commerce.miscroservice.product.vo.ProductSubmitParamView;
import com.e_commerce.miscroservice.product.vo.ServiceParamView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
	 *                 "imgUrl": "封面图"
	 *                 }
	 *                 ],
	 *                 "totalCount": 总条数
	 *                 }
	 *                 }
	 * @return
	 */
	@PostMapping("/pageMine")
	public Object pageMine(String token, Integer pageNum, Integer pageSize) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		try {
			QueryResult<PageMineReturnView> list = productService.pageMine(user, pageNum, pageSize, ProductEnum.TYPE_SEEK_HELP.getValue());
			result.setData(list);
			result.setSuccess(true);
			result.setMsg("查询成功");
		} catch (MessageException e) {
			logger.warn("查询失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败," + e.getMessage());
		} catch (Exception e) {
			logger.error("查询失败" + errInfo(e), e);
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("查询失败");
		}
		return result;
	}

	/**
	 * 上架求助服务
	 *
	 * @param token     当前用户token
	 * @param productId 商品ID
	 *                  {
	 *                  "success": 是否成功,
	 *                  "msg": "成功或错误消息"
	 *                  }
	 * @return
	 */
	@PostMapping("/upperFrame")
	public Object upperFrame(String token, Long productId) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		try {
			productService.upperFrame(user, productId);
			result.setSuccess(true);
			result.setMsg("上架成功");
			return result;
		} catch (MessageException e) {
			logger.warn("上架失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg("上架失败," + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error("上架失败" + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("上架失败");
			return result;
		}
	}


	/**
	 * 删除求助
	 *
	 * @param token     当前用户token
	 * @param productId 商品ID
	 *                  {
	 *                  "success": 是否成功,
	 *                  "msg": "成功或错误消息"
	 *                  }
	 * @return
	 */
	@PostMapping("/delSeekHelp")
	public Object delSeekHelp(String token, Long productId) {
		AjaxResult result = new AjaxResult();
		TUser user = (TUser) redisUtil.get(token);
		try {
			productService.del(user, productId);
			result.setSuccess(true);
			result.setMsg("删除成功");
			return result;
		} catch (MessageException e) {
			logger.warn("删除失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg("删除失败," + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error("删除失败" + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("删除失败");
			return result;
		}
	}

	/**
	 * 下架求助
	 *
	 * @param token     用户token
	 * @param productId 商品ID
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
			result.setMsg("下架成功");
			return result;
		} catch (MessageException e) {
			logger.warn("下架失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg("下架失败," + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error("下架失败" + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("下架失败");
			return result;
		}
	}


	/**
	 * 发布求助
	 *
	 * @param request
	 * @param param
	 * @param token
	 * @return
	 */
	@PostMapping("/submit")
	@Consume(ProductSubmitParamView.class)
	public Object submitSeekHelp(HttpServletRequest request, @RequestBody ServiceParamView param, String token) {
		AjaxResult result = new AjaxResult();
		//从拦截器中获取参数的String
/*		String paramString = (String) request.getAttribute("paramString");
		request.removeAttribute("paramString");
		ServiceParamView param = JsonUtil.parseFromJson(paramString, ServiceParamView.class);
		String token = param.getToken();*/

		TUser user = (TUser) redisUtil.get(token);
		//这一层可判断出是求助，手动设置type参数
		param.getService().setType(ProductEnum.TYPE_SEEK_HELP.getValue());
		try {
			productService.submitSeekHelp(user, param, token);
			result.setSuccess(true);
			result.setMsg("发布求助成功");
			return result;
		} catch (MessageException e) {
			logger.warn("发布求助失败," + e.getMessage());
			result.setSuccess(false);
			result.setErrorCode(e.getErrorCode());
			result.setMsg("发布求助失败," + e.getMessage());
			return result;
		} catch (Exception e) {
			logger.error("发布求助失败" + errInfo(e));
			result.setSuccess(false);
			result.setErrorCode("500");
			result.setMsg("发布求助失败");
			return result;
		}

	}
}
