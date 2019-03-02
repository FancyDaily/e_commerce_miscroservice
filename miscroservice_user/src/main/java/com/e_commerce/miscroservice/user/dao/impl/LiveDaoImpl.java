package com.e_commerce.miscroservice.user.dao.impl;

import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.commons.enums.StateEnum;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.user.dao.LiveDao;
import com.e_commerce.miscroservice.user.entity.StoreBusiness;
import com.e_commerce.miscroservice.user.entity.rep.AnchorRep;
import com.e_commerce.miscroservice.user.mapper.LiveUserMapper;
import com.e_commerce.miscroservice.user.mapper.StoreBusinessMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/15 17:04
 */
@Repository
public class LiveDaoImpl implements LiveDao {

    @Resource
    private StoreBusinessMapper storeBusinessMapper;

    @Resource
    private LiveUserMapper liveUserMapper;
    @Override
    public Boolean openWxUserByStoreId(Long storeId) {
        Boolean flag = false;
        StoreBusiness storeBusiness = storeBusinessMapper.openWxUserByStoreId(storeId);
        if (storeBusiness!=null&&storeBusiness.getWxaOpenTime()!=null&&storeBusiness.getWxaCloseTime()!=null){
            if (storeBusiness.getWxaCloseTime()>System.currentTimeMillis()&&storeBusiness.getIsOpenWxa().equals(1)){
                flag=Boolean.TRUE;
            }
        }
        return flag;
    }

    @Override
    public void applyLive(LiveUser obj) {
        MybatisOperaterUtil.getInstance().save(obj);
    }

    @Override
    public Integer findApplyLiveStatus(Long id) {
        Long in = MybatisOperaterUtil.getInstance().count(new MybatisSqlWhereBuild(LiveUser.class).eq(LiveUser::getStoreId, id).eq(LiveUser::getDelStatus, StateEnum.NORMAL));
        return in.intValue();
    }

    @Override
    public LiveUser findLiveUserByPhone(String phone) {
        LiveUser liveUser = MybatisOperaterUtil.getInstance().findOne(new LiveUser(), new MybatisSqlWhereBuild(LiveUser.class).eq(LiveUser::getPhone,phone).eq(LiveUser::getDelStatus,StateEnum.NORMAL));
        return liveUser;
    }

    @Override
    public Integer upLiveAnchor(LiveUser liveUser) {
        Integer up = MybatisOperaterUtil.getInstance().update(liveUser, new MybatisSqlWhereBuild(LiveUser.class).eq(LiveUser::getId,liveUser.getId()));
        return up;
    }

    @Override
    public LiveUser findLiveUserByMemberId(Long memberId)
    {
        LiveUser liveUser = MybatisOperaterUtil.getInstance().findOne(new LiveUser(), new MybatisSqlWhereBuild(LiveUser.class).eq(LiveUser::getMemberId,memberId).eq(LiveUser::getDelStatus, StateEnum.NORMAL));
        return liveUser;
    }

    @Override
    public LiveUser findLiveUserById(Long userId) {
        LiveUser liveUser = MybatisOperaterUtil.getInstance().findOne(new LiveUser(), new MybatisSqlWhereBuild(LiveUser.class).eq(LiveUser::getId,userId).eq(LiveUser::getDelStatus, StateEnum.NORMAL));
        return liveUser;
    }

    /**
     * 通过房间号查找
     *
     * @param roomNum roomNum
     * @return com.e_commerce.miscroservice.user.entity.LiveUser
     * @author Charlie
     * @date 2019/1/17 13:07
     */
    @Override
    public LiveUser findLiveUserByRoomNum(Long roomNum) {
        return MybatisOperaterUtil.getInstance().findOne(new LiveUser(), new MybatisSqlWhereBuild(LiveUser.class).eq(LiveUser::getRoomNum,roomNum).eq(LiveUser::getDelStatus, StateEnum.NORMAL));
    }

    @Override
    public List<AnchorRep> findLiveUserByStoreId(Long storeId, Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        List<AnchorRep> liveUsers = liveUserMapper.findLiveUserStoreId(storeId);
        return liveUsers;
    }

}
