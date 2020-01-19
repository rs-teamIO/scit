package com.scit.xml.repository;

import static java.util.UUID.randomUUID;

import com.scit.xml.common.util.ResourceSetUtils;
import com.scit.xml.common.util.XmlMapper;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import com.scit.xml.model.user.Person;
import com.scit.xml.model.user.User;
import lombok.RequiredArgsConstructor;
import org.exist.xquery.functions.util.BaseConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.ResourceSet;

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

    public UserRepository(XQueryBuilder xQueryBuilder, XQueryExecutor xQueryExecutor) {
        super(xQueryBuilder, xQueryExecutor);
    }

    public String save(User user, Person person) {
        user.setId(randomUUID().toString());
        String xml = XmlMapper.toXml(user, person);
        String query = this.xQueryBuilder.buildQuery(this.appendTemplate, USER_NAMESPACE_ALIAS, USER_NAMESPACE, USERS_COLLECTION, xml, USERS_NAMESPACE);

        this.xQueryExecutor.updateResource(DOCUMENT_ID, query);

        return String.format(USERS_NAMESPACE_FORMAT, user.getId());
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
}
