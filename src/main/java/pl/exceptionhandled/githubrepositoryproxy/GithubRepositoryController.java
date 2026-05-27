package pl.exceptionhandled.githubrepositoryproxy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
class GithubRepositoryController {

    private final GithubRepositoryService githubRepositoryService;

    GithubRepositoryController(GithubRepositoryService githubRepositoryService) {
        this.githubRepositoryService = githubRepositoryService;
    }

    @GetMapping("/api/v1/github/users/{username}/repositories")
    List<RepositoryResponseDto> getNonForkRepositories(@PathVariable String username) {
        return githubRepositoryService.getNonForkRepositories(username).stream()
                .map(this::toResponseDto)
                .toList();
    }

    private RepositoryResponseDto toResponseDto(Repository repository) {
        return new RepositoryResponseDto(
                repository.name(),
                repository.ownerLogin(),
                repository.branches().stream()
                        .map(this::toResponseDto)
                        .toList()
        );
    }

    private BranchResponseDto toResponseDto(Branch branch) {
        return new BranchResponseDto(
                branch.name(),
                branch.lastCommitSha()
        );
    }
}