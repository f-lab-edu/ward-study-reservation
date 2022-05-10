package com.dsg.wardstudy.integration.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.dto.studyGroup.StudyGroupRequest;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StudyGroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

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
    public void givenStudyGroupRequest_whenCreate_thenReturnStudyGroupResponse() throws Exception {
        // given - precondition or setup
        StudyGroupRequest studyGroupRequest = StudyGroupRequest.builder()
                .title(studyGroup.getTitle())
                .content(studyGroup.getContent())
                .build();

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
    public void givenListOfStudyGroupResponses_whenGet_thenReturnStudyGroupResponseList() throws Exception {
        // given - precondition or setup
        int length = 3;
        IntStream.rangeClosed(1, length).forEach(i -> {
            StudyGroup studyGroup = StudyGroup.builder()
                    .title("sg_dsg" + "_" + i)
                    .content("spring_study" + "_" + i)
                    .build();
            studyGroupRepository.save(studyGroup);
        });

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/study-group"))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    public void givenStudyGroupId_whenGet_thenReturnStudyGroupResponse() throws Exception {
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/study-group/" + savedStudyGroup.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(studyGroup.getTitle())))
                .andExpect(jsonPath("$.content", is(studyGroup.getContent())));

    }

    @Test
    public void givenInvalidStudyGroupId_whenGet_thenReturnEmpty() throws Exception {
        Long studyGroupId = 100L;
        // given - precondition or setup
        studyGroupRepository.save(studyGroup);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(get("/study-group/" + studyGroupId))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void getAllByUserId() throws Exception {
        // TODO : controller 메서드 만들기
        // given - precondition or setup

        // when - action or the behaviour that we are going test

        // then - verify the output

    }

    @Test
    public void givenStudyGroupIdAndUpdatedStudyGroupRequest_whenUpdate_thenReturnUpdateStudyGroupId() throws Exception {
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);

        StudyGroupRequest updateStudyGroupRequest = StudyGroupRequest.builder()
                .title("Jasi")
                .content("springboot_study!!")
                .build();


        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(put("/study-group/{id}", savedStudyGroup.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStudyGroupRequest)))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void givenStudyGroupId_whenDelete_thenReturn200() throws Exception {
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);

        // when - action or the behaviour that we are going test
        // then - verify the output
        mockMvc.perform(delete("/study-group/{id}", savedStudyGroup.getId()))
                .andDo(print())
                .andExpect(status().isOk());

    }
}