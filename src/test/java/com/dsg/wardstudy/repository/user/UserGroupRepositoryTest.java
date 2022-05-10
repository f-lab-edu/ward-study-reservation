package com.dsg.wardstudy.repository.user;

import com.dsg.wardstudy.type.UserType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
}