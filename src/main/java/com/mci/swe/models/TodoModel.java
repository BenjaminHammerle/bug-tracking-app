package com.mci.swe.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class TodoModel {
    public int id;
    public String titel;
    public String beschreibung;
    public String prio;
    public String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime erstellt_am;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime bearbeitet_am;
    public String nachname;
    public String firma;
    private Integer assignee_id;

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }
    public int owner_id;
    

    public TodoModel() {
    }

    public TodoModel(int id, String titel, String beschreibung, String prio, String status, LocalDateTime erstellt_am, LocalDateTime bearbeitet_am, String nachname, String firma) {
        this.id = id;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.prio = prio;
        this.status = status;
        this.erstellt_am = erstellt_am;
        this.bearbeitet_am = bearbeitet_am;
        this.nachname = nachname;
        this.firma = firma;
    }

    public TodoModel(int id, String titel, String beschreibung, String prio, String status, LocalDateTime erstellt_am, LocalDateTime bearbeitet_am, String nachname) {
        this.id = id;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.prio = prio;
        this.status = status;
        this.erstellt_am = erstellt_am;
        this.bearbeitet_am = bearbeitet_am;
        this.nachname = nachname;
    }

    public TodoModel(int id, String titel, String beschreibung, String prio, String status, LocalDateTime erstellt_am, LocalDateTime bearbeitet_am, String nachname, String firma, int owner_id) {
        this.id = id;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.prio = prio;
        this.status = status;
        this.erstellt_am = erstellt_am;
        this.bearbeitet_am = bearbeitet_am;
        this.nachname = nachname;
        this.firma = firma;
        this.owner_id = owner_id;
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

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getPrio() {
        return prio;
    }

    public void setPrio(String prio) {
        this.prio = prio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getErstellt_am() {
        return erstellt_am;
    }

    public void setErstellt_am(LocalDateTime erstellt_am) {
        this.erstellt_am = erstellt_am;
    }

    public LocalDateTime getBearbeitet_am() {
        return bearbeitet_am;
    }

    public void setBearbeitet_am(LocalDateTime bearbeitet_am) {
        this.bearbeitet_am = bearbeitet_am;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }
    
    public Integer getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(Integer assignee_id) {
        this.assignee_id = assignee_id;
    }
}
