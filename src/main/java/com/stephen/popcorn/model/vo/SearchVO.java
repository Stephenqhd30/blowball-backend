package com.stephen.popcorn.model.vo;

import com.stephen.popcorn.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: stephen qiu
 * @create: 2024-05-26 20:11
 **/
@Data
public class SearchVO implements Serializable {
	private static final long serialVersionUID = 9065946273183024389L;
	
	/**
	 * 用户
	 */
	private List<UserVO> userVOList;
	
	/**
	 * 帖子
	 */
	private List<PostVO> postVOList;
	
	/**
	 * 图片
	 */
	private List<Picture> pictureList;
}
