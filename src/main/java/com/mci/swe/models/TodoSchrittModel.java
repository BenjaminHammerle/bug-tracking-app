package com.mci.swe.models;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

public class TodoSchrittModel {
    public int id;
    public int Parentid;
    public String text;
    public LocalDateTime erstelltAm;
    public String erstelltVon;
    public LocalDateTime bearbeitetAm;
    public String bearbeitetVon;
}
