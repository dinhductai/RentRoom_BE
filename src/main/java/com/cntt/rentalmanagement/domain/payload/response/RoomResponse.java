package com.cntt.rentalmanagement.domain.payload.response;

import com.cntt.rentalmanagement.domain.enums.RoomStatus;
import com.cntt.rentalmanagement.domain.payload.request.AssetRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RoomResponse {
    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private Double latitude;

    private Double longitude;

    private String address;

    private String status;

    private String isLocked;

    private Boolean isApprove;

    private Boolean isRemove;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private LocationResponse location;

    private CategoryResponse category;

    private List<AssetResponse> assets;

    private List<RoomMediaResponse> roomMedia;

    private UserResponse user;
    private BigDecimal waterCost;
    private BigDecimal publicElectricCost;
    private BigDecimal internetCost;
}
