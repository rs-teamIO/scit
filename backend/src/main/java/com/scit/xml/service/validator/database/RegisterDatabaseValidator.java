package com.scit.xml.service.validator.database;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.util.BadRequestUtils;
import com.scit.xml.dto.RegisterDto;
import com.scit.xml.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

@Service
@RequiredArgsConstructor
public class RegisterDatabaseValidator {

    private final UserRepository userRepository;

    public void validate(RegisterDto registerDto) {
        validateUserWithGivenEmailDoesNotExist(registerDto.getUser().getEmail());
        validateUserWithGivenUsernameDoesNotExist(registerDto.getUser().getUsername());
    }

    private void validateUserWithGivenUsernameDoesNotExist(String username) {
        String xml = userRepository.findByUsername(username);
        BadRequestUtils.throwInvalidRequestDataExceptionIf(!StringUtils.isEmpty(xml),
                RestApiErrors.entityWithGivenFieldAlreadyExists(RestApiConstants.USER, RestApiConstants.USERNAME));
    }

    private void validateUserWithGivenEmailDoesNotExist(String email) {
        String xml = userRepository.findByEmail(email);
        BadRequestUtils.throwInvalidRequestDataExceptionIf(!StringUtils.isEmpty(xml),
                RestApiErrors.entityWithGivenFieldAlreadyExists(RestApiConstants.USER, RestApiConstants.EMAIL));
    }
}
