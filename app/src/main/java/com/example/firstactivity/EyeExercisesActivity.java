package com.example.firstactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class EyeExercisesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_exercises);

        YouTubePlayerView youtubePlayer1 = findViewById(R.id.youtube_player1);
        YouTubePlayerView youtubePlayer2 = findViewById(R.id.youtube_player2);
        Button btnBack = findViewById(R.id.btn_back);

        getLifecycle().addObserver(youtubePlayer1);
        getLifecycle().addObserver(youtubePlayer2);

        // Load first eye exercise video
        youtubePlayer1.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo("iVb4vUp70zY", 0);  // Video ID for "7 Easy Eye Exercises To Improve Your Vision"
            }
        });

        // Load second eye exercise video
        youtubePlayer2.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo("UicwXt3jHvE", 0);  // Video ID for "Daily Yoga for Eyes | 5 Eye Exercises to Relax & Strengthen"
            }
        });


        // Go back to User Profile
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
