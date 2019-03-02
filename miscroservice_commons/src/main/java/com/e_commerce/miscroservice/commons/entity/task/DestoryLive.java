package com.e_commerce.miscroservice.commons.entity.task;

import lombok.Data;

/**
 * @Author hyf
 * @Date 2019/1/25 15:21
 */
@Data
public class DestoryLive {

    private Long minutes;
    private Long totalMember;
    private String nickName;
    private String icon;
    private Long create;
    private Long current;
    private Integer orderSize;

}
