package com.e_commerce.miscroservice.user.vo;

import lombok.Data;

@Data
public class DailyTask {

    private Long targetId;  //id

    private String name;  //名字

    private Integer bonus;  //价格

    private boolean done; //是否完成

    private Integer currentNum; //当前次数

    private Integer totalNum;   //总次数
}
