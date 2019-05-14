package com.e_commerce.miscroservice.push_controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZLessonService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZSubjectService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

    private final String EMPTY = "";
    private final Pattern filePattern=Pattern.compile("(.*)\\.mp4$");


    /**
     * 推送
     * @param subjectId
     * @param fileName
     */
    @GetMapping("push")
    @ResponseBody
    public Object push(Long subjectId, String fileName) {
        log.info("开始推送文件={}",fileName);
        AjaxResult result = new AjaxResult();
        try {
            Boolean isPushSuccess = fileUrlManagers.push(fileName);
            if(isPushSuccess) {
                gzLessonService.sendUnlockTask(subjectId, fileName);
            }
            result.setSuccess(true);
        } catch (Exception e) {
            log.error("推送error{}", e);
        }
        return result;

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
        if(user==null) {
            user = new TUser();
            user.setId(42l);
            user.setName("三胖");
        }
        if(StringUtil.isEmpty(fileName)||
                !filePattern.matcher(fileName).matches()){

            log.info("获取文件播放地址失败,文件名称={}不正确",fileName);
            return EMPTY;
        }

        log.info("开始获取播放文件={}地址",fileName);
        //权限校验
        try {
            gzLessonService.authCheck(sign, user.getId(), fileName, subjectId, lessonId);
            request.setAttribute(fileName, Md5Util.md5(fileName));
        } catch (MessageException e) {
            AjaxResult result = new AjaxResult();
            result.setSuccess(false);
            result.setErrorCode(e.getErrorCode());
            result.setMsg(e.getMessage());
//          request.setAttribute("errorMsg", e.getMessage());
            return result;
        } catch (Exception e) {
            return new AjaxResult();
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

        Object hash = request.getAttribute(fileName==null?EMPTY:fileName);
        if (hash == null ||
                !Md5Util.md5(fileName).equals(hash)) {
            result.setData(EMPTY);
            result.setSuccess(false);
        }

        String errorMsg = (String) request.getAttribute("errorMsg");
        if(errorMsg != null) {
            result.setMsg(errorMsg);
            result.setSuccess(false);
        }

        result.setSuccess(true);
        result.setData(fileUrlManagers.getUrl(fileName));
        return result;

    }


}