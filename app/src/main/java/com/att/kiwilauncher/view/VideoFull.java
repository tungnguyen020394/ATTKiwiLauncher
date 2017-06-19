package com.att.kiwilauncher.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;

import com.att.kiwilauncher.R;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.util.CheckLink;
import com.att.kiwilauncher.util.Volume;
import com.bumptech.glide.Glide;
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

import butterknife.BindView;

public class VideoFull extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Intent intent;
    int timePause;
    int indexVideo=0;
    MediaPlayer mp;
    Handler handler;
    ArrayList<String> listvideo;

    CheckLink checkLink;
    Volume volume;
    ViewHoder vh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full);

        initVideo();
    }

    private void initVideo() {
        vh=new ViewHoder();
        listvideo = new ArrayList<>();
//        list = new ArrayList<>();
        checkLink = new CheckLink();
        intent = getIntent();
        handler = new Handler();
        volume = new Volume();

        vh.ibtNextVideo.setOnClickListener(this);
        vh.ibtPlayVideo.setOnClickListener(this);
        vh.ibtBackVideo.setOnClickListener(this);
        vh.ibtExitFullVideo.setOnClickListener(this);
        vh.ibtVolumeOfVideo.setOnClickListener(this);
        vh.ibtVolumeOnVideo.setOnClickListener(this);
        vh.ibtPauseVideo.setOnClickListener(this);
        vh.imgWebVideo.setOnClickListener(this);

        indexVideo = intent.getIntExtra("index", 0);
        listvideo = intent.getStringArrayListExtra("list");
        timePause = intent.getIntExtra("timePause", 0);

        setVideoOrImager(listvideo.get(indexVideo));
    }

    private class ViewHoder{

        ImageButton ibtPlayVideo,ibtNextVideo,ibtBackVideo,ibtPauseVideo,ibtExitFullVideo,ibtVolumeOfVideo,ibtVolumeOnVideo;

        ImageView imgWebVideo,imgView;
        VideoView video;
        TextView tvTimeVideo,tvTimeStartVideo,tvTimeEndVideo;

        public ViewHoder() {
            ibtBackVideo= (ImageButton) findViewById(R.id.imgBack_video);
            ibtPlayVideo= (ImageButton) findViewById(R.id.imgPlay_video);
            ibtNextVideo= (ImageButton) findViewById(R.id.imgNext_video);
            ibtPauseVideo= (ImageButton) findViewById(R.id.imgPause_video);
            ibtExitFullVideo= (ImageButton) findViewById(R.id.imgExitFull);
            ibtVolumeOfVideo= (ImageButton) findViewById(R.id.imgVolumeOf_video);
            ibtVolumeOnVideo= (ImageButton) findViewById(R.id.imgVolumeOn_video);

            video= (VideoView) findViewById(R.id.video_Full);

            imgWebVideo= (ImageView) findViewById(R.id.imgWeb_video);
            imgView= (ImageView) findViewById(R.id.imgView_Full);

            tvTimeVideo= (TextView) findViewById(R.id.tvTime_video);
            tvTimeStartVideo= (TextView) findViewById(R.id.tvTimeBegin_video);
            tvTimeEndVideo= (TextView) findViewById(R.id.tvTimeEnd_video);

        }
    }

    private void setVideoOrImager(String check) {

        int position = checkLink.CheckLinkURL(check);
        if (position == 1) {
            vh.imgView.setVisibility(View.VISIBLE);
            vh.video.setVisibility(View.GONE);
            vh.ibtPauseVideo.setVisibility(View.GONE);
            vh.ibtVolumeOfVideo.setVisibility(View.GONE);
            vh.tvTimeStartVideo.setVisibility(View.GONE);
            vh.tvTimeVideo.setVisibility(View.GONE);

            vh.tvTimeEndVideo.setText("   ");
            Glide.with(this)
                    .load(listvideo.get(indexVideo))
                    .into(vh.imgView);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (indexVideo == listvideo.size() - 1) indexVideo = 0;
                    else indexVideo++;

                    setVideoOrImager(listvideo.get(indexVideo));
                }
            }, 5000);

        } else if (position == 2) {
            vh.imgView.setVisibility(View.GONE);
            vh.video.setVisibility(View.VISIBLE);

            vh.ibtPauseVideo.setVisibility(View.VISIBLE);
            vh.ibtVolumeOfVideo.setVisibility(View.VISIBLE);
            vh.tvTimeStartVideo.setVisibility(View.VISIBLE);
            vh.tvTimeVideo.setVisibility(View.VISIBLE);

            // đọ dài của video
            mp = MediaPlayer.create(this, Uri.parse(check));
            int duration = mp.getDuration();
            mp.release();

            vh.tvTimeEndVideo.setText(checkLink.stringForTime(duration));
            updateTime(vh.tvTimeStartVideo);

            vh.video.setVideoPath(listvideo.get(indexVideo));

            vh.video.start();
            vh.video.seekTo(timePause);

            vh.video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    indexVideo++;
                    setVideoOrImager(listvideo.get(indexVideo));
                }
            });
        } else if (position == 3) {
            vh.imgView.setVisibility(View.GONE);
            vh.video.setVisibility(View.VISIBLE);

            MediaController mc = new MediaController(this);
            vh.video.setMediaController(mc);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    vh.video.setVideoURI(Uri.parse(listvideo.get(indexVideo)));
                    vh.video.start();
                }
            });
        }
    }

    private void updateTime(final TextView tv) {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                tv.setText(checkLink.stringForTime(vh.video.getCurrentPosition()));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgWeb_video:
                Uri uri = Uri.parse("http://www.bongdaso.com/news.aspx");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.imgExitFull:
                intent = new Intent();
                intent.putExtra("index", indexVideo);
                intent.putExtra("list", listvideo);

                // độ dài video đang chạy
                int timepause = vh.video.getCurrentPosition();
                intent.putExtra("timePause", timepause);

                onBackPressed();
                break;
            case R.id.imgVolumeOf_video:
                vh.ibtVolumeOnVideo.setVisibility(View.VISIBLE);
                vh.ibtVolumeOfVideo.setVisibility(View.GONE);

                volume.MuteAudio(this);
                break;

            case R.id.imgVolumeOn_video:
                vh.ibtVolumeOnVideo.setVisibility(View.GONE);
                vh.ibtVolumeOfVideo.setVisibility(View.VISIBLE);
                volume.UnMuteAudio(this);

                break;
//
            case R.id.imgPause_video:
                vh.ibtPlayVideo.setVisibility(View.VISIBLE);
                vh.ibtPauseVideo.setVisibility(View.GONE);
                vh.video.pause();
                break;
            case R.id.imgPlay_video:
                vh.ibtPlayVideo.setVisibility(View.GONE);
                vh.ibtPauseVideo.setVisibility(View.VISIBLE);
                vh.video.start();
                break;

            case R.id.imgNext_video:
                if (indexVideo == (listvideo.size() - 1)) {
                    indexVideo = 0;
                } else indexVideo++;
                setVideoOrImager(listvideo.get(indexVideo));

                break;
            case R.id.imgBack_video:
                if (indexVideo == 0) {
                    indexVideo = listvideo.size() - 1;
                } else indexVideo--;

                setVideoOrImager(listvideo.get(indexVideo));
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
