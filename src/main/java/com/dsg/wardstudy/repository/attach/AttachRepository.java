package com.dsg.wardstudy.repository.attach;

import com.dsg.wardstudy.domain.attach.entity.Attach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachRepository extends JpaRepository<Attach, Long> {

    void deleteAllByStudyGroupId(Long sgId);

    List<Attach> findAllByStudyGroupId(Long sgId);
}
