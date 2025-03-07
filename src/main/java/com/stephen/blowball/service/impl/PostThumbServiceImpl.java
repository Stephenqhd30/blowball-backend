package com.stephen.blowball.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stephen.blowball.common.ErrorCode;
import com.stephen.blowball.common.exception.BusinessException;
import com.stephen.blowball.mapper.PostThumbMapper;
import com.stephen.blowball.model.entity.Post;
import com.stephen.blowball.model.entity.PostThumb;
import com.stephen.blowball.model.entity.User;
import com.stephen.blowball.service.PostService;
import com.stephen.blowball.service.PostThumbService;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 帖子点赞服务实现
 *
 * @author stephen qiu
 */
@Service
public class PostThumbServiceImpl extends ServiceImpl<PostThumbMapper, PostThumb>
		implements PostThumbService {
	
	@Resource
	private PostService postService;
	
	/**
	 * 点赞
	 *
	 * @param postId    postId
	 * @param loginUser loginUser
	 * @return int
	 */
	@Override
	public int doPostThumb(long postId, User loginUser) {
		// 判断实体是否存在，根据类别获取实体
		Post post = postService.getById(postId);
		if (post == null) {
			throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
		}
		// 是否已点赞
		long userId = loginUser.getId();
		// 每个用户串行点赞
		// 锁必须要包裹住事务方法
		PostThumbService postThumbService = (PostThumbService) AopContext.currentProxy();
		synchronized (String.valueOf(userId).intern()) {
			return postThumbService.doPostThumbInner(userId, postId);
		}
	}
	
	/**
	 * 封装了事务的方法
	 *
	 * @param userId userId
	 * @param postId postId
	 * @return int
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public int doPostThumbInner(long userId, long postId) {
		PostThumb postThumb = new PostThumb();
		postThumb.setUserId(userId);
		postThumb.setPostId(postId);
		QueryWrapper<PostThumb> thumbQueryWrapper = new QueryWrapper<>(postThumb);
		PostThumb oldPostThumb = this.getOne(thumbQueryWrapper);
		boolean result;
		// 已点赞
		if (oldPostThumb != null) {
			result = this.remove(thumbQueryWrapper);
			if (result) {
				// 点赞数 - 1
				result = postService.update()
						.eq("id", postId)
						.gt("thumbNum", 0)
						.setSql("thumbNum = thumbNum - 1")
						.update();
				return result ? -1 : 0;
			} else {
				throw new BusinessException(ErrorCode.SYSTEM_ERROR);
			}
		} else {
			// 未点赞
			result = this.save(postThumb);
			if (result) {
				// 点赞数 + 1
				result = postService.update()
						.eq("id", postId)
						.setSql("thumbNum = thumbNum + 1")
						.update();
				return result ? 1 : 0;
			} else {
				throw new BusinessException(ErrorCode.SYSTEM_ERROR);
			}
		}
	}
	
	/**
	 * 分页获取用户点赞的帖子列表
	 *
	 * @param page         page
	 * @param queryWrapper queryWrapper
	 * @param thumbUserId thumbUserId
	 * @return {@link Page <Post>}
	 */
	@Override
	public Page<Post> listThumbPostByPage(IPage<Post> page, Wrapper<Post> queryWrapper, long thumbUserId) {
		if (thumbUserId <= 0) {
			return new Page<>();
		}
		return baseMapper.listThumbPostByPage(page, queryWrapper, thumbUserId);
	}
	
}




