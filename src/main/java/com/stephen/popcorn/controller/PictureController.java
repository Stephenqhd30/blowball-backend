package com.stephen.popcorn.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.popcorn.common.BaseResponse;
import com.stephen.popcorn.common.ErrorCode;
import com.stephen.popcorn.common.ResultUtils;
import com.stephen.popcorn.exception.ThrowUtils;
import com.stephen.popcorn.model.dto.picture.PictureQueryRequest;
import com.stephen.popcorn.model.entity.Picture;
import com.stephen.popcorn.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子接口
 *
 * @author stephen qiu
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {
	
	@Resource
	private PictureService pictureService;
	
	/**
	 * 分页获取列表（封装类）
	 *
	 * @param pictureQueryRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/list/page/vo")
	public BaseResponse<Page<Picture>> listPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
	                                                     HttpServletRequest request) {
		long current = pictureQueryRequest.getCurrent();
		long size = pictureQueryRequest.getPageSize();
		String searchText = pictureQueryRequest.getSearchText();
		// 限制爬虫
		ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
		Page<Picture> pictures = pictureService.searchPicture(searchText, current, size);
		
		return ResultUtils.success(pictures);
	}
	
	
	
}
