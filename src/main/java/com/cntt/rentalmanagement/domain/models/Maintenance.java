package com.cntt.rentalmanagement.domain.models;

import com.cntt.rentalmanagement.domain.models.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Maintenance extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime maintenanceDate;
    private BigDecimal price;
    private String files;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Maintenance(LocalDateTime maintenanceDate, BigDecimal price, String files, String createdBy, String updatedBy, Room room) {
        this.maintenanceDate = maintenanceDate;
        this.price = price;
        this.files = files;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.room = room;
    }
}
