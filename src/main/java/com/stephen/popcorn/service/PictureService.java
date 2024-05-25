package com.stephen.popcorn.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.popcorn.model.entity.Picture;

/**
 * 帖子服务
 *
 * @author stephen qiu
 * 
 */
public interface PictureService {
    
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
