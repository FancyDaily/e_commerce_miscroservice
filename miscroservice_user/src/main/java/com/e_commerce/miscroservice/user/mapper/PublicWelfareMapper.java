package com.e_commerce.miscroservice.user.mapper;

import com.e_commerce.miscroservice.user.vo.WelfareParamView;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PublicWelfareMapper {

    /**
     * 获取年度公益时
     *
     * @param param
     * @return
     */
    Long getYearWelfare(WelfareParamView param);

}
