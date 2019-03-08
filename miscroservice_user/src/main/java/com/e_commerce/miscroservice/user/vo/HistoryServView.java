package com.e_commerce.miscroservice.user.vo;

import com.e_commerce.miscroservice.order.po.TEvaluate;
import com.e_commerce.miscroservice.order.po.TOrder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class HistoryServView {
    TOrder order;

    List<TEvaluate> evaluates;

}
