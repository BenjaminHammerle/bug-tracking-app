
package com.mci.swe.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import com.mci.swe.models.TodoModel;
import com.mci.swe.models.TodoSchrittModel;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SchrittService {

    
    private final ObjectMapper mapper = new ObjectMapper()
    .registerModule(new JavaTimeModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final HttpClient client = HttpClient.newHttpClient();
    
     public void addSchritt(TodoSchrittModel schritt, String mainId) {
                try {
                // JSON-Payload erstellen
                Map<String, Object> payload = new HashMap<>();
                payload.put("assignee_id", schritt.assignee_id);
                payload.put("kommentar", schritt.kommentar);
                payload.put("status_aenderung", schritt.status_change);
              
                // JSON serialisieren
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(payload);

                // Payload in der Konsole ausgeben
                System.out.println("=== Request Payload ===");
                System.out.println(json);
                System.out.println("=======================");

                // HTTP POST-Request
                URI uri = new URI("https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Tickets/" + mainId);
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                // Anfrage senden
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Response in der Konsole ausgeben
                System.out.println("=== Response ===");
                System.out.println("Status Code: " + response.statusCode());
                System.out.println("Body:");
                System.out.println(response.body());
                System.out.println("================");

                if (response.statusCode() == 200) {
                    System.out.println("Schritt erfolgreich aktualisiert!");
                } else {
                    System.out.println("Schritt Fehler !");
                    System.out.println(response);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public List<TodoSchrittModel> getSchritte(String mainId) {
            try {
                URI uri = new URI("https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Ticket/" + mainId + "/Steps");
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
        
                HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
        
                if (response.statusCode() == 200) {
                    // JSON in Baum lesen
                    JsonNode root = mapper.readTree(response.body());
                    JsonNode stepsNode = root.get("steps");
                    // stepsNode (Array) in List<TodoSchrittModel> umwandeln
                    return mapper.convertValue(
                        stepsNode,
                        new TypeReference<List<TodoSchrittModel>>() {}
                    );
                } else {
                    throw new RuntimeException(
                      "Fehler beim Laden der Schritte: HTTP " + response.statusCode()
                    );
                }
            } catch (Exception e) {
                throw new RuntimeException("Fehler beim Abholen der Schritte", e);
            }
        }
    }