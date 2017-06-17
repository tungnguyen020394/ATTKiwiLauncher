package com.att.kiwilauncher.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

import com.att.kiwilauncher.R;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class VideoFull extends AppCompatActivity implements View.OnTouchListener{

    Intent intent;

    VideoView video;
    ArrayList<String> list;
    int timePause;
    int indexvideo;
    ArrayList<String> listvideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full);
        listvideo=new ArrayList<>();
        list=new ArrayList<>();

        video= (VideoView) findViewById(R.id.video_Full);

        intent =getIntent();
        indexvideo = intent.getIntExtra("index",0);
        list=intent.getStringArrayListExtra("list");
        timePause =intent.getIntExtra("timePause",0);

    }

    @Override
    protected void onResume() {
        super.onResume();

        video.setVideoPath(list.get(indexvideo));
        video.seekTo(timePause);
        video.start();

    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
