package jk.manager;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;

public class LibraryApiClient {
    private final String baseURL = "http://localhost:3000/";

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