package com.tomkasp.registration;

public class Constants {

    public static final String ALPHANUMERIC_REGEX = "^[a-zA-Z0-9]*$";
    public static final String PASSWORD_REGEX = "^.*(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$";

    //error Messages
    public static final String USERNAME_LENGTH_ERROR ="The username length must be no less than 5 characters. ";
    public static final String USERNAME_ALPHANUMERIC_ERROR = "The username field accepts alpha-numeric values only. ";
    public static final String PASSWORD_LENGTH_ERROR = "The password must have a minimum length of 8 characters. ";
    public static final String PASSWORD_CHARACTERS_ERROR = " The password must contains at least 1 number, 1 uppercase, and 1 lowercase character. ";

}
