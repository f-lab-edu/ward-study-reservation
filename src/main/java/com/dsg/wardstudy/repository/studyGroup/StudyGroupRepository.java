package com.dsg.wardstudy.repository.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

// @Query in 절안에 컬렉션 사용하기
    @Query("select s from StudyGroup s left join fetch s.userGroups where s.id in :ids")
    List<StudyGroup> findByIds(@Param("ids") List<Long> ids);
}
