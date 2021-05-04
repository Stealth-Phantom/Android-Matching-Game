package eng.asu.khmatchinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaDescrambler;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "bestScore";

    MediaPlayer mp;
    MediaPlayer bgm;
    Random rand;
    Button[] btn;
    boolean flipped = false;
    int flipCount = 0;
    int flippedButtons = 0;
    Button flippedBtn;
    int a; //Placeholder for the random number
    long startTime, endTime, score, bestScore;
    ArrayList<Integer> numbers; //it holds the numbers for characters, 0 = sora, 1 = donald, 2 = goofy, 3 = mickey
    Intent intent;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        settings = getSharedPreferences(PREFS_NAME,0);
        editor = settings.edit();

        bestScore = settings.getLong("Best",999999);

        intent = new Intent(MainActivity.this,ResultScreen.class);
        rand = new Random();
        mp = new MediaPlayer();
        //bgm = MediaPlayer.create(this,R.raw.working_together);
        btn = new Button[8];
        flippedBtn = new Button(getApplicationContext());
        flippedBtn.setText("");
        numbers = new ArrayList();
        for(int i=0; i<8; i++) {
            String buttonID = "btn" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            btn[i] = findViewById(resID);
            numbers.add(i%4);
        }
        for(int i=0; i<8; i++)
        {
            if(i==7)
                a = 0;
            else
                a = Math.abs(rand.nextInt())%(numbers.size() - 1);
            btn[i].setText(String.valueOf(numbers.get(a)));
            numbers.remove(a);
        }
        startTime = System.currentTimeMillis();
        //bgm.start();
    }

    public void btnClicked(View v)
    {
        if(flippedButtons<2)
        {
            if(mp.isPlaying()) {
                mp.stop();
                mp.release();
            }
            final Button clicked = (Button) v;
            final String clickedChar = clicked.getText().toString();
            Handler handler = new Handler();
            switch(clickedChar)
            {
                case "0":
                    clicked.setBackgroundResource(R.drawable.sora_sprite);
                    mp = MediaPlayer.create(this,R.raw.sora_name);
                    break;
                case "1":
                    clicked.setBackgroundResource(R.drawable.donald_sprite);
                    mp = MediaPlayer.create(this,R.raw.donald_name);
                    break;
                case "2":
                    clicked.setBackgroundResource(R.drawable.goofy_sprite);
                    mp = MediaPlayer.create(this,R.raw.goofy_name);
                    break;
                case "3":
                    clicked.setBackgroundResource(R.drawable.mickey_sprite);
                    mp = MediaPlayer.create(this,R.raw.mickey_name);
                    break;
            }
            clicked.setEnabled(false);
            mp.start();
            if(flipped)
            {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(clickedChar.equals(flippedBtn.getText().toString()))
                        {
                            clicked.setVisibility(View.INVISIBLE);
                            flippedBtn.setVisibility(View.INVISIBLE);
                            flipCount++;
                            if(flipCount == 4)
                            {
                                endTime = System.currentTimeMillis();
                                score = (endTime - startTime)/1000;
                                intent.putExtra("result",score);
                                if(score < bestScore) {
                                    editor.putLong("Best", score);
                                    editor.commit();
                                }
                                //bgm.stop();
                                //bgm.release();
                                startActivity(intent);
                                finish();
                            }
                        }
                        else
                        {
                            clicked.setBackgroundResource(R.drawable.ripple);
                            flippedBtn.setBackgroundResource(R.drawable.ripple);
                            clicked.setEnabled(true);
                            flippedBtn.setEnabled(true);
                        }
                        flippedButtons = 0;
                        flipped = false;
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                },500);
            }
            else
            {
                flippedBtn = clicked;
                flipped = true;
            }
            flippedButtons++;
        }
    }
}
