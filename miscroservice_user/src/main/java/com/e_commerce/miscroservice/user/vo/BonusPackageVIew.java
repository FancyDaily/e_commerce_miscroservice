package com.e_commerce.miscroservice.user.vo;

import lombok.Data;

@Data
public class BonusPackageVIew {

    private Long id;

    private Long userId;

    private String description;

    private Long time;

    private Long createTime;

    private String isValid;

    private String userHeadPortraitPath;

    private String name;

}
