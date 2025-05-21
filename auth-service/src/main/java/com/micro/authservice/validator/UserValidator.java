package com.micro.authservice.validator;

import com.micro.authservice.repository.UserRepository;
import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;
    public void checkDuplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new StandardException(ErrorMessages.DUPLICATE, "Username already exists");
        }
    }
}
