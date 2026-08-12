package com.sakshi.TextSummarizationApp.ai;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class SummarizationModel {

    private final RestClient restClient;

    public SummarizationModel(RestClient huggingFaceRestClient) {
        this.restClient = huggingFaceRestClient;
    }

    public String generateSummary(String text) {

        Map<String, Object> parameters = Map.of(
                "min_length", 60,
                "max_length", 180,
                "num_beams", 4,
                "length_penalty", 1.5,
                "no_repeat_ngram_size", 3
        );

        Map<String, Object> requestBody = Map.of(
                "inputs", text,
                "parameters", parameters
        );

        List<Map<String, String>> response = restClient.post()
                .body(requestBody)
                .retrieve()
                .body(List.class);

        if (response == null || response.isEmpty()) {
            throw new RuntimeException("No summary was generated.");
        }

        return response.get(0).get("summary_text");
    }
}