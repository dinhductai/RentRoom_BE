package com.cntt.rentalmanagement.domain.models;

import com.cntt.rentalmanagement.domain.models.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contract extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String files;
    @Column(name = "name_of_rent")
    private String nameOfRent;
    @Column(name = "deadline_contract")
    private LocalDateTime deadlineContract;
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "num_of_people")
    private Long numOfPeople;

    private String phone;

    @Column(name = "rental_code")
    private String rentalCode;

    @ManyToOne
    @JoinColumn(name = "renter_user_id")
    private User renterUser;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public Contract(String name, String files, String nameOfRent, String deadlineContract, String createdBy,
            String updatedBy, Room room) {
        this.name = name;
        this.files = files;
        this.nameOfRent = nameOfRent;
        this.numOfPeople = numOfPeople;
        this.phone = phone;
        this.deadlineContract = LocalDateTime.parse(deadlineContract);
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.room = room;
    }
}
