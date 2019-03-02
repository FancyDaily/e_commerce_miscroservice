package com.e_commerce.miscroservice.commons.helper.plug.mybatis.service;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.page.DataGrid;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @ClassName: BaseService
 * @author hyq
 */
public interface BaseService<T> {

	/**
	 * 新增一个实体，方法的实现需保证：当返回true时，实体entity的id属性已被赋值。
	 * 
	 * @author hyq
	 * @param entity
	 * @return
	 * @since
	 */
	boolean insert(T entity);

	/**
	 * Null字段使用数据库默认值
	 * 
	 * @author hyq
	 * @param entity
	 * @return
	 * @since
	 */
	boolean insertSelective(T entity);

	/**
	 * 根据主键删除一个实体
	 * 
	 * @author hyq
	 * @param key
	 * @return
	 * @since
	 */
	boolean deleteById(Object key);

	/**
	 * 根据主键字段进行查询
	 * 
	 * @author hyq
	 * @param key
	 * @return
	 * @since
	 */
	Optional<T> selectById(Object key);

	/**
	 * 根据主键字段进行查询
	 *
	 * @author hyq
	 * @param key
	 * @return
	 * @since
	 */
	T selectByIdREntity(Object key);

	/**
	 * 根据主键更新实体全部字段( 公共属性部分不必赋值，入库拦截前会自动将修改人 修改时间加上)
	 * 
	 * @author hyq
	 * @param entity
	 * @return
	 * @since
	 */
	boolean updateById(T entity);

	/**
	 * 根据主键更新不为NUll的值
	 * 
	 * @author hyq
	 * @param entity
	 * @return
	 * @since
	 */
	boolean updateSelectiveById(T entity);

	/**
	 * 根据条件更新不为NUll的值
	 *
	 * @author hyq
	 * @param entity
	 * @return
	 * @since
	 */
	public int updateByExampleSelective(T entity,Object example);
	
	List<T> listAll();

	List<T> listForDataGridL(DataGrid grid);

	PageInfo<T> listForDataGrid(DataGrid grid);
	
	PageInfo<T> listForDataGrid(DataGrid grid, T entity);

	List<T> listForDataGridL(DataGrid grid, T entity);
}
