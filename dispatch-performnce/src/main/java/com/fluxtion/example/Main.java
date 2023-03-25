package com.fluxtion.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Object obj = new Object();
        Object x = switch (obj){
            case Integer i -> 22;
            default -> "";
        };
    }


}