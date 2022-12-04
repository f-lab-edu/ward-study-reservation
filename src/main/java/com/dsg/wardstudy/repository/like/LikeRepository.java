package com.dsg.wardstudy.repository.like;

import com.dsg.wardstudy.domain.studyGroup.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserId(Long userId);

    @Query("select COUNT(entity) from Like entity where entity.studyGroup.id = :studyGroupId")
    int countByStudyGroupId(@Param("studyGroupId") Long studyGroupId);

}
