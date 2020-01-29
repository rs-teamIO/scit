package com.scit.xml.common;

/**
 * This class contains common constants
 */
public final class Constants {

    private Constants() { }

    public static final String EDITOR_USERNAME = "editor1";

    public static final String PAPER_DOCUMENT_ID = "papers.xml";
    public static final String COVER_LETTER_DOCUMENT_ID = "cover_letters.xml";
    public static final String EVALUATION_FORM_DOCUMENT_ID = "evaluation_forms.xml";

    private static final String SCHEMA_PATH = "http://www.scit.org/schema/";
    public static final String PAPER_SCHEMA_URL = SCHEMA_PATH + "paper";
    public static final String COVER_LETTER_SCHEMA_URL = SCHEMA_PATH + "cover_letter";
    public static final String EVALUATION_FORM_SCHEMA_URL = SCHEMA_PATH + "evaluation_form";
}
