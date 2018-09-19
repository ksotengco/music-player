package musicplayer.cs371m.musicplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static private ArrayList<Song> songList = new ArrayList<>();
    private ListView listView;

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
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.song_list);

        final SongAdapter adapter = new SongAdapter(getApplicationContext());
        adapter.addAll(songList);

        listView.setAdapter(adapter);
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
            // TODO: add settings intent
        }

        return super.onOptionsItemSelected(item);
    }

    public void testClick(View view) {
        Toast.makeText(this, "lets go", Toast.LENGTH_SHORT).show();
    }
}
