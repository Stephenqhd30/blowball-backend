package com.stephen.blowball.datasources;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 数据源接口（需要接入的数据源必须实现）
 * @author: stephen qiu
 * @create: 2024-06-13 16:38
 **/
public interface DataSource<T> {
	
	/**
	 * 搜索
	 *
	 * @param searchText 搜索关键词
	 * @param pageNum 当前页码
	 * @param pageSize 每次分页查询的数量
	 * @return 分页
	 */
	Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
