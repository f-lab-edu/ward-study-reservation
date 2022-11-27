package com.dsg.wardstudy.repository.user;

import com.dsg.wardstudy.domain.reservation.Room;
import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserGroupRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    private User user;
    private UserGroup userGroup;
    private StudyGroup studyGroup;
    private Room room;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("dsgfunk@gmail.com")
                .build();

        userGroup = UserGroup.builder()
                .userType(UserType.PARTICIPANT)
                .build();

        studyGroup = StudyGroup.builder()
                .title("title_")
                .content("study 번 방")
                .build();

        room = Room.builder()
                .name("roomA")
                .build();

    }

    @Test
    public void givenUserIdAndSGId_whenFindById_thenReturnUserType() {
        // given - precondition or setup
        User savedUser = userRepository.save(user);
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        UserGroup savedUserGroup = userGroupRepository.save(userGroup);
        // when - action or the behaviour that we are going test
        UserType userType = userGroupRepository.findUserTypeByUserIdAndSGId(savedUser.getId(), savedStudyGroup.getId()).get();
        log.info("userType: {}", userType);
        // then - verify the output
        assertThat(userType).isNotNull();
        assertThat(userType).isEqualTo(savedUserGroup.getUserType());

    }

    @Test
    public void givenStudyGroupId_whenFindById_thenUserList(){
        // given - precondition or setup
        User savedUser = userRepository.save(user);
        StudyGroup savedStudyGroup = studyGroupRepository.save(studyGroup);
        UserGroup savedUserGroup = userGroupRepository.save(userGroup);
        // when - action or the behaviour that we are going test
        List<User> users = userGroupRepository.findUserBySGId(savedStudyGroup.getId());
        // then - verify the output
        log.info("users: {}", users);
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(2);

    }
}
