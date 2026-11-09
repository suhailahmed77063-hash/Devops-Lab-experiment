package com.example;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Experiment 10 - install and explore Selenium.
 *
 * Selenium 4.6 and later download and manage ChromeDriver automatically through
 * Selenium Manager, so no "webdriver.chrome.driver" path is needed.
 */
public class GoogleTitleTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1280,800", "--no-sandbox");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @Test
    public void opensGoogleAndPrintsTitle() {
        driver.get("https://www.google.com");
        String title = driver.getTitle();
        System.out.println("Page title: " + title);
        assertFalse(title.isBlank(), "The page title should not be empty");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
