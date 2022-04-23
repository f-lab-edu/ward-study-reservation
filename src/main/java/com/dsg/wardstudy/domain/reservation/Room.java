package com.dsg.wardstudy.domain.reservation;

import com.dsg.wardstudy.dto.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();


}
