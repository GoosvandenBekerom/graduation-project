//*************************************************//
//          INTHER LOGISTICS ENGINEERING           //
//*************************************************//

package com.inthergroup.webpoc.framework.config;

/**
 * @author gvandenbekerom
 * @since 17-Sep-18
 *
 * Constant values that are used in multiple locations throughout the application
 */
public class Constants {
    // REGEX patterns
    public static final String REGEX_PATTERN_URL = "^((https?|ftp)?:\\/\\/)?((([A-z\\d_]+\\.)?(localhost|[A-z\\d_-]+[.][A-z]+))|((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])))(:\\d+)?(\\/[^\\s\\/$.?#]+)*\\/?((\\?([A-z]|\\d)+=[^:\\/?#\\[\\]@!$&'()*+,;=._~ ]+)(\\&([A-z]|\\d)+=[^:\\/?#\\[\\]@!$&'()*+,;=._~ ]+)*)?(#[A-z\\d_-]+)?$";

    // Constant numbers
    public static final int MAX_MENU_DEPTH = 2;

    // Validation messages
    public static final String VALIDATION_MESSAGE_MENU_ACTION = "Make sure to enter all required fields for the chosen ActionType";
    public static final String VALIDATION_MESSAGE_SELECT_QUERY = "Query validation failed. Make sure the sql is a valid SELECT statement";

    // Constant strings
    public static final String QUERY_TOTAL_COUNT_KEY = "total_count";

    // Security constants
    public static final String PASSWORD_SALT = "inther-secret-salt";
    public static final String TOKEN_SECRET = "inther-token-secret";
    public static final String AUTHORIZATION_PREFIX = "Bearer";
    public static final String AUTH_GOD_MODE = "DEV";
}
