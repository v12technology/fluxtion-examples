package com.fluxtion.example;

public class Shape {
    private final int width;
    private final int height;
    private final int sides;

    private Shape(int width, int height, int sides) {
        this.width = width;
        this.height = height;
        this.sides = sides;
    }

    public final int getWidth() {
        return width;
    }

    public final int getHeight() {
        return height;
    }

    public final int getSides() {
        return sides;
    }

    public final int filterId() {
        return sides;
    }

    public static class Square extends Shape {
        public Square(int length) {
            super(length, length, 4);
        }
    }

    public static class Rectangle extends Shape {
        public Rectangle(int length, int width) {
            super(length, width, 4);
        }
    }

    public static class Circle extends Shape {
        public Circle(int radius) {
            super(radius, radius, 0);
        }
    }


    public static class Triangle extends Shape {
        public Triangle(int base, int height) {
            super(base, height, 3);
        }
    }
}
