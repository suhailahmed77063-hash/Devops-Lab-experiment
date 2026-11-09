package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Experiment 11 - test the JavaScript counter page with Selenium.
 *
 * The page is opened from the local filesystem, so no web server is required.
 */
public class IncrementButtonTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1280,800", "--no-sandbox");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @Test
    public void testIncrementButton() {
        Path page = Path.of(System.getProperty("user.dir"), "program.html").toAbsolutePath();
        driver.get(page.toUri().toString());

        assertEquals("0", driver.findElement(By.id("output")).getText());

        driver.findElement(By.id("increment-button")).click();
        assertEquals("1", driver.findElement(By.id("output")).getText());

        driver.findElement(By.id("increment-button")).click();
        assertEquals("2", driver.findElement(By.id("output")).getText());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
