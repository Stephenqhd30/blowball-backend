package com.stephen.blowball.esdao;

import com.stephen.blowball.model.dto.post.PostEsDTO;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 帖子 ES 操作
 *
 * @author stephen qiu
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {
	
	List<PostEsDTO> findByUserId(Long userId);
	
	List<PostEsDTO> findByTitle(String title);
}