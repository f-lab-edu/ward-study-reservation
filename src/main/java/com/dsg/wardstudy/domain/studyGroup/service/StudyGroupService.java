package com.dsg.wardstudy.domain.studyGroup.service;

import com.dsg.wardstudy.domain.studyGroup.dto.PageResponse;
import com.dsg.wardstudy.domain.studyGroup.dto.StudyGroupRequest;
import com.dsg.wardstudy.domain.studyGroup.dto.StudyGroupResponse;
import com.dsg.wardstudy.domain.user.entity.UserGroup;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyGroupService {

    StudyGroupResponse register(Long userId, StudyGroupRequest studyGroupRequest);

    StudyGroupResponse getById(Long studyGroupId);

    PageResponse.StudyGroupDetail getAll(Pageable pageable, String type, String keyword);

    Long updateById(Long userId, Long studyGroupId, StudyGroupRequest studyGroupRequest);

    void deleteById(Long userId, Long studyGroupId);

    List<StudyGroupResponse> getAllByUserId(Long userId);

    UserGroup participate(Long userId, Long studyGroupId);

    void like(Long userId, Long studyGroupId);

    int likeCount(Long studyGroupId);
}
