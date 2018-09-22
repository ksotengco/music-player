package musicplayer.cs371m.musicplayer;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.Calendar;

// Repurposed from demo code
public class TimeElaspedHandler {
    public interface IUpdate {
        void updateTimeElasped(final int timeValue);
    }
    protected Runnable rateLimitRequest;
    protected final int rateLimitMillis = 200; // 1/5 sec

    protected IUpdate iUpdate;
    protected Handler handler;

    protected HandlerThread handlerThread;
    protected boolean isPaused;

    protected Song currentSong;

    public TimeElaspedHandler(final IUpdate iUpdate, final Song song) {
        handlerThread = new HandlerThread("TimeElaspedHandler");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        this.iUpdate = iUpdate;
        this.currentSong = song;
        isPaused = false;

        rateLimitRequest = new Runnable() {
            @Override
            public void run() {
                int seconds = currentSong.getDuration();
                // Handlers usually communicate with message queues, but this is simpler
                iUpdate.updateTimeElasped(seconds);
                handler.postDelayed(this, rateLimitMillis);
            }
        };

        handler.postDelayed(rateLimitRequest, rateLimitMillis);
    }

    public void togglePause() {
        isPaused = !isPaused;
        if (!isPaused) {
            handler.postDelayed(rateLimitRequest, rateLimitMillis);
        } else {
            handler.removeCallbacks(rateLimitRequest);
        }
    }

    public void stopHandler() {
        handler.removeCallbacks(rateLimitRequest);
    }
}
