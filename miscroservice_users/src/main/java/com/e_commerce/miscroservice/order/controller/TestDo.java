package com.e_commerce.miscroservice.order.controller;

import com.e_commerce.miscroservice.commons.helper.util.colligate.encrypt.Md5Util;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;
import sun.net.ftp.FtpClient;

@Controller
@Data
public class TestDo {

    @Autowired
    private FileUrlManagers fileUrlManagers;

    private final String EMPTY = "";
    private final Pattern filePattern=Pattern.compile("(.*)\\.mp4$");



    /**
     * 推送
     *
     * @param fileName
     */
    @GetMapping("push")
    @ResponseBody
    public void push(String fileName) {

        log.info("开始推送文件={}",fileName);
        fileUrlManagers.push(fileName);
    }


    @Autowired
    private HttpServletRequest request;

    /**
     * 获取播放
     *
     * @param fileName 文件名称
     * @return
     */
    @GetMapping("play")
    public String play(String fileName) {
        if(StringUtil.isEmpty(fileName)||
                !filePattern.matcher(fileName).matches()){

            log.info("获取文件播放地址失败,文件名称={}不正确",fileName);
            return EMPTY;
        }


        log.info("开始获取播放文件={}地址",fileName);
        //权限校验
        // TODO:

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
