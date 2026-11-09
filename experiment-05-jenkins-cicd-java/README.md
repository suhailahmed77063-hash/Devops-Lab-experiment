# Experiment 5 — Continuous integration and delivery using Jenkins

**Aim:** Demonstrate continuous integration and continuous delivery of a Java
application with Jenkins.

**Tools:** Jenkins, Maven 3.9, JDK 17, JUnit 4.13.2, Git

## Files

| File | Purpose |
|------|---------|
| `pom.xml` | Maven build: compile, test, package |
| `src/main/java/com/example/App.java` | Application with `greeting()` and `add()` |
| `src/test/java/com/example/AppTest.java` | Three JUnit tests |
| `Jenkinsfile` | Declarative pipeline: checkout → build → test → package → deploy |

## Run the build locally first

```bash
mvn -B clean verify        # compiles, runs tests, produces target/hello-jenkins.jar
java -jar target/hello-jenkins.jar
```

Expected output:

```
Hello World
2 + 3 = 5
```

## Create the Jenkins job

### Option A — Pipeline job (recommended)

1. **New Item → Pipeline**, name `hello-jenkins-cicd`, click **OK**.
2. Under **Pipeline**, choose **Pipeline script from SCM**.
3. SCM: **Git**, Repository URL: `https://github.com/suhailahmed77063-hash/Devops-Lab-experiment.git`.
4. Branch specifier: `*/main` (or `*/master`).
5. Script Path: `experiment-05-jenkins-cicd-java/Jenkinsfile`.
6. Under **Build Triggers**, tick **GitHub hook trigger for GITScm polling**
   (or **Poll SCM** with `H/5 * * * *`).
7. **Save → Build Now**.

### Option B — Freestyle job (matches the manual's steps)

1. **New Item → Freestyle project**, name `hello-jenkins-freestyle`.
2. **Source Code Management → Git** → repository URL, branch `main`.
3. **Build Triggers → Poll SCM** → `H/5 * * * *`.
4. **Build → Add build step → Invoke top-level Maven targets**:
   Goals `clean package`.
5. **Post-build Actions → Archive the artifacts**: `target/*.jar`.
6. **Post-build Actions → Publish JUnit test result report**: `target/surefire-reports/*.xml`.
7. **Save → Build Now**.

> The pipeline uses the tool names `Maven-3.9` and `JDK-17`. Create those names
> under **Manage Jenkins → Tools**, or delete the `tools { }` block and let the
> agent use `mvn` / `java` from `PATH`.
>
> The `Deploy` stage only runs on `main`. It copies the jar to `deploy/` and runs
> it, which is the "copy the jar to a production server" deployment described in
> the manual. Replace the `sh` step with your real deployment command (for
> example `docker build`/`docker push` + `kubectl set image`).

## Monitor a build

* **Build history** shows number, status, duration and trend.
* **Console Output** shows each pipeline stage and the Maven log.
* **Test Result** shows the three tests and their durations.
* A commit pushed to the repository triggers the next build automatically and
  the dashboard shows the new build number.

## What this demonstrates

| CI/CD concept | Where it happens |
|---------------|------------------|
| Continuous integration | Every push compiles the code and runs the tests |
| Fast feedback | A failing test fails the build before packaging |
| Build artifact | `target/hello-jenkins.jar` archived with the build |
| Continuous delivery | The `Deploy` stage publishes the jar on `main` |
| Repeatability | Jenkins checks out a clean workspace and reruns the same pipeline |
