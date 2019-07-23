package com.e_commerce.miscroservice.user.vo;

import com.e_commerce.miscroservice.commons.entity.application.TEvaluate;
import com.e_commerce.miscroservice.commons.entity.application.TOrder;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import lombok.Data;

import java.util.List;
@Data
public class HistoryServView {
    TOrder order;

    List<TEvaluate> evaluates;

    TUser user;



}
