package com.e_commerce.miscroservice.commons.entity.application.order;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/9/13 21:05
 * @Copyright 玖远网络
 */
@Data
public class BaseEntity implements Serializable {

    private static Integer DEFAULT_SIZE=10;
    private static Integer DEFAULT_NUMBER=1;


    private Integer join=1;
    private Integer pageSize;
    private Integer pageNumber;
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize==null||pageSize<0){
            pageSize=DEFAULT_SIZE;
        }
        this.pageSize = pageSize;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        if (pageNumber==null||pageNumber<0){
            pageNumber=DEFAULT_NUMBER;
        }
        this.pageNumber = pageNumber;
    }
}
