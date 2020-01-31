package com.scit.xml.common.api;

public final class RestApiEndpoints {

    private RestApiEndpoints() { }

    private static final String API_ROOT = "/api/v1";

    public static final String USER = API_ROOT + "/user";
    public static final String USERS = API_ROOT + "/users";
    public static final String CURRENT_USER = "/me";

    public static final String PAPER = API_ROOT + "/paper";
    public static final String DOWNLOAD_RAW = "/raw/download";
    public static final String DOWNLOAD_PDF = "/pdf/download";
    public static final String ASSIGN = "/assign";
    public static final String ANONYMOUS =  "/anonymous";
    public static final String AUTHOR =  "/author";
    public static final String ASSIGNED =  "/assigned";
    public static final String SUBMITTED =  "/submitted";
    public static final String IN_REVIEW = "/in_review";

    public static final String PAPERS = API_ROOT + "/papers";

    public static final String COVER_LETTER = API_ROOT + "/cover-letter";
    public static final String COVER_LETTERS = API_ROOT + "/cover-letters";

    public static final String REVIEW = API_ROOT + "/review";
    public static final String REVIEWS = API_ROOT + "/reviews";
    public static final String ACCEPT = "/accept";
    public static final String DECLINE = "/decline";


    public static final String EVALUATION_FORM = API_ROOT + "/evaluation-form";
    public static final String EVALUATION_FORMS = API_ROOT + "/evaluation-forms";

    public static final String TEST = "/test";
    public static final String TEST_AUTHOR = "/author";
    public static final String TEST_EDITOR = "/editor";
}
