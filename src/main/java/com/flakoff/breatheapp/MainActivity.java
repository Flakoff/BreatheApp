package com.flakoff.breatheapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flakoff.breatheapp.util.Prefs;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.time.LocalDate;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView breathsTakenTextView, todayTotalTimeTextView, lastBreathTextView, guideTextView;
    private Button startButton;
    private Prefs prefs;
    private boolean breathing;
    private Vibrator vibrator;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        breathsTakenTextView = findViewById(R.id.breathsTakenTextView);
        todayTotalTimeTextView = findViewById(R.id.todayTotalTimeTextView);

        lastBreathTextView = findViewById(R.id.lastBreathTextView);
        guideTextView = findViewById(R.id.guideTextView);
        startButton = findViewById(R.id.startButton);
        prefs = new Prefs(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        todayTotalTimeTextView.setText("Total " + prefs.getSessions() + " minutes");
        breathsTakenTextView.setText(prefs.getBreaths() + " breaths taken");
        lastBreathTextView.setText("Last time was at " + prefs.getDate());



        startIntroAnimation();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation();
                }
        });
    }

    private void startIntroAnimation(){

        ViewAnimator
                .animate(guideTextView)
                .scale(0, 1)
                .duration(1540)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        guideTextView.setText("Push Start button");
                    }
                })
                .start();
    }

    private void startAnimation(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ViewAnimator
                .animate(guideTextView)
                .onStart(new AnimationListener.Start() {
                    @Override
                    public void onStart() {
                        guideTextView.setText("Please, be ready");
                        startButton.setEnabled(false);
                    }
                })
                .duration(1000)
                .andAnimate(imageView)
                .scale(1, 0.1f)
                .rotation(360)
                .duration(1500)
                .thenAnimate(imageView)
                .scale(0.1f, 1.5f, 0.1f)
                .rotation(-720)
                .repeatCount(7)
                .duration(7500)
                .andAnimate(guideTextView)
                .onStart(new AnimationListener.Start() {
                    @Override
                        public void onStart() {
                            breathing = true;
                            guideTextView.setText("Inhale ... Exhale");

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    int[] amplitudes = new int[]{0, 50, 0};
                                    long[] timings = new long[]{0, 3750, 3750};
                                    vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, 8));
                                }else{
                                    //deprecated in API 26
                                    long[] timings = new long[]{0, 3750, 3750};
                                    vibrator.vibrate(timings, 0);
                                }


                        }
                    })
                .onStop(new AnimationListener.Stop() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onStop() {
                        guideTextView.setText("Good job! \n Push Start button for more");
                        ViewAnimator.animate(imageView)
                                .scale(0.1f, 1)
                                .rotation(-360)
                                .start();

                            Date cur = new Date(System.currentTimeMillis());
                            Date last = new Date(prefs.getDateInMillis());
                            if (cur.after(last)) {
                                prefs.setSessions(1);
                                prefs.setBreaths(8);
                            }
                            else {
                                prefs.setSessions(prefs.getSessions() + 1);
                                prefs.setBreaths(prefs.getBreaths() + 8);
                            }

                        prefs.setDate(System.currentTimeMillis());
                        todayTotalTimeTextView.setText("Total " + prefs.getSessions() + " min");
                        breathsTakenTextView.setText(prefs.getBreaths() + " breaths taken");
                        lastBreathTextView.setText("Last was at " + prefs.getDate());
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                        breathing = false;
                        vibrator.cancel();
                        startButton.setEnabled(true);


                    }
                })
                    .start();


    }
}
