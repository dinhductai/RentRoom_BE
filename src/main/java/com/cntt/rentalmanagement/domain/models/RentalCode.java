package com.cntt.rentalmanagement.domain.models;

import com.cntt.rentalmanagement.domain.models.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental_code")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentalCode extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length = 10)
    private String code;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_used")
    private Boolean isUsed = false;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;
}
