package com.e_commerce.miscroservice.user.vo;


import com.e_commerce.miscroservice.commons.entity.application.TService;
import com.e_commerce.miscroservice.commons.entity.application.TServiceDescribe;

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
public class ServiceParamView {
    /**
     * 求助服务
     */
    private TService service;
    /**
     * 每一个求助服务的描述
     */
    private List<TServiceDescribe> listServiceDescribe;
    
    private String token;
    /**
     * 区分发布时候是个人发布还是组织发布的发布类型（组织发布为2，个人小程序暂时不传该参数）
     */
    private Integer publishType;
    
    public Integer getPublishType() {
		return publishType;
	}

	public void setPublishType(Integer publishType) {
		this.publishType = publishType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TService getService() {
        return service;
    }

    public void setService(TService service) {
        this.service = service;
    }

    public List<TServiceDescribe> getListServiceDescribe() {
        return listServiceDescribe;
    }

    public void setListServiceDescribe(List<TServiceDescribe> listServiceDescribe) {
        this.listServiceDescribe = listServiceDescribe;
    }
}
