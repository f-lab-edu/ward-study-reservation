package com.dsg.wardstudy.repository.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long>, QuerydslPredicateExecutor<StudyGroup> {

    // in 절안에 컬렉션 findByIdIn
    List<StudyGroup> findByIdIn(List<Long> ids);
}
