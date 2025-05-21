package com.mci.swe.models;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;


public class TodoSchrittModel {
    public int id;
    public int assignee_id;
    public String assignee_name;
    public String kommentar;
    public String status_change;
    @JsonProperty("erstellt_am")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime erstelltAm;

    public TodoSchrittModel(int id, int assignee_id, String assignee_name, String kommentar, String status_change, LocalDateTime erstelltAm) {
        this.id = id;
        this.assignee_id = assignee_id;
        this.assignee_name = assignee_name;
        this.kommentar = kommentar;
        this.status_change = status_change;
        this.erstelltAm = erstelltAm;
    }

    public TodoSchrittModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(int assignee_id) {
        this.assignee_id = assignee_id;
    }

    public String getAssignee_name() {
        return assignee_name;
    }

    public void setAssignee_name(String assignee_name) {
        this.assignee_name = assignee_name;
    }

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public String getStatus_change() {
        return status_change;
    }

    public void setStatus_change(String status_change) {
        this.status_change = status_change;
    }

    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(LocalDateTime erstelltAm) {
        this.erstelltAm = erstelltAm;
    }

    
}
