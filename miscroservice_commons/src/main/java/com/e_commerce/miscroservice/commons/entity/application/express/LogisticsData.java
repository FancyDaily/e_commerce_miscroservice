package com.e_commerce.miscroservice.commons.entity.application.express;

import lombok.Data;

import java.util.Date;
@Data
public class LogisticsData {
	private static final long serialVersionUID = 1L;
	private Date time;
    private String context;
}
