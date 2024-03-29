package com.e_commerce.miscroservice.push_controller;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;
import com.e_commerce.miscroservice.commons.util.colligate.HttpsUtils;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZLessonService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZPayService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@Log
public class TestDo {

    @Autowired
    private FileUrlManagers fileUrlManagers;

    @Autowired
    private GZSubjectService gzSubjectService;

    @Autowired
    private GZLessonService gzLessonService;

    @Autowired
    private GZPayService gzPayService;

    private final String EMPTY = "";
    private final Pattern filePattern=Pattern.compile("(.*)\\.mp4$");

    /**
     * 支付成功
     * @param orderNo
     * @return
     */
    @GetMapping("deals")
    @ResponseBody
    public Object deal(String orderNo) {
        log.info("支付成功orderNo={}", orderNo);
        AjaxResult result = new AjaxResult();
        try {
            gzPayService.afterPaySuccess(null, orderNo);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("支付成功错误={}", e);
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 仅推送文件
     * @param fileName
     * @return
     */
    @GetMapping("push/pre")
    @ResponseBody
    public Object pushSuccess(String fileName) {
        AjaxResult result = new AjaxResult();
        Boolean push = fileUrlManagers.push(fileName);
        result.setData(push);
        if(push) {
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * 推送
     * @param subjectId
     * @param fileName
     */
    @GetMapping("push")
    @ResponseBody
    public Object push(Long subjectId, Long lessonId, String fileName) {
        log.info("开始推送文件={}",fileName);
        AjaxResult result = new AjaxResult();
        try {
            //判断是测试环境还是正式环境
            //发送http请求，请求测试线的"仅推送文件请求"
            Boolean isPushSuccess = false;
            if (!ApplicationContextUtil.isDevEnviron()) { // 表示当前运行环境为调试
                isPushSuccess = fileUrlManagers.push(fileName);
            } else {    //当前环境为生产
                String url = "https://getScene.xiaoshitimebank.com/user/push/pre";
                Map<String, Object> params = new HashMap<>();
                params.put("fileName", fileName);
                JSONObject jsonObject = HttpsUtils.doGet(url, params);
                if(jsonObject!=null) {
                    Object data = jsonObject.get("data");
                    if(data!=null && data != "" && data != "{}") {
                        isPushSuccess = (Boolean) data;
                    }
                }
            }
            if(isPushSuccess) {
                gzLessonService.sendUnlockTask(subjectId, lessonId, fileName);
                result.setSuccess(true);
            } else {
                result.setMsg("推送结果->False, 请检查文件名");
            }
        } catch (Exception e) {
            log.error("推送error{}", e);
        }
        return result;

    }

    public static void main(String[] args) {
        String fileName = "Ivy的第一堂课01.mp4";
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = "https://getScene.xiaoshitimebank.com/user/push/pre";
        Map<String, Object> params = new HashMap<>();
        params.put("fileName", fileName);
        JSONObject jsonObject = HttpsUtils.doGet(url, params);
        System.out.println(jsonObject);
        if(jsonObject!=null) {
            Object data = jsonObject.get("data");
            System.out.println(data);
            if(data!=null && data != "" && data != "{}") {
                System.out.println(data);
            }
        }
    }


    @Autowired
    private HttpServletRequest request;

    /**
     * 获取播放
     *
     * @param fileName 文件名称
     * @return
     */
    @GetMapping("play/" + TokenUtil.AUTH_SUFFIX)
    public Object play(String fileName, String sign, Long subjectId, Long lessonId) {
        TUser user = UserUtil.getUser();
        if(StringUtil.isEmpty(fileName)||
                !filePattern.matcher(fileName).matches()){

            log.info("获取文件播放地址失败,文件名称={}不正确",fileName);
            return EMPTY;
        }

        log.info("开始获取播放文件={}地址",fileName);
        //权限校验
        try {
            gzLessonService.authCheck(sign, user.getId(), subjectId, lessonId);
            request.setAttribute(fileName, Md5Util.md5(fileName));
        } catch (MessageException e) {
            request.setAttribute("errorMsg", e.getMessage());
        } catch (Exception e) {
            request.setAttribute("errorMsg", e.getMessage());
        }
        return "forward:/doPlay";
    }


    /**
     * 内部获取播放入口
     *
     * @param fileName
     * @return
     */
    @GetMapping("doPlay")
    @ResponseBody
    public Object doPlay(String fileName) {
        AjaxResult result = new AjaxResult();

        String errorMsg = (String) request.getAttribute("errorMsg");
        if(errorMsg != null) {
            result.setMsg(errorMsg);
            result.setSuccess(false);
            return result;
        }

        Object hash = request.getAttribute(fileName==null?EMPTY:fileName);
        if (hash == null ||
                !Md5Util.md5(fileName).equals(hash)) {
            result.setData(EMPTY);
            result.setSuccess(false);
            return result;
        }

        result.setSuccess(true);
        result.setData(fileUrlManagers.getUrl(fileName));
        return result;

    }


}
