package com.e_commerce.miscroservice.commons.entity.application.express;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
public class LogisticsResult {
	private static final long serialVersionUID = 1L;
	private String no;
    private boolean ischeck;
    private String com;
    private String company;
    private Date updatetime;
    private List<LogisticsData> data;
    
}
