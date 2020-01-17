package com.scit.xml.service;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.util.NotFoundUtils;
import com.scit.xml.dto.RegisterDto;
import com.scit.xml.repository.UserRepository;
import com.scit.xml.service.validator.database.RegisterDatabaseValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RegisterDatabaseValidator registerDatabaseValidator;

    public String register(RegisterDto registerDto) {
        this.registerDatabaseValidator.validate(registerDto);
        String userId = this.userRepository.save(registerDto.getUser(), registerDto.getPerson());

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

    public String findById(String id) {
        String userXml = this.userRepository.findById(id);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(userXml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.USER, RestApiConstants.ID));
        return userXml;
    }
}
