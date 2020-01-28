package com.scit.xml.service;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.util.BadRequestUtils;
import com.scit.xml.common.util.NotFoundUtils;
import com.scit.xml.common.util.XmlExtractorUtil;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.model.user.Role;
import com.scit.xml.repository.UserRepository;
import com.scit.xml.security.JwtTokenDetailsUtil;
import com.scit.xml.service.validator.database.RegisterDatabaseValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Service
@RequiredArgsConstructor
public class UserService {

    private final String USERNAME = "/user/username";
    private final String EMAIL = "/user/email";

    private final UserRepository userRepository;
    private final RegisterDatabaseValidator registerDatabaseValidator;

    public String register(XmlWrapper registerXmlWrapper) {
        Document document = registerXmlWrapper.getDocument();
        final String username = XmlExtractorUtil.extractStringAndValidateNotBlank(document, this.USERNAME);
        final String email = XmlExtractorUtil.extractStringAndValidateNotBlank(document, this.EMAIL);
        this.registerDatabaseValidator.validate(username, email);

        final String role = this.getRoleFromRestAPI("author").getName();
        final Node roleTag = registerXmlWrapper.getDocument().createElement("user:role");
        roleTag.setTextContent(role);
        registerXmlWrapper.getDocument().getDocumentElement().appendChild(roleTag);
        registerXmlWrapper.updateXml();

        String userId = this.userRepository.save(registerXmlWrapper);

        return userId;
    }

    public String findById(String id) {
        final String userXml = this.userRepository.findById(id);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(userXml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.USER, RestApiConstants.ID));
        return userXml;
    }

    // TODO: Should be changed
    public String getUserEmail(String username) {
        final String userXml = this.userRepository.findByUsername(username);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(userXml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.USER, RestApiConstants.USERNAME));
        final Document document = new XmlWrapper(userXml).getDocument();
        return XmlExtractorUtil.extractStringAndValidateNotBlank(document, "user/email");
    }

    private Role getRoleFromRestAPI(String roleString) {
        Role role = Role.getRoleByName(roleString);
        BadRequestUtils.throwInvalidRequestDataExceptionIf(role == null, "Unexpected user role value: '" + roleString + "'");
        return role;
    }
}
