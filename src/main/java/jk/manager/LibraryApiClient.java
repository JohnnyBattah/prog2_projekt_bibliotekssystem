package jk.manager;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.HttpResponse;

public class LibraryApiClient {
    private final String baseURL = "http://localhost:3000/";

    public HttpResponse<String> get(String endpoint) throws UnirestException {
        return Unirest.get(baseURL + endpoint).asString();
    }

    public HttpResponse<String> postJson(String endpoint, String jsonBody) throws UnirestException {
        return Unirest.post(baseURL + endpoint)
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .asString();
    }

    public HttpResponse<String> putJson(String endpoint, String jsonBody) throws UnirestException {
        return Unirest.put(baseURL + endpoint)
                .header("Content-Type", "application/json")
                .body(jsonBody)
                .asString();
    }

    public HttpResponse<String> delete(String endpoint) throws UnirestException {
        return Unirest.delete(baseURL + endpoint).asString();
    }
}