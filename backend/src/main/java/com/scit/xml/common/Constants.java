package com.scit.xml.common;

/**
 * This class contains common constants
 */
public final class Constants {

    private Constants() { }

    public static final String EDITOR_USERNAME = "editor1";

    public static final String USERS_DOCUMENT_ID = "users.xml";
    public static final String PAPER_DOCUMENT_ID = "papers.xml";
    public static final String COVER_LETTER_DOCUMENT_ID = "cover_letters.xml";
    public static final String EVALUATION_FORM_DOCUMENT_ID = "evaluation_forms.xml";
    public static final String REVIEW_DOCUMENT_ID = "reviews.xml";

    private static final String SCHEMA_PATH = "http://www.scit.org/schema/";
    public static final String PAPER_SCHEMA_URL = SCHEMA_PATH + "paper";
    public static final String COVER_LETTER_SCHEMA_URL = SCHEMA_PATH + "cover_letter";
    public static final String EVALUATION_FORM_SCHEMA_URL = SCHEMA_PATH + "evaluation_form";
    public static final String REVIEW_SCHEMA_URL = SCHEMA_PATH + "review";

    /**
     * This class contains SPARQL Query templates
     */
    public final class SPARQLQueries {

        private SPARQLQueries() { }

        private static final String VOCABULARY_PREFIX = "PREFIX rv: <http://www.scit.org/rdfvocabulary/>\n\n";

        public static final String ASK_IS_PAPER_PUBLISHED_QUERY = VOCABULARY_PREFIX +
                "ASK WHERE { ?s " + Predicate.PUBLISHED + " <%s>.\n }";

        // TODO: This query should be "created", not submitted
        public static final String ASK_IS_USER_AUTHOR_OF_PAPER_QUERY = VOCABULARY_PREFIX +
                "ASK WHERE { <%s>" + Predicate.SUBMITTED + " <%s>.\n }";

        public static final String GET_SUBMITTED_PAPERS_QUERY = VOCABULARY_PREFIX +
                "SELECT ?o WHERE { ?s " + Predicate.SUBMITTED + " ?o.\n }";

        public static final String GET_ASSIGNED_PAPERS_QUERY = VOCABULARY_PREFIX +
                "SELECT ?o WHERE { <%s> " + Predicate.ASSIGNED_TO + " ?o.\n }";

        public static final String GET_PAPERS_IN_REVIEW_QUERY = VOCABULARY_PREFIX +
                "SELECT ?o WHERE { <%s> " + Predicate.CURRENTLY_REVIEWING + " ?o.\n }";

        public static final String GET_PAPERS_OF_USER_QUERY = VOCABULARY_PREFIX +
                "SELECT ?o WHERE { <%s> " + Predicate.CREATED + " ?o.\n }";

        public static final String GET_REVIEWED_PAPERS_QUERY = VOCABULARY_PREFIX +
                "SELECT DISTINCT ?o WHERE { ?s " + Predicate.REVIEWED + " ?o.\n }";

        public static final String GET_PUBLISHED_PAPERS_QUERY = VOCABULARY_PREFIX +
                "SELECT ?o WHERE { ?s " + Predicate.PUBLISHED + " ?o.\n }";

        public static final String GET_PUBLISHED_PAPERS_BY_METADATA_QUERY = VOCABULARY_PREFIX +
                "SELECT DISTINCT ?paperID WHERE { \n" +
                "  ?paperID " + Predicate.WAS_PUBLISHED_BY + " ?o ; rv:authors:author:name ?authorName .\n" +
                "  ?paperID rv:paper_info:doi %s .\n" +
                "  ?paperID rv:paper_info:journal_id %s .\n" +
                "  ?paperID rv:paper_info:category %s;\n" +
                "  rv:paper_info:submission_dates ?date .\n" +
                "  FILTER contains(?date, \"%s\")\n" +
                "  FILTER contains(?authorName, \"%s\")\n" +
                "}";

        public static final String GET_USERS_PAPERS_BY_METADATA_QUERY = VOCABULARY_PREFIX +
                "SELECT DISTINCT ?paperID WHERE { \n" +
                "  <%s> " + Predicate.CREATED + " ?paperID .\n" +
                "  ?paperID " + Predicate.TYPE_OF + " <http://www.scit.org/schema/paper> ; rv:authors:author:name ?authorName .\n" +
                "  ?paperID rv:paper_info:doi ?doi .\n" +
                "  ?paperID rv:paper_info:journal_id ?journalId .\n" +
                "  ?paperID rv:paper_info:category ?category;\n" +
                "  rv:paper_info:submission_dates ?date .\n" +
                "  FILTER contains(?date, \"\")\n" +
                "  FILTER contains(?authorName, \"\")\n" +
                "}";

        public static final String GET_AUTHORS_OF_PAPER_QUERY = VOCABULARY_PREFIX +
                "SELECT ?s WHERE { ?s " + Predicate.CREATED + " <%s>.\n }";

        public static final String GET_REVIEWERS_OF_PAPER_QUERY = VOCABULARY_PREFIX +
                "SELECT DISTINCT ?s WHERE { ?s " + Predicate.ASSIGNED_TO + "|" + Predicate.CURRENTLY_REVIEWING + "|" + Predicate.REVIEWED + " <%s>.\n }";


        public static final String GET_SUGGESTED_AUTHORS_QUERY = VOCABULARY_PREFIX +
                "SELECT DISTINCT ?s WHERE { ?s " + Predicate.TYPE_OF + " <http://www.scit.org/schema/user>\n" +
                "MINUS { ?s " + Predicate.CREATED + "|" + Predicate.ASSIGNED_TO + "|" + Predicate.CURRENTLY_REVIEWING + "|" + Predicate.REVIEWED + " <%s> } }" +
                "LIMIT 10";

        public static final String COUNT_PAPERS_OF_AUTHOR_CONTAINING_KEYWORD_QUERY = VOCABULARY_PREFIX +
                "SELECT (COUNT(distinct ?paper) as ?count) WHERE { ?paper " + Predicate.CONTAINS_KEYWORD + " \"%s\" . <%s> " + Predicate.CREATED + " ?paper }\n";

    }

}
