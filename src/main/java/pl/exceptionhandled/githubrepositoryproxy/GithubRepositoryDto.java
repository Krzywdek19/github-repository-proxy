package pl.exceptionhandled.githubrepositoryproxy;

record GithubRepositoryDto(
        String name,
        GithubOwnerDto owner,
        boolean fork
) {
}
