package com.dsg.wardstudy.domain.attach.service;

import com.dsg.wardstudy.domain.attach.dto.AttachDTO;
import com.dsg.wardstudy.repository.attach.AttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class AttachService {

    private final AttachRepository attachRepository;

    public List<AttachDTO> getAttachList(Long sgId) {
        List<AttachDTO> attachDTOS = attachRepository.findAllByStudyGroupId(sgId).stream()
                .map(AttachDTO::toDTO)
                .collect(Collectors.toList());
        log.info("service getAttachList, attachDTOS: {}", attachDTOS);
        return attachDTOS;
    }

}
