package com.mci.swe.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.mci.swe.models.TodoModel;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andreas
 */
public class TodoService {
    
     private final List<TodoModel> todos = new java.util.ArrayList<>();

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
            return todos;
        }

        public List<TodoModel> search(String keyword) {
            return todos.stream()
                    .filter(t -> t.titel.toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
        }
}
