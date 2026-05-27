package pl.exceptionhandled.githubrepositoryproxy;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "github.api.base-url=http://localhost:8089"
)
@WireMockTest(httpPort = 8089)
class GithubRepositoryControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void shouldReturnNonForkRepositoriesWithBranches() throws Exception {
        stubFor(get(urlEqualTo("/users/existing-user/repos"))
                .willReturn(okJson("""
                        [
                          {
                            "name": "repo-one",
                            "owner": {
                              "login": "existing-user"
                            },
                            "fork": false
                          },
                          {
                            "name": "repo-fork",
                            "owner": {
                              "login": "existing-user"
                            },
                            "fork": true
                          }
                        ]
                        """)));

        stubFor(get(urlEqualTo("/repos/existing-user/repo-one/branches"))
                .willReturn(okJson("""
                        [
                          {
                            "name": "main",
                            "commit": {
                              "sha": "abc123"
                            }
                          },
                          {
                            "name": "develop",
                            "commit": {
                              "sha": "def456"
                            }
                          }
                        ]
                        """)));

        HttpResponse<String> response = sendGetRequest(
                "/api/v1/github/users/existing-user/repositories"
        );

        assertThat(response.statusCode()).isEqualTo(200);

        assertThat(response.body()).contains("\"repositoryName\":\"repo-one\"");
        assertThat(response.body()).contains("\"ownerLogin\":\"existing-user\"");
        assertThat(response.body()).contains("\"name\":\"main\"");
        assertThat(response.body()).contains("\"lastCommitSha\":\"abc123\"");
        assertThat(response.body()).contains("\"name\":\"develop\"");
        assertThat(response.body()).contains("\"lastCommitSha\":\"def456\"");

        assertThat(response.body()).doesNotContain("repo-fork");

        verify(getRequestedFor(urlEqualTo("/users/existing-user/repos")));
        verify(getRequestedFor(urlEqualTo("/repos/existing-user/repo-one/branches")));
        verify(0, getRequestedFor(urlEqualTo("/repos/existing-user/repo-fork/branches")));
    }

    @Test
    void shouldReturnNotFoundWhenGithubUserDoesNotExist() throws Exception {
        stubFor(get(urlEqualTo("/users/unknown-user/repos"))
                .willReturn(notFound()));

        HttpResponse<String> response = sendGetRequest(
                "/api/v1/github/users/unknown-user/repositories"
        );

        assertThat(response.statusCode()).isEqualTo(404);
        assertThat(response.body()).contains("\"status\":404");
        assertThat(response.body()).contains("\"message\":\"GitHub user not found: unknown-user\"");

        verify(getRequestedFor(urlEqualTo("/users/unknown-user/repos")));
    }

    private HttpResponse<String> sendGetRequest(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}