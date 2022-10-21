package com.dsg.wardstudy.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Log4j2
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private static final String USER_ID = "USER_ID";
    private final HttpSession session;

    @Override
    public void loginUser(Long id) {
        session.setAttribute(USER_ID, id);
    }

    @Override
    public void logoutUser() {
        session.removeAttribute(USER_ID);
    }
}
