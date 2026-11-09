# Experiment 4 — Jenkins installation, setup and environment exploration

**Aim:** Install and set up Jenkins and explore its environment.

**Tools:** Jenkins LTS, JDK 17 (or 21), Git

## 1. Install Jenkins

### Windows

1. Download the Windows installer (`jenkins.msi`) from <https://www.jenkins.io/download/>.
2. Run the installer, accept the default port `8080` and the suggested JDK.
3. The installer registers Jenkins as a Windows service and starts it.

Verify and control the service:

```powershell
Get-Service jenkins
Start-Service jenkins
Stop-Service jenkins
```

### Linux (Debian / Ubuntu)

```bash
sudo apt update
sudo apt install -y fontconfig openjdk-17-jre
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key \
  | sudo tee /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
https://pkg.jenkins.io/debian-stable binary/" \
  | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt update
sudo apt install -y jenkins
sudo systemctl enable --now jenkins
sudo systemctl status jenkins
```

### Docker (quickest for a lab machine)

```bash
docker run -d --name jenkins -p 8080:8080 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk17
docker logs jenkins
```

## 2. Unlock the web interface

Open <http://localhost:8080>. Jenkins asks for the initial administrator password:

```bash
# Linux
sudo cat /var/lib/jenkins/secrets/initialAdminPassword

# Docker
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

Paste it and continue.

## 3. Initialise the environment

1. Choose **Install suggested plugins** (Git, Pipeline, GitHub, Credentials,
   Email Extension, Ant, Gradle, Maven Integration, SSH agents).
2. Create the first admin user (username, password, full name, e-mail) and save.
3. Accept the instance URL (`http://localhost:8080/`) and finish.
4. Confirm **Manage Jenkins → System** shows no failed checks.

Additional plugins used later in this lab:

| Plugin | Used for |
|--------|----------|
| Git plugin | cloning the repository |
| Pipeline | `Jenkinsfile` based builds (Experiment 5) |
| Maven Integration | Java builds |
| GitHub plugin | webhook-triggered builds |
| Docker Pipeline | building images from a pipeline |

## 4. Explore the environment

| Area | What to observe |
|------|-----------------|
| **Dashboard** | job list, build queue, build executor status |
| **New Item** | Freestyle project, Pipeline, Multibranch Pipeline, Folder |
| **Manage Jenkins → System** | system message, number of executors, environment variables |
| **Manage Jenkins → Tools** | JDK, Git and Maven installations used by jobs |
| **Manage Jenkins → Plugins** | installed and available plugins |
| **Manage Jenkins → Nodes** | built-in node and build agents |
| **Manage Jenkins → Credentials** | stored credentials, scoped per domain |
| **Build history** | build number, status, duration, console output, test results |
| **My Views** | saved job views |
| **System Information** | JVM, OS and environment details |

## 5. Basic checks performed

```bash
java -version            # Jenkins needs JDK 17+
git --version
curl -I http://localhost:8080   # HTTP/1.1 200 OK
```

## Viva notes

* Jenkins is an open-source automation server used mainly for CI/CD; it is
  written in Java and runs as a servlet in an embedded Jetty container.
* A **job** (freestyle or pipeline) defines what to build, when to build it
  (triggers) and on which agent.
* The **workspace** is the directory each job checks the source into.
* **Executors** are build slots; the built-in node uses two by default.
* **Credentials** are stored encrypted and referenced by ID, never hard-coded in
  a job.
