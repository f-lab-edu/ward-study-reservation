package com.dsg.wardstudy.service.studyGroup;

import com.dsg.wardstudy.dto.studyGroup.StudyGroupRequest;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupResponse;

import java.util.List;

public interface StudyGroupService {

    StudyGroupResponse create(Long userId, StudyGroupRequest studyGroupRequest);

    StudyGroupResponse getById(Long studyGroupId);
    List<StudyGroupResponse> getAll();

    Long updateById(Long userId, Long studyGroupId, StudyGroupRequest studyGroupRequest);

    void deleteById(Long studyGroupId);

    List<StudyGroupResponse> getAllByUserId(Long userId);
}
