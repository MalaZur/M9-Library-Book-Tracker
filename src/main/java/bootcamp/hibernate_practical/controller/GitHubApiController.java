package bootcamp.hibernate_practical.controller;

import bootcamp.hibernate_practical.service.GitHubApiService;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/external")
public class GitHubApiController {

    private final GitHubApiService gitHubApiService;

    public GitHubApiController(GitHubApiService gitHubApiService) {
        this.gitHubApiService = gitHubApiService;
    }

    @GetMapping("/github-profile")
    public Map<String, String> getProfile() {
        return gitHubApiService.getProfile();
    }
}