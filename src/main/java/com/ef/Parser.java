package com.ef;

public class Parser {


    private static final String SEPARATOR = "=";

    public static void main(String[] args) throws Exception {

        String filePath = args[0].split(SEPARATOR)[1];
        String startDate = args[1].split(SEPARATOR)[1];
        String duration = args[2].split(SEPARATOR)[1];
        String threshold = args[3].split(SEPARATOR)[1];

        System.out.println("filePath : " + filePath);
        System.out.println("startDate : " + startDate);
        System.out.println("duration : " + duration);
        System.out.println("threshold : " + threshold);
    }
}
