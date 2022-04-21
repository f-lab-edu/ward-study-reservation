package com.dsg.wardstudy.service.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupRequest;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupResponse;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyGroupService {

    private final StudyGroupRepository studyGroupRepository;

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


    public StudyGroupResponse getById(Long groupId) {

        StudyGroup studyGroup = studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return mapToDto(studyGroup);
    }

    public List<StudyGroupResponse> getAll() {

        List<StudyGroup> all = studyGroupRepository.findAll();
        return all.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
