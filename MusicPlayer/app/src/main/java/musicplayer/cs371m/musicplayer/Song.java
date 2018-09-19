package musicplayer.cs371m.musicplayer;

public class Song {
    private String songTitle;
    private int songID;

    public Song (String title, int id) {
        songTitle = title;
        songID = id;
    }

    String getSongTitle() { return songTitle; }
}
