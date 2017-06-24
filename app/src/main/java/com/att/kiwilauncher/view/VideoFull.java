package com.att.kiwilauncher.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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

import com.att.kiwilauncher.R;
import com.att.kiwilauncher.util.CheckLink;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class VideoFull extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
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

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full);

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

        public ViewHoder() {
            ibtBackVideo = (ImageButton) findViewById(R.id.imgBack_video);
            ibtPlayVideo = (ImageButton) findViewById(R.id.imgPlay_video);
            ibtNextVideo = (ImageButton) findViewById(R.id.imgNext_video);
            ibtExitFullVideo = (ImageButton) findViewById(R.id.imgExitFull);
            ibtVolumeOnVideo = (ImageButton) findViewById(R.id.imgVolumeOn_video);

            video = (VideoView) findViewById(R.id.video_Full);

            imgWebVideo = (ImageView) findViewById(R.id.imgWeb_video);
            imgView = (ImageView) findViewById(R.id.imgView_Full);

            tvTimeVideo = (TextView) findViewById(R.id.tvTime_video);
            tvTimeStartVideo = (TextView) findViewById(R.id.tvTimeBegin_video);
            tvTimeEndVideo = (TextView) findViewById(R.id.tvTimeEnd_video);

        }
    }

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

                }
            });
        }
    }

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
}
