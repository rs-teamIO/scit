package com.scit.xml.repository;

import com.scit.xml.common.util.ResourceSetUtils;
import com.scit.xml.common.util.XmlWrapper;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

import java.util.List;

import static java.util.UUID.randomUUID;

@Component
public class UserRepository extends BaseRepository {

    private final String DOCUMENT_ID = "users.xml";
    private final String USER_NAMESPACE_ALIAS = "user";
    private final String USER_NAMESPACE = "http://www.scit.org/schema/user";
    private final String USERS_COLLECTION = "/users:users";
    private final String USERS_NAMESPACE = "xmlns:users=\"http://www.scit.org/schema/users\"";

    private final String USERS_NAMESPACE_FORMAT = "http://www.scit.org/users/%s";

    @Value("classpath:xq/user/findById.xq")
    private Resource findByIdQuery;

    @Value("classpath:xq/user/findByUsername.xq")
    private Resource findByUsernameQuery;

    @Value("classpath:xq/user/findByEmail.xq")
    private Resource findByEmailQuery;

    @Value("classpath:xq/user/findAllByRole.xq")
    private Resource findAllByRoleQuery;

    public UserRepository(XQueryBuilder xQueryBuilder, XQueryExecutor xQueryExecutor) {
        super(xQueryBuilder, xQueryExecutor);
    }

    public String save(XmlWrapper xmlWrapper) {
        final String id = String.format(USERS_NAMESPACE_FORMAT, randomUUID().toString());
        xmlWrapper.getDocument().getDocumentElement().setAttribute("id", id);
        xmlWrapper.updateXml();

        String xml = xmlWrapper.getXml();
        final String query = this.xQueryBuilder.buildQuery(this.appendTemplate, USER_NAMESPACE_ALIAS, USER_NAMESPACE, USERS_COLLECTION, xml, USERS_NAMESPACE);
        this.xQueryExecutor.updateResource(DOCUMENT_ID, query);

        return id;
    }

    public String findById(String id) {
        String query = xQueryBuilder.buildQuery(findByIdQuery, id);
        ResourceSet resourceSet = xQueryExecutor.execute(DOCUMENT_ID, query);

        return ResourceSetUtils.toXml(resourceSet);
    }

    public String findByUsername(String username) {
        String query = xQueryBuilder.buildQuery(findByUsernameQuery, username);
        ResourceSet resourceSet = xQueryExecutor.execute(DOCUMENT_ID, query);

        return ResourceSetUtils.toXml(resourceSet);
    }

    public String findByEmail(String email) {
        String query = xQueryBuilder.buildQuery(findByEmailQuery, email);
        ResourceSet resourceSet = xQueryExecutor.execute(DOCUMENT_ID, query);

        return ResourceSetUtils.toXml(resourceSet);
    }

    public List<String> findAllAuthors() {
        String query = xQueryBuilder.buildQuery(findAllByRoleQuery, "author");
        ResourceSet resourceSet = xQueryExecutor.execute(DOCUMENT_ID, query);

        return ResourceSetUtils.toList(resourceSet);
    }
}
