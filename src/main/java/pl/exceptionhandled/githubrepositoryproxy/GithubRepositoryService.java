package pl.exceptionhandled.githubrepositoryproxy;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class GithubRepositoryService {

    private final GithubClient githubClient;

    GithubRepositoryService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    List<Repository> getNonForkRepositories(String username) {
        return githubClient.getRepositories(username).stream()
                .filter(repository -> !repository.fork())
                .map(this::toRepository)
                .toList();
    }

    private Repository toRepository(GithubRepositoryDto githubRepository) {
        List<Branch> branches = githubClient.getBranches(
                        githubRepository.owner().login(),
                        githubRepository.name()
                ).stream()
                .map(this::toBranch)
                .toList();

        return new Repository(
                githubRepository.name(),
                githubRepository.owner().login(),
                branches
        );
    }

    private Branch toBranch(GithubBranchDto githubBranch) {
        return new Branch(
                githubBranch.name(),
                githubBranch.commit().sha()
        );
    }
}