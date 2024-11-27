package com.stephen.blowball.elasticsearch.mapper;

import com.stephen.blowball.elasticsearch.modal.entity.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 帖子 ES 操作
 *
 * @author stephen qiu
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {
	
	List<PostEsDTO> findByUserId(Long userId);
}