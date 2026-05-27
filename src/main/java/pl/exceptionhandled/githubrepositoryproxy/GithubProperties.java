package pl.exceptionhandled.githubrepositoryproxy;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github.api")
record GithubProperties(String baseUrl) {
}