package com.e_commerce.miscroservice.guanzhao_proj.product_order.vo;

import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Id;
import lombok.Data;

/**
 * @Description 我的正在学习
 * @ClassName MyLearningSubjectVO
 * @Auhor huangyangfeng
 * @Date 2019-05-10 16:44
 * @Version 1.0
 */
@Data
public class MyLearningSubjectVO {
@Id
    private Long id;

    private Long userId;

    private Long subjectId;

    @Column(commit = "状态")
    private Integer status;

    @Column(commit = "笔记数量")
    private Integer notesNum;

    @Column(commit = "作业总数")
    private Integer homeworkExpectedNum;

    @Column(commit = "作业完成数")
    private Integer homeworkDoneNum;

    @Column(commit = "学习进度")
    private Integer completion;

    @Column(commit = "失效时间戳")
    private Long expireTime;

    @Column(commit = "失效时间：n天后")
    private Integer afterTime;

    @Column(commit = "扩展字段")
    private String extend;

    @Column(commit = "有效性")
    private String isValid;

    private String subjectName;

    private static final long serialVersionUID = 1L;

    public Integer getAfterTime() {
       Long mill = (expireTime-System.currentTimeMillis())/1000/3600/24;
        afterTime = mill.intValue();
        return afterTime;
    }

}
