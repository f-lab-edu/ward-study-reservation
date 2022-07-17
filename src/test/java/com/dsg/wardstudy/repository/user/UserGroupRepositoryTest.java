package com.dsg.wardstudy.repository.user;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.domain.user.UserGroup;
import com.dsg.wardstudy.repository.studyGroup.StudyGroupRepository;
import com.dsg.wardstudy.type.UserType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
class UserGroupRepositoryTest {

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    private User user;
    private UserGroup userGroup;
    private StudyGroup studyGroup;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("dsgfunk@gmail.com")
                .build();

        studyGroup = StudyGroup.builder()
                .id(1L)
                .title("title_")
                .content("study 번 방")
                .build();

        userGroup = UserGroup.builder()
                .user(user)
                .studyGroup(studyGroup)
                .userType(UserType.P)
                .build();

    }

    @Test
    public void givenUserIdAndSGId_whenFindById_thenReturnUserType() {
        // given - precondition or setup
        userRepository.save(user);
        studyGroupRepository.save(studyGroup);
        userGroupRepository.save(userGroup);

        // when - action or the behaviour that we are going test
        UserType userType = userGroupRepository.findUserTypeByUserIdAndSGId(user.getId(), studyGroup.getId()).get();
        // then - verify the output
        assertThat(userType).isNotNull();
        assertThat(userType).isEqualTo(userGroup.getUserType());

    }

    @Test
    public void givenStudyGroupId_whenFindById_thenUserList(){
        // given - precondition or setup
        userRepository.save(user);
        studyGroupRepository.save(studyGroup);
        userGroupRepository.save(userGroup);

        // when - action or the behaviour that we are going test
        List<User> users = userGroupRepository.findUserBySGId(studyGroup.getId());
        // then - verify the output
        log.info("users: {}", users);
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);

    }

    @Test
    public void givenPageInfo_whenFindBy_thenUserPageList(){
        // given - precondition or setup
        LongStream.rangeClosed(1,30).forEach(i -> {
            User user = User.builder()
                    .id(i)
                    .email(i+"_email")
                    .name("name_"+i)
                    .build();

            userRepository.save(user);
        });

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        // when - action or the behaviour that we are going test
        Page<User> userPages = userRepository.findBy(pageable);
        log.info("userPages.getContent(): {}", userPages.getContent());
        // then - verify the output
        userPages.get().forEach(System.out::println);

    }
}