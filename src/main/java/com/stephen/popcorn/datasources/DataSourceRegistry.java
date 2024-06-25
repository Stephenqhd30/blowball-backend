package com.stephen.popcorn.datasources;

import com.stephen.popcorn.model.enums.SearchTypeEnum;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: stephen qiu
 * @create: 2024-06-25 18:48
 **/
@Component
public class DataSourceRegistry {
	@Resource
	private PostDataSource postDataSource;
	
	@Resource
	private UserDataSource userDataSource;
	
	@Resource
	private PictureDataSource pictureDataSource;
	
	private Map<String, DataSource<T>> typeDataSourceMap;
	
	/**
	 * 初始化的时候执行一次
	 */
	@PostConstruct
	public void doInit() {
		// 注册数据源
		typeDataSourceMap = new HashMap() {{
			put(SearchTypeEnum.POST.getValue(), postDataSource);
			put(SearchTypeEnum.CONSUMER.getValue(), userDataSource);
			put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
		}};
	}
	
	
	
	public DataSource<T> getDataSourceByType(String type) {
		if (typeDataSourceMap == null) {
			return null;
		}
		return typeDataSourceMap.get(type);
	}
}
