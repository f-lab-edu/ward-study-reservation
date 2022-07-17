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
        User user = mapToEntity(signUpDto);
        log.info("signUp user : {}", user);
        return mapToDto(userRepository.save(user));

    }

    @Override
    public LoginDto getByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new WSApiException(ErrorCode.USER_NOT_FOUND, "cant' not found User"));
        log.info("getByEmailAndPassword user : {}", user);

        return mapToDto(user);
    }

    private LoginDto mapToDto(User user) {
        return LoginDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }

    private User mapToEntity(SignUpRequest signUpDto) {
        return User.builder()
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .nickname(signUpDto.getNickname())
                .password(signUpDto.getPassword())
                .build();
    }
}
