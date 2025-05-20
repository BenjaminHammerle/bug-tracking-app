
package com.mci.swe.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mci.swe.models.TodoModel;
import com.mci.swe.models.TodoSchrittModel;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;


public class SchrittService {
    
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

                // HTTP POST-Request
                URI uri = new URI("https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Tickets/" + mainId + "/steps");
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                // Anfrage senden
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 201) {
                    System.out.println("Schritt rfolgreich aktualisiert!");
                } else {
                    System.out.println("Schritt Fehler !");
                    System.out.println(response);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
