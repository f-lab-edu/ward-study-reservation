package com.dsg.wardstudy.controller.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupRequest;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupResponse;
import com.dsg.wardstudy.exception.ErrorCode;
import com.dsg.wardstudy.exception.ResourceNotFoundException;
import com.dsg.wardstudy.service.studyGroup.StudyGroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @BeforeEach
    void setup() {
        studyGroup = StudyGroup.builder()
                .title("testSG")
                .content("인원 4명의 스터디그룹을 모집합니다.")
                .build();
    }

    @Test
    public void create() throws Exception {
        // given - precondition or setup
        StudyGroupRequest studyGroupRequest = StudyGroupRequest.builder()
                .title(studyGroup.getTitle())
                .content(studyGroup.getContent())
                .build();

        StudyGroupResponse studyGroupResponse = StudyGroupResponse.builder()
                .title(studyGroup.getTitle())
                .content(studyGroup.getContent())
                .build();

        given(studyGroupService.create(any(StudyGroupRequest.class)))
                .willReturn(studyGroupResponse);

        // when - action or the behaviour that we are going test
        ResultActions resultActions = mockMvc.perform(post("/study-group")
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
    public void getAll() throws Exception {
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

        given(studyGroupService.getAll()).willReturn(studyGroupResponses);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/study-group"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(length));

    }

    @Test
    public void getById() throws Exception {
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
    public void getById_ThrowException() throws Exception {
        Long studyGroupId = 1L;
        // given - precondition or setup
        given(studyGroupService.getById(studyGroupId)).willThrow(new ResourceNotFoundException(ErrorCode.NO_TARGET));

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/study-group/" + studyGroupId))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void getAllByUserId() throws Exception {
        // TODO
        // given - precondition or setup

        // when - action or the behaviour that we are going test

        // then - verify the output

    }

    @Test
    public void updateById() throws Exception {
        // given - precondition or setup
        Long studyGroupId = 1L;

        StudyGroupRequest updateStudyGroupRequest = StudyGroupRequest.builder()
                .title("RamStudy")
                .content("Ram effective Java study")
                .build();

        given(studyGroupService.updateById(studyGroupId, updateStudyGroupRequest))
                .willReturn(studyGroupId);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(put("/study-group/{id}", studyGroupId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStudyGroupRequest)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void deleteById() throws Exception {
        // given - precondition or setup
        Long studyGroupId = 1L;
        willDoNothing().given(studyGroupService).deleteById(studyGroupId);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(delete("/study-group/{id}", studyGroupId))
                .andDo(print())
                .andExpect(status().isOk());

    }
}