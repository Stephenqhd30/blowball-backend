package com.stephen.blowball.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stephen.blowball.model.entity.Post;
import com.stephen.blowball.model.entity.PostThumb;
import com.stephen.blowball.model.entity.User;

/**
 * 帖子点赞服务
 *
 * @author stephen qiu
 */
public interface PostThumbService extends IService<PostThumb> {
	
	/**
	 * 点赞
	 *
	 * @param postId
	 * @param loginUser
	 * @return
	 */
	int doPostThumb(long postId, User loginUser);
	
	/**
	 * 帖子点赞（内部服务）
	 *
	 * @param userId
	 * @param postId
	 * @return
	 */
	int doPostThumbInner(long userId, long postId);
	
	/**
	 * 分页获取用户点赞的帖子列表
	 *
	 * @param page         page
	 * @param queryWrapper queryWrapper
	 * @param thumbUserId thumbUserId
	 * @return {@link Page <Post>}
	 */
	Page<Post> listThumbPostByPage(IPage<Post> page, Wrapper<Post> queryWrapper, long thumbUserId);
}
