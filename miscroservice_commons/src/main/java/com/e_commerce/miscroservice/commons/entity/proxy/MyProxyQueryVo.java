package com.e_commerce.miscroservice.commons.entity.proxy;

import com.e_commerce.miscroservice.commons.entity.BaseEntity;
import lombok.Data;

/**
 * 用户查询我的代理
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/26 21:16
 * @Copyright 玖远网络
 */
@Data
public class MyProxyQueryVo extends BaseEntity{


    /**
     * 微信昵称/姓名
     */
    private String name;
    private String phone;
    private Long publicAccountUserId;
    /**
     * 根据名称或者手机号查询 或的关系
     */
    private String phoneOrName;
    /**
     * 1.查询我的客户,2.查询我的县级代理
     */
    private Integer type;
}
