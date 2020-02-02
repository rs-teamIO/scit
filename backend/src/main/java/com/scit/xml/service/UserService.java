package com.scit.xml.service;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.util.NotFoundUtils;
import com.scit.xml.common.util.XmlExtractorUtil;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.model.user.Role;
import com.scit.xml.repository.UserRepository;
import com.scit.xml.service.validator.database.UserDatabaseValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final String USERNAME_XPATH = "/user/username";
    private final String EMAIL_XPATH = "/user/email";
    private final String ROLE_TAG_NAME = "user:role";

    private final UserRepository userRepository;
    private final UserDatabaseValidator userDatabaseValidator;

    /**
     * Performs registration of an {@link User} with {@link Role.AUTHOR} role
     * @param registerXmlWrapper object wrapping XML representation of {@link User}
     * @return unique identifier of the created {@link User}
     */
    public String register(XmlWrapper registerXmlWrapper) {
        Document document = registerXmlWrapper.getDocument();
        final String username = XmlExtractorUtil.extractStringAndValidateNotBlank(document, this.USERNAME_XPATH);
        final String email = XmlExtractorUtil.extractStringAndValidateNotBlank(document, this.EMAIL_XPATH);
        this.userDatabaseValidator.validateCreateRequest(username, email);

        final String role = Role.AUTHOR.getName();
        final Node roleTag = registerXmlWrapper.getDocument().createElement(ROLE_TAG_NAME);
        roleTag.setTextContent(role);
        registerXmlWrapper.getDocument().getDocumentElement().appendChild(roleTag);
        registerXmlWrapper.updateXml();

        String userId = this.userRepository.save(registerXmlWrapper);

        return userId;
    }

    /**
     * Returns XML String representation of {@link User} with given ID
     * @param id unique identifier of the {@link User}
     * @return String representation of {@link User} instance
     * @throws {@link com.scit.xml.exception.NotFoundException} in case the user is not found
     */
    public String findById(String id) {
        final String userXml = this.userRepository.findById(id);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(userXml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.USER, RestApiConstants.ID));
        return userXml;
    }

    /**
     * Returns XML String representation of {@link User} with given username
     * @param username username of the {@link User}
     * @return String representation of {@link User} instance
     * @throws {@link com.scit.xml.exception.NotFoundException} in case the user is not found
     */
    public String findByUsername(String username) {
        final String userXml = this.userRepository.findByUsername(username);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(userXml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.USER, RestApiConstants.USERNAME));
        return userXml;
    }

    /**
     * Returns a list of all authors in system
     * @return a list of XML String representations of {@link User} with {@link Role.AUTHOR} role
     */
    public List<String> getAllAuthors() {
        List<String> authors = this.userRepository.findAllAuthors();
        return authors;
    }
}
