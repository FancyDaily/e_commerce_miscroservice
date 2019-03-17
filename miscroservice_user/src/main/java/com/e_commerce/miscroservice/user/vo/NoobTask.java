package com.e_commerce.miscroservice.user.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class NoobTask {

    private Long targetId;   //id

    private String name;    //名字

    private Integer bonus;  //金额

    private boolean isDone; //是否完成
}
