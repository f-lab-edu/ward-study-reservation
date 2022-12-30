package com.dsg.wardstudy.domain.wish.entity;

import com.dsg.wardstudy.domain.wish.dto.WishDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "wish")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_id")
    private Long id;

    private String title;                   // 음식명, 장소명
    private String category;                // 카테고리
    private String address;                 // 주소

    @Column(name = "road_address")
    private String roadAddress;             // 도로명

    @Column(name = "home_page_link")
    private String homePageLink;            // 홈페이지 주소

    @Column(name = "image_link")
    private String imageLink;               // 음식, 가게 이미지 주소

    @Column(name = "is_visit")
    private boolean isVisit;                // 방문여부

    @Column(name = "visit_count")
    private int visitCount;                 // 방문 카운트

    @Column(name = "last_visit_date")
    private LocalDateTime lastVisitDate;    // 마지막 방문일자


    @Builder
    public Wish(Long id, String title, String category, String address, String roadAddress, String homePageLink, String imageLink, boolean isVisit, int visitCount, LocalDateTime lastVisitDate) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.address = address;
        this.roadAddress = roadAddress;
        this.homePageLink = homePageLink;
        this.imageLink = imageLink;
        this.isVisit = isVisit;
        this.visitCount = visitCount;
        this.lastVisitDate = lastVisitDate;
    }

    public static Wish of(WishDto wishDto) {
        return Wish.builder()
            .id(wishDto.getWishId())
            .title(wishDto.getTitle())
            .category(wishDto.getCategory())
            .address(wishDto.getAddress())
            .roadAddress(wishDto.getRoadAddress())
            .homePageLink(wishDto.getHomePageLink())
            .imageLink(wishDto.getImageLink())
            .isVisit(wishDto.isVisit())
            .visitCount(wishDto.getVisitCount())
            .lastVisitDate(wishDto.getLastVisitDate())
        .build();
    }

    public void addVisit() {
        this.isVisit = true;
        this.visitCount = this.visitCount + 1;
    }
}
