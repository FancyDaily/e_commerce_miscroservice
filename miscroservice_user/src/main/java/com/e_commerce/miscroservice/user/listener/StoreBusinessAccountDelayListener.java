/*
package com.e_commerce.miscroservice.user.listener;

import com.e_commerce.miscroservice.commons.config.colligate.MqListenerConvert;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

*/
/**
 *
 *//*

@Component
public class StoreBusinessAccountDelayListener extends MqListenerConvert {
    private static Log logger = Log.getInstance(StoreBusinessAccountDelayListener.class);

	@Autowired
	private StoreBusinessAccountService storeBusinessAccountService;
    
	@Override
	protected void transferTo(String transferData) {
		logger.error("结算资金={}", transferData);
		if( transferData == null ) {
			logger.warn("结算资金,解析参数为空={}", transferData);
			return;
		}

		try {
			storeBusinessAccountService.waitInMoneyToUseAll();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("结算资金{}处理。异常信息：{}", transferData, e);
		}
	}

}
*/
