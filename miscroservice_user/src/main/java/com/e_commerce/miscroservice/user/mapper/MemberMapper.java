package com.e_commerce.miscroservice.user.mapper;

import com.e_commerce.miscroservice.commons.entity.application.user.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/29 21:05
 * @Copyright 玖远网络
 */
@Mapper
public interface MemberMapper{



    /**
     * 更新会员账号过期时间
     *
     * @param id 会员表主键
     * @param memberPackageType 会员类型
     * @param endTime 更新的时间
     * @param totalMoney 需要累加的购买金额
     * @param validTimeQueue 更新后的时间
     * @param historyValidTimeQueue 历史时间
     * @return int
     * @author Charlie
     * @date 2018/8/16 9:30
     */
    int updateEndTime(@Param( "id" ) Long id,
                      @Param( "memberPackageType" ) Integer memberPackageType,
                      @Param( "endTime" ) Long endTime,
                      @Param( "totalMoney" ) Double totalMoney,
                      @Param( "validTimeQueue" ) String validTimeQueue,
                      @Param( "historyValidTimeQueue" ) String historyValidTimeQueue
    );

    Member selectByPrimaryKey(Long id);

    int insertSelective(Member member);

    void updateByPrimaryKeySelective(Member member);
}
