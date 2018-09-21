package musicplayer.cs371m.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;

interface SongManager {
    void playSong(Context context);
    void updateSong(View view);
    void stopSong();
}

public class Song implements SongManager {
    private String songTitle;
    private int songID;
    private boolean isPaused;

    private static MediaPlayer songPlayer = null;
    private static Song currentSong;

    public Song () { }
    public Song (String title, int id) {
        songTitle = title;
        songID = id;
        isPaused = false;
    }

    public void playSong(Context context) {
        if (songPlayer != null) {
            System.out.println("freeing");
            songPlayer.release();
        }
        songPlayer = MediaPlayer.create(context, songID);
        songPlayer.start();
        currentSong = this;
    }

    public void updateSong(View view) {
        if (songPlayer != null && currentSong != null) {
            ImageButton playPauseButton = (ImageButton)  view.findViewById(R.id.play_button);
            if (!currentSong.isPaused) {
                songPlayer.pause();
                playPauseButton.setImageResource(R.drawable.play_button);
            } else {
                songPlayer.start();
                playPauseButton.setImageResource(R.drawable.pause_button);
            }
            currentSong.isPaused = !currentSong.isPaused;
        }
    }

    public void stopSong() {
        songPlayer.stop();
        songPlayer.release();
    }

    String getSongTitle() { return songTitle; }
    int getSongID() { return songID; }
}
