package com.mci.swe.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.mci.swe.models.TodoModel;

/**
 *
 * @author Andreas
 */
public class TodoService {
    
     private final List<TodoModel> todos = new java.util.ArrayList<>();

        public TodoService() {
            todos.add(new TodoModel(1, "Meeting vorbereiten", "Details...", "Firma A", LocalDateTime.now(), "Max"));
            todos.add(new TodoModel(2, "Code Review", "Details...", "Firma B", LocalDateTime.now().minusDays(1), "Anna"));
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
