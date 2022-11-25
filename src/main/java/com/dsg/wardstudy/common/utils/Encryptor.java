package com.dsg.wardstudy.common.utils;

import org.mindrot.jbcrypt.BCrypt;

public class Encryptor {

    public static String encrypt(String origin) {
        return BCrypt.hashpw(origin, BCrypt.gensalt());
    }

    public static boolean isMatch(String origin, String hashed) {
        try {
            return BCrypt.checkpw(origin, hashed);
        } catch (Exception e) { // 여러 예외가 있다.
            return false;
        }
    }
}
