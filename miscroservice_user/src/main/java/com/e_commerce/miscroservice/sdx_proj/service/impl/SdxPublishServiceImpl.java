package com.e_commerce.miscroservice.sdx_proj.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.annotation.colligate.generate.Log;
import com.e_commerce.miscroservice.commons.constant.colligate.AppErrorConstant;
import com.e_commerce.miscroservice.commons.enums.application.CsqPublishEnum;
import com.e_commerce.miscroservice.commons.exception.colligate.MessageException;
import com.e_commerce.miscroservice.csq_proj.vo.CsqBasicPublishVo;
import com.e_commerce.miscroservice.sdx_proj.dao.SdxPublishDao;
import com.e_commerce.miscroservice.sdx_proj.po.TSdxPublish;
import com.e_commerce.miscroservice.sdx_proj.service.SdxPublishService;
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
public class SdxPublishServiceImpl implements SdxPublishService {

	@Autowired
	private SdxPublishDao sdxPublishDao;

	@Override
	public String getPublishName(int mainKey, Integer trendPubKey) {
		String jsonString = getValue(mainKey);
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
		String jsonString = getValue(mainKey);
		return toMap(jsonString);
	}

	@Override
	public void setPublishName(int mainKey, Map<Long, Long> dailyDonateMap) {
		setPublishName(mainKey, JSONObject.toJSONString(dailyDonateMap));
	}

	@Override
	public void setPublishName(Integer mainKey, Integer[] keys, String[] names, String keyDesc, boolean isObjectArray) {
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
		Map<Integer, String> keyValueMap  = new HashMap<>();
		for(int i=0; i<names.length; i++) {
			keyValueMap.put(keys[i], names[i]);
		}
		String value = isObjectArray? JSONObject.toJSONString(keyValueMap): toObjectArrayJson(keyValueMap);

		//获取描述
		if(keyDesc == null) {
			List<String> existList = Arrays.stream(CsqPublishEnum.values()).filter(a -> a.toCode().equals(mainKey)).map(CsqPublishEnum::getMsg).collect(Collectors.toList());
			keyDesc = existList.isEmpty()? null:existList.get(0);
		}

		TSdxPublish build;
		if((build = sdxPublishDao.selectByMainKey(mainKey)) != null) {
			log.info("更新了 MainKey={},theValue={}的publish记录", mainKey, value);
			build.setKeyDesc(keyDesc);
			build.setValue(value);
			sdxPublishDao.update(build);
		} else {
			log.info("插入了 MainKey={},theValue={}的publish记录", mainKey, value);
			build = TSdxPublish.builder().mainKey(mainKey)
				.keyDesc(keyDesc)
				.value(value).build();
			sdxPublishDao.insert(build);
		}
	}

	private Map<String, Object> parseFromJson(String value) {
		Map<String, Object> resultMap = new HashMap<>();
		//尝试解析为map
		Object object;
		String description = "isObjectArray";
		try {
			Map map = get(value);
			object = toList(map);
			resultMap.put(description, false);
		} catch (Exception e) {	//若解析出错，尝试对象解析
			object = getObjectList(value);
			resultMap.put(description, true);
		}
		resultMap.put("result", object);
		return resultMap;
	}

	private String toObjectArrayJson(Map<Integer, String> keyValueMap) {
		List<CsqBasicPublishVo> javaObject = keyValueMap.entrySet().stream()
			.map(a -> CsqBasicPublishVo.builder().key(a.getKey())
				.value(a.getValue()).build()).collect(Collectors.toList());
		return JSONObject.toJSONString(javaObject);
	}

	@Override
	public Map get(Integer mainKey) {
		String value = getValue(mainKey);
		return toMap(value);
	}

	private Map get(String map) {
		return toMap(map);
	}

	@Override
	public String getValue(Integer mainKey) {
		TSdxPublish tSdxPublish = sdxPublishDao.selectByMainKey(mainKey);
		return tSdxPublish.getValue();
	}

	@Override
	public Object getAsList(Integer mainKey) {
		Object result;
		try {
			Map map = get(mainKey);
			result = toList(map);
		} catch (Exception e) {	//若解析出错，尝试对象解析
			result = getObjectList(mainKey);
		}
		return result;
	}

	private List<CsqBasicPublishVo> getObjectList(Integer mainKey) {
		String value = getValue(mainKey);
		List<CsqBasicPublishVo> lists = parseToObjectArray(value);
		return lists;
	}

	private List<CsqBasicPublishVo> getObjectList(String value) {
		List<CsqBasicPublishVo> lists = parseToObjectArray(value);
		return lists;
	}

	private Object toList(Map map) {
		return map.values().stream()
			.collect(Collectors.toList());
	}

	private void setPublishName(int mainKey, String jsonValue) {
		String desc = null;
		for(CsqPublishEnum csqPublishEnum:CsqPublishEnum.values()) {
			if(csqPublishEnum.toCode().equals(mainKey)) {
				desc = csqPublishEnum.getMsg();
			}
		}
		TSdxPublish tSdxPublish = sdxPublishDao.selectByMainKey(mainKey);
		if(tSdxPublish!=null) {	//更新
			sdxPublishDao.update(tSdxPublish);
			return;
		}
		//插入
		tSdxPublish = TSdxPublish.builder().mainKey(mainKey)
			.value(jsonValue)
			.keyDesc(desc)
			.build();
		sdxPublishDao.insert(tSdxPublish);
	}

	private static Map toMap(String text) {
		JSONObject jsonObject = JSONObject.parseObject(text);
		return jsonObject.toJavaObject(Map.class);
	}

	private String getPublishName(String text, Integer key) {
		Map map = null;
		try {
			map = toMap(text);
		} catch (Exception e) {	//解析出错
			return findTrueValueFromObjectArray(text, key);
		}
		return (String) map.get(key.toString());
	}

	private String findTrueValueFromObjectArray(String text, Integer key) {
		//parse
		List<CsqBasicPublishVo> csqBasicPublishVos = parseToObjectArray(text);
		return csqBasicPublishVos.stream()
			.filter(a -> key.equals(a.getKey()))
			.map(CsqBasicPublishVo::getValue)
			.reduce(null, (a,b) -> b);
	}

	public static void main(String[] args) {
		/*HashMap<Long, String> map = new HashMap<>();
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

		CsqBasicPublishVo basicPublishVo = new CsqBasicPublishVo();
		basicPublishVo.setKey(1);
		basicPublishVo.setValue("13");

		CsqBasicPublishVo vo21 = new CsqBasicPublishVo();
		vo21.setKey(2);
		vo21.setValue("123");

		String s1 = JSONObject.toJSONString(Arrays.asList(basicPublishVo, vo21));

		System.out.println(s1);
		List<CsqBasicPublishVo> lists = parseToObjectArray(s1);
		lists.stream()
			.forEach(a -> a.getValue());
*/

		/*List<Long> bookInfoIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);
		String s2 = JSONObject.toJSONString(bookInfoIds);
		System.out.println(s2);

		Map<String, List<Long>> resultMap = new HashMap() {
			{
				put("1", bookInfoIds);
				put("2", bookInfoIds);
				put("3", bookInfoIds);
			}
		};

		String s = JSONObject.toJSONString(resultMap);
		System.out.println(s);

		Map map = JSONObject.toJavaObject(JSONObject.parseObject(s), Map.class);
		List<Long> list = (List) map.get("2");

		System.out.println(list.get(3));*/

		List<String> urls = new ArrayList<>();
		urls.add("http://imgsrc.baidu.com/forum/w=580/sign=686022c859b5c9ea62f303ebe53bb622/4d9457cb0a46f21fe2f24a90f4246b600e33ae89.jpg");
		String s = JSONObject.toJSONString(urls);
		System.out.println(s);
	}

	private static List<CsqBasicPublishVo> parseToObjectArray(String s1) {
		JSONArray objects = JSONObject.parseArray(s1);
		return objects.toJavaList(CsqBasicPublishVo.class);
	}

}
