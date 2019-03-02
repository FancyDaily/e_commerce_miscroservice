package com.e_commerce.miscroservice.commons.entity;

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



    private Integer pageSize=DEFAULT_SIZE;
    private Integer pageNumber=DEFAULT_NUMBER;


}
