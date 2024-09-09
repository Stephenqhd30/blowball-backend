package com.stephen.blowball.crawler;
import java.io.IOException;
import java.util.ArrayList;

import cn.hutool.core.lang.Assert;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.stephen.blowball.model.entity.Picture;
import com.stephen.blowball.model.entity.Post;
import com.stephen.blowball.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: stephen qiu
 * @create: 2024-05-25 15:59
 **/
@SpringBootTest
public class CrawlerTest {
	
	@Resource
	private PostService postService;
	
	@Test
	void testFetchPicture() throws IOException {
		int current = 1;
		String url = "https://cn.bing.com/images/search?q=Curry&first=" + current;
		Document doc = Jsoup.connect(url).get();
		Elements elements = doc.select(".iuscp.isv");
		List<Picture> pictures = new ArrayList<>();
		for (Element element : elements) {
			// 先去出来图片的地址(murl)
			String m = element.select(".iusc").get(0).attr("m");
			Map<String, Object> map = JSONUtil.toBean(m, Map.class);
			String murl = (String) map.get("murl");
			// 取标题
			String title = element.select(".inflnk").get(0).attr("aria-label");
			Picture picture = new Picture();
			picture.setTitle(title);
			picture.setUrl(murl);
			pictures.add(picture);
		}
		System.out.println(pictures);
	}
	
	
	@Test
	void testFetchPassage() {
		// 1. 获取数据
		String json = "{\n" +
				"  \"sortField\": \"createTime\",\n" +
				"  \"sortOrder\": \"descend\",\n" +
				"  \"reviewStatus\": 1,\n" +
				"  \"current\": 2\n" +
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
		Assert.isTrue(b);
		
	}
}
