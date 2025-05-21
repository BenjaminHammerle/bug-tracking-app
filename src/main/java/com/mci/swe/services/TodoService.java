package com.mci.swe.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mci.swe.models.TodoModel;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;


@Service
public class TodoService {
    private static final String BASE_URL = "https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Tickets";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final List<TodoModel> todos = new ArrayList<>();

    public void addTodo(TodoModel todo) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("titel", todo.getTitel());
            payload.put("beschreibung", todo.getBeschreibung());
            payload.put("owner_id", todo.getOwner_id());
            payload.put("prio", todo.getPrio());

            String json = mapper.writeValueAsString(payload);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201) {
                // Nach erfolgreichem Erstellen Cache aktualisieren
                findAll();
            } else {
                throw new RuntimeException("Fehler beim Erstellen: HTTP " + response.statusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error adding todo", e);
        }
    }

    public List<TodoModel> findAll() {
        List<TodoModel> result = fetchFromApi(BASE_URL);
        todos.clear();
        todos.addAll(result);
        return new ArrayList<>(todos);
    }

    public List<TodoModel> findFiltered(
            String status,
            String prio,
            Integer ownerId,
            Integer assigneeId,
            String firma,
            String search
    ) {
        StringJoiner qs = new StringJoiner("&");
        if (status != null && !status.isBlank())    qs.add("status="    + status);
        if (prio != null && !prio.isBlank())        qs.add("prio="      + prio);
        if (ownerId != null)   qs.add("owner_id="  + ownerId);
        if (assigneeId != null)qs.add("assignee_id="+ assigneeId);
        if (firma != null && !firma.isBlank())       qs.add("firma="      + firma);
        if (search != null && !search.isBlank())     qs.add("search="     + search);

        String url = BASE_URL + (qs.length() > 0 ? "?" + qs.toString() : "");
        return fetchFromApi(url);
    }

    public TodoModel findById(String id) {
        String url = BASE_URL + "?id=" + id;
        List<TodoModel> list = fetchFromApi(url);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<TodoModel> search(String keyword) {
        return todos.stream()
            .filter(t -> t.getTitel() != null
                      && t.getTitel().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }

    private List<TodoModel> fetchFromApi(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                TodoModel[] arr = mapper.readValue(response.body(), TodoModel[].class);
                return new ArrayList<>(Arrays.asList(arr));
            } else {
                throw new RuntimeException("HTTP " + response.statusCode() + " fetching todos");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching todos from " + url, e);
        }
    }
}
