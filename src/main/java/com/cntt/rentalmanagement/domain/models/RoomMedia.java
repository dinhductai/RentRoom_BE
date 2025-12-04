package com.cntt.rentalmanagement.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "room_media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String files;

    @Column(name = "media_type")
    private String mediaType; // "IMAGE" or "VIDEO"

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
}
