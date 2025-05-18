package todo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class BenutzerModel {
    
    public int id;
    public String vorname;
    public String nachname;
    public String firma;
    public LocalDateTime erstelltAm;
    public LocalDateTime bearbeitetAm;
    public int istAdmin;
    public int istMitarbeiter;
    public int istKunde;
}

