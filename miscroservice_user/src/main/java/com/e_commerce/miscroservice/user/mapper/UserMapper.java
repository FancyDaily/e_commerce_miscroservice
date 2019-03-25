package com.e_commerce.miscroservice.user.mapper;

import com.e_commerce.miscroservice.user.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    /**
     * 根据用户手机查询用户信息
     *
     * @param userPhone 用户的手机
     * @return
     */
//    UserPO findUserOneByUserPhone(@Param("userPhone") String userPhone);

    /**
     * 用户登陆日志
     * @param userLoginLog
     */
//    void addUserLoginLog(UserLoginLog userLoginLog);


}
