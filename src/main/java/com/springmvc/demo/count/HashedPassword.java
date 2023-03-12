package com.springmvc.demo.count;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashedPassword {
    public static String hash(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    public static boolean match(String password, String insertPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(insertPassword);
        if (passwordEncoder.matches(password, hashedPassword)) {
            return true;
        } else {
            return false;
        }
    }
}
