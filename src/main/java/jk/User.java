package jk;

/**
 * Författare: Johnny Battah
 * Klassen User representerar en användare i bibliotekssystemet.
 * Den innehåller: id, namn och email för en kund.
 */

public class User implements Comparable<User>{
    private String id;
    private String name;
    private String email;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInfo() {
        return "ID: " + id + ", Namn: " + name + ", Email: " + email;
    }

    @Override
    public int compareTo(User other){
        return this.name.compareToIgnoreCase(other.name);
    }

}
