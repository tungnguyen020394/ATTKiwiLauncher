package com.att.kiwilauncher.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
<<<<<<< HEAD
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
=======
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
>>>>>>> master

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

<<<<<<< HEAD
public class VideoFull extends AppCompatActivity implements View.OnTouchListener{
=======
public class VideoFull extends AppCompatActivity implements View.OnClickListener {
>>>>>>> master

    SimpleExoPlayerView exoPlayer;
    SimpleExoPlayer player;
    TrackSelector trackSelector;
    Intent intent;
<<<<<<< HEAD
    ArrayList<String> list;
    private long timePause;
    int indexvideo;
    ArrayList<String> listvideo;
    DatabaseHelper mDatabaseHelper;
=======
    int timePause = 0;
    int didIndex = 0;
    int indexVideo = 0, position;
    MediaPlayer mp;
    Handler handler;
    ArrayList<String> listvideo;
    List<View> listItem;
    CheckLink checkLink;
    ViewHoder vh;
    LinearLayout layoutControl;
    private int currentApiVersion;
    int intVolum;
    boolean playing = true, mute = false, canclick = true;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    AudioManager audioManager;
>>>>>>> master

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full);
        listvideo=new ArrayList<>();
        mDatabaseHelper=new DatabaseHelper(this);
        exoPlayer= (SimpleExoPlayerView) findViewById(R.id.video_Full);

<<<<<<< HEAD
        intent =getIntent();
=======
        hideTabBar();
        initVideo();
        addListMap();
    }

    private void hideTabBar() {

        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void initVideo() {
        vh = new ViewHoder();
        listvideo = new ArrayList<>();
//        list = new ArrayList<>();
        checkLink = new CheckLink();
        intent = getIntent();
        listvideo = intent.getStringArrayListExtra("list");
        handler = new Handler();
//        volume = new Volume();

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        preferences = getSharedPreferences("volume", MODE_PRIVATE);
        editor = preferences.edit();

        vh.ibtNextVideo.setOnClickListener(this);
        vh.ibtPlayVideo.setOnClickListener(this);
        vh.ibtBackVideo.setOnClickListener(this);
        vh.ibtExitFullVideo.setOnClickListener(this);
        vh.ibtVolumeOnVideo.setOnClickListener(this);
        vh.imgWebVideo.setOnClickListener(this);

        layoutControl = (LinearLayout) findViewById(R.id.layout_control);
    }

    private class ViewHoder {

        ImageButton ibtPlayVideo, ibtNextVideo, ibtBackVideo, ibtExitFullVideo, ibtVolumeOnVideo;

        ImageView imgWebVideo, imgView;
        VideoView video;
        TextView tvTimeVideo, tvTimeStartVideo, tvTimeEndVideo;
>>>>>>> master

        indexvideo = intent.getIntExtra("index",0);
        list=intent.getStringArrayListExtra("list");
        timePause =intent.getLongExtra("timePause",0);

    }

<<<<<<< HEAD
    @Override
    protected void onResume() {
        super.onResume();

        createPlayer(listvideo.get(indexvideo));
        exoPlayer.setOnTouchListener(this);
=======
    private void setVideoOrImager(String check) {

        position = checkLink.CheckLinkURL(check);
        if (position == 1) {

            vh.imgView.setVisibility(View.VISIBLE);
            vh.video.setVisibility(View.GONE);
            vh.ibtPlayVideo.setVisibility(View.GONE);
            vh.ibtVolumeOnVideo.setVisibility(View.GONE);
            vh.tvTimeStartVideo.setVisibility(View.GONE);
            vh.tvTimeVideo.setVisibility(View.GONE);

            vh.tvTimeEndVideo.setText("   ");
            Glide.with(this)
                    .load(listvideo.get(indexVideo))
                    .into(vh.imgView);

            handler.postDelayed(nextvideo, 5000);

        } else if (position == 2) {
            playing = true;
            vh.ibtPlayVideo.setImageResource(R.drawable.ic_pause);

            vh.imgView.setVisibility(View.GONE);
            vh.video.setVisibility(View.VISIBLE);
            vh.ibtPlayVideo.setVisibility(View.VISIBLE);
            vh.ibtVolumeOnVideo.setVisibility(View.VISIBLE);
            vh.tvTimeStartVideo.setVisibility(View.VISIBLE);
            vh.tvTimeVideo.setVisibility(View.VISIBLE);

            // đọ dài của video
            mp = MediaPlayer.create(this, Uri.parse(check));
            int duration = mp.getDuration();
            mp.release();

            vh.tvTimeEndVideo.setText(checkLink.stringForTime(duration));
            updateTime(vh.tvTimeStartVideo);

            try {
                vh.video.setVideoPath(listvideo.get(indexVideo));

                vh.video.start();
                vh.video.seekTo(timePause);
                timePause = 0;
            } catch (Exception e) {

                e.printStackTrace();
                if (indexVideo == listvideo.size() - 1) indexVideo = 0;
                else indexVideo++;
                setVideoOrImager(listvideo.get(indexVideo));
            }


            vh.video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (indexVideo == listvideo.size() - 1) indexVideo = 0;
                    else indexVideo++;

                    setVideoOrImager(listvideo.get(indexVideo));
                }
            });
        } else if (position == 3) {
            vh.imgView.setVisibility(View.GONE);
            vh.video.setVisibility(View.VISIBLE);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    vh.video.setVideoURI(Uri.parse(listvideo.get(indexVideo)));
                    vh.video.start();
>>>>>>> master

    }

<<<<<<< HEAD
    public void createPlayer(String link) {
=======
    @Override
    protected void onResume() {
        super.onResume();


        vh.ibtVolumeOnVideo.setImageResource(R.drawable.ic_volumeon);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        timePause = intent.getIntExtra("timePause", 0);
        indexVideo = intent.getIntExtra("index", 0);

        setVideoOrImager(listvideo.get(indexVideo));
    }

    private void updateTime(final TextView tv) {
        Thread t = new Thread() {
>>>>>>> master

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        exoPlayer.setPlayer(player);
        prepareVideo(link);
    }

<<<<<<< HEAD
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
=======
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgWeb_video:
                Uri uri = Uri.parse("http://www.bongdaso.com/news.aspx");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                timePause = vh.video.getCurrentPosition();

                intent.putExtra("index", indexVideo);
                intent.putExtra("timePause", timePause);
                startActivity(intent);
                break;

            case R.id.imgExitFull:
                intent = new Intent();
                // độ dài video đang chạy
                int timepause = vh.video.getCurrentPosition();

                intent.putExtra("index", indexVideo);
                intent.putExtra("timePause", timepause);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.imgVolumeOn_video:

                if (mute == true) {
                    vh.ibtVolumeOnVideo.setImageResource(R.drawable.ic_volumeon);
//                    volume.UnMuteAudio(this,intVolum);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

                    mute = false;
                } else {
                    vh.ibtVolumeOnVideo.setImageResource(R.drawable.ic_volumeoff);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

//                    volume.MuteAudio(this);
                    mute = true;
                }
                break;

            case R.id.imgPlay_video:
                if (playing == false) {
                    vh.ibtPlayVideo.setImageResource(R.drawable.ic_pause);
                    vh.video.start();
                    playing = true;
                } else {
                    vh.ibtPlayVideo.setImageResource(R.drawable.ic_playvideo);
                    vh.video.pause();
                    playing = false;
                }
                break;

            case R.id.imgNext_video:
                handler.removeCallbacks(nextvideo);
                if (indexVideo == listvideo.size() - 1) indexVideo = 0;
                else indexVideo++;
                setVideoOrImager(listvideo.get(indexVideo));
                break;
            case R.id.imgBack_video:
                handler.removeCallbacks(nextvideo);
                if (indexVideo == 0) {
                    indexVideo = listvideo.size() - 1;
                } else indexVideo--;

                setVideoOrImager(listvideo.get(indexVideo));
                break;
        }

    }

    public void addListMap() {
        listItem = new ArrayList<>();

        listItem.add(vh.imgWebVideo);
        listItem.add(vh.ibtExitFullVideo);
        listItem.add(vh.ibtBackVideo);
        listItem.add(vh.ibtPlayVideo);
        listItem.add(vh.ibtNextVideo);
        listItem.add(vh.ibtVolumeOnVideo);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                layoutControl.setVisibility(View.GONE);
                canclick = false;
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                layoutControl.setVisibility(View.VISIBLE);
                canclick = true;
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (didIndex < 6 && didIndex > 0) {
                    if (listItem.get(didIndex) instanceof ImageButton) {
                        ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorWhite));
                    }
//                    if (didIndex == 4 && position == 1) {
//                        didIndex--;
//                    }
                    didIndex--;
                    if (listItem.get(didIndex) instanceof ImageButton) {
                        ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorcatenew));
                    }
                    if (didIndex == 0 && listItem.get(didIndex) instanceof ImageView)
                        ((ImageView) listItem.get(didIndex)).setImageResource(R.drawable.ic_web);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (didIndex < 5) {
                    if (listItem.get(didIndex) instanceof ImageButton) {
                        ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorWhite));
                    }
                    if (didIndex == 0 && listItem.get(didIndex) instanceof ImageView)
                        ((ImageView) listItem.get(didIndex)).setImageResource(R.drawable.ic_website);
//                    if (didIndex == 2 && position == 1) {
//                        didIndex++;
//                    }
                    didIndex++;
                    if (listItem.get(didIndex) instanceof ImageButton) {
                        ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorcatenew));
                    }
                }
                break;

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if (canclick = true) listItem.get(didIndex).callOnClick();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Runnable nextvideo = new Runnable() {
        @Override
        public void run() {
            if (indexVideo == listvideo.size() - 1) indexVideo = 0;
            else indexVideo++;
            setVideoOrImager(listvideo.get(indexVideo));
        }
    };
>>>>>>> master
}
