package com.fluxtion.example.reference.dsl;

public class MyFunctions {

    private long totalCharCount;
    private long upperCaseCharCount;

    public static long charCount(String s) {
        return s.length();
    }

    public static long upperCaseCharCount(String s) {
        return s.chars().filter(Character::isUpperCase).count();
    }

    public long totalCharCount(String s) {
        totalCharCount += charCount(s);
        return totalCharCount;
    }

    public long totalUpperCaseCharCount(String s) {
        upperCaseCharCount += upperCaseCharCount(s);
        return upperCaseCharCount;
    }

    public static double wordUpperCasePercentage(long longA, long longB) {
        return (double) longA /longB;
    }

    public static class SimpleMath {
        private double a;
        private double b;
        private double percentage;

        public double updatePercentage(long longA, long longB) {
            a += longA;
            b += longB;
            percentage = a / b;
            return percentage;
        }
    }
}
