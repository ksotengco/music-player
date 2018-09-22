package musicplayer.cs371m.musicplayer;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Settings extends AppCompatActivity {
    private boolean toLoop;
    private Intent goBackToMain;

    private ImageButton loopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        Intent in = getIntent();
        this.toLoop = in.getBooleanExtra("loop?", toLoop);

        loopButton = (ImageButton) findViewById(R.id.loop_button);
        changeLoopImage();
        goBackToMain = new Intent(this, MainActivity.class);
    }

    public void changeLoopImage () {
        if (toLoop) {
            loopButton.setImageResource(R.drawable.loop_black);
        } else {
            loopButton.setImageResource(R.drawable.loop);
        }
    }

    public void loopSettings (View view) {
        toLoop = !toLoop;
        changeLoopImage();
    }

    public void onCancel (View view) {
        setResult(RESULT_CANCELED, goBackToMain);
        finish();
    }

    public void onApply (View view) {
        goBackToMain.putExtra("loop?", toLoop);

        setResult(RESULT_OK, goBackToMain);
        finish();
    }
}
