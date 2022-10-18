package com.dsg.wardstudy.common.adapter.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailSendService mailSendService;

    @GetMapping("/mail/send")
    public void sendMail(String email) {

        mailSendService.sendMail("ehtjd33@gmail.com", "제목입니다.", "테스트입니다.");

    }
}
