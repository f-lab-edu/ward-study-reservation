package com.dsg.wardstudy.integration.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudyGroupRepositoryIntegrationTest {

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserRepository userRepository;

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
        assertThat(savedStudyGroup.getId()).isGreaterThan(0);
        assertThat(savedStudyGroup.getTitle()).isEqualTo("testSG");

    }

    @Test
    @DisplayName("스터디그룹 전체조회")
    public void givenStudyGroupList_whenFindAll_thenStudyGroupList() {
        // given - precondition or setup
        // when - action or the behaviour that we are going test
        List<StudyGroup> studyGroups = studyGroupRepository.findAll();
        log.info("studyGroups : {}", studyGroups);

        // then - verify the output
        assertThat(studyGroups).isNotNull();

    }

    @Test
    @DisplayName("스터디그룹 상세보기")
    public void givenStudyGroup_whenFindById_thenReturnStudyGroup() {
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(this.studyGroup);
        // when - action or the behaviour that we are going test
        studyGroupRepository.findById(savedStudyGroup.getId());

        // then - verify the output
        assertThat(this.studyGroup).isNotNull();

    }

    @Test
    @DisplayName("스터디그룹들 userId로 가져오기")
    public void givenUserId_whenFindByUserId_thenReturnStudyGroupsIdList() {
        // given - precondition or setup
        userRepository.save(user);
        studyGroupRepository.save(studyGroup);
        userGroupRepository.save(userGroup);

        // when - action or the behaviour that we are going test
        List<UserGroup> iByUserId = userGroupRepository.findByUserId(user.getId());
        List<Long> studyGroupsIds = iByUserId.stream()
                .map(d -> d.getStudyGroup().getId())
                .collect(Collectors.toList());

        List<StudyGroup> studyGroups = studyGroupRepository.findByIdIn(studyGroupsIds);
        // then - verify the output
        assertThat(studyGroups).isNotNull();
        log.info("studyGroups.size(): " + studyGroups.size());

    }

    @Test
    @DisplayName("스터디그룹 수정")
    public void givenStudyGroup_whenUpdateStudyGroup_thenReturnUpdatedStudyGroup() {
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);

        // when - action or the behaviour that we are going test
        StudyGroup findStudyGroup = studyGroupRepository.findById(savedStudyGroup.getId()).get();
        findStudyGroup.update("new_title", "new_content");
        log.info("findStudyGroup: {}", findStudyGroup);

        // then - verify the output
        assertThat(findStudyGroup.getTitle()).isEqualTo("new_title");
        assertThat(findStudyGroup.getContent()).isEqualTo("new_content");

    }

    @Test
    @DisplayName("스터디그룹 삭제")
    public void givenStudyGroup_whenDelete_thenRemoveStudyGroup() {
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        // when - action or the behaviour that we are going test
        studyGroupRepository.deleteById(savedStudyGroup.getId());
        Optional<StudyGroup> deletedStudyGroup = studyGroupRepository.findById(savedStudyGroup.getId());

        // then - verify the output
        assertThat(deletedStudyGroup).isEmpty();

    }

}