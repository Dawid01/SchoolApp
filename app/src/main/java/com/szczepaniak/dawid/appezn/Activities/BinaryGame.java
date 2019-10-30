package com.szczepaniak.dawid.appezn.Activities;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.szczepaniak.dawid.appezn.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BinaryGame extends AppCompatActivity {

    private TextView number;
    private TextView score;
    private Button next;
    private ProgressBar progressBar;
    private List<Button> buttons;
    private LinearLayout buttonsLayout;

    private int decimalNumber;
    private int oldDecimalNumber = 0;
    private int scoreValue = 0;

    boolean start = false;
    Timer t = new Timer();

    private View parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binary_game);

        number = findViewById(R.id.number);
        number.setText("?");
        score = findViewById(R.id.score);
        score.setText("score: " + scoreValue);
        next = findViewById(R.id.next);
        next.setText("start");
        progressBar = findViewById(R.id.timer);
        buttons = new ArrayList<>();
        buttonsLayout = findViewById(R.id.buttonsLayout);
        parent = findViewById(R.id.parent);

        for(int i = 0; i < buttonsLayout.getChildCount(); i++){

            buttons.add((Button) buttonsLayout.getChildAt(i));
        }
        buttonsClickLisnter();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(start) {
                    String binary = "";
                    for (Button button : buttons) {
                        binary = binary + button.getText().toString();
                    }

                    int decimal = Integer.parseInt(binary, 2);

                    if (decimal == decimalNumber) {

                        scoreValue++;
                        score.setText("score: " + scoreValue);
                        createNewNumber();
                    } else {

                        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vib.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vib.vibrate(500);
                        }
                    }
                }else {

                    createTimer();
                    createNewNumber();
                }

            }
        });


    }

    void gameOver(){

        LayoutInflater inflater = LayoutInflater.from(BinaryGame.this);
        View popUpView = inflater.inflate(R.layout.game_over, null);
        final PopupWindow popupWindow = new PopupWindow(popUpView, parent.getWidth(), parent.getHeight(), true);
        popupWindow.setFocusable(false);
        popupWindow.setAnimationStyle(R.style.galeryPopup);
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

        TextView s = popUpView.findViewById(R.id.Score);
        s.setText("" + scoreValue);
        TextView best = popUpView.findViewById(R.id.best);
        Button playAgain = popUpView.findViewById(R.id.playAgain);
        final Button ranking = popUpView.findViewById(R.id.ranking);
        final ConstraintLayout rankingLayout = popUpView.findViewById(R.id.ranking_layout);


        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start = false;
                next.setText("start");
                number.setText("?");
                progressBar.setProgress(progressBar.getMax());
                scoreValue = 0;
                score.setText("score: " + scoreValue);
                popupWindow.dismiss();
            }
        });

        ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rankingLayout.getVisibility() == View.GONE) {
                    rankingLayout.setVisibility(View.VISIBLE);
                    ranking.setText("back");
                }else {
                    rankingLayout.setVisibility(View.GONE);
                    ranking.setText("ranking");
                }
            }
        });
    }


    void createTimer(){

        start = true;
        next.setText("next");
        t = new Timer();

            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressBar.getProgress() - 1);
                            if (progressBar.getProgress() == 0) {
                                t.cancel();
                                gameOver();

                            }
                        }
                    });
                }
            }, 100, 100);

    }

    void buttonsClickLisnter(){

        for(final Button button : buttons){

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String s = button.getText().toString();

                    if(s.equals("0")){
                        button.setText("1");
                    }else {
                        button.setText("0");
                    }
                }
            });
        }
    }

    void createNewNumber(){

        progressBar.setProgress(progressBar.getMax());

        for(Button button : buttons){
            button.setText("0");
        }

        boolean r = true;

        while (r) {
            Random random = new Random();
            int max = 255;
            if(scoreValue < 10){
                max = 64;
            }
            decimalNumber = random.nextInt(max);

            if(decimalNumber != oldDecimalNumber){
                r = false;
                oldDecimalNumber = decimalNumber;
                number.setText("" + decimalNumber);
            }
        }


    }


    @Override
    public void finish() {
        super.finish();
        t.cancel();
    }
}
