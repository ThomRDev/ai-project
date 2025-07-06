package com.thomrdev.aiproject.ai_project.infrastructure.out.strategy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GeminiMessageAnalysisStrategy implements MessageAnalysisStrategy {

    @Value("${api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(String engine) {
        return "gemini".equalsIgnoreCase(engine);
    }

    @Override
    public String analyze(String messageJson) {
        try {
            JsonNode rootNode = objectMapper.readTree(messageJson);
            String userText = rootNode.path("text").asText("");

            String prompt = """
            responde de forma breve y natural, como lo haría una persona.
            No des explicaciones ni análisis semántico. Mantén las respuestas por debajo de 200 palabras como máximo.

            Mensaje: %s
        """.formatted(userText.replace("\"", "'"));

            ObjectNode part = objectMapper.createObjectNode();
            part.put("text", prompt);

            ArrayNode parts = objectMapper.createArrayNode().add(part);

            ObjectNode content = objectMapper.createObjectNode();
            content.set("parts", parts);

            ArrayNode contents = objectMapper.createArrayNode().add(content);

            ObjectNode root = objectMapper.createObjectNode();
            root.set("contents", contents);

            String body = objectMapper.writeValueAsString(root);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + geminiApiKey;

            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    JsonNode.class
            );

            JsonNode json = response.getBody();
            return json.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText("No entendí.");

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al procesar el mensaje";
        }
    }
}