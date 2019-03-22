package com.e_commerce.miscroservice.user.service;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.QueryResult;
import com.e_commerce.miscroservice.user.vo.CompanyRecentView;
import com.e_commerce.miscroservice.user.vo.SmartUserView;
import com.e_commerce.miscroservice.user.vo.UserCompanyView;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GroupService {

    /**
     * 组织概况
     * @param user
     * @return
     */
    UserCompanyView gatherInfo(TUser user);

    Long getOwnCompanyId(Long id);

    /**
     * 组织近况
     * @param user
     * @return
     */
    CompanyRecentView companyRecent(TUser user);

    /**
     * 组内成员列表
     * @param user
     * @param groupId
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    QueryResult<SmartUserView> userList(TUser user, Long groupId, String param, Integer pageNum, Integer pageSize);

    /**
     * 成员新增
     * @param user
     * @param groupId
     * @param userIds
     */
    void userAdd(TUser user, Long groupId, String userIds);

    /**
     * 创建新成员
     * @param user
     * @param groupId
     * @param phone
     * @param userName
     * @param sex
     */
    void userInsert(TUser user, Long groupId, String phone, String userName, Integer sex);

    /**
     * 删除组织成员
     * @param user
     * @param userIds
     */
    void delete(TUser user, String userIds);

    /**
     * 成员审核通过
     * @param user
     * @param userIds
     */
    void userPass(TUser user, String userIds);

    void userReject(TUser user, String userIds);

    List<String> multiUserInsert(TUser user, MultipartFile file) throws IOException;
}
