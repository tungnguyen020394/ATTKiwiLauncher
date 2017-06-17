package com.att.kiwilauncher.view;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

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

    SimpleExoPlayerView exoPlayer;
    SimpleExoPlayer player;
    TrackSelector trackSelector;
    Intent intent;
    ArrayList<String> list;
    private long timePause;
    int indexvideo;
    ArrayList<String> listvideo;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full);
        listvideo=new ArrayList<>();
        mDatabaseHelper=new DatabaseHelper(this);
        listvideo= mDatabaseHelper.getListVideoAnhQuangCao();

        exoPlayer= (SimpleExoPlayerView) findViewById(R.id.video_Full);
        intent =getIntent();

        indexvideo = intent.getIntExtra("index",0);
        list=intent.getStringArrayListExtra("list");
        timePause =intent.getLongExtra("timePause",0);

    }

    @Override
    protected void onResume() {
        super.onResume();

        createPlayer(listvideo.get(indexvideo).split(";")[0]);
        exoPlayer.setOnTouchListener(this);

    }

    public void createPlayer(String link) {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        exoPlayer.setPlayer(player);
        prepareVideo(link);
    }

    public void prepareVideo(String link) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "ShweVideo"), bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(link),
                dataSourceFactory, extractorsFactory, null, null);

        player.prepare(videoSource);
        player.seekTo(timePause);
        player.setPlayWhenReady(true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
