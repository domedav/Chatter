package com.domedav.chatter.ui.login.credentials;

public class CredentialValidator {
    public static boolean GoodForProceed_Username;
    public static boolean GoodForProceed_Password;
    public enum LoginUsernameErrorType{
        username_short,
        username_long,
        username_invalid,
        ignore
    }
    public static LoginUsernameErrorType ValidateUsername(String username){
        if(username.length() <= 3){
            return LoginUsernameErrorType.username_short;
        }
        else if(username.length() > 24){
            return LoginUsernameErrorType.username_long;
        }
        else if(!username.matches("[a-zA-Z0-9_.]*") || CountChars(username, '.') > 2){
            return LoginUsernameErrorType.username_invalid;
        }
        return LoginUsernameErrorType.ignore;
    }

    private static int CountChars(String main, char match){
        char[] chars = main.toCharArray();
        int count = 0;
        for (int i = 0; i < chars.length; i++)
        {
            if(chars[i] == match){
                count++;
            }
        }
        return count;
    }

    public static boolean ValidatePasswordLength(int plen){
        return plen >= 5;
    }
}
