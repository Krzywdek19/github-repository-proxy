package pl.exceptionhandled.githubrepositoryproxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
class GithubClientConfiguration {

    @Bean
    RestClient githubRestClient(RestClient.Builder builder, GithubProperties properties) {
        return builder
                .baseUrl(properties.baseUrl())
                .build();
    }
}