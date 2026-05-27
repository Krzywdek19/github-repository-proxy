package pl.exceptionhandled.githubrepositoryproxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
class GithubClient {

    private final RestClient restClient;

    GithubClient(
            RestClient.Builder restClientBuilder,
            @Value("${github.api.base-url}") String githubBaseUrl
    ) {
        this.restClient = restClientBuilder
                .baseUrl(githubBaseUrl)
                .build();
    }

    List<GithubRepositoryDto> getRepositories(String username) {
        return restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        (request, response) -> {
                            throw new GithubUserNotFoundException(username);
                        }
                )
                .body(new ParameterizedTypeReference<>() {
                });
    }

    List<GithubBranchDto> getBranches(String ownerLogin, String repositoryName) {
        return restClient.get()
                .uri("/repos/{ownerLogin}/{repositoryName}/branches", ownerLogin, repositoryName)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}