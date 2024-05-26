package com.stephen.popcorn.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.popcorn.common.BaseResponse;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.common.ResultUtils;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.exception.ThrowUtils;
import com.stephen.popcorn.model.dto.picture.PictureQueryRequest;
import com.stephen.popcorn.model.dto.post.PostQueryRequest;
import com.stephen.popcorn.model.dto.search.SearchRequest;
import com.stephen.popcorn.model.dto.user.UserQueryRequest;
import com.stephen.popcorn.model.entity.Picture;
import com.stephen.popcorn.model.vo.PostVO;
import com.stephen.popcorn.model.vo.SearchVO;
import com.stephen.popcorn.model.vo.UserVO;
import com.stephen.popcorn.service.PictureService;
import com.stephen.popcorn.service.PostService;
import com.stephen.popcorn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 帖子接口
 *
 * @author stephen qiu
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {
	@Resource
	private UserService userService;
	
	@Resource
	private PostService postService;
	
	@Resource
	private PictureService pictureService;
	
	
	/**
	 * 聚合搜索查询
	 *
	 * @param searchRequest
	 * @return
	 */
	@PostMapping("/all")
	public BaseResponse<SearchVO> doSearchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
		String searchText = searchRequest.getSearchText();
		
		// 并发
		CompletableFuture<Page<Picture>> picturePageCompletableFuture = CompletableFuture.supplyAsync(() -> pictureService.searchPicture(searchText, 1, 10));
		
		CompletableFuture<Page<UserVO>> userPageCompletableFuture = CompletableFuture.supplyAsync(() -> {
			UserQueryRequest userQueryRequest = new UserQueryRequest();
			userQueryRequest.setUserName(searchText);
			return userService.listUserVOByPage(userQueryRequest);
		});
		
		CompletableFuture<Page<PostVO>> postPpageCompletableFuture = CompletableFuture.supplyAsync(() -> {
			PostQueryRequest postQueryRequest = new PostQueryRequest();
			postQueryRequest.setSearchText(searchText);
			return postService.listPostVOPage(postQueryRequest, request);
		});
		
		CompletableFuture.allOf(userPageCompletableFuture, postPpageCompletableFuture, picturePageCompletableFuture).join();
		Page<UserVO> userVOPage = null;
		Page<PostVO> postVOPage = null;
		Page<Picture> picturePage = null;
		try {
			userVOPage = userPageCompletableFuture.get();
			postVOPage = postPpageCompletableFuture.get();
			picturePage = picturePageCompletableFuture.get();
		} catch (Exception e) {
			log.error("查询异常:{}", e);
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
		}
		SearchVO searchVO = new SearchVO();
		searchVO.setUserVOList(userVOPage.getRecords());
		searchVO.setPostVOList(postVOPage.getRecords());
		searchVO.setPictureList(picturePage.getRecords());
		
		
		return ResultUtils.success(searchVO);
	}
	
	
}
