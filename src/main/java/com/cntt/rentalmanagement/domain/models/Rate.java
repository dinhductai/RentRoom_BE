package com.cntt.rentalmanagement.domain.models;

import com.cntt.rentalmanagement.domain.models.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rate extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Double rating;
    
    @OneToOne(mappedBy = "rate", cascade = CascadeType.ALL)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

	public Rate(Double rating, Comment comment, User user, Room room) {
		super();
		this.rating = rating;
		this.comment = comment;
		this.user = user;
		this.room = room;
	}
    
    
}
