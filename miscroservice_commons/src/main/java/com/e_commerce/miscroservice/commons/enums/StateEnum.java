package com.e_commerce.miscroservice.commons.enums;


/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/22 12:55
 * @Copyright 玖远网络
 */
public enum StateEnum{

    NULL ();

    /**
     * 未删除
     */
    public static final int NORMAL = 0;
    /**删除
     *
     */
    public static final int DELETE = 1;

    /**
     * 失效
     */
    public static final int LOSE = 2;
    /**
     * 隐藏
     */
    public static final int HIDE = 3;
    /**
     * 草稿
     */
    public static final int DRAFT = 4;
}
