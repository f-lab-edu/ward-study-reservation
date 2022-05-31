package com.dsg.wardstudy.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Slf4j
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
