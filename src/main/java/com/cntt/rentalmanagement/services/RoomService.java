package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.enums.RoomStatus;
import com.cntt.rentalmanagement.domain.models.DTO.CommentDTO;
import com.cntt.rentalmanagement.domain.models.Room;
import com.cntt.rentalmanagement.domain.models.User;
import com.cntt.rentalmanagement.domain.payload.request.RoomRequest;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import com.cntt.rentalmanagement.domain.payload.response.RoomResponse;

import java.util.List;

import org.springframework.data.domain.Page;

public interface RoomService {

    MessageResponse addNewRoom(RoomRequest roomRequest);

    Page<RoomResponse> getRoomByRentaler(String title, Integer pageNo, Integer pageSize);

    RoomResponse getRoomById(Long id);

    Room getRoom(Long id);

    MessageResponse disableRoom(Long id);

    MessageResponse updateRoomInfo(Long id, RoomRequest roomRequest);

    Page<RoomResponse> getRentOfHome();

    MessageResponse checkoutRoom(Long id);

    MessageResponse isApproveRoom(Long id);

    MessageResponse removeRoom(Long id);

    String addComment(Long id, CommentDTO commentDTO);

    List<CommentDTO> getAllCommentRoom(Long id);

    Page<RoomResponse> getAllRoomForAdmin(String title, Boolean approve, Integer pageNo, Integer pageSize);

    Page<RoomResponse> getRoomByUserId(Long userId, Integer pageNo, Integer pageSize);

    List<RoomResponse> getRoomByUser(User user);

    Room updateRoom(Room room, Long id);

    Page<RoomResponse> getHiredRoomsByRentaler(Integer pageNo, Integer pageSize);
}
