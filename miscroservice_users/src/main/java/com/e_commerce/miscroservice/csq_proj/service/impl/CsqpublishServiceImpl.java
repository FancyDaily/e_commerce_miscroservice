package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.csq_proj.dao.CsqPublishDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqPublish;
import com.e_commerce.miscroservice.csq_proj.service.CsqPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 11:29
 */
@Service
public class CsqpublishServiceImpl implements CsqPublishService {

	@Autowired
	private CsqPublishDao csqPublishDao;

	@Override
	public String getPublishName(int mainKey, Integer trendPubKey) {
		TCsqPublish tCsqPublish = csqPublishDao.selectByMainKey(mainKey);
		String jsonString = tCsqPublish.getValue();
		return getPublishName(jsonString, trendPubKey);
	}

	private static Map toMap(String text) {
		JSONObject jsonObject = JSONObject.parseObject(text);
		return jsonObject.toJavaObject(Map.class);
	}

	private static String getPublishName(String text, Integer key) {
		Map map = toMap(text);
		return (String) map.get(key.toString());
	}

	public static void main(String[] args) {
		HashMap<Long, String> map = new HashMap<>();
		map.put(1l, "zhangsan");
		map.put(2l, "lisi");
		map.put(3l, "wangwu");
		String s = JSONObject.toJSONString(map);
		System.out.println(s);

		JSONObject jsonObject = JSONObject.parseObject(s);
		Map map1 = jsonObject.toJavaObject(Map.class);
		System.out.println(jsonObject);
		System.out.println(map1);
		Object o = map1.get(1);
		System.out.println(o);
		Object o1 = map1.get("1");
		System.out.println(o1);
	}
}
