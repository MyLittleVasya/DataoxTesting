package com.code.dataoxtesting.utils;

import lombok.experimental.UtilityClass;

/**
 * Constants for whole application.
 */
@UtilityClass
public class ApplicationConstants {

  public static final String NOT_FOUND_PLACEHOLDER = "NOT_FOUND";

  public static final String TAGS_DELIMITER = ", ";

  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd";

  public static final String HTTP = "http";

  /**
   * Constants related to html. There can be attribute names, css selector, etc.
   */
  @UtilityClass
  public class HtmlRelated {

    public static final String JOBS_CSS_SELECTOR = "div.job-card";

    public static final String HREF_ATTRIBUTE = "href";

    public static final String ITEMPROP_ATTRIBUTE = "itemprop";

    public static final String DATA_TESTID_ATTRIBUTE = "data-testid";

    public static final String CONTENT_ATTRIBUTE = "content";

    public static final String LINK_ATTRIBUTE = "link";

    public static final String ADDRESS_ATTRIBUTE = "address";

    public static final String DATE_POSTED_ATTRIBUTE = "datePosted";

    public static final String LOGO_ATTRIBUTE = "logo";

    public static final String READ_MORE_ATTRIBUTE = "read-more";

    public static final String TITLE_ATTRIBUTE = "title";

    public static final String TAG_ATTRIBUTE = "tag";

    public static final String CAREER_ATTRIBUTE = "careerPage";

  }

}
