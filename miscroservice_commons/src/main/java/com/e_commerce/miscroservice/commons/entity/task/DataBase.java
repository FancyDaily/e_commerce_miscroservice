package com.e_commerce.miscroservice.commons.entity.task;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/12/5 19:26
 * @Copyright 玖远网络
 */
@Data
public class DataBase {
    private static Long PAGE_NUMBER = 1L;
    private static Long PAGE_SIZE = 10L;
    private String requestDataJson;

    public static DataBase me() {
        return new DataBase();
    }

    /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long memberId;
    /**
     * 房间号
     */
    private Long roomId;
    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 经销商id
     */
    private Long distributorId;
    /**
     * 模块：默认：0：直播，1 ： 店中店 3:用户行为模块
     * {@link TaskTypeEnums}
     */
    private Integer moduleType;

    /**
     * 状态 默认：0 查询，1 添加 2修改 3销毁
     */
    private Integer code;

    /**
     * 直播间属性 ：1店家直播 2 平台直播  3   经销商直播
     */
    private Integer liveStatus;
    /**
     * 同步
     */
    private CompletableFuture<Object> objectCompletableFuture;

    /**
     * 页面
     */
    private Long pageNumber;

    /**
     * 条数
     */
    private Long pageSize;


    /**
     * 数据存放
     */
    private Object obj;

    public Long getPageNumber() {

        return pageNumber = pageNumber==null||pageNumber<=0||pageNumber==1?PAGE_NUMBER-1:(pageNumber-1)*PAGE_SIZE;

    }



    public Long getPageSize() {
        return pageSize = pageSize==null||pageSize<=0?pageNumber+PAGE_SIZE-1:pageNumber+pageSize-1;
    }

    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        System.out.println(dataBase);
    }

}
