package com.dsg.wardstudy.domain.studyGroup.controller;

import com.dsg.wardstudy.common.auth.AuthUser;
import com.dsg.wardstudy.domain.studyGroup.dto.PageResponse;
import com.dsg.wardstudy.domain.studyGroup.dto.StudyGroupRequest;
import com.dsg.wardstudy.domain.studyGroup.dto.StudyGroupResponse;
import com.dsg.wardstudy.domain.studyGroup.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    // 스터디그룹 등록(등록한 유저는 자동 리더가 됨)
    @PostMapping("/study-group")
    public ResponseEntity<StudyGroupResponse> register(
            @RequestBody StudyGroupRequest studyGroupRequest,
            @AuthUser Long userId
    ) {
        log.info("studyGroup register, studyGroupRequest: {}", studyGroupRequest);
        log.info("studyGroup register, userId: {}", userId);
        return new ResponseEntity<>(studyGroupService.register(userId, studyGroupRequest), HttpStatus.CREATED);
    }

    // 스터디그룹 상세보기
    @GetMapping("/study-group/{studyGroupId}")
    public ResponseEntity<StudyGroupResponse> getById(
            @PathVariable("studyGroupId") Long studyGroupId,
            @AuthUser Long userId
    ) {
        log.info("studyGroup getById, studyGroupId: {}", studyGroupId);
        log.info("studyGroup getById, userId: {}", userId);
        return ResponseEntity.ok(studyGroupService.getById(studyGroupId));
    }

    // 스터디그룹 전체조회 paging 조건에 따른
    @GetMapping("/study-group/page")
    public ResponseEntity<PageResponse.StudyGroupDetail> getAllPage(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "keyword", required = false) String keyword
            ) {
        log.info("studyGroup getAll type: {}, keyword: {}", type, keyword);
        return ResponseEntity.ok(studyGroupService.getAll(pageable, type, keyword));
    }

    // 사용자가 참여한 스터디그룹 조회
    @GetMapping("/study-group")
    public ResponseEntity<List<StudyGroupResponse>> getAllByUserId(
            @AuthUser Long userId
    ) {
        log.info("studyGroup getAllByUserId, userId: {}", userId);
        return ResponseEntity.ok(studyGroupService.getAllByUserId(userId));
    }

    // 스터디그룹 수정(리더만)
    @PutMapping("/study-group/{studyGroupId}")
    public Long updateById(
            @PathVariable("studyGroupId") Long studyGroupId,
            @RequestBody StudyGroupRequest studyGroupRequest,
            @AuthUser Long userId
    ) {
        log.info("studyGroup updateById, userId: {}, studyGroupId: {}, ", userId, studyGroupId);
        return studyGroupService.updateById(userId, studyGroupId, studyGroupRequest);
    }

    // 스터디그룹 삭제(리더만)
    @DeleteMapping("/study-group/{studyGroupId}")
    public ResponseEntity<String> deleteById(
            @PathVariable("studyGroupId") Long studyGroupId,
            @AuthUser Long userId
    ) {
        log.info("studyGroup deleteById, userId: {}, studyGroupId: {}", userId, studyGroupId);
        studyGroupService.deleteById(userId, studyGroupId);
        return new ResponseEntity<>("a study-group successfully deleted!", HttpStatus.OK);
    }
}
