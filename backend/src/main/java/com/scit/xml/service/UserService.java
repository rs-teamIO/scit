package com.scit.xml.service;

import com.scit.xml.dto.RegisterDto;
import com.scit.xml.repository.UserRepository;
import com.scit.xml.service.validator.database.RegisterDatabaseValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RegisterDatabaseValidator registerDatabaseValidator;

    public String register(RegisterDto registerDto) {
        registerDatabaseValidator.validate(registerDto);

        String userId = userRepository.save(registerDto.getUser(), registerDto.getPerson());

        switch (registerDto.getUser().getRole()) {
            case AUTHOR:
                // TODO:
                break;
            case EDITOR:
                // TODO:
                break;
        }

        return userId;
    }
}
