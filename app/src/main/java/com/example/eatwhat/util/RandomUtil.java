package com.example.eatwhat.util;

public class RandomUtil {

    // define the range

    public static int getRandomNumber(int max, int min){
        int range = max - min + 1;
        int rand = (int)(Math.random() * range) + min;
        return rand;
    }

}
