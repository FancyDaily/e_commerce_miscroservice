package com.e_commerce.miscroservice.guanzhao_proj.product_order.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.po.TGzVoucher;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.service.GZVoucherService;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.vo.MyVoucherVo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 观照代金券模块
 *
 */
@RestController
@RequestMapping("gz/api/v1/voucher")
@Log
public class GZVoucherController {

    @Autowired
    private GZVoucherService gzVoucherService;

    /**
     * 我的代金券列表
     * @param option
     * @return
     */
    @RequestMapping("list/my/" + TokenUtil.AUTH_SUFFIX)
    public Object myVoucherList( Integer pageNum, Integer pageSize, Integer... option) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser();
        if(user==null) {
            user = new TUser();
            user.setId(42l);
            user.setName("三胖");
        }
        try {
            List<MyVoucherVo> vouchers = gzVoucherService.myVoucherList(user,option);
            result.setData(vouchers);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "我的代金券列表", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("我的代金券列表",e);
            result.setSuccess(false);
        }
        return result;
    }
}
