package com.e_commerce.miscroservice.commons.helper.plug.mybatis.page;

import lombok.Data;

@Data
public class DataGrid {

	private int pageSize = 10;
	private int pageNum = 1;
	private String sort;
	private String order = " desc";

}
