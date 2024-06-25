package com.stephen.popcorn.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.datasources.*;
import com.stephen.popcorn.exception.BusinessException;
import com.stephen.popcorn.exception.ThrowUtils;
import com.stephen.popcorn.model.dto.picture.PictureQueryRequest;
import com.stephen.popcorn.model.dto.post.PostQueryRequest;
import com.stephen.popcorn.model.dto.search.SearchRequest;
import com.stephen.popcorn.model.dto.user.UserQueryRequest;
import com.stephen.popcorn.model.entity.Picture;
import com.stephen.popcorn.model.enums.SearchTypeEnum;
import com.stephen.popcorn.model.vo.PostVO;
import com.stephen.popcorn.model.vo.SearchVO;
import com.stephen.popcorn.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * @author: stephen qiu
 * @create: 2024-06-13 16:01
 **/
@Component
@Slf4j
public class SearchFacade {
	
	@Resource
	private PostDataSource postDataSource;
	
	@Resource
	private UserDataSource userDataSource;
	
	@Resource
	private PictureDataSource pictureDataSource;
	
	@Resource
	private DataSourceRegistry dataSourceRegistry;
	
	
	/**
	 * 聚合搜索查询
	 *
	 * @param searchRequest
	 * @return
	 */
	public SearchVO searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
		// 先对 type 进行判断
		String type = searchRequest.getType();
		SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
		ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
		String searchText = searchRequest.getSearchText();
		int current = searchRequest.getCurrent();
		int pageSize = searchRequest.getPageSize();
		// 如果 type 为空
		if (searchTypeEnum == null) {
			// 加入并发
			CompletableFuture<Page<UserVO>> userPageCompletableFuture = CompletableFuture.supplyAsync(() -> {
				UserQueryRequest userQueryRequest = new UserQueryRequest();
				userQueryRequest.setUserName(searchText);
				return userDataSource.doSearch(searchText, current, pageSize);
			});
			
			CompletableFuture<Page<PostVO>> postPpageCompletableFuture = CompletableFuture.supplyAsync(() -> {
				PostQueryRequest postQueryRequest = new PostQueryRequest();
				postQueryRequest.setSearchText(searchText);
				return postDataSource.doSearch(searchText, current, pageSize);
			});
			CompletableFuture<Page<Picture>> picturePageCompletableFuture = CompletableFuture.supplyAsync(() -> {
				PictureQueryRequest pictureQueryRequest = new PictureQueryRequest();
				pictureQueryRequest.setSearchText(searchText);
				return pictureDataSource.doSearch(searchText, current, pageSize);
			});
			
			CompletableFuture.allOf(userPageCompletableFuture, postPpageCompletableFuture, picturePageCompletableFuture).join();
			try {
				Page<UserVO> userVOPage = userPageCompletableFuture.get();
				Page<PostVO> postVOPage  = postPpageCompletableFuture.get();
				Page<Picture> picturePage = picturePageCompletableFuture.get();
				SearchVO searchVO = new SearchVO();
				searchVO.setUserVOList(userVOPage.getRecords());
				searchVO.setPostVOList(postVOPage.getRecords());
				searchVO.setPictureList(picturePage.getRecords());
				return searchVO;
			} catch (Exception e) {
				log.error("查询异常:{}", e);
				throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
			}
			
		} else {
			SearchVO searchVO = new SearchVO();
			DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
			Page<?> page = dataSource.doSearch(searchText, current, pageSize);
			searchVO.setDateList(page.getRecords());
			return searchVO;
		}
	}
}
