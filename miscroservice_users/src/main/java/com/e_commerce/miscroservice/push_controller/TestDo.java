package com.e_commerce.miscroservice.push_controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
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
    public void push(Long subjectId, String fileName) {
        log.info("开始推送文件={}",fileName);
        Boolean isPushSuccess = fileUrlManagers.push(fileName);
        if(isPushSuccess) {
            gzLessonService.sendUnlockTask(subjectId, fileName);
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
    public String play(String fileName, String sign, Long subjectId, Long lessonId) {
        TUser user = UserUtil.getUser();
        user = new TUser();
        user.setId(42l);
        user.setName("三胖");
        if(StringUtil.isEmpty(fileName)||
                !filePattern.matcher(fileName).matches()){

            log.info("获取文件播放地址失败,文件名称={}不正确",fileName);
            return EMPTY;
        }

        log.info("开始获取播放文件={}地址",fileName);
        //权限校验
        // TODO
        gzLessonService.authCheck(sign, user.getId(), fileName, subjectId, lessonId);

        request.setAttribute(fileName, Md5Util.md5(fileName));
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
    public String doPlay(String fileName) {


        Object hash = request.getAttribute(fileName==null?EMPTY:fileName);
        if (hash == null ||
                !Md5Util.md5(fileName).equals(hash)) {
            return EMPTY;
        }


        return fileUrlManagers.getUrl(fileName);

    }


}
