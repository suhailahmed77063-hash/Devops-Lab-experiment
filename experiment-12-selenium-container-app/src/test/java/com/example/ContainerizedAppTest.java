package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Experiment 12 - Selenium test cases for the containerized application.
 *
 * Start the container first:
 *   docker build -t my-container-app .
 *   docker run -d --name my-container-app -p 8080:8080 my-container-app
 *
 * Override the URL when the app runs elsewhere:
 *   mvn test -Dapp.url=http://localhost:9090
 */
public class ContainerizedAppTest {

    private WebDriver driver;
    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = System.getProperty("app.url", "http://localhost:8080");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1280,800", "--no-sandbox");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }

    @Test
    public void testHomePageLoads() {
        driver.get(baseUrl);

        String title = driver.getTitle();
        System.out.println("Application title: " + title);
        assertEquals("My Containerized Application", title);
        assertTrue(driver.findElement(By.name("name")).isDisplayed());
    }

    @Test
    public void testSubmitForm() {
        driver.get(baseUrl);

        driver.findElement(By.name("name")).sendKeys("John Doe");
        driver.findElement(By.name("email")).sendKeys("john.doe@example.com");
        driver.findElement(By.name("submit")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBe(
                By.id("result"), "Form submitted successfully!"));

        String result = driver.findElement(By.id("result")).getText();
        assertEquals("Form submitted successfully!", result);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
