package com.dsg.wardstudy.service.studyGroup;

import com.dsg.wardstudy.dto.PageResponse;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupRequest;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyGroupService {

    StudyGroupResponse create(Long userId, StudyGroupRequest studyGroupRequest);

    StudyGroupResponse getById(Long studyGroupId);

    PageResponse.StudyGroup getAll(Pageable pageable);

    Long updateById(Long userId, Long studyGroupId, StudyGroupRequest studyGroupRequest);

    void deleteById(Long userId, Long studyGroupId);

    List<StudyGroupResponse> getAllByUserId(Long userId);
}
