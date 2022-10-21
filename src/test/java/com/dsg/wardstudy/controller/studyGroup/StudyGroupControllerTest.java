package com.dsg.wardstudy.controller.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.QStudyGroup;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.studyGroup.controller.StudyGroupController;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.domain.studyGroup.dto.PageResponse;
import com.dsg.wardstudy.domain.studyGroup.dto.StudyGroupRequest;
import com.dsg.wardstudy.domain.studyGroup.dto.StudyGroupResponse;
import com.dsg.wardstudy.common.exception.ErrorCode;
import com.dsg.wardstudy.common.exception.WSApiException;
import com.dsg.wardstudy.domain.studyGroup.service.StudyGroupService;
import com.dsg.wardstudy.type.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudyGroupController.class)
class StudyGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyGroupService studyGroupService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudyGroup studyGroup;
    private User user;
    private UserGroup userGroup;

    @BeforeEach
    void setup() {
        studyGroup = StudyGroup.builder()
                .title("testSG")
                .content("인원 4명의 스터디그룹을 모집합니다.")
                .build();

        user = User.builder()
                .id(1L)
                .build();

        userGroup = UserGroup.builder()
                .user(user)
                .studyGroup(studyGroup)
                .userType(UserType.LEADER)
                .build();


    }

    @Test
    public void givenStudyGroupRequest_whenCreate_thenReturnStudyGroupResponse() throws Exception {
        // given - precondition or setup
        StudyGroupRequest studyGroupRequest = StudyGroupRequest.builder()
                .title(studyGroup.getTitle())
                .content(studyGroup.getContent())
                .build();

        StudyGroupResponse studyGroupResponse = StudyGroupResponse.builder()
                .title(userGroup.getStudyGroup().getTitle())
                .content(userGroup.getStudyGroup().getContent())
                .build();

        given(studyGroupService.register(anyLong(), any(StudyGroupRequest.class)))
                .willReturn(studyGroupResponse);

        // when - action or the behaviour that we are going test
        ResultActions resultActions = mockMvc.perform(post("/users/{userId}/study-group", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studyGroupRequest)));

        // then - verify the output
        resultActions
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(studyGroup.getTitle())))
                .andExpect(jsonPath("$.content", is(studyGroup.getContent())));

    }

    @Test
    public void givenListOfStudyGroupResponses_whenGet_thenReturnStudyGroupResponseList() throws Exception {
        // given - precondition or setup
        int length = 10;
        List<StudyGroupResponse> studyGroupResponses = new ArrayList<>();
        StudyGroupResponse studyGroupResponse = StudyGroupResponse.builder()
                .title(studyGroup.getTitle())
                .content(studyGroup.getContent())
                .build();
        for (int i = 0; i < length; i++) {
            studyGroupResponses.add(
                    studyGroupResponse
            );
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        QStudyGroup qStudyGroup = QStudyGroup.studyGroup;

        String type = "t";
        String keyword = "test";

        // 검색조건 추가
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")) {
            conditionBuilder.or(qStudyGroup.title.contains(keyword));
        }
        if(type.contains("c")) {
            conditionBuilder.or(qStudyGroup.content.contains(keyword));
        }

        given(studyGroupService.getAll(pageable, type, keyword))
                .willReturn(PageResponse.StudyGroupDetail.builder()
                        .content(studyGroupResponses)
                        .pageNo(pageable.getPageNumber())
                        .pageSize(pageable.getPageSize())
                        .totalElements(length)
                        .build());

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/study-group")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,desc")
                        .param("type", "t")
                        .param("keyword", "test")
                )
                .andDo(print())
                .andExpect(status().isOk());

        verify(studyGroupService).getAll(pageable, type, keyword);
    }

    @Test
    public void givenStudyGroupId_whenGet_thenReturnStudyGroupResponse() throws Exception {
        Long studyGroupId = 1L;
        // given - precondition or setup
        StudyGroupResponse studyGroupResponse = StudyGroupResponse.builder()
                .title(studyGroup.getTitle())
                .content(studyGroup.getContent())
                .build();
        given(studyGroupService.getById(anyLong())).willReturn(studyGroupResponse);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/study-group/" + studyGroupId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(studyGroupResponse.getTitle())))
                .andExpect(jsonPath("$.content", is(studyGroupResponse.getContent())));

    }

    @Test
    public void givenInvalidStudyGroupId_whenGet_thenReturn404() throws Exception {
        Long studyGroupId = 190L;
        // given - precondition or setup
        given(studyGroupService.getById(studyGroupId))
                .willThrow(new WSApiException(ErrorCode.NO_FOUND_ENTITY,
                        "can't find a studyGroup by " + "studyGroupId: " +  studyGroupId));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/study-group/" + studyGroupId))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void givenUserId_whenGetAll_thenReturnStudyGroupResponseList() throws Exception {
        // given - precondition or setup
        Long userId = 1L;
        StudyGroupResponse studyGroupResponse = StudyGroupResponse.builder()
                .title(studyGroup.getTitle())
                .content(studyGroup.getContent())
                .build();

        given(studyGroupService.getAllByUserId(userId))
                .willReturn(List.of(studyGroupResponse));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/users/{userId}/study-group",userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void givenStudyGroupIdAndUpdatedStudyGroupRequest_whenUpdate_thenReturnUpdateStudyGroupId() throws Exception {
        // given - precondition or setup
        Long studyGroupId = 1L;
        Long userId = 1L;

        StudyGroupRequest updateStudyGroupRequest = StudyGroupRequest.builder()
                .title("RamStudy")
                .content("Ram effective Java study")
                .build();

        given(studyGroupService.updateById(userId, studyGroupId, updateStudyGroupRequest))
                .willReturn(studyGroupId);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(put("/users/{userId}/study-group/{studyGroupId}",user.getId(), studyGroupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStudyGroupRequest)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void givenStudyGroupId_whenDelete_thenReturn200() throws Exception {
        // given - precondition or setup
        Long userId = 1L;
        Long studyGroupId = 1L;
        willDoNothing().given(studyGroupService).deleteById(userId, studyGroupId);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(delete("/users/{userId}/study-group/{studyGroupId}",userId, studyGroupId))
                .andDo(print())
                .andExpect(status().isOk());

    }
}
