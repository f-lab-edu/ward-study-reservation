package com.dsg.wardstudy.domain.wish.controller;

import com.dsg.wardstudy.domain.wish.dto.WishDto;
import com.dsg.wardstudy.domain.wish.service.WishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class WishController {

    private final WishService wishService;

    @GetMapping("/wish/search")
    public WishDto search(@RequestParam String query) {
        return wishService.search(query);
    }

    @PostMapping("/wish")
    public WishDto add(@RequestBody WishDto wishDto){
        log.info("{}", wishDto);
        return wishService.add(wishDto);
    }

    @GetMapping("/wish")
    public List<WishDto> getAll() {
        return wishService.getAll();
    }

    @DeleteMapping("/wish/{wishId}")
    public void delete(@PathVariable long wishId){
        wishService.delete(wishId);
    }

    @PostMapping("/wish/{wishId}")
    public void addVisit(@PathVariable long wishId) {
        wishService.addVisit(wishId);
    }

}
