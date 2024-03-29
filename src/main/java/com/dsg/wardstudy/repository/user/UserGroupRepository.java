package com.dsg.wardstudy.repository.user;

import com.dsg.wardstudy.domain.user.entity.User;
import com.dsg.wardstudy.domain.user.entity.UserGroup;
import com.dsg.wardstudy.domain.user.constant.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query("select ug from UserGroup ug left join fetch ug.user where ug.user.id = :userId")
    List<UserGroup> findByUserId(@Param("userId") Long userId);

    @Query("select ug from UserGroup ug where ug.user.id = :userId and ug.studyGroup.id = :sgId")
    Optional<UserGroup> findByUserIdAndSGId(@Param("userId") Long userId, @Param("sgId") Long sgId);

    @Query("select ug.userType from UserGroup ug where ug.user.id = :userId and ug.studyGroup.id = :sgId")
    Optional<UserType> findUserTypeByUserIdAndSGId(@Param("userId") Long userId, @Param("sgId") Long sgId);

    @Query("select ug.studyGroup.id from UserGroup ug where ug.user.id = :userId")
    List<Long> findSgIdsByUserId(@Param("userId") Long userId);

    @Query("select ug.user from UserGroup ug where ug.studyGroup.id = :sgId")
    List<User> findUserBySGId(@Param("sgId") Long sgId);

}
