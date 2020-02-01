package com.scit.xml.repository;

import com.scit.xml.common.Constants;
import com.scit.xml.common.util.ResourceSetUtils;
import com.scit.xml.config.XQueryBuilder;
import com.scit.xml.config.XQueryExecutor;
import com.scit.xml.model.evaluation_form.EvaluationForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xmldb.api.base.ResourceSet;

import static java.util.UUID.randomUUID;

@Component
public class EvaluationFormRepository extends BaseRepository {

    private final String EVALUATION_FORM_NAMESPACE_ALIAS = "evaluation_form";
    private final String EVALUATION_FORM_NAMESPACE = "http://www.scit.org/schema/evaluation_form";
    private final String EVALUATION_FORMS_COLLECTION = "/evaluation_forms:evaluation_forms";
    private final String EVALUATION_FORMS_NAMESPACE = "xmlns:evaluation_forms=\"http://www.scit.org/schema/evaluation_forms\"";

    private final String EVALUATION_FORM_NAMESPACE_FORMAT = "http://www.scit.org/evaluation_form/%s";

    @Value("classpath:xq/evaluation_form/findById.xq")
    private Resource findByIdQuery;

    public EvaluationFormRepository(XQueryBuilder xQueryBuilder, XQueryExecutor xQueryExecutor) {
        super(xQueryBuilder, xQueryExecutor, Constants.EVALUATION_FORM_DOCUMENT_ID);
    }

    public String save(EvaluationForm evaluationForm) {
        String id = String.format(EVALUATION_FORM_NAMESPACE_FORMAT, randomUUID().toString());
        evaluationForm.setId(id);

        String xml = this.marshal(EvaluationForm.class, evaluationForm);
        String query = this.xQueryBuilder.buildQuery(this.appendTemplate, EVALUATION_FORM_NAMESPACE_ALIAS,
                EVALUATION_FORM_NAMESPACE, EVALUATION_FORMS_COLLECTION, xml, EVALUATION_FORMS_NAMESPACE);

        this.xQueryExecutor.updateResource(this.documentId, query);

        return id;
    }

    public String findById(String id) {
        String query = xQueryBuilder.buildQuery(findByIdQuery, id);
        ResourceSet resourceSet = xQueryExecutor.execute(this.documentId, query);

        return ResourceSetUtils.toXml(resourceSet);
    }
}
