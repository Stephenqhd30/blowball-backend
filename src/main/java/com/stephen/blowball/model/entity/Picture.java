package com.stephen.blowball.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图片
 * @author: stephen qiu
 * @create: 2024-05-25 17:58
 **/
@Data
public class Picture implements Serializable {
	private static final long serialVersionUID = -6915687534756244316L;
	/**
	 * 图片的标题
	 */
	private String title;
	
	/**
	 * 图片的地址
	 */
	private String url;
}
