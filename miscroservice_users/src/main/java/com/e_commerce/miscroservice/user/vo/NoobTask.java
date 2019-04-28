package com.e_commerce.miscroservice.user.vo;

import lombok.Data;

@Data
public class NoobTask {

    private Long targetId;   //id

    private String name;    //名字

    private Integer bonus;  //金额

    private boolean isDone; //是否完成


    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
