package com.mci.swe.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BenutzerModel {

    @JsonProperty("id")
    private int id;

    @JsonProperty("nachname")
    private String nachname;

    @JsonProperty("vorname")
    private String vorname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("passwort")
    private String password;

    @JsonProperty("firma")
    private String firma;

    @JsonProperty("ist_admin")
    private int istAdmin;

    @JsonProperty("ist_mitarbeiter")
    private int istMitarbeiter;

    @JsonProperty("ist_kunde")
    private int istKunde;

    @JsonProperty("erstellt_am")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime erstelltAm;

    @JsonProperty("bearbeitet_am")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bearbeitetAm;

    // Default-Konstruktor für Jackson
    public BenutzerModel() {
    }

    // Ursprüngliche Konstruktoren
    public BenutzerModel(String nachname, String firma, String email, String password) {
        this.nachname = nachname;
        this.firma = firma;
        this.email = email;
        this.password = password;
    }

    public BenutzerModel(String nachname, String vorname, String email, String password, String firma) {
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.password = password;
        this.firma = firma;
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

    public BenutzerModel(int id, String nachname, String vorname, String email, String firma) {
        this.id = id;
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.firma = firma;
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

    // Getter und Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirma() { return firma; }
    public void setFirma(String firma) { this.firma = firma; }

    public int getIstAdmin() { return istAdmin; }
    public void setIstAdmin(int istAdmin) { this.istAdmin = istAdmin; }

    public int getIstMitarbeiter() { return istMitarbeiter; }
    public void setIstMitarbeiter(int istMitarbeiter) { this.istMitarbeiter = istMitarbeiter; }

    public int getIstKunde() { return istKunde; }
    public void setIstKunde(int istKunde) { this.istKunde = istKunde; }

    public LocalDateTime getErstelltAm() { return erstelltAm; }
    public void setErstelltAm(LocalDateTime erstelltAm) { this.erstelltAm = erstelltAm; }

    public LocalDateTime getBearbeitetAm() { return bearbeitetAm; }
    public void setBearbeitetAm(LocalDateTime bearbeitetAm) { this.bearbeitetAm = bearbeitetAm; }

    // Convenience-Methoden für Boolean-Abfragen
    public boolean isAdmin() { return istAdmin == 1; }
    public boolean isMitarbeiter() { return istMitarbeiter == 1; }
    public boolean isKunde() { return istKunde == 1; }

    @Override
    public String toString() {
        return "BenutzerModel{" +
               "id=" + id +
               ", nachname='" + nachname + '\'' +
               ", vorname='" + vorname + '\'' +
               ", email='" + email + '\'' +
               ", firma='" + firma + '\'' +
               ", istAdmin=" + istAdmin +
               ", istMitarbeiter=" + istMitarbeiter +
               ", istKunde=" + istKunde +
               ", erstelltAm=" + erstelltAm +
               ", bearbeitetAm=" + bearbeitetAm +
               '}';
    }
}