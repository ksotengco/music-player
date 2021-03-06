package musicplayer.cs371m.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;

interface SongManager {
    void playSong(Context context);
    void updateSong(View view);
    void playSong();
    void pauseSong();
    void stopSong();
}

public class Song implements SongManager {
    private String songTitle;
    private int songID;
    private boolean isPaused;

    private static MediaPlayer songPlayer = null;
    private static MainActivity callback;

    public Song (String title, int id) {
        songTitle = title;
        songID = id;
        isPaused = false;
    }

    // could not think of any other way to do this
    // should only be called once
    public void initActivity (MainActivity cb) {
        callback = cb;
    }

    public void playSong(Context context) {
        if (songPlayer != null) {
            stopSong();
        }
        songPlayer = MediaPlayer.create(context, songID);
        songPlayer.start();
        isPaused = false;

        songPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (callback.toLoop()) {
                    playSong();
                } else {
                    callback.nextSong();
                }
            }
        });
    }

    public void updateSong(View view) {
        if (songPlayer != null) {
            ImageButton playPauseButton = (ImageButton)  view.findViewById(R.id.play_button);
            if (isPaused) {
                playSong();
                playPauseButton.setImageResource(R.drawable.pause_button);
            } else {
                pauseSong();
                playPauseButton.setImageResource(R.drawable.play_button);
            }
            isPaused = !isPaused;
        }
    }

    public void playSong () {
        songPlayer.start();
    }

    public void pauseSong () {
        songPlayer.pause();
    }

    public void changeProgress (int progress) {
        songPlayer.seekTo(progress * 1000);
    }

    public void stopSong() {
        songPlayer.stop();
        songPlayer.reset();
        songPlayer.release();
        songPlayer = null;
    }

    String getSongTitle() { return songTitle; }
    int getSongID() { return songID; }
    int getCurrentPosition () { return songPlayer.getCurrentPosition()/1000; }
    int getDuration () { return songPlayer.getDuration()/1000; }
}
