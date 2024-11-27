package com.stephen.blowball.elasticsearch.datasources;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.blowball.elasticsearch.annotation.DataSourceType;
import com.stephen.blowball.elasticsearch.modal.dto.SearchRequest;
import com.stephen.blowball.elasticsearch.modal.enums.SearchTypeEnum;
import com.stephen.blowball.elasticsearch.service.UserEsService;
import com.stephen.blowball.model.dto.user.UserQueryRequest;
import com.stephen.blowball.model.entity.User;
import com.stephen.blowball.model.vo.UserVO;
import com.stephen.blowball.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务实现
 *
 * @author stephen qiu
 */
@DataSourceType(SearchTypeEnum.CONSUMER)
@Component
@Slf4j
public class UserDataSource implements DataSource<UserVO> {
	
	@Resource
	private UserService userService;
	
	@Resource
	private UserEsService userEsService;
	
	/**
	 * 从ES中搜索用户
	 *
	 * @param searchRequest 搜索条件
	 * @param request       request
	 * @return {@link Page {@link UserVO}}
	 */
	@Override
	public Page<UserVO> doSearch(SearchRequest searchRequest, HttpServletRequest request) {
		UserQueryRequest userQueryRequest = new UserQueryRequest();
		BeanUtils.copyProperties(searchRequest, userQueryRequest);
		Page<User> userPage = userEsService.searchUserFromEs(userQueryRequest);
		return userService.getUserVOPage(userPage, request);
	}
}