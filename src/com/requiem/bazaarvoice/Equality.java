package com.requiem.bazaarvoice;

/**
 * User: gary
 * Date: 4/24/12
 * Time: 9:39 PM
 *
 * Enum for all available equality types in BazaarParams requests
 */
public enum Equality {
    LESS_THAN("lt"),
    LESS_THAN_EQ("lte"),
    EQUAL("eq"),
    GREATER_THAN("gt"),
    GREATER_THAN_EQ("gte");

    private String equalityStr;

    Equality(String text) {
        equalityStr = text;
    }

    public String getEquality(){
        return equalityStr;
    }

    public static Equality fromString(String text) {
        if (text != null) {
            for (Equality b : Equality.values()) {
                if (text.equalsIgnoreCase(b.equalityStr)) {
                    return b;
                }
            }
        }
        return null;
    }
}
