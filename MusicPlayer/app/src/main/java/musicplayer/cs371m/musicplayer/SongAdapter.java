package musicplayer.cs371m.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

public class SongAdapter extends ArrayAdapter<Song>{
    private LayoutInflater theInflater = null;

    public SongAdapter (Context context) {
        super(context, R.layout.row);
        theInflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = theInflater.inflate(R.layout.row, parent, false);
        }

        TextView songName = (TextView) convertView.findViewById(R.id.row_text);
        Song song = (Song) getItem(position);
        songName.setText(song.getSongTitle());

        return convertView;
    }
}
