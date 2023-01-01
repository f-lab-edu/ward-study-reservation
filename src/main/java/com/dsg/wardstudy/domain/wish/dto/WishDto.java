package com.dsg.wardstudy.domain.wish.dto;

import com.dsg.wardstudy.domain.wish.entity.Wish;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class WishDto {

    private Long wishId;
    private String title;                   // 스터디카페명
    private String category;                // 카테고리
    private String address;                 // 주소
    private String roadAddress;             // 도로명
    private String homePageLink;            // 홈페이지 주소
    private String imageLink;               // 이미지 주소
    private boolean isVisit;                // 방문여부
    private int visitCount;                 // 방문 카운트
    private LocalDateTime lastVisitDate;    // 마지막 방문일자

    @Builder
    public WishDto(Long wishId, String title, String category, String address, String roadAddress, String homePageLink, String imageLink, boolean isVisit, int visitCount, LocalDateTime lastVisitDate) {
        this.wishId = wishId;
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

    public static WishDto mapToDto(Wish wish) {
        return WishDto.builder()
                .wishId(wish.getId())
                .title(wish.getTitle())
                .category(wish.getCategory())
                .address(wish.getAddress())
                .roadAddress(wish.getRoadAddress())
                .homePageLink(wish.getHomePageLink())
                .imageLink(wish.getImageLink())
                .isVisit(wish.isVisit())
                .visitCount(wish.getVisitCount())
                .lastVisitDate(wish.getLastVisitDate())
                .build();
    }
}
