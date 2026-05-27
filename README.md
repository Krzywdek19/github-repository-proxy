# GitHub Repository Proxy

A small Spring Boot application that works as a proxy for the GitHub REST API.

The application exposes an endpoint that returns all non-fork repositories for a given GitHub user.
For each repository, the response contains the repository name, owner login, and all branches with the SHA of the last commit.

## Technology stack

- Java 25
- Spring Boot 4
- Gradle Kotlin DSL
- Spring Web
- RestClient
- JUnit 5
- WireMock

## Requirements

- Java 25
- Gradle

## Running the application

Linux / macOS:

```bash
./gradlew bootRun
```

Windows:

```bash
gradlew bootRun
```

The application starts on:

```text
http://localhost:8080
```

## API

### List non-fork repositories

```http
GET /api/v1/github/users/{username}/repositories
```

Example:

```http
GET http://localhost:8080/api/v1/github/users/octocat/repositories
```

### Successful response

```json
[
  {
    "repositoryName": "Hello-World",
    "ownerLogin": "octocat",
    "branches": [
      {
        "name": "main",
        "lastCommitSha": "abc123"
      }
    ]
  }
]
```

## Error response

When the GitHub user does not exist, the application returns HTTP 404.

```json
{
  "status": 404,
  "message": "GitHub user not found: unknown-user"
}
```

## Running tests

Linux / macOS:

```bash
./gradlew test
```

Windows:

```bash
gradlew test
```

