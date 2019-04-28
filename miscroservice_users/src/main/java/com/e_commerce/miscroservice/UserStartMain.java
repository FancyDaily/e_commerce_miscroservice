package com.e_commerce.miscroservice;

import com.e_commerce.miscroservice.commons.annotation.colligate.init.Start;
import com.e_commerce.miscroservice.commons.helper.util.colligate.other.ApplicationContextUtil;

@Start
public class UserStartMain {

	public static void main(String[] args) {
		System.out.println("___________________");
		System.out.println("___________________");
		System.out.println("___________________");
		ApplicationContextUtil.run(UserStartMain.class, Boolean.TRUE, args);
	}
}
