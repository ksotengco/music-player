package musicplayer.cs371m.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;

public class MyMediaPlayer extends MediaPlayer {
    private Song currentSong;
    private Song nextSong;

    public static MediaPlayer playSong(Context context, Song song) {
        return MediaPlayer.create(context, song.getSongID());
    }
}
