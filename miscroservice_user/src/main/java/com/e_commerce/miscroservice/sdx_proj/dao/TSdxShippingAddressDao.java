package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShippingAddressPo;
import java.util.List;
public interface TSdxShippingAddressDao {
    int saveTSdxShippingAddressIfNotExist(TSdxShippingAddressPo tSdxShippingAddressPo);
    int modTSdxShippingAddress(TSdxShippingAddressPo tSdxShippingAddressPo);
    int delTSdxShippingAddressByIds(Long... ids);
    TSdxShippingAddressPo findTSdxShippingAddressById(Long id);
    List<TSdxShippingAddressPo> findTSdxShippingAddressByAll(TSdxShippingAddressPo tSdxShippingAddressPo, Integer page, Integer size);
}
