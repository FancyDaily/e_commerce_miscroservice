package com.e_commerce.miscroservice.guanzhao_proj.product_order.vo;

import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzUserLesson;
import lombok.Data;

import java.util.List;

/**
 * @Author: FangyiXu
 * @Date: 2019-05-10 17:04
 */
@Data
public class MySubjectDetailVO extends TGzUserLesson {

    List<MyLessonVO> myLessonVOS;

}
