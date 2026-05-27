package pl.exceptionhandled.githubrepositoryproxy;

import java.util.List;

record RepositoryResponseDto(
        String repositoryName,
        String ownerLogin,
        List<BranchResponseDto> branches
) {
}
