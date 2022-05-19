package com.dsg.wardstudy.repository.user;

import com.dsg.wardstudy.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void givenPageInfo_whenFindBy_thenPageList(){
        // given - precondition or setup
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        // when - action or the behaviour that we are going test
        Page<User> userPages = userRepository.findBy(pageable);
        // then - verify the output
        userPages.get().forEach(System.out::println);

    }
}