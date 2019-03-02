package com.e_commerce.miscroservice.user.controller.rpc;

import com.e_commerce.miscroservice.commons.annotation.service.InnerRestController;
import com.e_commerce.miscroservice.commons.entity.user.LiveUserDTO;
import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.user.service.live.LiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/17 12:56
 * @Copyright 玖远网络
 */
@InnerRestController
@RequestMapping( "system/rpc/liveUserRpcController" )
public class LiveUserRpcController {

    @Autowired
    private LiveService liveService;

    /**
     * 查询liveUser
     *
     * @param anchorId anchorId
     * @return com.e_commerce.miscroservice.commons.entity.user.LiveUser
     * @author Charlie
     * @date 2019/1/17 12:58
     */
    @RequestMapping("findLiveUserByAnchorId")
    public LiveUserDTO findLiveUserByAnchorId(@RequestParam( "anchorId" ) Long anchorId) {
        LiveUser user = liveService.findLiveUserByAnchorId(anchorId);
        if (user == null) {
            return null;
        }
        LiveUserDTO retVal = new LiveUserDTO();
        retVal.setId(user.getId());
        retVal.setStoreId(user.getStoreId());
        retVal.setLiveType(user.getLiveType());
        retVal.setMemberId(user.getMemberId());
        retVal.setRoomNum(user.getRoomNum());
        retVal.setStatus(user.getStatus());
        return retVal;
    }
    /**
     * 查询liveUser
     *
     * @param roomNum roomNum
     * @return com.e_commerce.miscroservice.commons.entity.user.LiveUser
     * @author Charlie
     * @date 2019/1/17 12:58
     */
    @RequestMapping("findLiveUserByRoomNum")
    public LiveUserDTO findLiveUserByRoomNum(@RequestParam( "roomNum" ) Long roomNum) {
        LiveUser user = liveService.findLiveUserByRoomNum(roomNum);
        if (user == null) {
            return null;
        }
        LiveUserDTO retVal = new LiveUserDTO();
        retVal.setId(user.getId());
        retVal.setStoreId(user.getStoreId());
        retVal.setLiveType(user.getLiveType());
        retVal.setMemberId(user.getMemberId());
        retVal.setRoomNum(user.getRoomNum());
        retVal.setStatus(user.getStatus());
        return retVal;
    }




}
