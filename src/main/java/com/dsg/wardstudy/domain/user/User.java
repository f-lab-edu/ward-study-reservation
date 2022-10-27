package com.dsg.wardstudy.domain.user;

import com.dsg.wardstudy.domain.BaseTimeEntity;
import com.dsg.wardstudy.domain.reservation.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@ToString(of = {"id", "name", "nickname", "email"})
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String nickname;
    private String email;
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserGroup> userGroups = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @Builder
    public User(Long id, String name, String nickname, String email, String password) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
