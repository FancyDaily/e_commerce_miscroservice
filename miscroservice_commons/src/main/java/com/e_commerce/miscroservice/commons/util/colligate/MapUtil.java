package com.e_commerce.miscroservice.commons.util.colligate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: FangyiXu
 * @Date: 2019-06-26 16:39
 */
public class MapUtil {

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDesc(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();

		map.entrySet().stream()
			.sorted(Map.Entry.<K, V>comparingByValue()
				.reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
		return result;
	}

	public static <K extends Comparable<? super K>, V > Map<K, V> sortByKeyAsc(Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();

		map.entrySet().stream()
			.sorted(Map.Entry.<K, V>comparingByKey()).
			forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
		return result;
	}

}
