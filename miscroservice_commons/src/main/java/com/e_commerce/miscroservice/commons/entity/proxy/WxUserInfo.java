package com.e_commerce.miscroservice.commons.entity.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/9/25 14:57
 * @Copyright 玖远网络
 */
@Data
@AllArgsConstructor
public class WxUserInfo{

    public static WxUserInfo build(String openId, String wxName, String wxUserIcon) {
        return new WxUserInfo (openId, wxName, wxUserIcon);
    }

    private String openId;
    private String wxName;
    private String wxUserIcon;
}
