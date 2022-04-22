package com.dsg.wardstudy.repository.userGroup;

import com.dsg.wardstudy.domain.user.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

        List<UserGroup> findIByUserId(Long userId);
}
