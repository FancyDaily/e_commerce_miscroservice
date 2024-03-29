package com.e_commerce.miscroservice.guanzhao_proj.product_order.controller;

import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.commons.entity.colligate.AjaxResult;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.commons.helper.util.application.generate.TokenUtil;
import com.e_commerce.miscroservice.commons.utils.UserUtil;
import com.e_commerce.miscroservice.guanzhao_proj.product_order.dao.GZVoucherDao;
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
    @RequestMapping("purchaseList/my/" + TokenUtil.AUTH_SUFFIX)
    public Object myVoucherList( Integer pageNum, Integer pageSize, Integer... option) {
        AjaxResult result = new AjaxResult();
        TUser user = UserUtil.getUser();
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

    /**
     * 添加优惠券
     * @param price
     * @param userId
     * @param count
     * @return
     */
    @RequestMapping("add")
    public Object addVoucher(int price,int userId, int count) {
        AjaxResult result = new AjaxResult();
        try {
            gzVoucherService.addVoucher(price, userId, count);
            result.setSuccess(true);
        } catch (MessageException e) {
            log.warn("====方法描述: {}, Message: {}====", "添加代金券", e.getMessage());
            result.setMsg(e.getMessage());
            result.setSuccess(false);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加代金券",e);
            result.setSuccess(false);
        }
        return result;
    }
}
