package com.e_commerce.miscroservice.message.vo;

/**
 * 功能描述:
 * 模块:
 * 项目:时间银行
 * 版本号:V1.0
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:姜修弘
 * 邮箱:414368243@qq.com
 * 创建时间:2019/3/5 下午6:12
 * ************************************
 * ************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class UserInfoView {

    private String name;//名字

    private String userHeadPortraitPath;//头像

    private int status;//状态

    private String toStringId;//string型的用户id

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserHeadPortraitPath() {
        return userHeadPortraitPath;
    }

    public void setUserHeadPortraitPath(String userHeadPortraitPath) {
        this.userHeadPortraitPath = userHeadPortraitPath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getToStringId() {
        return toStringId;
    }

    public void setToStringId(Long toStringId) {
        this.toStringId = toStringId+"";
    }
}
