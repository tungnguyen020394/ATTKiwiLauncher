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

    @BindView(R.id.imgPlay_video)
    ImageButton ibtPlayVideo;
    @BindView(R.id.imgNext_video)
    ImageButton ibtNextVideo;
    @BindView(R.id.imgBack_video)
    ImageButton ibtBackVideo;
    @BindView(R.id.imgPause_video)
    ImageButton ibtPauseVideo;
    @BindView(R.id.imgExitFull)
    ImageButton ibtExitFullVideo;
    @BindView(R.id.imgVolumeOf_video)
    ImageButton ibtVolumeOfVideo;
    @BindView(R.id.imgVolumeOn_video)
    ImageButton ibtVolumeOnVideo;
    @BindView(R.id.imgWeb_video)
    ImageView imgWebVideo;
    @BindView(R.id.video_Full)
    VideoView video;
    @BindView(R.id.imgView_Full)
    ImageView imgView;
    @BindView(R.id.tvTime_video)
    TextView tvTimeVideo;
    @BindView(R.id.tvTimeBegin_video)
    TextView tvTimeStartVideo;
    @BindView(R.id.tvTimeEnd_video)
    TextView tvTimeEndVideo;

    Intent intent;
    int timePause;
    int indexVideo;
    MediaPlayer mp;
    Handler handler;
    ArrayList<String> listvideo;
    ArrayList<String> list;
    CheckLink checkLink;
    Volume volume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full);

        initVideo();

        indexVideo = intent.getIntExtra("index", 0);
        list = intent.getStringArrayListExtra("list");
        timePause = intent.getIntExtra("timePause", 0);
        setVideoOrImager(list.get(indexVideo));

    }

    private void initVideo() {
        listvideo = new ArrayList<>();
        list = new ArrayList<>();
        checkLink = new CheckLink();
        intent = getIntent();
        handler = new Handler();
        volume = new Volume();

        ibtNextVideo.setOnClickListener(this);
        ibtPlayVideo.setOnClickListener(this);
        ibtBackVideo.setOnClickListener(this);
        ibtExitFullVideo.setOnClickListener(this);
        ibtVolumeOfVideo.setOnClickListener(this);
        ibtVolumeOnVideo.setOnClickListener(this);
        ibtPauseVideo.setOnClickListener(this);
        imgWebVideo.setOnClickListener(this);
    }


    private void setVideoOrImager(String check) {

        int position = checkLink.CheckLinkURL(check);
        if (position == 1) {
            imgView.setVisibility(View.VISIBLE);
            video.setVisibility(View.GONE);
            ibtPauseVideo.setVisibility(View.GONE);
            ibtVolumeOfVideo.setVisibility(View.GONE);
            tvTimeStartVideo.setVisibility(View.GONE);
            tvTimeVideo.setVisibility(View.GONE);

            tvTimeEndVideo.setText("   ");
            Glide.with(this)
                    .load(listvideo.get(indexVideo))
                    .into(imgView);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (indexVideo == listvideo.size() - 1) indexVideo = 0;
                    else indexVideo++;

                    setVideoOrImager(listvideo.get(indexVideo));
                }
            }, 5000);

        } else if (position == 2) {
            imgView.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);

            ibtPauseVideo.setVisibility(View.VISIBLE);
            ibtVolumeOfVideo.setVisibility(View.VISIBLE);
            tvTimeStartVideo.setVisibility(View.VISIBLE);
            tvTimeVideo.setVisibility(View.VISIBLE);

            // đọ dài của video
            mp = MediaPlayer.create(this, Uri.parse(check));
            int duration = mp.getDuration();
            mp.release();

            tvTimeEndVideo.setText(checkLink.stringForTime(duration));
            updateTime(tvTimeStartVideo);

            video.setVideoPath(listvideo.get(indexVideo));
            video.start();

            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    indexVideo++;
                    setVideoOrImager(listvideo.get(indexVideo));
                }
            });
        } else if (position == 3) {
            imgView.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);

            MediaController mc = new MediaController(this);
            video.setMediaController(mc);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    video.setVideoURI(Uri.parse(listvideo.get(indexVideo)));
                    video.start();
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
                                tv.setText(checkLink.stringForTime(video.getCurrentPosition()));
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
                intent = new Intent(getBaseContext(), VideoFull.class);
                intent.putExtra("index", indexVideo);
                intent.putExtra("list", listvideo);
                String str = listvideo.get(indexVideo);

                // độ dài video đang chạy
                int timepause = video.getCurrentPosition();
                intent.putExtra("timePause", timepause);
                startActivity(intent);
                break;
            case R.id.imgVolumeOf_video:
                ibtVolumeOnVideo.setVisibility(View.VISIBLE);
                ibtVolumeOfVideo.setVisibility(View.GONE);

                volume.MuteAudio(this);
                break;

            case R.id.imgVolumeOn_video:
                ibtVolumeOnVideo.setVisibility(View.GONE);
                ibtVolumeOfVideo.setVisibility(View.VISIBLE);
                volume.UnMuteAudio(this);

                break;
//
            case R.id.imgPause_video:
                ibtPlayVideo.setVisibility(View.VISIBLE);
                ibtPauseVideo.setVisibility(View.GONE);
                video.pause();
                break;
            case R.id.imgPlay_video:
                ibtPlayVideo.setVisibility(View.GONE);
                ibtPauseVideo.setVisibility(View.VISIBLE);
                video.start();
                break;

            case R.id.imgNext_video:
                if (indexVideo == (listvideo.size() - 1)) {
                    indexVideo = 0;
                } else indexVideo++;

                setVideoOrImager(listvideo.get(indexVideo));

                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        indexVideo++;
                        setVideoOrImager(listvideo.get(indexVideo));
                    }
                });
                break;
//
            case R.id.imgBack_video:
                if (indexVideo == 0) {
                    indexVideo = listvideo.size() - 1;
                } else indexVideo--;

                setVideoOrImager(listvideo.get(indexVideo));

                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        indexVideo++;
                        setVideoOrImager(listvideo.get(indexVideo));
                    }
                });
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
