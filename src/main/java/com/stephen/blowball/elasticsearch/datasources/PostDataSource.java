package com.stephen.blowball.elasticsearch.datasources;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.blowball.elasticsearch.annotation.DataSourceType;
import com.stephen.blowball.elasticsearch.modal.dto.SearchRequest;
import com.stephen.blowball.elasticsearch.modal.enums.SearchTypeEnum;
import com.stephen.blowball.elasticsearch.service.PostEsService;
import com.stephen.blowball.model.dto.post.PostQueryRequest;
import com.stephen.blowball.model.entity.Post;
import com.stephen.blowball.model.vo.PostVO;
import com.stephen.blowball.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子服务实现
 *
 * @author stephen qiu
 */

@DataSourceType(SearchTypeEnum.POST)
@Component
@Slf4j
public class PostDataSource implements DataSource<PostVO> {
	
	@Resource
	private PostEsService postEsService;
	
	@Resource
	private PostService postService;
	
	/**
	 * 从ES中搜索帖子
	 *
	 * @param searchRequest 搜索条件
	 * @param request       request
	 * @return {@link Page {@link PostVO }}
	 */
	@Override
	public Page<PostVO> doSearch(SearchRequest searchRequest, HttpServletRequest request) {
		PostQueryRequest postQueryRequest = new PostQueryRequest();
		BeanUtils.copyProperties(searchRequest, postQueryRequest);
		Page<Post> postPage = postEsService.searchPostFromEs(postQueryRequest);
		return postService.getPostVOPage(postPage, request);
	}
}