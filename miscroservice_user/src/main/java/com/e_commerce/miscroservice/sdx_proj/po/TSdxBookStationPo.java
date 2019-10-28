package com.e_commerce.miscroservice.sdx_proj.po;

import com.e_commerce.miscroservice.sdx_proj.vo.TSdxBookStationVo;


import com.e_commerce.miscroservice.commons.annotation.colligate.table.Column;
import com.e_commerce.miscroservice.commons.annotation.colligate.table.Table;
import lombok.Builder;
import lombok.Data;

/**
 * 书籍回收驿站
 * @Author: FangyiXu
 * @Date: 2019-10-23 15:51
 */
@Table(commit = "书籍回收驿站")
@Data
@Builder
public class TSdxBookStationPo extends BaseEntity {

	@Column(commit = "驿站名字")
	private String name;

	@Column(commit = "地址")
	private String address;

	@Column(commit = "经度", precision = 2)
	private Double longitude;

	@Column(commit = "纬度", precision = 2)
	private Double latitude;

    public TSdxBookStationVo  copyTSdxBookStationVo() {
        return null;
     }

}
