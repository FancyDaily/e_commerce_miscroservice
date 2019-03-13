package com.e_commerce.miscroservice.user.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
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
