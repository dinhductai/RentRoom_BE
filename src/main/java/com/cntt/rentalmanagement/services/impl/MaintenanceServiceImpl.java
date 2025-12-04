package com.cntt.rentalmanagement.services.impl;

import com.cntt.rentalmanagement.domain.enums.LockedStatus;
import com.cntt.rentalmanagement.domain.models.Maintenance;
import com.cntt.rentalmanagement.domain.models.Room;
import com.cntt.rentalmanagement.domain.payload.response.MaintenanceResponse;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import com.cntt.rentalmanagement.exception.BadRequestException;
import com.cntt.rentalmanagement.repository.MaintenanceRepository;
import com.cntt.rentalmanagement.repository.RoomRepository;
import com.cntt.rentalmanagement.services.BaseService;
import com.cntt.rentalmanagement.services.FileStorageService;
import com.cntt.rentalmanagement.services.MaintenanceService;
import com.cntt.rentalmanagement.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl extends BaseService implements MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final MapperUtils mapperUtils;
    private final RoomRepository roomRepository;
    private final FileStorageService fileStorageService;

    @Override
    public Page<MaintenanceResponse> getAllMaintenance(String keyword, Integer pageNo, Integer pageSize) {
        int page = pageNo == 0 ? pageNo : pageNo - 1;
        Pageable pageable = PageRequest.of(page, pageSize);
        return mapperUtils.convertToResponsePage(
                maintenanceRepository.searchingMaintenance(keyword, getUserId(), pageable), MaintenanceResponse.class,
                pageable);
    }

    @Override
    public MessageResponse addNewMaintenance(String maintenanceDate, BigDecimal price, Long roomId,
            List<MultipartFile> files) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BadRequestException("Phòng đã không tồn tại"));

        String fileName = fileStorageService.storeFile(files.get(0));
        Maintenance maintenance = new Maintenance(LocalDateTime.parse(maintenanceDate), price, fileName, getUsername(),
                getUsername(), room);
        maintenanceRepository.save(maintenance);
        return MessageResponse.builder().message("Thêm phiếu bảo trì thành công").build();
    }

    @Override
    public MessageResponse editMaintenance(Long id, String maintenanceDate, BigDecimal price, Long roomId,
            List<MultipartFile> files) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BadRequestException("Phòng đã không tồn tại"));

        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Phiếu bảo trì không tồn tại"));
        maintenance.setMaintenanceDate(LocalDateTime.parse(maintenanceDate));
        maintenance.setPrice(price);
        if (Objects.nonNull(files.get(0))) {
            String file = fileStorageService.storeFile(files.get(0)).replace("photographer/files/", "");
            maintenance.setFiles(file);
        }
        maintenance.setRoom(room);
        maintenanceRepository.save(maintenance);
        return MessageResponse.builder().message("Cập nhật thành công").build();
    }

    @Override
    public MessageResponse deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
        return MessageResponse.builder().message("Xóa phiếu bảo trì thành công").build();
    }

    @Override
    public MaintenanceResponse getMaintenance(Long id) {
        return mapperUtils.convertToResponse(
                maintenanceRepository.findById(id).orElseThrow(() -> new BadRequestException("Không tồn tại")),
                MaintenanceResponse.class);
    }

}
