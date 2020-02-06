package com.scit.xml.common;

/**
 * This class contains predicate constants for RDF
 */
public final class Predicate {

    private Predicate() { }

    public static final String PREFIX = "rv";

    public static final String CREATED =  PREFIX + ":created";
    public static final String SUBMITTED =  PREFIX + ":submitted";
    public static final String CONTAINS_KEYWORD = PREFIX + ":abstract:keywords";
    public static final String EVALUATES = PREFIX + ":evaluates";
    public static final String ACCOMPANIES = PREFIX + ":accompanies";
    public static final String ASSIGNED_TO = PREFIX + ":assigned_to";
    public static final String CURRENTLY_REVIEWING = PREFIX + ":currently_reviewing";
    public static final String REVIEWS = PREFIX + ":reviews";
    public static final String WRITTEN_BY = PREFIX + ":written_by";
    public static final String REVIEWED = PREFIX + ":reviewed";
    public static final String PUBLISHED = PREFIX + ":published";
    public static final String WAS_PUBLISHED_BY = PREFIX + ":was_published_by";
    public static final String TYPE_OF = PREFIX + ":type_of";

}
