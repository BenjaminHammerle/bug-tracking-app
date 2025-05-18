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

    public TodoModel() {
    }

    public TodoModel(int id, String titel, String text, String firma, LocalDateTime erstelltAm, String erstelltVon) {
        this.id = id;
        this.titel = titel;
        this.text = text;
        this.firma = firma;
        this.erstelltAm = erstelltAm;
        this.erstelltVon = erstelltVon;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(LocalDateTime erstelltAm) {
        this.erstelltAm = erstelltAm;
    }

    public String getErstelltVon() {
        return erstelltVon;
    }

    public void setErstelltVon(String erstelltVon) {
        this.erstelltVon = erstelltVon;
    }
    
    
}
