package com.e_commerce.miscroservice.product.vo;


import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 发布的json参数view
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
