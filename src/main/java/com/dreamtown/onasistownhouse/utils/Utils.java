package com.dreamtown.onasistownhouse.utils;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class Utils {

    public String[] checkNullOrEmptyString(String word) {
        try {
            if (word.trim().equalsIgnoreCase("")) {
                return null;
            } else {
                if (word.trim().contains("\n")) {
                    return word.split("\n");
                } else {
                    return new String[] { word.trim() };
                }
            }
        } catch (NullPointerException e) {
            return null;
        }
    }

    public Integer getRandomIndex(Integer length) {
        Random rand = new Random();
        return rand.nextInt(length);
    }
}
