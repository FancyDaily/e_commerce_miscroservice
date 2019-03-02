package com.e_commerce.miscroservice.commons.entity.application.proxy;

import lombok.Data;

@Data
public class ProxyRewardQuery extends ProxyReward {

    private String oneLevelName;

    private int oneLevelSelfRole;

    private String twoLevelName;

    private String selfName;

    private int selfRole;

    private String goodsName;

    private String payOrderNo;

}
