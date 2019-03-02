package com.e_commerce.miscroservice.user.entity.req;

import com.e_commerce.miscroservice.commons.annotations.application.IsEmptyAnnotation;
import lombok.Data;

/**
 * @Author hyf
 * @Date 2019/1/15 17:26
 */
@Data
public class UpAnchorRequest {
    private Long userId;
    private Long storeId;
    @IsEmptyAnnotation
    private String icon;
    @IsEmptyAnnotation
    private String nickName;
    @IsEmptyAnnotation
    private Integer sex;
}
