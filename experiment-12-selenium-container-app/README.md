# Experiment 12 — Selenium test cases for the containerized application

**Aim:** Develop Selenium test cases for the containerized application.

**Tools:** Docker, Flask 3.0.3, Selenium 4.27, JUnit 5, Maven, Chrome

## Files

| File | Purpose |
|------|---------|
| `app.py` | Flask app on port 8080: home page + form submit |
| `templates/index.html` | Page titled `My Containerized Application` with the form and `#result` |
| `Dockerfile` | Image for the app, exposes 8080 |
| `pom.xml` | Selenium + JUnit 5, passes `app.url` to the tests |
| `src/test/java/com/example/ContainerizedAppTest.java` | `testHomePageLoads` and `testSubmitForm` |

## 1. Build and run the containerized application

```bash
docker build -t my-container-app .
docker run -d --name my-container-app -p 8080:8080 my-container-app

# confirm it is answering
curl -I http://localhost:8080
```

Open <http://localhost:8080> — the title bar reads **My Containerized Application**.

## 2. Run the Selenium tests

```bash
mvn test
```

Expected result:

```console
[INFO] Running com.example.ContainerizedAppTest
Application title: My Containerized Application
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

If the application is published on another port:

```bash
mvn test -Dapp.url=http://localhost:9090
```

## The test cases

| Test | Steps | Expected |
|------|-------|----------|
| `testHomePageLoads` | Open `app.url` | Title equals `My Containerized Application`, name field visible |
| `testSubmitForm` | Enter name + email, click **Submit** | `#result` shows `Form submitted successfully!` |

## 3. Cleanup

```bash
docker stop my-container-app
docker rm my-container-app
```

## Notes

* The manual's assertions use Java's `assert`, which is disabled unless the JVM
  runs with `-ea`; these tests use JUnit `assertEquals`, so a mismatch fails the
  build.
* `testSubmitForm` waits explicitly for the result text instead of reading it
  immediately, which avoids a race when the page re-renders after the POST.
* Running the same suite in Jenkins or GitHub Actions only needs Chrome on the
  agent and the container started before `mvn test` — that combination is the
  end-to-end pipeline the lab builds up to.
