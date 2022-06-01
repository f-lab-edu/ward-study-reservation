package com.dsg.wardstudy.service.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupRequest;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupResponse;
import com.dsg.wardstudy.exception.WSApiException;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class StudyGroupServiceTest {

    @Mock
    private StudyGroupRepository studyGroupRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @InjectMocks
    private StudyGroupServiceImpl studyGroupService;

    private StudyGroup studyGroup;
    private User user;
    private UserGroup userGroup;

    private StudyGroupRequest studyGroupRequest;

    @BeforeEach
    void setup() {
        studyGroup = StudyGroup.builder()
                .id(1L)
                .title("testSG")
                .content("인원 4명의 스터디그룹을 모집합니다.")
                .build();

        user = User.builder()
                .id(1L)
                .build();

        userGroup = UserGroup.builder()
                .user(user)
                .studyGroup(studyGroup)
                .userType(UserType.L)
                .build();
    }

    @Test
    public void givenStudyGroup_whenSave_thenReturnStudyGroupResponse() {
        // given - precondition or setup
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(user));
        given(studyGroupRepository.save(any(StudyGroup.class)))
                .willReturn(studyGroup);

        given(userGroupRepository.save(any(UserGroup.class)))
                .willReturn(userGroup);

        studyGroupRequest = StudyGroupRequest.builder()
                .title(studyGroup.getTitle())
                .content(studyGroup.getContent())
                .build();
        // when - action or the behaviour that we are going test
        StudyGroupResponse studyGroupResponse = studyGroupService.create(user.getId(), studyGroupRequest);
        log.info("studyGroupResponse: {}", studyGroupResponse);

        // then - verify the output
        assertThat(studyGroupResponse).isNotNull();
        assertThat(studyGroupResponse.getTitle()).isEqualTo(studyGroupRequest.getTitle());
        assertThat(studyGroupResponse.getContent()).isEqualTo(studyGroupRequest.getContent());

    }

    @Test
    public void givenStudyGroup_whenGetById_thenReturnStudyGroupResponse() {
        // getById
        // given - precondition or setup
        Optional<StudyGroup> studyGroup = Optional.of(this.studyGroup);
        given(studyGroupRepository.findById(1L))
                .willReturn(studyGroup);
        // when - action or the behaviour that we are going test
        StudyGroupResponse studyGroupResponse = studyGroupService.getById(1L);
        log.info("studyGroupResponse: {}", studyGroupResponse);

        // then - verify the output
        assertThat(studyGroupResponse.getTitle()).isEqualTo("testSG");
        assertThat(studyGroupResponse.getContent()).isEqualTo("인원 4명의 스터디그룹을 모집합니다.");

    }

    @Test
    public void givenStudyGroup_whenGetById_thenThrowsException() {
        // given - precondition or setup
        // when - action or the behaviour that we are going test
        given(studyGroupRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        // then - verify the output
        assertThatThrownBy(() -> {
            studyGroupService.getById(1L);
        }).isInstanceOf(WSApiException.class);
    }


    @Test
    public void givenStudyGroupList_whenGetAll_thenReturnStudyGroupResponseList() {
        // given - precondition or setup
        StudyGroup studyGroup1 = StudyGroup.builder()
                .id(100L)
                .title("testSG2")
                .content("인원 6명의 스터디그룹을 모집합니다.")
                .build();
        given(studyGroupRepository.findAll())
                .willReturn(List.of(studyGroup, studyGroup1));
        // when - action or the behaviour that we are going test
        List<StudyGroupResponse> studyGroupResponses = studyGroupService.getAll();
        log.info("studyGroupResponses: {}", studyGroupResponses);
        // then - verify the output
        assertThat(studyGroupResponses).isNotNull();
        assertThat(studyGroupResponses.size()).isEqualTo(2);

    }

    @Test
    public void givenStudyGroupList_whenGetAll_Negative_thenReturnStudyGroupResponseList() {
        // given - precondition or setup
        StudyGroup studyGroup1 = StudyGroup.builder()
                .id(2L)
                .title("testSG2")
                .content("인원 6명의 스터디그룹을 모집합니다.")
                .build();
        given(studyGroupRepository.findAll())
                .willReturn(Collections.emptyList());
        // when - action or the behaviour that we are going test
        List<StudyGroupResponse> studyGroupResponses = studyGroupService.getAll();
        log.info("studyGroupResponses: {}", studyGroupResponses);
        // then - verify the output
        assertThat(studyGroupResponses).isEmpty();
        assertThat(studyGroupResponses.size()).isEqualTo(0);

    }

    @Test
    public void givenStudyGroup_whenUpdate_thenReturnUpdatedStudyGroup() {
        // given - precondition or setup
        given(studyGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(studyGroup));

        studyGroupRequest = StudyGroupRequest.builder()
                .title("JumpToSpringboot_study")
                .content("JumpTo님이 진행하는 스터디")
                .build();

        // when - action or the behaviour that we are going test
        Long updateId = studyGroupService.updateById(studyGroup.getId(), studyGroupRequest);
        log.info("updateId: {}", updateId);
        StudyGroupResponse updatedResponse = studyGroupService.getById(updateId);

        // then - verify the output
        assertThat(this.studyGroup.getId()).isEqualTo(updateId);
        assertThat(updatedResponse.getTitle()).isEqualTo(studyGroupRequest.getTitle());
        assertThat(updatedResponse.getContent()).isEqualTo(studyGroupRequest.getContent());

    }

    @Test
    public void givenStudyGroupId_whenDelete_thenNothing() {
        // given - precondition or setup
        Long studyGroupId = 1L;
        willDoNothing().given(studyGroupRepository).deleteById(studyGroupId);

        // when - action or the behaviour that we are going test
        studyGroupService.deleteById(studyGroupId);

        // then - verify the output
        verify(studyGroupRepository).deleteById(studyGroupId);
        assertThat(studyGroupRepository.findById(studyGroupId)).isEmpty();

    }

}