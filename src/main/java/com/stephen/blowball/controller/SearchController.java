package com.stephen.blowball.controller;

import com.stephen.blowball.common.BaseResponse;
import com.stephen.blowball.common.ResultUtils;
import com.stephen.blowball.manager.SearchFacade;
import com.stephen.blowball.model.dto.search.SearchRequest;
import com.stephen.blowball.model.vo.SearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
	
	/**
	 * 使用门面模式进行重构
	 * 聚合搜索查询
	 *
	 * @param searchRequest
	 * @return
	 */
	@PostMapping("/all")
	public BaseResponse<SearchVO> doSearchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
		return ResultUtils.success(searchFacade.searchAll(searchRequest, request));
	}
}
