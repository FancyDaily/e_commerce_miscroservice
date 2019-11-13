package com.e_commerce.miscroservice.sdx_proj.dao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxShippingAddressPo;
import java.util.List;
public interface SdxShippingAddressDao {
    int saveTSdxShippingAddressIfNotExist(TSdxShippingAddressPo tSdxShippingAddressPo);
    int modTSdxShippingAddress(TSdxShippingAddressPo tSdxShippingAddressPo);
    int delTSdxShippingAddressByIds(Long... ids);
    TSdxShippingAddressPo findTSdxShippingAddressById(Long id);
    List<TSdxShippingAddressPo> findTSdxShippingAddressByAll(TSdxShippingAddressPo tSdxShippingAddressPo, Integer page, Integer size);

	List<TSdxShippingAddressPo> selectByUserId(Long id, boolean isPage, Integer pageNum, Integer pageSize);

	TSdxShippingAddressPo selectByPrimaryKey(Long shippingAddressId);
}
