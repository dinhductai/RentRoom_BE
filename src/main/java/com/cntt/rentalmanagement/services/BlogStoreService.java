package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.payload.request.BlogStoreRequest;
import com.cntt.rentalmanagement.domain.payload.response.BlogStoreResponse;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import org.springframework.data.domain.Page;

public interface BlogStoreService {
    MessageResponse saveBlog(BlogStoreRequest storeRequest);

    Page<BlogStoreResponse> getPageOfBlog(Integer pageNo, Integer pageSize);
}
