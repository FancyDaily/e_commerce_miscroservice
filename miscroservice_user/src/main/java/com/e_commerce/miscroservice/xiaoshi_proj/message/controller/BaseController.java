package com.e_commerce.miscroservice.xiaoshi_proj.message.controller;

import com.e_commerce.miscroservice.commons.util.colligate.RedisUtil;
import com.e_commerce.miscroservice.commons.util.colligate.SnowflakeIdWorker;
import com.e_commerce.miscroservice.xiaoshi_proj.order.service.OrderService;
import com.e_commerce.miscroservice.xiaoshi_proj.product.service.ProductService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 功能描述:Controller的公共方法
 */
public class BaseController {

    @Autowired
    protected ProductService seekHelpService;
    @Autowired
    protected RedisUtil redisUtil;
    @Autowired
    protected OrderService orderService;

    protected SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();

    /**
     * 功能描述:输出错误消息
     * 作者:马晓晨
     * 创建时间:2018年12月9日 下午2:26:43
     *
     * @param e
     * @return
     */
    public String errInfo(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            // 将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }


}
