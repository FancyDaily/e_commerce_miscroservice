package com.e_commerce.miscroservice.commons.entity.distribution;

import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/15 14:40
 * @Copyright 玖远网络
 */
@Data
public class UnderMyClassAResponse {
    private String nickName;
    private String img;
    private String time;

    public void setTime(String time) {

        this.time = TimeUtils.stamp2Str(Timestamp.valueOf(time));
    }
}
