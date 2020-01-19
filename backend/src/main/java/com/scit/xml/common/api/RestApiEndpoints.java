package com.scit.xml.common.api;

public final class RestApiEndpoints {

    private RestApiEndpoints() { }

    private static final String API_ROOT = "/api/v1";

    public static final String USER = API_ROOT + "/user";
    public static final String USERS = API_ROOT + "/users";

    public static final String PAPER = API_ROOT + "/paper";
    public static final String PAPERS = API_ROOT + "/papers";

    public static final String TEST = "/test";
    public static final String TEST_AUTHOR = TEST + "/author";
    public static final String TEST_EDITOR = TEST + "/editor";
}
