package com.scit.xml.common.api;

public final class RestApiEndpoints {

    private RestApiEndpoints() { }

    private static final String API_ROOT = "/api/v1";

    public static final String USER = API_ROOT + "/user";
    public static final String USERS = API_ROOT + "/users";
    public static final String CURRENT_USER = "/me";
    public static final String AUTHORS = "/authors";
    public static final String REVIEWERS = "/reviewers";
    public static final String RECOMMENDED_AUTHORS = "/recommended-authors";

    public static final String PAPER = API_ROOT + "/paper";
    public static final String DOWNLOAD_RAW = "/raw/download";
    public static final String DOWNLOAD_PDF = "/pdf/download";
    public static final String ASSIGN = "/assign";
    public static final String ANONYMOUS =  "/anonymous";
    public static final String AUTHOR =  "/author";
    public static final String ASSIGNED =  "/assigned";
    public static final String SUBMITTED =  "/submitted";
    public static final String IN_REVIEW = "/in-review";
    public static final String REVIEWED = "/reviewed";
    public static final String HTML = "/html";
    public static final String TRANSFORM = "/transform";
    public static final String SEARCH_BY_TEXT = "/search-by-text";
    public static final String SEARCH_BY_TEXT_PRIVATE = "/search-by-text/private";
    public static final String SEARCH_BY_METADATA = "/search-by-metadata";
    public static final String SEARCH_BY_METADATA_PRIVATE = "/search-by-metadata/private";

    public static final String PAPERS = API_ROOT + "/papers";
    public static final String PUBLISHED = "/published";

    public static final String COVER_LETTER = API_ROOT + "/cover-letter";
    public static final String COVER_LETTERS = API_ROOT + "/cover-letters";

    public static final String REVIEW = API_ROOT + "/review";
    public static final String REVIEWS = API_ROOT + "/reviews";
    public static final String ACCEPT = "/accept";
    public static final String DECLINE = "/decline";
    public static final String REJECT = "/reject";
    public static final String PUBLISH = "/publish";

    public static final String EVALUATION_FORM = API_ROOT + "/evaluation-form";
    public static final String EVALUATION_FORMS = API_ROOT + "/evaluation-forms";

    public static final String TEST = "/test";
    public static final String TEST_AUTHOR = "/author";
    public static final String TEST_EDITOR = "/editor";
}
