package com.e_commerce.miscroservice.user.entity.rep;

import com.e_commerce.miscroservice.commons.entity.user.LiveUser;
import com.e_commerce.miscroservice.commons.utils.TimeUtils;
import lombok.Data;

/**
 * @Author hyf
 * @Date 2019/1/23 14:27
 */
@Data
public class AnchorRep extends LiveUser {

    private String time;

    public void setTime(String time) {
        time = TimeUtils.stamp2Str(getCreateTime());
        this.time = time;
    }
}
