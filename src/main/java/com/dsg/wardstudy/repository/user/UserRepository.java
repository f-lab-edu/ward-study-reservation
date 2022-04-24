package com.dsg.wardstudy.repository.user;

import com.dsg.wardstudy.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
