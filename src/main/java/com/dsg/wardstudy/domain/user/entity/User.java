package com.dsg.wardstudy.domain.user.entity;

import com.dsg.wardstudy.domain.BaseTimeEntity;
import com.dsg.wardstudy.domain.reservation.entity.Reservation;
import com.dsg.wardstudy.domain.user.dto.LoginDto;
import com.dsg.wardstudy.domain.user.dto.SignUpRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.dsg.wardstudy.common.utils.Encryptor.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@ToString(of = {"id", "name", "nickname", "email", "isDeleted"})
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String nickname;
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 200, nullable = false)
    private String password;

    @Column(name = "deleted", columnDefinition="bit default 0")
    private boolean isDeleted;
    @Column(name = "delete_date")
    private LocalDateTime deleteDate;

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

    public static User of(LoginDto loginDto) {
        return User.builder()
                .id(loginDto.getId())
                .email(loginDto.getEmail())
                .name(loginDto.getName())
                .password(loginDto.getPassword())
                .build();
    }

    public static User of(SignUpRequest signUpRequest) {
        return User.builder()
                .email(signUpRequest.getEmail())
                .name(signUpRequest.getName())
                .nickname(signUpRequest.getNickname())
                .password(encrypt(signUpRequest.getPassword()))
                .build();
    }

    public void withdrawUser(boolean isDeleted) {
        this.isDeleted = isDeleted;
        this.deleteDate = LocalDateTime.now();
    }
}
