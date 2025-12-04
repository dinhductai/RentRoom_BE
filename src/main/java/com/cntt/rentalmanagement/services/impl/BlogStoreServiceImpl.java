package com.cntt.rentalmanagement.services.impl;

import com.cntt.rentalmanagement.domain.models.BlogStore;
import com.cntt.rentalmanagement.domain.models.Room;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.payload.request.BlogStoreRequest;
import com.cntt.rentalmanagement.domain.payload.response.BlogStoreResponse;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import com.cntt.rentalmanagement.exception.BadRequestException;
import com.cntt.rentalmanagement.repository.BlogStoreRepository;
import com.cntt.rentalmanagement.repository.RoomRepository;
import com.cntt.rentalmanagement.repository.UserRepository;
import com.cntt.rentalmanagement.services.BaseService;
import com.cntt.rentalmanagement.services.BlogStoreService;
import com.cntt.rentalmanagement.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogStoreServiceImpl extends BaseService implements BlogStoreService {
    private final BlogStoreRepository blogStoreRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MapperUtils mapperUtils;
    @Override
    public MessageResponse saveBlog(BlogStoreRequest storeRequest) {
        User customer = userRepository.findById(getUserId()).orElseThrow(() -> new BadRequestException("Tài khoảng không tồn tại"));
        Room room = roomRepository.findById(storeRequest.getRoomId()).orElseThrow(() -> new BadRequestException("Thông tin phòng không tồn tại."));
        Optional<BlogStore> blogStore = blogStoreRepository.findByRoomAndUser(room, customer);
        if (blogStore.isPresent()) {
            throw new BadRequestException("Bài đăng đã được lưu.");
        }
        BlogStore blogStore1 = new BlogStore();
        blogStore1.setRoom(room);
        blogStore1.setUser(customer);
        blogStoreRepository.save(blogStore1);
        return MessageResponse.builder().message("Đã lưu bài đăng thành công.").build();
    }

    @Override
    public Page<BlogStoreResponse> getPageOfBlog(Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(blogStoreRepository.getPageOfBlogStore(getUserId(), pageable),BlogStoreResponse.class, pageable);
    }
}
