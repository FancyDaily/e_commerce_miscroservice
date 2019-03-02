package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.annotation.service.Consume;
import com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.ConsumeHelper;
import com.e_commerce.miscroservice.commons.helper.util.service.IdUtil;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.user.config.TimerSchedulerPO;
import com.e_commerce.miscroservice.user.entity.req.AddAnchorRequest;
import com.e_commerce.miscroservice.user.entity.req.UpAnchorRequest;
import com.e_commerce.miscroservice.user.service.live.LiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author hyf
 * @Date 2019/1/15 17:02
 */
@RestController
@RequestMapping("live")
public class LiveController {
    @Autowired
    private TimerSchedulerDaoImpl dao;

    @RequestMapping("test")
    public void test() {
        Executor executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {


                List<Testaa> testaas = MybatisOperaterUtil.getInstance().finAllNative(new Testaa(), "SELECT * FROM `dept` ");

                System.out.println(testaas.size());
                System.out.println(testaas);

                TimerSchedulerPO timerSchedulerPO = new TimerSchedulerPO();
                timerSchedulerPO.setUuid("1");
                timerSchedulerPO.setIsDeleted(2);
                timerSchedulerPO.setTimerSchedulerParam("1");
                timerSchedulerPO.setTimerSchedulerName("22");

                dao.saveTimerScheduler(timerSchedulerPO);


                testaas = MybatisOperaterUtil.getInstance().finAllNative(new Testaa(), "SELECT * FROM `dept` ");

                System.out.println(testaas.size());
                System.out.println(testaas);


                timerSchedulerPO = new TimerSchedulerPO();
                timerSchedulerPO.setUuid("1");
                timerSchedulerPO.setIsDeleted(2);
                timerSchedulerPO.setTimerSchedulerParam("1");
                timerSchedulerPO.setTimerSchedulerName("22");

                dao.saveTimerScheduler(timerSchedulerPO);

                timerSchedulerPO = new TimerSchedulerPO();
                timerSchedulerPO.setUuid("444");
                timerSchedulerPO.setIsDeleted(2);
                timerSchedulerPO.setTimerSchedulerParam("1");
                timerSchedulerPO.setTimerSchedulerName("22");

                dao.saveTimerScheduler(timerSchedulerPO);

                testaas = MybatisOperaterUtil.getInstance().finAllNative(new Testaa(), "SELECT * FROM `dept` ");

                System.out.println(testaas.size());
                System.out.println(testaas);


            });
        }
    }

    @Autowired
    private LiveService liveService;

    /**
     * 登陆后绑定直播间账号并返回直播间信息
     *
     * @param memberId 会员id
     * @param storeId  店铺id
     * @return
     */
    @RequestMapping("/reback/information")
    public Response rebackInformation(Long memberId, Long storeId) {
        return liveService.rebackInformation(memberId, storeId);
    }


    /**
     * 用户信息
     *
     * @param userId 用户id
     * @return
     */
    @RequestMapping("/information")
    public Response information(Long userId) {
        return liveService.information(userId);

    }

    /**
     * 申请主播
     *
     * @param name     姓名
     * @param nickName 昵称
     * @param icon     头像
     * @param age      年龄
     * @param idCard   身份证
     * @param sex      性别
     * @return
     */
    @Consume(AddAnchorRequest.class)
    @RequestMapping("apply/live/auth")
    public Response applyLive(String name, String nickName, String icon, Integer age, String idCard, Integer sex) {
        AddAnchorRequest applyLiveReq = (AddAnchorRequest) ConsumeHelper.getObj();

        applyLiveReq.setStoreId(Long.valueOf(IdUtil.getId()));
        applyLiveReq.setLiveType(0);
        applyLiveReq.setLiveRoomType(TaskTypeEnums.LIVE_COMMON_SHOP.getKey());
        return liveService.applyLive(applyLiveReq);
    }

    /**
     * 开通状态
     *
     * @return
     * @Param userId 用户id
     */
    @RequestMapping("apply/live/status/auth")
    public Response applyLiveStatus() {

        return liveService.applyLiveStatus(Long.valueOf(IdUtil.getId()));
    }


    /**
     * 添加主播
     *
     * @param storeId  店铺id
     * @param name     姓名
     * @param nickName 昵称
     * @param icon     头像
     * @param age      年龄
     * @param idCard   身份证
     * @param phone    手机号
     * @param sex      性别
     * @return
     */
    @Consume(AddAnchorRequest.class)
    @RequestMapping("add/anchor")
    public Response addCommonAnchor(Long storeId, String name, String nickName, String icon, Integer age, String idCard, String phone, Integer sex) {
        AddAnchorRequest applyLiveReq = (AddAnchorRequest) ConsumeHelper.getObj();
        applyLiveReq.setStoreId(storeId);
        applyLiveReq.setLiveType(3);
        return liveService.applyLive(applyLiveReq);
    }


    /**
     * 编辑
     *
     * @param nickName 昵称
     * @param icon     头像
     * @param sex      性别
     * @param storeId  店铺id
     * @param userId   用户id
     * @return
     */
    @Consume(UpAnchorRequest.class)
    @RequestMapping("up/anchor")
    public Response upCommonAnchor(String nickName, String icon, Integer sex, Long storeId, Long userId) {
        UpAnchorRequest applyLiveReq = (UpAnchorRequest) ConsumeHelper.getObj();
        return liveService.upCommonAnchor(applyLiveReq);
    }

    /**
     * 删除主播
     *
     * @param anchorId 主播id
     * @param userId   用户id
     * @return
     */
    @RequestMapping("del/anchor")
    public Response delCommonAnchor(Long anchorId, Long userId) {
        return liveService.delCommonAnchor(anchorId, userId);
    }


    /**
     * 展示主播列表
     *
     * @param storeId
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping("show/anchor")
    public Response showAnchor(Long storeId, Integer pageNumber, Integer pageSize) {
        return liveService.showAnchor(storeId, pageNumber, pageSize);
    }

    /**
     * 开启平台直播
     *
     * @param userId
     * @param open   0默认关闭 1开启
     * @return
     */
    @RequestMapping("open/official")
    public Response openOfficialLive(Long userId, Integer open) {
        return liveService.openOfficialLive(userId, open);
    }
}
