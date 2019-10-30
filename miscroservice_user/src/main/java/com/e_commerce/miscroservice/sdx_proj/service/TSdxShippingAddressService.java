package com.e_commerce.miscroservice.sdx_proj.service;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShippingAddressPo;
import com.e_commerce.miscroservice.sdx_proj.vo.TSdxShippingAddressVo;
import java.util.List;
public interface TSdxShippingAddressService {
    long modTSdxShippingAddress(TSdxShippingAddressPo tSdxShippingAddressPo);
    int delTSdxShippingAddressByIds(Long... ids);
    TSdxShippingAddressVo findTSdxShippingAddressById(Long id);
    List<TSdxShippingAddressVo> findTSdxShippingAddressByAll(TSdxShippingAddressPo tSdxShippingAddressPo, Integer page, Integer size);
}
