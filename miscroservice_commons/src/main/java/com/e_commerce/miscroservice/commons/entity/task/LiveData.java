package com.e_commerce.miscroservice.commons.entity.task;

import lombok.Data;

/**
 * @Author hyf
 * @Date 2019/1/9 18:41
 */
@Data
public class LiveData {
    public static Integer DEFAULT_OPEN_STATUS=1;
    private Integer id;
    /**
     * 直播间类型：默认 1001 : 店家直播间 1002 : 平台直播间  TaskTypeEnums 详情
     */
    private Integer code;

    /**
     * 直播间 状态
     */
    private Integer status=DEFAULT_OPEN_STATUS;

    /**
     * 直播间首页主图
     */
    private String mainMap;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 直播间标题
     */
    private String title;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 房间号
     */
    private Long roomId;
   /**
     * 直播间创建时间 时间戳
     */
    private Long createTime;

    /**
     * 直播推流播放链接
     */
    private String playUrl;
}
