package jk;

public class MusicAlbum extends Media {
    private String artist;

    public MusicAlbum(String id, String title, boolean isAvailable, String artist) {
        super(id, "music_album", title, isAvailable);
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String getInfo() {
        return "ID: " + id + ", Typ: " + type + ", Titel: " + title + ", Artist: " + artist
                + ", Tillgänglig: " + isAvailable;
    }
}
