package com.stephen.popcorn.datasources;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.popcorn.model.dto.user.UserQueryRequest;
import com.stephen.popcorn.model.vo.UserVO;
import com.stephen.popcorn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户服务实现
 *
 * @author stephen qiu
 */
@Service
@Slf4j
public class UserDataSource implements DataSource<UserVO> {
	
	@Resource
	private UserService userService;
	
	@Override
	public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize) {
		UserQueryRequest userQueryRequest = new UserQueryRequest();
		userQueryRequest.setUserName(searchText);
		userQueryRequest.setCurrent((int)pageNum);
		userQueryRequest.setPageSize((int)pageSize);
		return userService.listUserVOByPage(userQueryRequest);
	}
}