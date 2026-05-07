package jk;

/**
 * Författare: Johnny Battah
 * Programmet är ett enkelt bibliotekssystem på E nivå
 * Det kan hämta böcker och tidningar från en server, visa dem, 
 * och lägga till Nya böcker/tidningar lokalt i Arraylist
 */

// Gson objekt som vi behöver
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

// Importera Type för att hjälpa json att omvandla data
import java.lang.reflect.Type;

// UniREST objekt som vi behöver
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestException;

// ArrayList för att lagra objekt
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String baseURL = "http://10.151.168.5:3137/";
        Gson gson = new Gson(); 

        ArrayList<Book> books = new ArrayList<>();
        ArrayList<Magazine> magazines = new ArrayList<>();

        boolean kör = true;

        while (kör) {
            IO.println("""
                        === MENY ===
                        1. Hämta böcker
                        2. Hämta tidningar
                        3. Skriv ut böcker
                        4. Skriv ut tidningar
                        5. Lägg till bok
                        6. Lägg till tidning
                        7. Avsluta
                    """);
                    
        
            String input = IO.readln("Välj ett alternativ (1-7): ");
            int val;

            try {
                val = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                IO.println("Felaktig inmatning. Ange en siffra");
                continue;
            }

            switch (val) {
                case 1:
                    IO.println("Hämta alla böcker");
                    HttpResponse<String> booksResponse;
                    try {
                        booksResponse = Unirest.get(baseURL + "/books").asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int bookStatus = booksResponse.getStatus();
                    if (bookStatus != 200) {
                        IO.println("Fel från servern vid hämtning av böcker. Statuskod: " + bookStatus);
                        break;
                    }

                    String booksBody = booksResponse.getBody();
                    Type bookListType = new TypeToken<ArrayList<Book>>(){}.getType();
                    books = gson.fromJson(booksBody, bookListType);

                    IO.println("Böcker hämtade från servern. Antal: " + books.size());
                    break;

                case 2: 
                    IO.println("Hämta alla tidningar");
                    HttpResponse<String> magazinesResponse;
                    try {
                        magazinesResponse = Unirest.get(baseURL + "/magazines").asString();
                    } catch (UnirestException e) {
                        IO.println("Fel vid uppkoppling mot servern: " + e.getLocalizedMessage());
                        break;
                    }

                    int magazineStatus = magazinesResponse.getStatus();
                    if (magazineStatus != 200) {
                        IO.println("Fel från servern vid hämtning av tidningar. Statuskod: " + magazineStatus);
                        break;
                    }

                    String magazinesBody = magazinesResponse.getBody();
                    Type magazineListType = new TypeToken<ArrayList<Magazine>>(){}.getType();
                    magazines = gson.fromJson(magazinesBody, magazineListType);

                    IO.println("Tidningar hämtade från servern. Antal: " + books.size());
                    break;

                
            
                default:
                    break;
            }

        }




    }
}