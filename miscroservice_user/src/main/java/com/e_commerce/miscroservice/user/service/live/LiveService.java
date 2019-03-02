package com.e_commerce.miscroservice.user.service.live;

import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.user.entity.req.AddAnchorRequest;
import com.e_commerce.miscroservice.user.entity.req.UpAnchorRequest;

/**
 * @Author hyf
 * @Date 2019/1/15 17:03
 */
public interface LiveService {

    /**
     * 直播间申请
     * @return
     */
    Response applyLive(AddAnchorRequest obj);

    /**
     * 主播开通状态
     * @param id
     * @return
     */
    Response applyLiveStatus(Long id);

    /**
     * 编辑
     * @param applyLiveReq
     * @return
     */
    Response upCommonAnchor(UpAnchorRequest applyLiveReq);

    /**
     *
     * @param anchorId
     * @param userId
     * @return
     */
    Response delCommonAnchor(Long anchorId, Long userId);

    /**
     * 返回直播间信息
     * @param memberId
     * @param storeId
     * @return
     */
    Response rebackInformation(Long memberId, Long storeId);

    /**
     * 根据用户id查询主播信息
     * @param userId
     * @return
     */
    Response information(Long userId);

    /**
     * 查询liveUser
     *
     * @param memberId memberId
     * @return com.e_commerce.miscroservice.commons.entity.user.LiveUser
     * @author Charlie
     * @date 2019/1/17 12:59
     */
    LiveUser findLiveUserByMemberId(Long memberId);


    /**
     * 查询liveUser
     *
     * @param anchor anchor
     * @return com.e_commerce.miscroservice.commons.entity.user.LiveUser
     * @author Charlie
     * @date 2019/1/17 12:59
     */
    LiveUser findLiveUserByAnchorId(Long anchor);

    /**
     * 通过房间号查找
     *
     * @param roomNum roomNum
     * @return com.e_commerce.miscroservice.user.entity.LiveUser
     * @author Charlie
     * @date 2019/1/17 13:07
     */
    LiveUser findLiveUserByRoomNum(Long roomNum);

    /**
     * 展示主播列表
     * @param storeId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    Response showAnchor(Long storeId, Integer pageNumber, Integer pageSize);

    /**
     * 开启平台直播
     * @param userId
     * @param open
     * @return
     */
    Response openOfficialLive(Long userId, Integer open);
}
