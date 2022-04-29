package com.dsg.wardstudy.integration.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.repository.user.UserGroupRepository;
import com.dsg.wardstudy.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
class StudyGroupRepositoryIntegrationTest {

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserRepository userRepository;

    private StudyGroup studyGroup;

    @BeforeEach
    void setup() {
        studyGroup = StudyGroup.builder()
                .title("testSG")
                .content("인원 4명의 스터디그룹을 모집합니다.")
                .build();
    }

    @Test
    @DisplayName("스터디그룹 생성테스트")
    public void saveTest(){
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
    public void getAllTest(){
        // given - precondition or setup
        // when - action or the behaviour that we are going test
        List<StudyGroup> studyGroups = studyGroupRepository.findAll();

        // then - verify the output
        assertThat(studyGroups).isNotNull();
        log.info("studyGroups : {}", studyGroups);

    }

    @Test
    @DisplayName("스터디그룹 상세보기")
    public void getById(){
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(this.studyGroup);
        // when - action or the behaviour that we are going test
        studyGroupRepository.findById(savedStudyGroup.getId());

        // then - verify the output
        assertThat(this.studyGroup).isNotNull();

    }

    @Test
    @DisplayName("스터디그룹들 userId로 가져오기")
    public void getAllByUserId(){
        // given - precondition or setup
        // TODO : User, UserGroup save API 만들어야
//        User user = User.builder()
//                .name("test")
//                .email("test@test.com")
//                .nickname("test")
//                .password("1234")
//                .build();
//        User savedUser = userRepository.save(user);
        // when - action or the behaviour that we are going test
        List<UserGroup> iByUserId = userGroupRepository.findByUserId(2L);
        List<Long> studyGroupsIds = iByUserId.stream()
                .map(d -> d.getStudyGroup().getId())
                .collect(Collectors.toList());

        List<StudyGroup> studyGroups = studyGroupRepository.findByIdIn(studyGroupsIds);
        // then - verify the output
        assertThat(studyGroups).isNotNull();
        log.info("studyGroups.size(): "+ studyGroups.size());


    }

    @Test
    @DisplayName("스터디그룹 수정")
    public void updateById(){
        // given - precondition or setup
        studyGroupRepository.save(studyGroup);

        // when - action or the behaviour that we are going test
        StudyGroup savedStudyGroup = studyGroupRepository.findById(this.studyGroup.getId()).get();
        savedStudyGroup.update("new_title", "new_content");

        // then - verify the output
        assertThat(studyGroup.getTitle()).isEqualTo("new_title");

    }

    @Test
    @DisplayName("스터디그룹 삭제")
    public void deleteById(){
        // given - precondition or setup
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        // when - action or the behaviour that we are going test
        studyGroupRepository.deleteById(savedStudyGroup.getId());
        Optional<StudyGroup> deletedStudyGroup = studyGroupRepository.findById(savedStudyGroup.getId());

        // then - verify the output
        assertThat(deletedStudyGroup).isEmpty();

    }

}