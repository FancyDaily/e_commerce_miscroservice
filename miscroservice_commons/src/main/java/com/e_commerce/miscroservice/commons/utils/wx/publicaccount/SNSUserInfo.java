package com.e_commerce.miscroservice.commons.utils.wx.publicaccount;

import lombok.Data;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/28 11:37
 * @Copyright 玖远网络
 */
@Data
public class SNSUserInfo{


    /**
     * 用户标识
     */
    private String openId;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 性别（1是男性，2是女性，0是未知）
     */
    private int sex;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 用户头像链接
     */
    private String headImgUrl;
    /**
     * 用户特权信息
     */
    private List<String> privilegeList;

    private String unionid;
}
