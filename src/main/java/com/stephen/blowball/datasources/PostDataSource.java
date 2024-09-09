package com.stephen.blowball.datasources;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.blowball.model.dto.post.PostQueryRequest;
import com.stephen.blowball.model.vo.PostVO;
import com.stephen.blowball.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子服务实现
 *
 * @author stephen qiu
 */
@Service
@Slf4j
public class PostDataSource implements DataSource<PostVO> {
	
	@Resource
	private PostService postService;
	
	@Override
	public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize) {
		PostQueryRequest postQueryRequest = new PostQueryRequest();
		postQueryRequest.setSearchText(searchText);
		postQueryRequest.setCurrent((int)pageNum);
		postQueryRequest.setPageSize((int)pageSize);
		HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
		return postService.listPostVOPage(postQueryRequest, request);
	}
}