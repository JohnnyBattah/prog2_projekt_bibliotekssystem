package jk.manager;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

/**
 * Klassen LibraryAppClient ansvarar för kommunikationen med JSON-servern.
 * Den skickar HTTP-anrop för att hämta, skapa, uppdatera, och ta bort data.
 * Klassen används av LibraryManager för att separera nätverkslogik från
 * programmets övriga funktionalitet.
 * 
 * @author Johnny Battah
 * @version 1.0
 * @since 2026
 */
public class LibraryApiClient {
    /**Basadressen till JSON-server som programmet kommunicerar med */
    private final String baseURL = "http://localhost:3000/";

    /**
     * Hämtar alla objekt från angiven endpoint (url) på servern
     * 
     * @param endpoint serverns endpoint, till exempel "/books" eller "/users"
     * @return JSON-svarsom text om anropet lyckas, annars null
     */
    public String fetchAll(String endpoint) {
        try {
            HttpResponse<String> response = Unirest.get(baseURL + endpoint).asString();

            if (response.getStatus() != 200) {
                IO.println("Fel från servern. Statuskod: " + response.getStatus());
                return null;
            }

            return response.getBody();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getMessage());
            return null;
        }

    }

    public String fetchOne(String endpoint, String id) {
        try {
            HttpResponse<String> response = Unirest.get(baseURL + endpoint + "/" + id).asString();

            if (response.getStatus() != 200) {
                IO.println("Fel från servern. Statuskod: " + response.getStatus());
                return null;
            }

            return response.getBody();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getMessage());
            return null;
        }

    }

    public String post(String endpoint, String jsonBody) {
        try {
            HttpResponse<String> response = Unirest.post(baseURL + endpoint)
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asString();

            if (response.getStatus() != 200 && response.getStatus() != 201) {
                IO.println("Fel från servern. Statuskod: " + response.getStatus());
                IO.println("Svar från servern: " + response.getBody());
                return null;
            }

            return response.getBody();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getMessage());
            return null;
        }

    }

    /**
     * Uppdaterar ett objekt på servern med hjälp av JSON-data.
     * 
     * @param endpoint endpoint inklusive objektets id
     * @param jsonBody jsonBody JSON-representation av objektet som ska uppdateras
     * @return serverns svar som text om uppdateringen lyckas, annars null.
     */
    public String put(String endpoint, String jsonBody) {
        try {
            HttpResponse<String> response = Unirest.put(baseURL + endpoint)
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asString();

            if (response.getStatus() != 200 && response.getStatus() != 204) {
                IO.println("Fel från servern. Statuskod: " + response.getStatus());
                IO.println("Svar från servern: " + response.getBody());
                return null;
            }

            return response.getBody();
        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getMessage());
            return null;
        }

    }

    public boolean delete(String endpoint) {
        try {
            HttpResponse<String> response = Unirest.delete(baseURL + endpoint).asString();

            if (response.getStatus() != 200 && response.getStatus() != 204) {
                IO.println("Fel från servern. Statuskod: " + response.getStatus());
                IO.println("Svar från servern: " + response.getBody());
                return false;
            }

            return true;

        } catch (UnirestException e) {
            IO.println("Fel vid uppkoppling mot servern: " + e.getMessage());
            return false;
        }

    }
}