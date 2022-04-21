package com.dsg.wardstudy.controller.studyGroup;

import com.dsg.wardstudy.dto.studyGroup.StudyGroupRequest;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupResponse;
import com.dsg.wardstudy.service.studyGroup.StudyGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("{id}")
    public ResponseEntity<StudyGroupResponse> getById(@PathVariable("id") Long groupId) {
        log.info("studyGroup getById");
        return ResponseEntity.ok(studyGroupService.getById(groupId));
    }
}
