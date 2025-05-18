package todo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

public class TodoModel {
    public int id;
    public String titel;
    public String text;
    public String firma;
    public LocalDateTime erstelltAm;
    public String erstelltVon;
}
