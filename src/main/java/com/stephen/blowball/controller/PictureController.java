package com.stephen.blowball.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.blowball.common.BaseResponse;
import com.stephen.blowball.common.ErrorCode;
import com.stephen.blowball.common.ResultUtils;
import com.stephen.blowball.common.ThrowUtils;
import com.stephen.blowball.model.dto.picture.PictureQueryRequest;
import com.stephen.blowball.model.entity.Picture;
import com.stephen.blowball.service.PictureService;
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
