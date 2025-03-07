package com.stephen.blowball.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.blowball.common.BaseResponse;
import com.stephen.blowball.common.ErrorCode;
import com.stephen.blowball.common.ResultUtils;
import com.stephen.blowball.common.ThrowUtils;
import com.stephen.blowball.elasticsearch.manager.SearchFacade;
import com.stephen.blowball.elasticsearch.modal.dto.SearchRequest;
import com.stephen.blowball.elasticsearch.modal.vo.SearchVO;
import com.stephen.blowball.elasticsearch.service.PostEsService;
import com.stephen.blowball.elasticsearch.service.UserEsService;
import com.stephen.blowball.model.dto.post.PostQueryRequest;
import com.stephen.blowball.model.dto.user.UserQueryRequest;
import com.stephen.blowball.model.entity.Post;
import com.stephen.blowball.model.entity.User;
import com.stephen.blowball.model.vo.PostVO;
import com.stephen.blowball.model.vo.UserVO;
import com.stephen.blowball.service.PostService;
import com.stephen.blowball.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 搜索接口
 *
 * @author stephen qiu
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {
	
	@Resource
	private SearchFacade searchFacade;
	
	@Resource
	private PostEsService postEsService;
	
	@Resource
	private UserEsService userEsService;
	
	@Resource
	private PostService postService;
	
	@Resource
	private UserService userService;
	
	/**
	 * 分页搜索帖子（从 ES 查询，封装类）
	 *
	 * @param postQueryRequest postQueryRequest
	 * @param request          request
	 * @return BaseResponse<Page < PostVO>>
	 */
	@PostMapping("/search/post/page/vo")
	public BaseResponse<Page<PostVO>> searchPostVOByPage(@RequestBody PostQueryRequest postQueryRequest,
	                                                     HttpServletRequest request) {
		long size = postQueryRequest.getPageSize();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Post> postPage = postEsService.searchPostFromEs(postQueryRequest);
		return ResultUtils.success(postService.getPostVOPage(postPage, request));
	}
	
	/**
	 * 分页搜索用户（从 ES 查询，封装类）
	 *
	 * @param userQueryRequest userQueryRequest
	 * @param request          request
	 * @return BaseResponse<Page < PostVO>>
	 */
	@PostMapping("/search/user/page/vo")
	public BaseResponse<Page<UserVO>> searchUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
	                                                     HttpServletRequest request) {
		long size = userQueryRequest.getPageSize();
		int current = userQueryRequest.getCurrent();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<User> userPage = userEsService.searchUserFromEs(userQueryRequest);
		Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
		List<UserVO> userVO = userService.getUserVO(userPage.getRecords(), request);
		userVOPage.setRecords(userVO);
		return ResultUtils.success(userVOPage);
	}
	
	/**
	 * 使用门面模式进行重构
	 * 聚合搜索查询
	 *
	 * @param searchRequest searchRequest
	 * @return {@link BaseResponse <{@link SearchVO } <{@link Object}>>}
	 */
	@PostMapping("/all")
	public BaseResponse<SearchVO<Object>> doSearchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
		return ResultUtils.success(searchFacade.searchAll(searchRequest, request));
	}
}
