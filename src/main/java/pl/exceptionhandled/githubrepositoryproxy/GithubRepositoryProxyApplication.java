package pl.exceptionhandled.githubrepositoryproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
class GithubRepositoryProxyApplication {
	static void main(String[] args) {
		SpringApplication.run(GithubRepositoryProxyApplication.class, args);
	}

}
