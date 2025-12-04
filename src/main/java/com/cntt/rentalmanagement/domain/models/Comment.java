package com.cntt.rentalmanagement.domain.models;

import com.cntt.rentalmanagement.domain.models.audit.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String content;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room rooms;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rate_id")
    private Rate rate;

	public Comment(String content, User user, Room rooms, Rate rate) {
		super();
		this.content = content;
		this.user = user;
		this.rooms = rooms;
		this.rate = rate;
	}
    
    
}
