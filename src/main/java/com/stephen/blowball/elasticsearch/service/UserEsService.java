package com.stephen.blowball.elasticsearch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.blowball.elasticsearch.modal.entity.UserEsDTO;
import com.stephen.blowball.model.dto.user.UserQueryRequest;
import com.stephen.blowball.model.entity.User;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;

/**
 * @author: stephen qiu
 * @create: 2024-11-05 14:35
 **/
public interface UserEsService {
	
	/**
	 * 从ES中搜索文章
	 *
	 * @param userQueryRequest userQueryRequest
	 * @return {@link Page}<{@link UserEsDTO}>
	 */
	Page<User> searchUserFromEs(UserQueryRequest userQueryRequest);
	
	/**
	 * 构建查询条件
	 *
	 * @param userQueryRequest userQueryRequest
	 * @return {@link BoolQueryBuilder}
	 */
	BoolQueryBuilder getBoolQueryBuilder(UserQueryRequest userQueryRequest);
	
	/**
	 * 构建排序条件
	 *
	 * @param sortField 排序字段
	 * @param sortOrder 排序顺序
	 * @return SortBuilder 排序构建器
	 */
	SortBuilder<?> getSortBuilder(String sortField, String sortOrder);
}
