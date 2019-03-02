package com.e_commerce.miscroservice.user.entity.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/26 11:27
 * @Copyright 玖远网络
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberPackageVo{

    private Integer memberType;
    /**
     *  1 :YEAR
     *  2 :MONTH
     *  3 :DATE
     *  4 :HOUR_OF_DAY
     *  5 :MINUTE
     */
    private int timeUnit;
    private Integer value;


    @Override
    public String toString() {
        return "MemberPackageVo{" +
                "memberType=" + memberType +
                ", timeUnit=" + timeUnit +
                ", value=" + value +
                '}';
    }
}
