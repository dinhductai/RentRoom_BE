package com.cntt.rentalmanagement.services;

import com.cntt.rentalmanagement.domain.payload.response.MaintenanceResponse;
import com.cntt.rentalmanagement.domain.payload.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface MaintenanceService {
        Page<MaintenanceResponse> getAllMaintenance(String keyword, Integer pageNo, Integer pageSize);

        MessageResponse addNewMaintenance(String maintenanceDate, BigDecimal price, Long roomId, List<MultipartFile> files);

        MessageResponse editMaintenance(Long id, String maintenanceDate, BigDecimal price,  Long roomId, List<MultipartFile> files);

        MessageResponse deleteMaintenance(Long id);

        MaintenanceResponse getMaintenance(Long id);
}
