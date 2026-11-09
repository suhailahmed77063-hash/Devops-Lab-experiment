# Experiment 11 — A JavaScript program tested with Selenium

**Aim:** Write a simple JavaScript program and test it using Selenium.

**Tools:** HTML + JavaScript, Selenium 4.27, JUnit 5, Maven, Chrome

## Files

| File | Purpose |
|------|---------|
| `program.html` | Counter page: `#output` starts at 0, `#increment-button` adds 1 |
| `pom.xml` | Selenium + JUnit 5 |
| `src/test/java/com/example/IncrementButtonTest.java` | Clicks the button and asserts the counter text |

## The program

```html
<p id="output">0</p>
<button id="increment-button">Increment</button>
<script>
  const output = document.getElementById("output");
  const incrementButton = document.getElementById("increment-button");
  let count = 0;
  incrementButton.addEventListener("click", function () {
    count += 1;
    output.innerHTML = count;
  });
</script>
```

## Run the test

```bash
mvn -q test
```

Expected result:

```console
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

The test opens the page through a `file:///` URL built from the project
directory, so it works on Windows, Linux and macOS without a local web server:

```java
Path page = Path.of(System.getProperty("user.dir"), "program.html").toAbsolutePath();
driver.get(page.toUri().toString());
```

## What is asserted

| Step | Assertion |
|------|-----------|
| Page loaded | `#output` text is `0` |
| First click | `#output` text is `1` |
| Second click | `#output` text is `2` |

## Notes

* The manual's snippet calls `assert result.equals("1")`. `assert` is disabled
  unless the JVM runs with `-ea`, so a failing comparison would be silently
  ignored. JUnit's `assertEquals` always runs and reports the real and expected
  values in the failure message.
* `--headless=new` lets the test run on a machine without a display; remove the
  flag to watch the browser.
* Opening `file://` URLs tests the page logic only. If the page makes network
  calls, serve it over HTTP (`python -m http.server`) and use `http://localhost`.
