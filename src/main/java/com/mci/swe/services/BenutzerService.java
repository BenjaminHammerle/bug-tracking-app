package com.mci.swe.services;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

import com.mci.swe.models.BenutzerModel;
import com.vaadin.flow.component.notification.Notification;
import java.io.IOException;
import static java.lang.System.console;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.core.userdetails.User;


public class BenutzerService {
      public  List<BenutzerModel> users = new ArrayList<>();

        public List<BenutzerModel> getAllUsers() {
              try {
            // API-Request vorbereiten
            URI uri = new URI("https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Users");
            HttpClient client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            // API-Abfrage durchführen
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

                // JSON → Array → Liste
                BenutzerModel[] benutzerArray = mapper.readValue(response.body(), BenutzerModel[].class);
                List<BenutzerModel> benutzerListe = List.of(benutzerArray);
                users = benutzerListe;
            } else {
                System.out.println("Benutzer erfolgreich aktualisiert!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
              
    return users;
    }


        public void addUser(BenutzerModel user) {
                        try {
                // JSON-Payload erstellen
                Map<String, Object> payload = new HashMap<>();
                payload.put("nachname", user.getNachname());
                payload.put("vorname", user.getVorname());
                payload.put("email", user.getEmail());
                payload.put("passwort", user.getPassword());
                payload.put("firma",user.getFirma());
                payload.put("istAdmin", user.istAdmin);       
                payload.put("istMitarbeiter", user.istMitarbeiter);  
                payload.put("istKunde", user.istKunde);      

                // JSON serialisieren
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(payload);

                // HTTP POST-Request
                URI uri = new URI("https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Users");
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();

                // Anfrage senden
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 201) {
                    System.out.println("Benutzer erfolgreich aktualisiert!");
                } else {
                    System.out.println("Benutzer fehler aktualisiert!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
         users.add(user);
        }

        public void deleteUser(BenutzerModel user) {
           
             var id = user.getId();
             try{
             HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Users/" + id))
                .DELETE()
                .build();
             
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            System.out.println("✅ Benutzer erfolgreich gelöscht");
             users.remove(user);
        } else if (response.statusCode() == 404) {
            System.out.println("❌ Benutzer nicht gefunden");
        } else {
            System.out.println("⚠️ Fehler beim Löschen: " + response.statusCode());
            System.out.println("Antwort: " + response.body());
        }
             } catch (Exception e) {
                e.printStackTrace();
            }
    }
        
        public void editUser(BenutzerModel benutzer){
               try {
         Map<String, Object> payload = new HashMap<>();
                payload.put("nachname", benutzer.getNachname());
                payload.put("vorname", benutzer.getVorname());
                payload.put("email", benutzer.getEmail());
                payload.put("passwort", benutzer.getPassword());
                payload.put("firma",benutzer.getFirma());
                payload.put("istAdmin", benutzer.istAdmin);       
                payload.put("istMitarbeiter", benutzer.istMitarbeiter);  
                payload.put("istKunde", benutzer.istKunde);      
    
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(payload);
        // Passwort wird bewusst nicht gesendet
        var id = benutzer.getId();
         HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://nx0u5kutgk.execute-api.eu-central-1.amazonaws.com/PROD/Users/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response;
       
              response = HttpClient.newHttpClient()
                      .send(request, HttpResponse.BodyHandlers.ofString());
             
               if (response.statusCode() == 200) {
            System.out.println("Benutzer erfolgreich aktualisiert!");
        } else if (response.statusCode() == 404) {
            System.out.println("Benutzer nicht gefunden.");
        } else {
            System.out.println("Fehler beim Aktualisieren: " + response.statusCode());
            System.out.println(response.body());
        }    
          } catch (IOException ex) {
              Logger.getLogger(BenutzerService.class.getName()).log(Level.SEVERE, null, ex);
          } catch (InterruptedException ex) {
              Logger.getLogger(BenutzerService.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
    }