package com.dsg.wardstudy.domain.wish.service;

import com.dsg.wardstudy.common.exception.ErrorCode;
import com.dsg.wardstudy.common.exception.WSApiException;
import com.dsg.wardstudy.domain.wish.dto.WishDto;
import com.dsg.wardstudy.domain.wish.entity.Wish;
import com.dsg.wardstudy.domain.wish.naver.NaverClient;
import com.dsg.wardstudy.domain.wish.naver.dto.SearchImageReq;
import com.dsg.wardstudy.domain.wish.naver.dto.SearchImageRes;
import com.dsg.wardstudy.domain.wish.naver.dto.SearchLocalReq;
import com.dsg.wardstudy.domain.wish.naver.dto.SearchLocalRes;
import com.dsg.wardstudy.repository.wish.WishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService {

    private final NaverClient naverClient;
    private final WishRepository wishRepository;


    @Transactional
    @Override
    public WishDto search(String query) {
        // 지역 검색 -> 이미지 검색 -> 결과 리턴 순
        // 지역 검색
        SearchLocalReq searchLocalReq = new SearchLocalReq();
        searchLocalReq.setQuery(query);
        SearchLocalRes searchLocalRes = naverClient.searchLocal(searchLocalReq);

        if (searchLocalRes.getTotal() > 0) {
            SearchLocalRes.SearchLocalItem localItem = searchLocalRes.getItems().stream().findFirst().get();

            String imageQuery = localItem.getTitle().replaceAll("<[^>]*>", ""); // 괄호 다 삭제

            SearchImageReq searchImageReq = new SearchImageReq();
            searchImageReq.setQuery(imageQuery);

            // 이미지 검색
            SearchImageRes searchImageRes = naverClient.searchImage(searchImageReq);

            if (searchImageRes.getTotal() > 0) {

                SearchImageRes.SearchImageItem imageItem = searchImageRes.getItems().stream().findFirst().get();

                // 결과 리턴
                return WishDto.builder()
                        .title(localItem.getTitle())
                        .category(localItem.getCategory())
                        .address(localItem.getAddress())
                        .roadAddress(localItem.getRoadAddress())
                        .homePageLink(localItem.getLink())
                        .imageLink(imageItem.getLink())
                        .build();
            }

        }

        return new WishDto();
    }


    @Transactional
    @Override
    public WishDto add(WishDto wishDto) {

        Wish savedWish = wishRepository.save(Wish.of(wishDto));
        log.info("savedWish: {}", savedWish);

        return WishDto.mapToDto(savedWish);
    }

    @Transactional(readOnly = true)
    @Override
    public List<WishDto> getAll() {
        return wishRepository.findAll().stream()
                .map(WishDto::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void delete(Long wishId) {
        wishRepository.deleteById(wishId);
    }

    @Transactional
    @Override
    public void addVisit(Long wishId) {
        Wish findWish = wishRepository.findById(wishId)
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER, "no wish"));

        findWish.addVisit();

    }
}
