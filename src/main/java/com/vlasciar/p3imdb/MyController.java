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
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MyController {

    static class SearchResult {
        private String textResult;
        private String imageUrl;
        private String textId;

        public SearchResult(String textResult, String imageUrl, String textId) {
            this.textResult = textResult;
            this.imageUrl = imageUrl;   
            this.textId = textId;        
        }

        public String getTextResult() {
            return textResult;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getTextId() {
            return textId;
        }
    }

    @GetMapping("/hellow")
    public String hellow(@RequestParam(required = false) String searchParam, Model model) throws IOException, InterruptedException {
     if(searchParam != null){ 
        String URL = "https://imdb8.p.rapidapi.com/title/find?q=" + searchParam.replaceAll(" ","%20");
        System.out.println(URL);
        HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create(URL))
		.header("X-RapidAPI-Key", "8af530af8bmsh4ffd0cbe5f30c3cp1c5f57jsn0f883cb5c11f")
		.header("X-RapidAPI-Host", "imdb8.p.rapidapi.com")
		.method("GET", HttpRequest.BodyPublishers.noBody())
		.build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    
    

    ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.body());
        List<SearchResult> searchResults=new ArrayList<>();;
        // Extract titles
        JsonNode dataNode = rootNode.get("results");
        //System.out.println(dataNode);
        if (dataNode.isArray()) {
            for (JsonNode item : dataNode) {

                if(item.get("title") != null){
                    String title = item.get("title").asText();
                    String id = item.get("id").asText();
                    if(item.get("image") != null){
                        String img = item.path("image").path("url").asText();                         
                        searchResults.add(new SearchResult("title: "+title, img,"https://www.imdb.com"+id));
                    }
                    else searchResults.add(new SearchResult("title: "+title, "error","https://www.imdb.com"+id));
                }

                else if(item.get("name") != null){
                    String title = item.get("name").asText();
                    String id = item.get("id").asText();
                    if(item.get("image") != null){
                        String img = item.path("image").path("url").asText();                         
                        searchResults.add(new SearchResult("name: "+title, img,"https://www.imdb.com"+id));
                    }
                    else searchResults.add(new SearchResult("name: "+title, "error","https://www.imdb.com"+id));
                }

            }
        }
        model.addAttribute("searchResults", searchResults);
        //System.out.println(messages);
    }
        return "hellow";  // This corresponds to "hellow.html"
    }

    
}
