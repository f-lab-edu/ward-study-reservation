package com.dsg.wardstudy.controller;

import com.dsg.wardstudy.dto.StudyGroupRequest;
import com.dsg.wardstudy.dto.StudyGroupResponse;
import com.dsg.wardstudy.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/study-group")
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    @PostMapping
    public ResponseEntity<StudyGroupResponse> create(@RequestBody StudyGroupRequest studyGroupRequest) {
        log.info("studyGroup create");
        return new ResponseEntity<>(studyGroupService.create(studyGroupRequest), HttpStatus.CREATED);
    }
}
