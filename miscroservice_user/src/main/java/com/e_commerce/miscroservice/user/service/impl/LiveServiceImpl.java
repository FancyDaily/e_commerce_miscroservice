package com.e_commerce.miscroservice.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.SimplePage;
import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.user.dao.LiveDao;
import com.e_commerce.miscroservice.user.dao.StoreBusinessDao;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.entity.rep.AnchorRep;
import com.e_commerce.miscroservice.user.entity.req.AddAnchorRequest;
import com.e_commerce.miscroservice.user.entity.req.UpAnchorRequest;
import com.e_commerce.miscroservice.user.service.live.LiveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/15 17:03
 */
@Service
public class LiveServiceImpl implements LiveService {
    private Log logger = Log.getInstance(LiveServiceImpl.class);

    @Resource
    private LiveDao liveDao;

    @Resource
    private StoreBusinessDao storeBusinessDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response applyLive(AddAnchorRequest obj) {
        logger.info("申请主播={}", obj);
        Boolean flag = liveDao.openWxUserByStoreId(obj.getStoreId());
        StoreBusiness storeBusiness = storeBusinessDao.findById(obj.getStoreId());
        if (storeBusiness==null){
            logger.warn("账号不存在 申请失败");
            return Response.errorMsg("账号不存在,申请失败");
        }
        String phone = null;
        if (obj.getPhone()==null){
            phone = storeBusiness.getPhoneNumber();
        }else {
            phone = obj.getPhone();
        }
        LiveUser user = liveDao.findLiveUserByPhone(phone);
        if (flag&&user==null){
            LiveUser liveUser = new LiveUser();
            liveUser.setAge(obj.getAge());
            liveUser.setSex(obj.getSex());
            liveUser.setIdCard(obj.getIdCard());
            liveUser.setName(obj.getName());
            liveUser.setPhone(phone);
            liveUser.setStoreId(obj.getStoreId());
            liveUser.setRoomNum(System.currentTimeMillis());
            liveUser.setIcon(obj.getIcon());
            liveUser.setNickName(obj.getNickName());
            liveUser.setLiveType(obj.getLiveType());
            liveUser.setLiveRoomType(obj.getLiveRoomType());
            liveDao.applyLive(liveUser);
            return Response.success();
        }
        return Response.errorMsg("申请不符合资格");
    }
    @Override
    public Response applyLiveStatus(Long id) {
        logger.info("主播申请状态={}", id);
        Boolean flag = false;
        Integer in = liveDao.findApplyLiveStatus(id);
        JSONObject jsonObject = new JSONObject();
        if (in>0){
            flag = Boolean.TRUE;
        }
        jsonObject.put("status",flag);
        StoreBusiness storeBusiness = storeBusinessDao.findById(id);
        jsonObject.put("phone", storeBusiness.getPhoneNumber());
        return Response.success(jsonObject);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response upCommonAnchor(UpAnchorRequest applyLiveReq) {
        logger.info("更新主播={}", applyLiveReq);
        LiveUser liveUser = new LiveUser();
        liveUser.setSex(applyLiveReq.getSex());
        liveUser.setStoreId(applyLiveReq.getStoreId());
        liveUser.setIcon(applyLiveReq.getIcon());
        liveUser.setNickName(applyLiveReq.getNickName());
        liveUser.setId(applyLiveReq.getUserId());
        Integer up = liveDao.upLiveAnchor(liveUser);
        if (up>0){
            return Response.success();
        }
        return Response.errorMsg("更新失败");
    }

    @Override
    public Response delCommonAnchor(Long anchorId, Long userId)
    {
        logger.info("删除普通主播anchorId={},userId={}",anchorId,userId);
        LiveUser liveUser = new LiveUser();
        liveUser.setId(anchorId);
        liveUser.setDelStatus(1);
        Integer up = liveDao.upLiveAnchor(liveUser);
        if (up>0){
            return Response.success();
        }
        return Response.errorMsg("更新失败");
    }

    @Override
    public Response rebackInformation(Long memberId, Long storeId)
    {
        logger.info("微信绑定直播间信息 并返回直播间信息 memberId={},storeId={}", memberId,storeId);
        LiveUser in = new LiveUser();

        LiveUser liveUser = liveDao.findLiveUserByMemberId(memberId);
        if (liveUser==null){
            in.setId(liveUser.getId());
            in.setMemberId(memberId);
            liveDao.upLiveAnchor(in);
        }

        return Response.success(in);
    }

    @Override
    public Response information(Long userId) {
        logger.info("直播间用户信息", userId);
        LiveUser liveUser = liveDao.findLiveUserById(userId);

        return Response.success(liveUser);
    }

    /**
     * 查询liveUser
     *
     * @param memberId memberId
     * @return com.e_commerce.miscroservice.commons.entity.user.LiveUser
     * @author Charlie
     * @date 2019/1/17 12:59
     */
    @Override
    public LiveUser findLiveUserByMemberId(Long memberId) {
        logger.info("查找播主 memberId={}", memberId);
        return liveDao.findLiveUserByMemberId(memberId);
    }

    /**
     * 查询liveUser
     *
     * @param anchor anchor
     * @return com.e_commerce.miscroservice.commons.entity.user.LiveUser
     * @author Charlie
     * @date 2019/1/17 12:59
     */
    @Override
    public LiveUser findLiveUserByAnchorId(Long anchor) {
        logger.info("查找播主 anchor={}", anchor);
        return liveDao.findLiveUserById(anchor);
    }

    @Override
    public LiveUser findLiveUserByRoomNum(Long roomNum) {
        logger.info("查找播主 roomNum={}", roomNum);
        return liveDao.findLiveUserByRoomNum(roomNum);

    }

    @Override
    public Response showAnchor(Long storeId, Integer pageNumber, Integer pageSize)
    {
        logger.info("展示主播列表={}",storeId  );
        List<AnchorRep> liveUsers = liveDao.findLiveUserByStoreId(storeId,pageNumber,pageSize);
        SimplePage<AnchorRep> simplePage = new SimplePage<AnchorRep>(liveUsers);
        return Response.success(simplePage);
    }

    @Override
    public Response openOfficialLive(Long userId, Integer open) {
        logger.info("开启平台直播userId={},open={}", userId,open);
        LiveUser liveUser = liveDao.findLiveUserById(userId);
        //只有店家主播能开启 官方主播
        if (liveUser!=null&&liveUser.getLiveType()==0){
            LiveUser user = new LiveUser();
            user.setOpenOfficial(open);
            user.setId(userId);
            liveDao.upLiveAnchor(user);
            return Response.success();
        }
        return Response.errorMsg("开启失败");
    }

}
