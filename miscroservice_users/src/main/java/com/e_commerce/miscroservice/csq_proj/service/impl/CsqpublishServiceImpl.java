package com.e_commerce.miscroservice.csq_proj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqPublishEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.dao.CsqPublishDao;
import com.e_commerce.miscroservice.csq_proj.po.TCsqPublish;
import com.e_commerce.miscroservice.csq_proj.service.CsqPublishService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-14 11:29
 */
@Transactional(rollbackFor = Throwable.class)
@Service
@Log
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

	@Override
	public void setPublishName(Integer mainKey, Integer[] keys, String[] names) {
		//check
		if(keys == null || names == null) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数keys、names不能为空！");
		}
		if(keys.length != names.length) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数keys、names长度不一致！");
		}
		if(Arrays.stream(keys).distinct().count() < keys.length) {
			throw new MessageException(AppErrorConstant.NOT_PASS_PARAM, "参数keys中存在重复编号!");
		}
		//构建map
		Map<Integer, String> keyValueMap = new HashMap<> ();
		for(int i=0; i<names.length; i++) {
			keyValueMap.put(keys[i], names[i]);
		}
		String value = JSONObject.toJSONString(keyValueMap);
		//获取描述
		List<String> existList = Arrays.stream(CsqPublishEnum.values()).filter(a -> a.toCode().equals(mainKey)).map(CsqPublishEnum::getMsg).collect(Collectors.toList());
		String keyDesc = existList.isEmpty()? null:existList.get(0);
		TCsqPublish build;
		if((build = csqPublishDao.selectByMainKey(mainKey))!= null) {
			log.info("更新了 MainKey={},theValue={}的publish记录", mainKey, value);
			build.setValue(value);
			csqPublishDao.update(build);
		} else {
			log.info("插入了 MainKey={},theValue={}的publish记录", mainKey, value);
			build = TCsqPublish.builder().mainKey(mainKey)
				.keyDesc(keyDesc)
				.value(value).build();
			csqPublishDao.insert(build);
		}
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
		tCsqPublish = TCsqPublish.builder().mainKey(mainKey)
			.value(jsonValue)
			.keyDesc(desc)
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
