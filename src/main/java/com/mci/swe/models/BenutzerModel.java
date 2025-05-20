package com.mci.swe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)

public class BenutzerModel {
    
    public int id;
    public String nachname;
    public String vorname;
    public String email;
    public String password;
    public String firma;
    public int istAdmin;
    public int istMitarbeiter;
    public int istKunde;
    public LocalDateTime erstelltAm;
    public LocalDateTime bearbeitetAm;

    

    
    public BenutzerModel(String nachname, String firma, String email, String password) {
        this.nachname = nachname;
        this.firma = firma;
        this.email = email;
        this.password = password;
    }
    
        public BenutzerModel(String nachname, String vorname, String email, String password, String firma, int istAdmin, int istMitarbeiter, int istKunde) {
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.password = password;
        this.firma = firma;
        this.istAdmin = istAdmin;
        this.istMitarbeiter = istMitarbeiter;
        this.istKunde = istKunde;
    }

    public BenutzerModel(int id, String nachname, String vorname, String email, String firma, int istAdmin, int istMitarbeiter, int istKunde) {
        this.id = id;
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.firma = firma;
        this.istAdmin = istAdmin;
        this.istMitarbeiter = istMitarbeiter;
        this.istKunde = istKunde;
    }

    public BenutzerModel(int id, String nachname, String vorname, String email, String password, String firma, int istAdmin, int istMitarbeiter, int istKunde) {
        this.id = id;
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.password = password;
        this.firma = firma;
        this.istAdmin = istAdmin;
        this.istMitarbeiter = istMitarbeiter;
        this.istKunde = istKunde;
    }
    
    
        
        

    public BenutzerModel(int id,  String nachname, String vorname, String email, String firma) {
        this.id = id;
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.firma = firma;
    }

    public BenutzerModel(String nachname, String vorname, String email, String password, String firma) {
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.password = password;
        this.firma = firma;
    }
    
    
    
    

    public BenutzerModel() {
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(LocalDateTime erstelltAm) {
        this.erstelltAm = erstelltAm;
    }

    public LocalDateTime getBearbeitetAm() {
        return bearbeitetAm;
    }

    public void setBearbeitetAm(LocalDateTime bearbeitetAm) {
        this.bearbeitetAm = bearbeitetAm;
    }

    public int getIstAdmin() {
        return istAdmin;
    }

    public void setIstAdmin(int istAdmin) {
        this.istAdmin = istAdmin;
    }

    public int getIstMitarbeiter() {
        return istMitarbeiter;
    }

    public void setIstMitarbeiter(int istMitarbeiter) {
        this.istMitarbeiter = istMitarbeiter;
    }

    public int getIstKunde() {
        return istKunde;
    }

    public void setIstKunde(int istKunde) {
        this.istKunde = istKunde;
    }
    
    
}

