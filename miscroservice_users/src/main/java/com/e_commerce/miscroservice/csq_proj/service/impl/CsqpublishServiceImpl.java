package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.enums.application.CsqPublishEnum;
import com.e_commerce.miscroservice.csq_proj.dao.CsqPublishDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqPublish;
import com.e_commerce.miscroservice.csq_proj.service.CsqPublishService;
import jodd.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

	@Override
	public List<String> getPublishName(int mainKey, String trendPubKeys) {
		List<String> resultList = new ArrayList<>();
		if (trendPubKeys != null) {
			String[] split = trendPubKeys.split(",");
			Arrays.stream(split).forEach(a ->
				resultList.add(getPublishName(mainKey, Integer.valueOf(a)))
			);
		}
		return resultList;
	}

	@Override
	public Map getPublishName(int mainKey) {
		TCsqPublish tCsqPublish = csqPublishDao.selectByMainKey(mainKey);
		String jsonString = tCsqPublish.getValue();
		return toMap(jsonString);
	}

	@Override
	public void setPublishName(int mainKey, Map<Long, Long> dailyDonateMap) {
		setPublishName(mainKey, JSONObject.toJSONString(dailyDonateMap));
	}

	private void setPublishName(int mainKey, String jsonValue) {
		String desc = null;
		for(CsqPublishEnum csqPublishEnum:CsqPublishEnum.values()) {
			if(csqPublishEnum.toCode().equals(mainKey)) {
				desc = csqPublishEnum.getMsg();
			}
		}
		TCsqPublish tCsqPublish = csqPublishDao.selectByMainKey(mainKey);
		if(tCsqPublish!=null) {	//更新
			csqPublishDao.update(tCsqPublish);
			return;
		}
		//插入
		tCsqPublish = TCsqPublish.builder().main_key(mainKey)
			.value(jsonValue)
			.key_desc(desc)
			.build();
		csqPublishDao.insert(tCsqPublish);
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


		System.out.println("===================");
		String str = "1";
		String[] split = str.split(",");
		Arrays.stream(split).forEach(System.out::println);
	}
}
