package com.stephen.blowball.model.enums.search;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索类型枚举
 *
 * @author stephen qiu
 */
public enum SearchTypeEnum {
	
	POST("帖子", "post"),
	CONSUMER("用户", "consumer"),
	PICTURE("图片", "picture");
	
	private final String text;
	
	private final String value;
	
	SearchTypeEnum(String text, String value) {
		this.text = text;
		this.value = value;
	}
	
	/**
	 * 获取值列表
	 *
	 * @return
	 */
	public static List<String> getValues() {
		return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
	}
	
	/**
	 * 根据 value 获取枚举
	 *
	 * @param value
	 * @return
	 */
	public static SearchTypeEnum getEnumByValue(String value) {
		if (ObjectUtils.isEmpty(value)) {
			return null;
		}
		for (SearchTypeEnum anEnum : SearchTypeEnum.values()) {
			if (anEnum.value.equals(value)) {
				return anEnum;
			}
		}
		return null;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getText() {
		return text;
	}
}
