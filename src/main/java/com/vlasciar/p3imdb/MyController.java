package com.vlasciar.p3imdb;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MyController {

    @GetMapping("/hellow")
    public String hellow(Model model) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create("https://imdb8.p.rapidapi.com/title/find?q=game%20of%20thr"))
		.header("X-RapidAPI-Key", "8af530af8bmsh4ffd0cbe5f30c3cp1c5f57jsn0f883cb5c11f")
		.header("X-RapidAPI-Host", "imdb8.p.rapidapi.com")
		.method("GET", HttpRequest.BodyPublishers.noBody())
		.build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    
    System.out.println(response.body());
    ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.body());
        List<String> messages=new ArrayList<>();;
        // Extract titles
        JsonNode dataNode = rootNode.get("results");
        if (dataNode.isArray()) {
            for (JsonNode item : dataNode) {
                 String title = item.get("title").asText();                
                messages.add(title);
            }
        }
        model.addAttribute("messages", messages);
        System.out.println(messages);
        return "hellow";  // This corresponds to "hellow.html"
    }
}
