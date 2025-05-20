package com.mci.swe.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mci.swe.models.BenutzerModel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.mci.swe.models.TodoModel;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andreas
 */
public class TodoService {
    
     private  List<TodoModel> todos = new ArrayList<>();

        public TodoService() {
        }
        
          public void addTodo(TodoModel todo) {
                try {

                // JSON-Payload erstellen
                Map<String, Object> payload = new HashMap<>();
                payload.put("titel", todo.titel);
                payload.put("beschreibung", todo.beschreibung);
                payload.put("owner_id", todo.id);
                payload.put("prio", todo.prio);
              
                // JSON serialisieren
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(payload);

                // HTTP POST-Request
                URI uri = new URI("https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Tickets");
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                // Anfrage senden
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 201) {
                    System.out.println("Tode rfolgreich aktualisiert!");
                } else {
                    System.out.println("Benutzer fehler aktualisiert!");
                    System.out.println(response);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public List<TodoModel> findAll() {
               try {
            // API-Request vorbereiten
            URI uri = new URI("https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Tickets");
            HttpClient client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            // API-Abfrage durchführen
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
                mapper.registerModule(new JavaTimeModule());

                // JSON → Array → Liste
                TodoModel[] todoArray = mapper.readValue(response.body(), TodoModel[].class);
                List<TodoModel> todoModel = List.of(todoArray);
                todos = todoModel;
            } else {
                System.out.println("Benutzer erfolgreich aktualisiert!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
              
        return todos;
        }

        public List<TodoModel> search(String keyword) {
            return todos.stream()
                    .filter(t -> t.titel.toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }
}
