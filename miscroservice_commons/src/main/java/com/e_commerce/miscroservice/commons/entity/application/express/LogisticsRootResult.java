package com.e_commerce.miscroservice.commons.entity.application.express;

import lombok.Data;

import java.io.Serializable;
@Data
public class LogisticsRootResult {
	private static final long serialVersionUID = 1L;
	private int error_code;
    private String reason;
    private LogisticsResult result;
    
}
