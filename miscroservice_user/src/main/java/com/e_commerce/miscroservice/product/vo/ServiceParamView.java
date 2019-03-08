package com.e_commerce.miscroservice.product.vo;


import com.e_commerce.miscroservice.order.po.TService;
import com.e_commerce.miscroservice.order.po.TServiceDescribe;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述:求助和服务参数view
 * 模块:
 * 项目:时间银行
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:马晓晨
 * 邮箱:747052172
 * 创建时间:2018/10/30 上午10:50
 */
@Data
public class ServiceParamView implements Serializable {
    /**
     * 求助服务
     */
    private TService service;
    /**
     * 每一个求助服务的描述
     */
    private List<TServiceDescribe> listServiceDescribe;
    
    private String token;
}
