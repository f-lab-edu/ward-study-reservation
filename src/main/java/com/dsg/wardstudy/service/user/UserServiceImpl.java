package com.dsg.wardstudy.service.user;

import com.dsg.wardstudy.domain.user.User;
import com.dsg.wardstudy.dto.user.LoginDto;
import com.dsg.wardstudy.dto.user.SignUpRequest;
import com.dsg.wardstudy.exception.ErrorCode;
import com.dsg.wardstudy.exception.WSApiException;
import com.dsg.wardstudy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public LoginDto signUp(SignUpRequest signUpDto) {
        User user = SignUpRequest.mapToEntity(signUpDto);
        log.info("signUp user : {}", user);
        return LoginDto.mapToDto(userRepository.save(user));

    }

    @Override
    public LoginDto getByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new WSApiException(ErrorCode.NOT_FOUND_USER, "cant' not found User"));
        log.info("getByEmailAndPassword user : {}", user);

        return LoginDto.mapToDto(user);
    }

}
