package eng.asu.khmatchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultScreen extends AppCompatActivity {

    public static final String PREFS_NAME = "bestScore";

    Intent prevIntent;
    Intent nextIntent;
    TextView scoreText;
    TextView bestScoreText;
    long score,bestScore;
    Button play;
    Button exit;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        mp = MediaPlayer.create(this,R.raw.level_clear);
        mp.start();
        scoreText = findViewById(R.id.scoreText);
        bestScoreText = findViewById(R.id.bestScoreText);
        play = findViewById(R.id.playAgainBtn);
        exit = findViewById(R.id.exitBtn);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        bestScore = settings.getLong("Best",999999);
        prevIntent = getIntent();
        nextIntent = new Intent(ResultScreen.this,MainActivity.class);

        score = prevIntent.getLongExtra("result",-1);
        scoreText.append(String.valueOf(score) + " Seconds");
        bestScoreText.append(String.valueOf(bestScore) + " Seconds");

    }

    public void option(View v)
    {
        Button clicked = (Button)v;
        mp.stop();
        mp.release();
        if(clicked.getText().toString().equals("Play again? :)"))
            startActivity(nextIntent);
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
