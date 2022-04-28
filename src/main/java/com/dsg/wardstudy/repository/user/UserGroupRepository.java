package com.dsg.wardstudy.repository.user;

import com.dsg.wardstudy.domain.user.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

        List<UserGroup> findByUserId(Long userId);

        @Query("select ug.studyGroup.id from UserGroup ug where ug.user.id = :userId")
        List<Long> findSgIdsByUserId(@Param("userId") Long userId);

}
