package jk.model;

public class MusicAlbum extends Media {
    private String artist;

    public MusicAlbum(String id, String title, boolean isAvailable, String artist) {
        super(id, "music_album", title, isAvailable);
        if (artist == null || artist.isBlank()) { throw new IllegalArgumentException("Artist får inte vara tom.");}

        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        if (artist == null || artist.isBlank()) { throw new IllegalArgumentException("Artist får inte vara tom.");}
        this.artist = artist;
    }

    @Override
    public String getInfo() {
        return """
                --- Musikalbum ---
                ID: %s
                Typ: %s
                Titel: %s
                Artist: %s
                Tillgänglig: %b
                """.formatted(id, type, title, artist, isAvailable);
    }
}
