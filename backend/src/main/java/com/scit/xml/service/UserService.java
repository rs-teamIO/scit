package com.scit.xml.service;

import com.scit.xml.common.api.RestApiConstants;
import com.scit.xml.common.api.RestApiErrors;
import com.scit.xml.common.util.BadRequestUtils;
import com.scit.xml.common.util.NotFoundUtils;
import com.scit.xml.common.util.XmlExtractorUtil;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.model.user.Role;
import com.scit.xml.repository.RdfRepository;
import com.scit.xml.repository.UserRepository;
import com.scit.xml.security.JwtTokenDetailsUtil;
import com.scit.xml.service.validator.database.RegisterDatabaseValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xmldb.api.base.XMLDBException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final String USERNAME = "/user/username";
    private final String EMAIL = "/user/email";

    private final UserRepository userRepository;
    private final RegisterDatabaseValidator registerDatabaseValidator;
    private final RdfRepository rdfRepository;

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

    public String findByUsername(String username) {
        final String userXml = this.userRepository.findByUsername(username);
        NotFoundUtils.throwNotFoundExceptionIf(StringUtils.isEmpty(userXml),
                RestApiErrors.entityWithGivenFieldNotFound(RestApiConstants.USER, RestApiConstants.USERNAME));
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

    public String getAllAuthors() {
        StringBuilder sb = new StringBuilder();
        this.userRepository.findAllAuthors().stream().map(s -> {
            return convertToXmlResponseString(s);
        }).collect(Collectors.toList()).forEach(s -> {
            sb.append(s.trim());
        });

        return sb.toString();
    }

    private final String SPARQL_GET_REVIEWERS_OF_PAPER_QUERY = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n" +
            "\n" + "SELECT DISTINCT ?s\n" + "WHERE {\n" + "\t?s rv:assigned_to|rv:currently_reviewing|rv:reviewed <%s>.\n" + "}";

    public String getReviewersOfPaper(String paperId) {
        List<String> userIds = rdfRepository.selectSubjects(String.format(SPARQL_GET_REVIEWERS_OF_PAPER_QUERY, paperId));

        StringBuilder sb = new StringBuilder();
        userIds.stream().map(s -> {
            return convertToXmlResponseString(this.findById(s));
        }).collect(Collectors.toList()).forEach(s2 -> {
            sb.append(s2);
        });

        return sb.toString();
    }

    private String convertToXmlResponseString(String xml) {
        XmlWrapper xmlWrapper = new XmlWrapper(xml);
        String username = XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), "/user/username");
        String email = XmlExtractorUtil.extractStringAndValidateNotBlank(xmlWrapper.getDocument(), "/user/email");

        // TODO: Refactor
        StringBuilder sb = new StringBuilder();
        String indent = "  ";

        sb.append("<user>\n");
        sb.append(indent).append("<username>");
        sb.append(username);
        sb.append("</username>");
        sb.append(indent).append("<email>");
        sb.append(email);
        sb.append("</email>");
        sb.append("</user>");

        return sb.toString();
    }
}
