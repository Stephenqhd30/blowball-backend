package com.stephen.popcorn.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.stephen.popcorn.model.entity.Post;
import com.stephen.popcorn.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取初始化文章列表
 *
 * @author stephen qiu
 */
// @Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {
	
	@Resource
	private PostService postService;
	
	@Override
	public void run(String... args) {
		// 1. 获取数据
		String json = "{\n" +
				"  \"sortField\": \"createTime\",\n" +
				"  \"sortOrder\": \"descend\",\n" +
				"  \"reviewStatus\": 1,\n" +
				"  \"current\": 1\n" +
				"}";
		String url = "https://api.code-nav.cn/api/post/list/page/vo";
		String result = HttpRequest
				.post(url)
				.body(json)
				.execute()
				.body();
		// 2. json转对象
		Map<String, Object> map = JSONUtil.toBean(result, Map.class);
		JSONObject data = (JSONObject) map.get("data");
		JSONArray records = (JSONArray) data.get("records");
		List<Post> postList = new ArrayList<>();
		
		for (Object record : records) {
			JSONObject tempRecord = (JSONObject) record;
			Post post = new Post();
			post.setTitle(tempRecord.getStr("title"));
			post.setContent(tempRecord.getStr("content"));
			JSONArray tags = (JSONArray) tempRecord.get("tags");
			List<String> tagList = tags.toList(String.class);
			post.setTags(JSONUtil.toJsonStr(tagList));
			post.setUserId(1782755477639864321L);
			postList.add(post);
		}
		// 数据入库
		boolean b = postService.saveBatch(postList);
		if (b) {
			log.info("初始化帖子列表成功, 条数 = {}", postList.size());
		} else {
			log.error("初始化帖子列表失败");
		}
		
	}
}

