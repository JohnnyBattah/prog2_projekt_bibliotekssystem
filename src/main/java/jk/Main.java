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
                    IO.println("Hämtar alla böcker...");
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
                    IO.println("Hämtar alla tidningar...");
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

                    IO.println("Tidningar hämtade från servern. Antal: " + magazines.size());
                    break;

                case 3: 
                    IO.println("Skriver ut alla böcker...");
                    if (books.isEmpty()) {
                        IO.println("Inga böcker finns");
                    } else {
                        IO.println("===Böcker===");
                        for (Book b : books) {
                            IO.println(b.getInfo());
                        }
                    }
                    break;

                case 4: 
                    IO.println("Skriver ut alla tidningar...");
                    if (magazines.isEmpty()) {
                        IO.println("Inga tidningar finns");
                    } else {
                        IO.println("===Tidningar===");
                        for (Magazine m : magazines) {
                            IO.println(m.getInfo());
                        }
                    }
                    break;

                case 5:
                    IO.println("Lägger till en bok...");
                    String bookId = IO.readln("Ange Id: ");
                    String bookTitle = IO.readln("Ange titel: ");
                    String bookAuthor = IO.readln("Ange författare: ");
                    String bookGenre = IO.readln("Ange genre: ");
                    int pages;
                    try {
                        pages = Integer.parseInt(IO.readln("Ange antal sidor: "));
                    } catch (NumberFormatException e) {
                        IO.println("Felaktigt antal sidor");
                        break;
                    }

                    Book newBook = new Book(bookId, bookTitle, true, bookAuthor, bookGenre, pages);
                    books.add(newBook);
                    IO.println("Boken lades till lokalt i samlingen");
                    break;

                case 6: 
                    IO.println("Lägger till en tidning...");
                    String magazineId = IO.readln("Ange Id: ");
                    String magazineTitle = IO.readln("Ange titel: ");
                    
                    int issueNumber;
                    try {
                        issueNumber = Integer.parseInt(IO.readln("Ange nummer: "));
                    } catch (NumberFormatException e) {
                        IO.println("Felaktigt nummer");
                        break;
                    }

                    String category = IO.readln("Ange kategori: ");

                    int publishedYear;
                    try {
                        publishedYear = Integer.parseInt(IO.readln("Ange publiceringsår: "));
                    } catch (NumberFormatException e) {
                        IO.println("Felaktigt år");
                        break;
                    }

                    Magazine newMagazine = new Magazine(magazineId, magazineTitle, true, issueNumber, category, publishedYear);
                    magazines.add(newMagazine);
                    IO.println("Tidningen lades till lokalt i samlingen");
                    break;

                case 7:
                    kör =false;
                    IO.println("Programmet avslutas");
                    break;
            
                default:
                    IO.println("Ogiltigt val");
            }   
        }
        Unirest.shutDown();




    }
}