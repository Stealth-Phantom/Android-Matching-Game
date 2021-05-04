package eng.asu.khmatchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartScreen extends AppCompatActivity {

    Intent intent;
    Button startBtn;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        intent = new Intent(StartScreen.this,MainActivity.class);
        startBtn = findViewById(R.id.startBtn);
        mp = MediaPlayer.create(this,R.raw.dearly_beloved);
        mp.start();
    }

    public void start(View v)
    {
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        mp.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mp.pause();
    }
}
