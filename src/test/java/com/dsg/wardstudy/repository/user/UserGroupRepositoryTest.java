package com.dsg.wardstudy.repository.user;

import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.type.UserType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserGroupRepositoryTest {

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Test
    public void givenUserIdAndSGId_whenFindById_thenReturnUserType() {
        // given - precondition or setup

        // when - action or the behaviour that we are going test
        UserType userType = userGroupRepository.findUserTypeByUserIdAndSGId(1L, 1L).get();
        // then - verify the output
        assertThat(userType).isNotNull();
        assertThat(userType).isEqualTo(UserType.P);

    }

    @Test
    public void givenStudyGroupId_whenFindById_thenUserList(){
        // given - precondition or setup

        // when - action or the behaviour that we are going test
        List<User> users = userGroupRepository.findUserBySGId(1L);
        // then - verify the output
        log.info("users: {}", users);
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(2);

    }
}