package com.stephen.blowball.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stephen.blowball.model.entity.Picture;

/**
 * 帖子服务
 *
 * @author stephen qiu
 * 
 */
public interface PictureService {
    
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
