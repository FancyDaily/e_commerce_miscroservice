package com.e_commerce.miscroservice.commons.util.colligate;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Sep 4, 2017 10:05:06 AM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */ 
@Service
public class RedisUtil {
 
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class); 
	
	@Resource(name="redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	//=============================common============================
	/**
	 * 指定缓存失效时间
	 * @param key 键
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean expire(String key,long time){
		try {
			if(time>0){
				redisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("-------设置超时时间出错-----------");
			return false;
		}
	}
	
	/**
	 * 根据key 获取过期时间
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public long getExpire(String key){
		return redisTemplate.getExpire(key,TimeUnit.SECONDS);
	}
	
	/**
	 * 判断key是否存在
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public boolean hasKey(String key){
		try {
			return redisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("-------判断key出错-----------");
			return false;
		}
	}
	
	/**
	 * 删除缓存
	 * @param key 可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public void del(String ... key){
		if(key!=null&&key.length>0){
			if(key.length==1){
				redisTemplate.delete(key[0]);
			}else{
				redisTemplate.delete(CollectionUtils.arrayToList(key));
			}
		}
	}
	
	//============================String=============================
	/**
	 * 普通缓存获取
	 * @param key 键
	 * @return 值
	 */
	public Object get(String key){
		return key==null?null:redisTemplate.opsForValue().get(key);
	}
	
	/**
	 * 普通缓存放入
	 * @param key 键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public boolean set(String key,Object value) {
		 try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("-------设置缓存出错-----------");
			return false;
		}
		
	}
	
	/**
	 * 普通缓存放入并设置时间
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public boolean set(String key,Object value,long time){
		try {
			if(time>0){
				redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			}else{
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("-------设置缓存超时时间出错-----------");
			return false;
		}
	}
	
	/**
	 * 递增
	 * @param key 键
	 * @param by 要增加几(大于0)
	 * @return
	 */
	public long incr(String key, long delta){  
		if(delta<0){
			throw new RuntimeException("递增因子必须大于0");
		}
		return redisTemplate.opsForValue().increment(key, delta);
    }
	
	/**
	 * 递减
	 * @param key 键
	 * @param by 要减少几(小于0)
	 * @return
	 */
	public long decr(String key, long delta){  
		if(delta<0){
			throw new RuntimeException("递减因子必须大于0");
		}
        return redisTemplate.opsForValue().increment(key, -delta);  
    }  
	
	//================================Map=================================
	/**
	 * HashGet
	 * @param key 键 不能为null
	 * @param item 项 不能为null
	 * @return 值
	 */
	public Object hget(String key,String item){
		return redisTemplate.opsForHash().get(key, item);
	}
	
	/**
	 * 获取hashKey对应的所有键值
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public Map<Object,Object> hmget(String key){
		return redisTemplate.opsForHash().entries(key);
	}
	
	/**
	 * HashSet
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public boolean hmset(String key, Map<String,Object> map){  
        try {
			redisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
	
	/**
	 * HashSet 并设置时间
	 * @param key 键
	 * @param map 对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
    public boolean hmset(String key, Map<String,Object> map, long time){  
        try {
			redisTemplate.opsForHash().putAll(key, map);
			if(time>0){
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("-------设置hashset超时时间出错-----------");
			return false;
		}
    }
	
	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public boolean hset(String key,String item,Object value) {
		 try {
			redisTemplate.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			LOGGER.error("-------向哈希表存放数据出错-----------");
			return false;
		}
	}
	
	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * @param key 键
	 * @param item 项
	 * @param value 值
	 * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public boolean hset(String key,String item,Object value,long time) {
		 try {
			redisTemplate.opsForHash().put(key, item, value);
			if(time>0){
				//expire(mainKey, time);
				expire(item, time);
			}
			return true;
		} catch (Exception e) {
			LOGGER.error("-------向哈希表存放数据并设置时间出错-----------");
			return false;
		}
	}
	
	/**
	 * 删除hash表中的值
	 * @param key 键 不能为null
	 * @param item 项 可以使多个 不能为null
	 */
    public void hdel(String key, Object... item){  
		redisTemplate.opsForHash().delete(key,item);
    } 
    
    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item){
		return redisTemplate.opsForHash().hasKey(key, item);
    } 
	
	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 * @param key 键
	 * @param item 项
	 * @param by 要增加几(大于0)
	 * @return
	 */
	public double hincr(String key, String item,double by){  
        return redisTemplate.opsForHash().increment(key, item, by);
    }
	
	/**
	 * hash递减
	 * @param key 键
	 * @param item 项
	 * @param by 要减少记(小于0)
	 * @return
	 */
	public double hdecr(String key, String item,double by){  
        return redisTemplate.opsForHash().increment(key, item,-by);  
    }  
	
	//============================set=============================
	/**
	 * 根据key获取Set中的所有值
	 * @param key 键
	 * @return
	 */
	public Set<Object> sGet(String key){
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			LOGGER.error("-------获取set集合数据出错-----------");
			return null;
		}
	}
	
	/**
	 * 根据value从一个set中查询,是否存在
	 * @param key 键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public boolean sHasKey(String key,Object value){
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			LOGGER.error("-------判断set集合是否存在数据出错-----------");
			return false;
		}
	}
	
	/**
	 * 将数据放入set缓存
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSet(String key, Object...values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
        	LOGGER.error("-------向set集合存放数据出错-----------");
        	return 0;
        }
    }
	
	/**
	 * 将set数据放入缓存
	 * @param key 键
	 * @param time 时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSetAndTime(String key,long time,Object...values) {
        try {
        	Long count = redisTemplate.opsForSet().add(key, values);
        	if(time>0) expire(key, time);
            return count;
        } catch (Exception e) {
        	LOGGER.error("-------向set集合存放数据并设置时间出错-----------");
        	return 0;
        }
    }
	
	/**
	 * 获取set缓存的长度
	 * @param key 键
	 * @return
	 */
	public long sGetSetSize(String key){
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			LOGGER.error("-------获取set集合数据长度出错-----------");
			return 0;
		}
	}
	
	/**
	 * 移除值为value的
	 * @param key 键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long setRemove(String key, Object ...values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
        	LOGGER.error("------删除key出错-----------");
        	return 0;
        }
    }
    //===============================list=================================
    
	/**
	 * 获取list缓存的内容
	 * @param key 键
	 * @param start 开始
	 * @param end 结束  0 到 -1代表所有值
	 * @return
	 */
	public List<Object> lGet(String key,long start, long end){
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			LOGGER.error("-------获取list中的数据出错-----------");
			return null;
		}
	}
	
	/**
	 * 获取list缓存的长度
	 * @param key 键
	 * @return
	 */
	public long lGetListSize(String key){
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			LOGGER.error("-------获取list数据长度出错-----------");
			return 0;
		}
	}
	
	/**
	 * 通过索引 获取list中的值
	 * @param key 键
	 * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public Object lGetIndex(String key,long index){
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			LOGGER.error("-------获取list中的索引数据出错-----------");
			return null;
		}
	}
	
	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
        	LOGGER.error("-------设置list中的数据出错-----------");
        	return false;
        }
    }
	
	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
        	LOGGER.error("-------设置list中的数据并设置时间出错-----------");
        	return false;
        }
    }
	
	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean lSet(String key, List<Object> value) {
	    try {
			redisTemplate.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			LOGGER.error("-------将list中的数据放入缓存出错-----------");
			return false;
		}
    }
	
	/**
	 * 将list放入缓存
	 * @param key 键
	 * @param value 值
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean lSet(String key, List<Object> value, long time) {
	    try {
			redisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0) expire(key, time);
			return true;
		} catch (Exception e) {
			LOGGER.error("-------将list中的数据放入缓存并设置时间出错-----------");
			return false;
		}
    }
	
	/**
	 * 根据索引修改list中的某条数据
	 * @param key 键
	 * @param index 索引
	 * @param value 值
	 * @return
	 */
	public boolean lUpdateIndex(String key, long index,Object value) {
	    try {
			redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			LOGGER.error("-------缓存数据出错-----------");
			LOGGER.error("缓存KEY为:"+ key + ",值为:" + value);
			return false;
		}
    } 
	
	/**
	 * 移除N个值为value 
	 * @param key 键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public long lRemove(String key,long count,Object value) {
		try {
			Long remove = redisTemplate.opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			LOGGER.error("-------缓存数据删除出错-----------");
			LOGGER.error("缓存KEY为:"+ key + ",值为:" + value);
			return 0;
		}
	}
	
	/**
	 *判断某个具体item的时间戳是否过期
	 * @param key
	 * @param item
	 * @param time
	 * @return
	 */
	public boolean isExpire(String key,String item,long time){
		long now = Calendar.getInstance().getTimeInMillis();
		
		long passed = (long) hget(key, item);
		
		long expired = (now-passed)/1000;
		return expired>=time?true:false;
		
	}
	
}
