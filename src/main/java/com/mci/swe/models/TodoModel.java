package com.mci.swe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

public class TodoModel {
    public int id;
    public String titel;
    public String beschreibung;
    public String prio;
    public String status;
    public LocalDateTime erstellt_am;
    public LocalDateTime bearbeitet_am;
    public String ersteller;
    public String firma;
    

    public TodoModel() {
    }

    public TodoModel(int id, String titel, String beschreibung, String prio, String status, LocalDateTime erstellt_am, LocalDateTime bearbeitet_am, String ersteller, String firma) {
        this.id = id;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.prio = prio;
        this.status = status;
        this.erstellt_am = erstellt_am;
        this.bearbeitet_am = bearbeitet_am;
        this.ersteller = ersteller;
        this.firma = firma;
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

    public String getErsteller() {
        return ersteller;
    }

    public void setErsteller(String ersteller) {
        this.ersteller = ersteller;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }
    
}
