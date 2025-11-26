# CI/CD Pipeline for Java Calculator Application

## Project Overview

This project implements a robust **Continuous Integration and Continuous Deployment (CI/CD)** pipeline for a Java-based Command Line Interface (CLI) Calculator application. The pipeline automates the entire software delivery lifecycle—from code commit to container registry publication—using `Jenkins`, `Docker`, and `GitHub`. The goal was to establish a reproducible, automated workflow that ensures code quality via testing before packaging the application into a portable Docker container.

## Architecture & Workflow

The pipeline orchestrates the following automated stages:

*   **Source Control Management (SCM)**: `Jenkins` polls the `GitHub` repository for changes.
*   **Build**: Compiles the `Java` source code using `javac`.
*   **Test**: Executes the test suite to verify arithmetic logic. If tests fail, the pipeline halts immediately to prevent bad builds.
*   **Containerization**: Builds a lightweight `Docker` image using a robust `OpenJDK` base.
*   **Deployment**: Authenticates securely with `Docker Hub` and pushes the image (tagged with both the specific build number and `latest`).
*   **Cleanup**: Logs out of the registry to ensure security.

## Technology Stack

*   **Application**: Java 17 (CLI)
*   **Orchestration**: Jenkins (Declarative Pipeline)
*   **Containerization**: Docker
*   **Registry**: Docker Hub
*   **OS/Environment**: Ubuntu (WSL)

## Configuration & Implementation Details

### Environment Setup

To ensure seamless integration between `Jenkins` and `Docker` within an `Ubuntu` environment, several system-level configurations were required:

*   **Java Environment**: The system was configured to use `Java 17` as the default runtime (`update-alternatives`) to match the project requirements, resolving conflicts with the system's default `Java 21` installation.
*   **Docker Permissions**: The `jenkins` user was added to the `docker` group (`usermod -aG docker jenkins`) to allow the CI server to execute `Docker` commands without root privileges.
*   **Services**: Both `Jenkins` and `Docker` services were enabled via `systemd` to ensure availability on startup.

### The Dockerfile

I optimized the `Dockerfile` to use a modern, supported base image. I initially attempted to use `openjdk:17-alpine`, but discovered it was deprecated. I migrated to `Eclipse Temurin`, which provides a production-ready, compliant JDK build.

```dockerfile
# Base image: Eclipse Temurin (Alpine Linux) for a small footprint
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy source code
COPY Calculator.java .

# Compile the application
RUN javac Calculator.java

# Define entrypoint
ENTRYPOINT ["java", "Calculator"]
```

### The Jenkins Pipeline (Jenkinsfile)

I utilized a Declarative Pipeline approach for version-controlled build logic. Key features of the `Jenkinsfile` include:

*   **Credential Management**: I utilized Jenkins' secure credential store to manage `GitHub` Personal Access Tokens (PAT) and `Docker Hub` Access Tokens. These are injected into the environment as `REGISTRY_CREDENTIALS` and `github-creds`, ensuring no sensitive passwords exist in plain text.
*   **Dynamic Tagging**: Images are pushed with the Jenkins `${BUILD_NUMBER}` for traceability and the `:latest` tag for easy access to the newest version.
*   **Strict Compilation**: The build step enforces `Java 17` compatibility using `javac --release 17`.

Snippet of the Pipeline:

```groovy
stage('Build Docker Image') {
    steps {
        echo 'Building Docker Image...'
        // Builds image tagged with unique build number
        sh "docker build -t paradox73/calculator-app:${BUILD_NUMBER} ."
        // Builds latest tag
        sh "docker build -t paradox73/calculator-app:latest ."
    }
}
```

## Challenges & Troubleshooting

During the development of this pipeline, I encountered and resolved several engineering challenges:

#### Java Version Mismatch

*   **Issue**: The build failed with `UnsupportedClassVersionError` (Class file 65.0 vs 61.0) because the local compiler defaulted to `Java 21` while `Jenkins` expected `Java 17`.
*   **Resolution**: I standardized the environment by forcing `update-alternatives` to `Java 17` and adding the `--release 17` flag to the `javac` command in the pipeline script.

#### Docker Socket Permissions

*   **Issue**: The pipeline failed with `permission denied` when accessing `/var/run/docker.sock`.
*   **Resolution**: I granted the `jenkins` service account specific permissions to the `Docker` daemon group and restarted the service.

#### Registry Authorization Scope

*   **Issue**: The push stage failed with `insufficient_scope` despite a successful login.
*   **Resolution**: I identified that the initial `Docker Hub` Access Token was "Read-only." I generated a new token with "Read & Write" permissions and updated the Jenkins Global Credentials.

## Results

The pipeline is fully operational.

*   **GitHub Repository**: [https://github.com/Paradox-73/calculator-app](https://github.com/Paradox-73/calculator-app)
*   **Docker Hub Registry**: [https://hub.docker.com/r/paradox73/calculator-app](https://hub.docker.com/r/paradox73/calculator-app)

Every commit to the `main` branch now automatically triggers a clean build, test, and release cycle, reducing manual deployment overhead to zero.