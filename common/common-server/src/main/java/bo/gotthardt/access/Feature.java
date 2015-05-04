package bo.gotthardt.access;

import com.avaje.ebean.annotation.EnumValue;

/**
 * A feature is a specific part of the system that is is possible to have access to or not.
 */
public enum Feature {
    @EnumValue("BLAH1")
    BLAH1,
    @EnumValue("BLAH2")
    BLAH2,
    @EnumValue("BLAH3")
    BLAH3,
    @EnumValue("TWO_FACTOR_LOGIN")
    TWO_FACTOR_LOGIN,
    @EnumValue("TWO_FACTOR_LOGIN_REQUIRED")
    TWO_FACTOR_LOGIN_REQUIRED
}
