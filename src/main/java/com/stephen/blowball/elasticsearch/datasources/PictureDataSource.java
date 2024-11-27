package com.stephen.blowball.elasticsearch.datasources;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.blowball.common.ErrorCode;
import com.stephen.blowball.common.exception.BusinessException;
import com.stephen.blowball.elasticsearch.annotation.DataSourceType;
import com.stephen.blowball.elasticsearch.modal.dto.SearchRequest;
import com.stephen.blowball.elasticsearch.modal.enums.SearchTypeEnum;
import com.stephen.blowball.model.entity.Picture;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 照片服务实现
 *
 * @author stephen qiu
 */
@Service
@Slf4j
@DataSourceType(SearchTypeEnum.PICTURE)
public class PictureDataSource implements DataSource<Picture> {
	
	@Override
	public Page<Picture> doSearch(SearchRequest searchRequest, HttpServletRequest request) {
		int pageNum = searchRequest.getCurrent();
		int pageSize = searchRequest.getPageSize();
		int searchSize = (pageNum - 1) * searchRequest.getPageSize();
		String searchText = searchRequest.getSearchText();
		String url = String.format("https://cn.bing.com/images/search?q=%s&first=%s", searchText, searchSize);
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
		}
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
			if (pictures.size() >= pageSize) {
				break;
			}
		}
		Page<Picture> picturePage = new Page<>(pageNum, pageSize);
		picturePage.setRecords(pictures);
		return picturePage;
	}
	
}