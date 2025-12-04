package com.cntt.rentalmanagement.repository;

import com.cntt.rentalmanagement.domain.models.BlogStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogStoreRepositoryCustom {
    Page<BlogStore> getPageOfBlogStore(Long userId, Pageable pageable);
}
