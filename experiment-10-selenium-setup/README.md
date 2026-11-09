# Experiment 10 — Install and explore Selenium for automated testing

**Aim:** Install Selenium, write a simple test and run it.

**Tools:** JDK 17, Maven 3.9, Selenium 4.27, JUnit 5, Google Chrome

## 1. Install the prerequisites

| Component | How to install | Check |
|-----------|----------------|-------|
| JDK 17 | <https://adoptium.net/> | `java -version` |
| Maven 3.9+ | <https://maven.apache.org/download.cgi> | `mvn -version` |
| Chrome | <https://www.google.com/chrome/> | `chrome --version` |

ChromeDriver is **not** installed manually. Selenium 4.6+ includes Selenium
Manager, which detects the installed browser and downloads the matching driver
into `~/.cache/selenium` on first run.

> The printed manual calls `System.setProperty("webdriver.chrome.driver", "path/to/chromedriver")`.
> That still works if you want to place a driver manually, but it is error-prone
> (the driver version must match Chrome exactly) and unnecessary in Selenium 4.
> This folder relies on Selenium Manager instead.

## 2. Files

| File | Purpose |
|------|---------|
| `pom.xml` | Selenium + JUnit 5 dependencies, Surefire |
| `src/test/java/com/example/GoogleTitleTest.java` | Opens google.com, prints the title, asserts it is non-empty |

## 3. Run the test

```bash
mvn -q test
```

Output (abridged):

```console
Page title: Google
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## 4. Explore the WebDriver API

Add these calls inside a test to explore the environment:

```java
driver.get("https://example.com");            // navigate
driver.getTitle();                            // page title
driver.getCurrentUrl();                       // current URL
driver.getPageSource();                       // HTML source
driver.findElement(By.id("id"));              // locate an element
driver.findElements(By.tagName("a")).size();  // count links
driver.manage().window().maximize();
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
```

Common locator strategies: `By.id`, `By.name`, `By.className`, `By.cssSelector`,
`By.xpath`, `By.linkText`. Prefer `id`/CSS selectors — XPath is the slowest and
most brittle.

## 5. Plain `javac` / `java` route from the manual

```bash
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
javac -cp "target/test-classes:$(cat cp.txt)" Main.java
java  -cp ".:$(cat cp.txt)" Main
```

A headless run works on servers without a display; drop `--headless=new` from
`ChromeOptions` to watch the browser locally.

## Viva notes

* **Selenium WebDriver** drives a real browser through the W3C WebDriver protocol;
  it is used for end-to-end UI testing.
* **ChromeDriver** is the browser-specific bridge between the test and Chrome.
* **Selenium Grid** runs tests in parallel across browsers and machines.
* **Implicit wait** applies to all `findElement` calls; **explicit wait**
  (`WebDriverWait`) applies to one condition and is preferred for dynamic pages.
* `driver.quit()` closes the browser and ends the session; `close()` closes only
  the current window.
