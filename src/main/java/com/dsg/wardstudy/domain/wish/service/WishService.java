package com.dsg.wardstudy.domain.wish.service;

import com.dsg.wardstudy.domain.wish.dto.WishDto;

import java.util.List;

public interface WishService {

    WishDto search(String query);

    WishDto add(WishDto wish);

    List<WishDto> getAll();

    void delete(Long wishId);

    void addVisit(Long wishId);

}
