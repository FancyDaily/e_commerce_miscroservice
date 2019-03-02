package com.e_commerce.miscroservice.commons.entity.live;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/18 13:49
 * @Copyright 玖远网络
 */
@Data
public class LiveRoomMsgDTO {
    private Long storeId;


    private Long liveProductId;
    private Long memberId;
    private Long orderItemId;

    private Long roomNum;
    private String speakerName;
    private Integer type;
    private String msg;
    /**
     * 1001 读取普通直播间
     * 1002 读取平台直播间
     * 1003 读取经销商直播间
     */
    private Integer roomCode;
    /**
     * 需要验证直播间能否发言
     * true,强制插入一条发言(用于系统发言,系统通知)
     */
    private Boolean needVerify;

    private Integer productCount;
    private String productName;

    /**
     * 查询起始位置
     */
    private Long offset;
    /**
     * 接收用户来源 0小程序端用户,2直播端播主
     */
    private Integer receiveUserType;
    private String speakerIcon;

    public static LiveRoomMsgDTO me() {
        return new LiveRoomMsgDTO();
    }

}
