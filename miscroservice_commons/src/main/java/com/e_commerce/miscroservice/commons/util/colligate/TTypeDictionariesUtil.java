/*
package com.e_commerce.miscroservice.commons.util.colligate;
import com.xiaoshitimebank.app.dao.TypeDictionariesDao;
import com.xiaoshitimebank.app.entity.TTypeDictionaries;
import com.xiaoshitimebank.app.entity.TTypeDictionariesExample;
import com.xiaoshitimebank.app.mapper.TTypeDictionariesMapper;
import com.xiaoshitimebank.core.redis.RedisAPIService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TTypeDictionariesUtil {
	
    private static RedisUtil redisService;

    private static TypeDictionariesDao tTypeDictionariesMapper;
    
	@Autowired
	public void settTypeDictionariesMapper(TypeDictionariesDao tTypeDictionariesMapper) {
		TTypeDictionariesUtil.tTypeDictionariesMapper = tTypeDictionariesMapper;
	}

	@Autowired
	public  void setRedisService(RedisUtil redisService) {
		TTypeDictionariesUtil.redisService = redisService;
	}

	//time秒单位
    public static String getValueByTypeDictionaries(Long entityId, Integer type, Integer subType, Long time) {
        String key = "t_type_dictionaries_value_" + entityId + type + subType;
        //获取缓存
        try {
            Object redis = redisService.get(key);
            if (redis != null) {
                return (String) redis;
            }
        } catch (Exception e) {
            redisService.del(key);
        }
        TTypeDictionariesExample example = new TTypeDictionariesExample();
        example.createCriteria().andEntityIdEqualTo(entityId).andTypeEqualTo(type).andSubTypeEqualTo(subType).andIsValidEqualTo("1");
        List<TTypeDictionaries> tTypeDictionariesList = tTypeDictionariesMapper.selectByExample(example);

        String value = "";
        if (tTypeDictionariesList != null && tTypeDictionariesList.size() > 0) {
            value = tTypeDictionariesList.get(0).getValue();
        }
        //设置缓存
        try {
            Object redis = redisService.set(key, value, time);
        } catch (Exception e) {
            redisService.del(key);
        }
        return value;
    }

    //time秒单位
    //返回-1为没有数据
    public static Long getTargetIdByTypeDictionaries(Long entityId, Integer type, Integer subType, Long time) {
        String key = "t_type_dictionaries_target_id_" + entityId + type + subType;
        //获取缓存
        try {
            Object redis = redisService.get(key);
            if (redis != null) {
                return (Long) redis;
            }
        } catch (Exception e) {
            redisService.del(key);
        }
        TTypeDictionariesExample example = new TTypeDictionariesExample();
        example.createCriteria().andEntityIdEqualTo(entityId).andTypeEqualTo(type).andSubTypeEqualTo(subType).andIsValidEqualTo("1");
        List<TTypeDictionaries> tTypeDictionariesList = tTypeDictionariesMapper.selectByExample(example);

        Long targetId = -1L;
        if (tTypeDictionariesList != null && tTypeDictionariesList.size() > 0) {
            targetId = tTypeDictionariesList.get(0).getTargetId();
        }
        //设置缓存
        try {
            Object redis = redisService.set(key, targetId, time);
        } catch (Exception e) {
            redisService.del(key);
        }
        return targetId;
    }

    //time秒单位
    //返回-1为没有数据
    public static Long getTargetNumByTypeDictionaries(Long entityId, Integer type, Integer subType, Long time) {
        String key = "t_type_dictionaries_target_num_" + entityId + type + subType;
        //获取缓存
        try {
            Object redis = redisService.get(key);
            if (redis != null) {
                return (Long) redis;
            }
        } catch (Exception e) {
            redisService.del(key);
        }
        TTypeDictionariesExample example = new TTypeDictionariesExample();
        example.createCriteria().andEntityIdEqualTo(entityId).andTypeEqualTo(type).andSubTypeEqualTo(subType).andIsValidEqualTo("1");
        List<TTypeDictionaries> tTypeDictionariesList = tTypeDictionariesMapper.selectByExample(example);

        Long targetNum = -1L;
        if (tTypeDictionariesList != null && tTypeDictionariesList.size() > 0) {
            targetNum = tTypeDictionariesList.get(0).getTargetNum();
        }
        //设置缓存
        try {
            Object redis = redisService.set(key, targetNum, time);
        } catch (Exception e) {
            redisService.del(key);
        }
        return targetNum;
    }

    //time秒单位
    //返回-1为没有数据
    public static TTypeDictionaries getTypeDictionaries(Long entityId, Integer type, Integer subType, Long time) {
        String key = "t_type_dictionaries_" + entityId + type + subType;
        //获取缓存
        try {
            Object redis = redisService.get(key);
            if (redis != null) {
                return (TTypeDictionaries) redis;
            }
        } catch (Exception e) {
            redisService.del(key);
        }
        TTypeDictionariesExample example = new TTypeDictionariesExample();
        example.createCriteria().andEntityIdEqualTo(entityId).andTypeEqualTo(type).andSubTypeEqualTo(subType).andIsValidEqualTo("1");
        List<TTypeDictionaries> tTypeDictionariesList = tTypeDictionariesMapper.selectByExample(example);

        TTypeDictionaries tTypeDictionaries = new TTypeDictionaries();
        if (tTypeDictionariesList != null && tTypeDictionariesList.size() > 0) {
            tTypeDictionaries = tTypeDictionariesList.get(0);
        }
        //设置缓存
        try {
            Object redis = redisService.set(key, tTypeDictionaries, time);
        } catch (Exception e) {
            redisService.del(key);
        }
        return tTypeDictionaries;
    }
    
    */
/**
     * 
     * 功能描述:从字典表获取list数据
     * 作者:马晓晨
     * 创建时间:2018年11月16日 下午9:26:56
     * @param entityId
     * @param type
     * @param subType
     * @param time
     * @return
     *//*

    public static List<TTypeDictionaries> getListTypeDictionaries(Long entityId, Integer type, Integer subType, Long time, String orderCause) {
    	String key;
    	if (StringUtil.isNotEmpty(orderCause)) {
    		key = "t_type_dictionaries_list_ordercreatetime_" + entityId + type + subType;
    	} else {
    		key = "t_type_listdictionaries_" + entityId + type + subType;
    	}
        //获取缓存
        try {
            Object redis = redisService.get(key);
            List<TTypeDictionaries> list = (List<TTypeDictionaries>) redis;
            if (list != null && list.size() > 0) {
                return (List<TTypeDictionaries>) redis;
            }
        } catch (Exception e) {
            redisService.del(key);
        }
        TTypeDictionariesExample example = new TTypeDictionariesExample();
        example.createCriteria().andEntityIdEqualTo(entityId).andTypeEqualTo(type).andSubTypeEqualTo(subType).andIsValidEqualTo("1");
        if (StringUtil.isNotEmpty(orderCause)) {
        	example.setOrderByClause(orderCause);
        }
        List<TTypeDictionaries> tTypeDictionariesList = tTypeDictionariesMapper.selectByExample(example);
        //设置缓存
        try {
            Object redis = redisService.set(key, tTypeDictionariesList, time);
        } catch (Exception e) {
            redisService.del(key);
        }
        return tTypeDictionariesList;
    }

}
*/
