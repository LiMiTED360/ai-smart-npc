package aiSmartNpc.helper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class HttpHelper {
    public static HttpResponse<String> sendHttpRequest(String jsonBody, String api_url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(URI.create(api_url))
                .header("Content-Type", "application/json")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}