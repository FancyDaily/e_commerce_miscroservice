package com.e_commerce.miscroservice.commons.helper.plug.mybatis.service.impl;

import com.e_commerce.miscroservice.commons.helper.plug.mybatis.mapper.BaseMapper;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.page.DataGrid;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.service.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public abstract class BaseServiceImpl<T> implements BaseService<T> {

	@Autowired(required = false)
	private BaseMapper<T> mapper;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean insert(T entity) {
		return mapper.insertSelective(entity) > 0;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean insertSelective(T entity) {
		return mapper.insertSelective(entity) > 0;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean deleteById(Object key) {
		return mapper.deleteByPrimaryKey(key) > 0;
	}

	@Override
	public Optional<T> selectById(Object key) {
		return Optional.ofNullable(mapper.selectByPrimaryKey(key));
	}

	@Override
	public T selectByIdREntity(Object key) {
		return mapper.selectByPrimaryKey(key);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateById(T entity) {
		return mapper.updateByPrimaryKey(entity) > 0;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean updateSelectiveById(T entity) {
		return mapper.updateByPrimaryKeySelective(entity) > 0;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateByExampleSelective(T entity,Object example) {
		return mapper.updateByExampleSelective(entity,example);
	}

	@Override
	public List<T> listAll() {
		return this.mapper.selectAll();
	}

	@Override
	public PageInfo<T> listForDataGrid(DataGrid grid) {
		return this.listForDataGrid(grid, null);
	}

	@Override
	public List<T> listForDataGridL(DataGrid grid) {
		return this.listForDataGridL(grid, null);
	}

	@Override
	public List<T> listForDataGridL(DataGrid grid, T entity) {
		//BasePage.setPage(grid.getPageNum());
		PageHelper.startPage(grid.getPageNum(), grid.getPageSize());
		return mapper.select(entity);
	}

	@Override
	public PageInfo<T> listForDataGrid(DataGrid grid, T entity) {
		//BasePage.setPage(grid.getPageNum());
		PageHelper.startPage(grid.getPageNum(), grid.getPageSize());
		return new PageInfo<T>(mapper.select(entity));
	}

	
}
