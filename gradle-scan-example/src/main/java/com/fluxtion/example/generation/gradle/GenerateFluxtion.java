package com.fluxtion.example.generation.gradle;

import com.fluxtion.compiler.Fluxtion;

import java.io.File;

public class GenerateFluxtion {
    public static void main(String[] args) throws Exception {
        File classesDirectory = new File("build/classes/java/main");
        System.out.println("Scanning " + classesDirectory.getAbsolutePath() + " for builders");
        Fluxtion.scanAndCompileFluxtionBuilders(classesDirectory);
    }
}