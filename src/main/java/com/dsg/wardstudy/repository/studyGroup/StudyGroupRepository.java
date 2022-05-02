package com.dsg.wardstudy.repository.studyGroup;

import com.dsg.wardstudy.domain.studyGroup.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {

    // in 절안에 컬렉션 findByIdIn
    List<StudyGroup> findByIdIn(List<Long> ids);
}
