package musicplayer.cs371m.musicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
                          implements TimeElaspedHandler.IUpdate {
    static private ArrayList<Song> songList = new ArrayList<>();
    private ListView listView;

    private SongManager manager;
    private Song currentSong;
    private Song nextSong;
    private Song prevSong;

    private TextView currentSongText;
    private TextView nextSongText;

    private TextView timeElasped;
    private TextView timeLeft;
    private TimeElaspedHandler handler;

    // SeekBar functionality: https://stackoverflow.com/questions/17168215/seekbar-and-media-player-in-android
    private SeekBar seekBar;
    private ImageButton playPauseButton;

    private boolean loop;
    final private int songSize = songList.size();

    static {
        songList.add(new Song("Fur Elise", R.raw.fur_elise));
        songList.add(new Song("Les Baricades Misterieuses", R.raw.les_baricades_misterieuses));
        songList.add(new Song("Piano Sonata K 310 MVT 1", R.raw.piano_sonata_k_310_mvt_1));
        songList.add(new Song("Piano Sonata K 545 MVT 1", R.raw.piano_sonata_k_545_mvt_1));
        songList.add(new Song("Prelude in C Major BWV 846a", R.raw.prelude_in_c_major_bwv_846a));
        songList.add(new Song("Prelude in C Minor", R.raw.prelude_in_c_minor));
        songList.add(new Song("Prelude in E Minor", R.raw.prelude_in_e_minor));
        songList.add(new Song("Rondo from Piano Sonata No. 58", R.raw.rondo_from_piano_sonata_no_58));
        songList.add(new Song("Sonata in G Minor MVT 1", R.raw.sonata_in_g_minor_mvt_1));
        songList.add(new Song("The Entertainer Rag", R.raw.the_entertainer_rag));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        play(0);

        currentSong.initActivity(this);
        listView = (ListView) findViewById(R.id.song_list);

        final SongAdapter adapter = new SongAdapter(getApplicationContext());
        adapter.addAll(songList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                handler.stopHandler();
                play(i);
            }
        });
    }

    public void init() {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loop = false;

        currentSongText = (TextView) findViewById(R.id.current_song_text);
        nextSongText    = (TextView) findViewById(R.id.next_song_text);

        playPauseButton = (ImageButton) findViewById(R.id.play_button);

        timeElasped     = (TextView) findViewById(R.id.time_passed);
        timeLeft        = (TextView) findViewById(R.id.time_left);

        seekBar         = (SeekBar) findViewById(R.id.seekbar);

        // StackOverflow link above next to SeekBar declaration
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (currentSong != null && fromUser) {
                    currentSong.changeProgress(i);
                    final int minutes = i/60;
                    final int seconds = i%60;

                    final int minutesLeft = (currentSong.getDuration() - i)/60;
                    final int secondsLeft = (currentSong.getDuration() - i)%60;

                    // change the time elapsed/time left based on SeekBar progress
                    timeElasped.setText(String.format("%02d:%02d", minutes, seconds));
                    timeLeft.setText(String.format("%02d:%02d", minutesLeft, secondsLeft));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void updateTimeElasped(final int timeValue) {
        final int minutes = timeValue/60;
        final int seconds = timeValue%60;

        final int minutesLeft = (currentSong.getDuration() - timeValue)/60;
        final int secondsLeft = (currentSong.getDuration() - timeValue)%60;

        if (false) {
            timeElasped.setText(String.format("%02d:%02d", minutes, seconds));
            timeLeft.setText(String.format("%02d:%02d", minutesLeft, secondsLeft));
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timeElasped.setText(String.format("%02d:%02d", minutes, seconds));
                    timeLeft.setText(String.format("%02d:%02d", minutesLeft, secondsLeft));
                    seekBar.setProgress(currentSong.getCurrentPosition());
                }
            });
        }
    }

    public void play (int songNum) {
        currentSong = songList.get(songNum);
        manager = currentSong;

        if (songNum == songSize - 1) {
            nextSong = songList.get(0);
        } else {
            nextSong = songList.get(songNum + 1);
        }

        if (songNum == 0) {
            prevSong = songList.get(songSize - 1);
        } else {
            prevSong = songList.get(songNum - 1);
        }

        currentSongText.setText("Current Song: " +  currentSong.getSongTitle());
        nextSongText.setText("Next Song: " + nextSong.getSongTitle());

        currentSong.playSong(getApplicationContext());
        playPauseButton.setImageResource(R.drawable.pause_button);

        seekBar.setMax(currentSong.getDuration());
        handler = new TimeElaspedHandler(this, currentSong);
    }

    public void pausePlay (View view) {
        if (currentSong != null) {
            handler.togglePause();
            manager.updateSong(view);
        }
    }

    // for the OnCompletion method
    public void nextSong() {
        if (nextSong != null) {
            handler.stopHandler();
            play(songList.indexOf(nextSong));
        }
    }

    public void skipForward(View view) {
        nextSong();
    }

    public void skipBackward(View view) {
        if (prevSong != null) {
            handler.stopHandler();
            play(songList.indexOf(prevSong));
        }
    }

    public boolean toLoop () { return loop; }

    @Override
    protected void onStop() {
        super.onStop();
        if (currentSong != null) {
            handler.togglePause();
            currentSong.pauseSong();
            playPauseButton.setImageResource(R.drawable.play_button);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentSong != null) {
            manager.stopSong();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (currentSong != null) {
            handler.togglePause();
            currentSong.playSong();
            playPauseButton.setImageResource(R.drawable.pause_button);
        }
    }

    // Taken from Demo repo code
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent settings = new Intent(this, Settings.class);
            Bundle toLoop = new Bundle();

            toLoop.putBoolean("loop?", loop);
            settings.putExtras(toLoop);

            final int result = 1;
            startActivityForResult(settings, result);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            loop = data.getBooleanExtra("loop?", loop);
        }
    }
}
