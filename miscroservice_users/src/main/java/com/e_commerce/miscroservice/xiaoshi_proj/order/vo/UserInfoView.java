package com.e_commerce.miscroservice.xiaoshi_proj.order.vo;

import lombok.Data;

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
@Data
public class UserInfoView {

    private String name;//名字

    private String userHeadPortraitPath;//头像

    private int status;//状态

    private long toStringId;//string型的用户id

    private Integer authStatus; // 实名认证状态

}
