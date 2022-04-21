package com.dsg.wardstudy.controller.studyGroup;

import com.dsg.wardstudy.dto.studyGroup.StudyGroupRequest;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupResponse;
import com.dsg.wardstudy.service.studyGroup.StudyGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    @PostMapping("/study-group")
    public ResponseEntity<StudyGroupResponse> create(@RequestBody StudyGroupRequest studyGroupRequest) {
        log.info("studyGroup create");
        return new ResponseEntity<>(studyGroupService.create(studyGroupRequest), HttpStatus.CREATED);
    }

    @GetMapping("/study-group/{id}")
    public ResponseEntity<StudyGroupResponse> getById(@PathVariable("id") Long studyGroupId) {
        log.info("studyGroup getById");
        return ResponseEntity.ok(studyGroupService.getById(studyGroupId));
    }

    @GetMapping("/study-group")
    public ResponseEntity<List<StudyGroupResponse>> getAll() {
        log.info("studyGroup getAll");
        return ResponseEntity.ok(studyGroupService.getAll());
    }

//    @GetMapping("/user/{id}/study-group/")
//    public ResponseEntity<List<StudyGroupResponse>> getAllByUserId(
//            @PathVariable("id") Long userId
//    ) {
//        log.info("studyGroup getAllByUserId");
//        return ResponseEntity.ok(studyGroupService.getAllByUserId(userId));
//    }

    @PutMapping("/study-group/{id}")
    public Long updateById(
            @PathVariable("id") Long studyGroupId,
            @RequestBody StudyGroupRequest studyGroupRequest) {
        log.info("studyGroup updateById");
        return studyGroupService.updateById(studyGroupId, studyGroupRequest);
    }

    @DeleteMapping("/study-group/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long studyGroupId) {
        log.info("studyGroup deleteById");
        studyGroupService.deleteById(studyGroupId);
        return new ResponseEntity<>("a study-group successfully deleted!", HttpStatus.OK);
    }
}
