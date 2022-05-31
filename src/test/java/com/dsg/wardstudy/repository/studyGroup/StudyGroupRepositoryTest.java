package com.dsg.wardstudy.repository.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
class StudyGroupRepositoryTest {

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    private StudyGroup studyGroup;
    private User user;
    private UserGroup userGroup;


    @BeforeEach
    void setup() {
        studyGroup = StudyGroup.builder()
                .id(1L)
                .title("testSG")
                .content("인원 4명의 스터디그룹을 모집합니다.")
                .build();

        user = User.builder()
                .id(1L)
                .name("dsg")
                .email("dsg@gmail.com")
                .build();

        userGroup = UserGroup.builder()
                .user(user)
                .studyGroup(studyGroup)
                .build();
    }

    @Test
    @DisplayName("스터디그룹 생성테스트")
    public void givenStudyGroup_whenSave_thenReturnSavedStudyGroup() {
        // given - precondition or setup
        // when - action or the behaviour that we are going test
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);

        // then - verify the output
        assertThat(savedStudyGroup).isNotNull();
        assertThat(savedStudyGroup.getTitle()).isEqualTo("testSG");

    }

    @Test
    @DisplayName("스터디그룹 전체조회")
    public void givenStudyGroupList_whenFindAll_thenStudyGroupList() {
        // given - precondition or setup
        // when - action or the behaviour that we are going test
        IntStream.rangeClosed(1, 10).forEach(i -> {
            StudyGroup studyGroup = StudyGroup.builder()
                    .title("testSG_" + i)
                    .content("인원 " + i + "명의 스터디그룹을 모집합니다.")
                    .build();
            studyGroupRepository.save(studyGroup);
        });


        List<StudyGroup> studyGroups = studyGroupRepository.findAll();
        log.info("studyGroups : {}", studyGroups);

        // then - verify the output
        assertThat(studyGroups).isNotNull();
        assertThat(studyGroups.size()).isEqualTo(10);

    }

    @Test
    @DisplayName("스터디그룹 상세보기")
    public void givenStudyGroup_whenFindById_thenReturnStudyGroup() {
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        // when - action or the behaviour that we are going test
        StudyGroup findStudyGroup = studyGroupRepository.findById(savedStudyGroup.getId()).get();

        // then - verify the output
        assertThat(this.studyGroup).isNotNull();
        assertThat(findStudyGroup.getTitle()).isEqualTo("testSG");

    }

    @Test
    @DisplayName("스터디그룹들 userId로 가져오기")
    public void givenUserId_whenFindByUserId_thenReturnStudyGroupsIdList() {
        // given - precondition or setup
        // TODO : User, UserGroup save API 만들어야
        userGroupRepository.save(userGroup);

        // when - action or the behaviour that we are going test
        List<UserGroup> userGroupList = userGroupRepository.findByUserId(user.getId());
        log.info("userGroupList: {}", userGroupList);
        List<Long> studyGroupsIds = userGroupList.stream()
                .map(ug -> ug.getStudyGroup().getId())
                .collect(Collectors.toList());
        log.info("studyGroupsIds : {}", studyGroupsIds);

        assertThat(studyGroupsIds).isNotNull();
        assertThat(studyGroupsIds).isEqualTo(List.of(studyGroup.getId()));

    }

    @Test
    @DisplayName("스터디그룹들 studyGroupIdList로 가져오기")
    public void givenSGIdList_whenFindBySGIdIn_thenStudyGroupList(){
        // given - precondition or setup
        LongStream.rangeClosed(1, 3).forEach(i -> {
            StudyGroup studyGroup = StudyGroup.builder()
                    .id(i)
                    .title("testSG_" + i)
                    .content("인원 " + i + "명의 스터디그룹을 모집합니다.")
                    .build();
            studyGroupRepository.save(studyGroup);
        });

        List<Long> studyGroupsIds = List.of(1L, 2L, 3L);

        // when - action or the behaviour that we are going test
        List<StudyGroup> studyGroups = studyGroupRepository.findByIdIn(studyGroupsIds);

        // then - verify the output
        assertThat(studyGroups).isNotNull();
        log.info("studyGroups.size(): " + studyGroups.size());
        assertThat(studyGroups.size()).isEqualTo(3);

    }

    @Test
    @DisplayName("스터디그룹 수정")
    public void givenStudyGroup_whenUpdateStudyGroup_thenReturnUpdatedStudyGroup() {
        // given - precondition or setup
        studyGroupRepository.save(studyGroup);

        // when - action or the behaviour that we are going test
        StudyGroup savedStudyGroup = studyGroupRepository.findById(this.studyGroup.getId()).get();
        savedStudyGroup.update("new_title", "new_content");

        // then - verify the output
        assertThat(studyGroup.getTitle()).isEqualTo("new_title");
        assertThat(studyGroup.getContent()).isEqualTo("new_content");

    }

    @Test
    @DisplayName("스터디그룹 삭제")
    public void givenStudyGroup_whenDelete_thenRemoveStudyGroup() {
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        // when - action or the behaviour that we are going test
        studyGroupRepository.delete(savedStudyGroup);
        Optional<StudyGroup> deletedStudyGroup = studyGroupRepository.findById(1L);

        // then - verify the output
        assertThat(deletedStudyGroup).isEmpty();

    }

}