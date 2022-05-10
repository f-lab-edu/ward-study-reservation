package com.dsg.wardstudy.service.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupRequest;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupResponse;
import com.dsg.wardstudy.exception.ErrorCode;
import com.dsg.wardstudy.exception.ResourceNotFoundException;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public StudyGroupResponse create(StudyGroupRequest studyGroupRequest) {

        StudyGroup studyGroup = mapToEntity(studyGroupRequest);
        StudyGroup savedGroup = studyGroupRepository.save(studyGroup);

        return mapToDto(savedGroup);
    }

    private StudyGroup mapToEntity(StudyGroupRequest studyGroupRequest) {
        return StudyGroup.builder()
                .title(studyGroupRequest.getTitle())
                .content(studyGroupRequest.getContent())
                .build();
    }

    private StudyGroupResponse mapToDto(StudyGroup savedGroup) {
        return StudyGroupResponse.builder()
                .title(savedGroup.getTitle())
                .content(savedGroup.getContent())
                .build();
    }


    @Transactional(readOnly = true)
    public StudyGroupResponse getById(Long studyGroupId) {

        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> {
                    log.error("studyGroup 대상이 없습니다. studyGroupId: {}", studyGroupId);
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });

        return mapToDto(studyGroup);
    }

    @Transactional(readOnly = true)
    public List<StudyGroupResponse> getAll() {

        List<StudyGroup> studyGroups = studyGroupRepository.findAll();
        return studyGroups.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long updateById(Long studyGroupId, StudyGroupRequest studyGroupRequest) {
        StudyGroup studyGroup = studyGroupRepository.findById(studyGroupId)
                .orElseThrow(() -> {
                    log.error("studyGroup 대상이 없습니다. studyGroupId: {}", studyGroupId);
                    throw new ResourceNotFoundException(ErrorCode.NO_TARGET);
                });

        studyGroup.update(studyGroupRequest.getTitle(), studyGroupRequest.getContent());

        return studyGroup.getId();

    }

    @Transactional
    public void deleteById(Long studyGroupId) {
        studyGroupRepository.deleteById(studyGroupId);
    }

    @Transactional(readOnly = true)
    public List<StudyGroupResponse> getAllByUserId(Long userId) {
        List<UserGroup> userGroups = userGroupRepository.findByUserId(userId);

        List<Long> studyGroupsIds = userGroups.stream()
                .map(d -> d.getStudyGroup().getId())
                .collect(Collectors.toList());

        List<StudyGroup> studyGroups = studyGroupRepository.findByIdIn(studyGroupsIds);
        return studyGroups.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

    }
}
