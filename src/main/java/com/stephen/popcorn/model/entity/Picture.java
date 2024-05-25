package com.stephen.popcorn.model.entity;

import lombok.Data;

/**
 * 图片
 * @author: stephen qiu
 * @create: 2024-05-25 17:58
 **/
@Data
public class Picture {
	/**
	 * 图片的标题
	 */
	private String title;
	
	/**
	 * 图片的地址
	 */
	private String url;
}
