package com.example;

/** Simple Java application used to demonstrate CI/CD with Jenkins. */
public final class App {

    private App() {
    }

    /** Returns the greeting printed by the build. */
    public static String greeting() {
        return "Hello World";
    }

    /** Simple calculation used by the unit tests. */
    public static int add(int first, int second) {
        return first + second;
    }

    public static void main(String[] args) {
        System.out.println(greeting());
        System.out.println("2 + 3 = " + add(2, 3));
    }
}
