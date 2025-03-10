package com.globits.da.utils;

import java.util.regex.Pattern;

public class ValidationUtil {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_REGEX = "^[0-9]{10,11}$";
    private static final String CODE_REGEX = "^[A-Za-z0-9]+$";

    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }


    public static boolean isPhone(String phone) {

        return phone != null && Pattern.matches(PHONE_REGEX, phone);
    }

    public static boolean isValidCode(String code) {
        return code != null && Pattern.matches(CODE_REGEX, code);
    }

}
