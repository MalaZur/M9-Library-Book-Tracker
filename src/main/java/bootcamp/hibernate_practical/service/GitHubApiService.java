package bootcamp.hibernate_practical.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class GitHubApiService {

    private final RestClient restClient;
    private final String token;

    public GitHubApiService(RestClient.Builder builder, @Value("${github.api.token:}") String token){
        this.restClient = builder.baseUrl("https://api.github.com").build();

        this.token = token;
    }
    public Map<String, String> getProfile(){

        if (token == null || token.isBlank()){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "GitHub API token is not configured");
        }

        try {
            Map<?, ?> response = restClient.get()
                    .uri("/user")
                    .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
                    .header(HttpHeaders.USER_AGENT, "LibraryBookTracker-Practical")
                    .headers(headers -> headers.setBearerAuth(token))
                    .retrieve()
                    .body(Map.class);

            if (response == null || !(response.get("login") instanceof String login) || !(response.get("html_url") instanceof String url)) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Invalid response from GitHub API");
            }

            return Map.of("username", login, "profileUrl", url);

        } catch (RestClientResponseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "GitHub API returned HTTP " + ex.getStatusCode().value());

        } catch (ResourceAccessException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "GitHub API is unreachable");
        }
    }
}