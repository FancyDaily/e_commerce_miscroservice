package com.e_commerce.miscroservice.xiaoshi_proj.product.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.colligate.AppMessageConstant;
import com.e_commerce.miscroservice.commons.entity.application.TServiceSummary;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.commons.enums.application.ProductEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.xiaoshi_proj.product.vo.PageMineReturnView;
import com.e_commerce.miscroservice.xiaoshi_proj.product.vo.ServiceParamView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


/**
 * 服务模块
 * 服务模块(包含发布服务  下架服务  删除服务  上架服务)
 */
@RestController
@RequestMapping("/api/v2/service")
@Log
public class ServiceController extends BaseController {

    /**
     * 我发布的服务
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
     *                 "servicePlace": 线上或线下 1、线上 2、线下,
     *                 "servicePersonnel": 需要人数,
     *                 "startTime": 开始时间毫秒值（单次显示开始时间使用此字段）,
     *                 "endTime": 结束时间毫秒值(单词显示结束时间使用此字段),
     *                 "timeType": 是否重复 0、不重复 1、重复性,
     *                 "collectType": 收取分类, 1、互助时 2、公益时
     *                 "collectTime": 收取时长,
     *                 "addressName": 地址名称,
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
    @PostMapping("pageMine/" + TokenUtil.AUTH_SUFFIX)
    public Object pageMineAuth(String token, Integer pageNum, Integer pageSize, String keyName) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser();
        try {
            QueryResult<PageMineReturnView> list = productService.pageMine(user, pageNum, pageSize, keyName, ProductEnum.TYPE_SERVICE.getValue());
            result.setData(list);
            result.setSuccess(true);
            result.setMsg(AppMessageConstant.PRODUCT_QUERY_SUCCESS);
            return result;
        } catch (MessageException e) {
            log.warn(AppMessageConstant.PRODUCT_QUERY_ERROR + e.getMessage());
            result.setSuccess(false);
            result.setMsg(AppMessageConstant.PRODUCT_QUERY_ERROR + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error(AppMessageConstant.PRODUCT_QUERY_ERROR, e);
            result.setSuccess(false);
            result.setMsg(AppMessageConstant.PRODUCT_QUERY_ERROR);
            return result;
        }
    }


    /**
     * 删除服务
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
    @PostMapping("/delService/" + TokenUtil.AUTH_SUFFIX)
    public Object delServiceAuth(String token, Long productId) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser();
        try {
            productService.del(user, productId);
            result.setSuccess(true);
            result.setMsg(AppMessageConstant.PRODUCT_DEL_SUCCESS);
            return result;
        } catch (MessageException e) {
            log.warn(AppMessageConstant.PRODUCT_DEL_ERROR + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(AppMessageConstant.PRODUCT_DEL_ERROR + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error(AppMessageConstant.PRODUCT_DEL_ERROR, e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg(AppMessageConstant.PRODUCT_DEL_ERROR);
            return result;
        }
    }

    /**
     * 删除服务
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
    @PostMapping("delService")
    public Object delService(String token, Long productId) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser(token);
        try {
            productService.del(user, productId);
            result.setSuccess(true);
            result.setMsg(AppMessageConstant.PRODUCT_DEL_SUCCESS);
            return result;
        } catch (MessageException e) {
            log.warn(AppMessageConstant.PRODUCT_DEL_ERROR + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(AppMessageConstant.PRODUCT_DEL_ERROR + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error(AppMessageConstant.PRODUCT_DEL_ERROR, e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg(AppMessageConstant.PRODUCT_DEL_ERROR);
            return result;
        }
    }

    /**
     * 下架服务
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
    @PostMapping("lowerFrameService/" + TokenUtil.AUTH_SUFFIX)
    public Object lowerFrameServiceAuth(String token, Long productId) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser();
        try {
            productService.lowerFrame(user, productId);
            result.setSuccess(true);
            result.setMsg(AppMessageConstant.PRODUCT_LOWERFRAME_SUCCESS);
            return result;
        } catch (MessageException e) {
            log.warn(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR, e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR);
            return result;
        }
    }

    /**
     * 下架服务
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
    @PostMapping("lowerFrameService/")
    public Object lowerFrameService(String token, Long productId) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser(token);
        try {
            productService.lowerFrame(user, productId);
            result.setSuccess(true);
            result.setMsg(AppMessageConstant.PRODUCT_LOWERFRAME_SUCCESS);
            return result;
        } catch (MessageException e) {
            log.warn(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR, e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg(AppMessageConstant.PRODUCT_LOWERFRAME_ERROR);
            return result;
        }
    }


    /**
     * 我发布的服务
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
     *                 "servicePlace": 线上或线下 1、线上 2、线下,
     *                 "servicePersonnel": 需要人数,
     *                 "startTime": 开始时间毫秒值（单次显示开始时间使用此字段）,
     *                 "endTime": 结束时间毫秒值(单词显示结束时间使用此字段),
     *                 "timeType": 是否重复 0、不重复 1、重复性,
     *                 "collectType": 收取分类, 1、互助时 2、公益时
     *                 "collectTime": 收取时长,
     *                 "addressName": 地址名称,
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
    public Object pageMine(String token, Integer pageNum, Integer pageSize, String keyName) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser(token);
        try {
            QueryResult<PageMineReturnView> list = productService.pageMine(user, pageNum, pageSize, keyName, ProductEnum.TYPE_SERVICE.getValue());
            result.setData(list);
            result.setSuccess(true);
            result.setMsg(AppMessageConstant.PRODUCT_QUERY_SUCCESS);
            return result;
        } catch (MessageException e) {
            log.warn(AppMessageConstant.PRODUCT_QUERY_ERROR + e.getMessage());
            result.setSuccess(false);
            result.setMsg(AppMessageConstant.PRODUCT_QUERY_ERROR + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error(AppMessageConstant.PRODUCT_QUERY_ERROR, e);
            result.setSuccess(false);
            result.setMsg(AppMessageConstant.PRODUCT_QUERY_ERROR);
            return result;
        }
    }

    /**
     * 发布服务
     *
     * @param request
     * @return
     */
//	@PostMapping("/submit")
    public Object submitService(HttpServletRequest request, @RequestBody String token, ServiceParamView param) {
        AjaxResult result = new AjaxResult();
        //从拦截器中获取参数的String
//		String paramString = (String)request.getAttribute("paramString");
//		request.removeAttribute("paramString");
//		ServiceParamView param = JsonUtil.parseFromJson(paramString, ServiceParamView.class);
//		String token = param.getToken();
        TUser user = UserUtil.getUser();
        try {
            if (Objects.equals(param.getService().getType(), ProductEnum.TYPE_SEEK_HELP.getValue())) {
                productService.submitSeekHelp(user, param, token);
            } else if (Objects.equals(param.getService().getType(), ProductEnum.TYPE_SERVICE.getValue())) {
                productService.submitService(user, param, token);
            }
            result.setSuccess(true);
            result.setMsg(AppMessageConstant.SERVICE_SUBMIT_SUCCESS);
            return result;
        } catch (MessageException e) {
            log.warn(AppMessageConstant.SERVICE_SUBMIT_ERROR + e.getMessage());
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(AppMessageConstant.SERVICE_SUBMIT_ERROR + e.getMessage());
            return result;
        } catch (Exception e) {
            log.error(AppMessageConstant.SERVICE_SUBMIT_ERROR, e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg(AppMessageConstant.SERVICE_SUBMIT_ERROR);
            return result;
        }
    }

    /**
     * 发布精彩瞬间
     *
     * @param token       tokne
     * @param serviceId   serviceId
     * @param description 精彩瞬间描述
     * @param url         图片，逗号分割
     * @return
     */
    @PostMapping("/sendServiceSummary")
    public Object sendServiceSummary(String token, Long serviceId, String description, String url) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser(token);
        try {
            productService.sendServiceSummary(serviceId, description, url, user);
            result.setSuccess(true);
            result.setMsg("发布精彩瞬间成功");
            return result;
        } catch (Exception e) {
            log.error("发布精彩瞬间失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("发布精彩瞬间失败");
            return result;
        }
    }


    /**
     * 查看精彩瞬间
     *
     * @param serviceId
     * @return
     */
    @PostMapping("/findServiceSummary")
    public Object findServiceSummary(Long serviceId) {
        AjaxResult result = new AjaxResult();
        try {
            TServiceSummary serviceSummary = productService.findServiceSummary(serviceId);
            result.setSuccess(true);
            result.setMsg("查看精彩瞬间成功");
            result.setData(serviceSummary);
            return result;
        } catch (Exception e) {
            log.error("查看精彩瞬间失败", e);
            result.setSuccess(false);
            result.setErrorCode("500");
            result.setMsg("查看精彩瞬间失败");
            return result;
        }
    }


}
