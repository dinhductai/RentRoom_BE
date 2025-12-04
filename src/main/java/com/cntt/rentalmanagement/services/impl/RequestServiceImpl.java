package com.cntt.rentalmanagement.services.impl;

import com.cntt.rentalmanagement.domain.models.Request;
import com.cntt.rentalmanagement.domain.models.Room;
import com.cntt.rentalmanagement.domain.payload.request.RequestRequest;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import com.cntt.rentalmanagement.domain.payload.response.RequireResponse;
import com.cntt.rentalmanagement.exception.BadRequestException;
import com.cntt.rentalmanagement.repository.RequestRepository;
import com.cntt.rentalmanagement.repository.RoomRepository;
import com.cntt.rentalmanagement.services.BaseService;
import com.cntt.rentalmanagement.services.RequestService;
import com.cntt.rentalmanagement.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl extends BaseService implements RequestService {

    private final RequestRepository requestRepository;
    private final RoomRepository roomRepository;
    private final MapperUtils mapperUtils;
    @Override
    public Page<RequireResponse> getRequestOfRentHome(String keyword, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(requestRepository.searchingOfRequest(keyword,getUserId(),pageable),RequireResponse.class, pageable);
    }

    @Override
    public MessageResponse changeStatusOfRequest(Long id) {
        Request request = requestRepository.findById(id).orElseThrow(() -> new BadRequestException("Yêu cầu này không tồn tại"));
        request.setIsAnswer(Boolean.TRUE);
        requestRepository.save(request);
        return MessageResponse.builder().message("Yêu cầu đã được xử lý").build();
    }

    @Override
    public RequireResponse getRequest(Long id) {
        return mapperUtils.convertToResponse(requestRepository.findById(id).orElseThrow(() -> new BadRequestException("Yêu cầu này không tồn tại")), RequireResponse.class);
    }

    @Override
    public Page<RequireResponse> getRequestOfCustomer(String keyword, String phone, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(requestRepository.searchingOfRequest(keyword,phone,pageable),RequireResponse.class, pageable);
    }

    @Override
    public MessageResponse addRequest(RequestRequest request) {
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() -> new BadRequestException("Thông tin phòng không tồn tại."));
        Request result = new Request(request.getNameOfRent(),request.getPhone(),request.getDescription(), room);
        result.setIsAnswer(false);
        requestRepository.save(result);
        return MessageResponse.builder().message("Gửi yêu cầu thành công.").build();
    }
}
