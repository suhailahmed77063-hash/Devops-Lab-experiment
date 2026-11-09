package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Unit tests executed by Jenkins during the build stage. */
public class AppTest {

    @Test
    public void greetingReturnsHelloWorld() {
        assertEquals("Hello World", App.greeting());
    }

    @Test
    public void addSumsTwoPositiveNumbers() {
        assertEquals(5, App.add(2, 3));
    }

    @Test
    public void addHandlesNegativeNumbers() {
        assertEquals(-1, App.add(2, -3));
    }
}
