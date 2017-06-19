package com.att.kiwilauncher.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.att.kiwilauncher.R;
import com.att.kiwilauncher.util.CheckLink;
import com.att.kiwilauncher.util.Volume;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class VideoFull extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    Intent intent;
    int timePause,didIndex = 0;
    int indexVideo=0 , position;
    MediaPlayer mp;
    Handler handler;
    ArrayList<String> listvideo;
    List<View> listItem;
    CheckLink checkLink;
    Volume volume;
    ViewHoder vh;
    LinearLayout layoutControl;
    boolean playing = true,mute = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_full);

        initVideo();
        addListMap();
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
        vh.ibtVolumeOnVideo.setOnClickListener(this);
        vh.imgWebVideo.setOnClickListener(this);

        indexVideo = intent.getIntExtra("index", 0);
        listvideo = intent.getStringArrayListExtra("list");
        timePause = intent.getIntExtra("timePause", 0);

        setVideoOrImager(listvideo.get(indexVideo));

        layoutControl = (LinearLayout) findViewById(R.id.layout_control);
    }

    private class ViewHoder{

        ImageButton ibtPlayVideo,ibtNextVideo,ibtBackVideo,ibtExitFullVideo,ibtVolumeOnVideo;

        ImageView imgWebVideo,imgView;
        VideoView video;
        TextView tvTimeVideo,tvTimeStartVideo,tvTimeEndVideo;

        public ViewHoder() {
            ibtBackVideo= (ImageButton) findViewById(R.id.imgBack_video);
            ibtPlayVideo= (ImageButton) findViewById(R.id.imgPlay_video);
            ibtNextVideo= (ImageButton) findViewById(R.id.imgNext_video);
            ibtExitFullVideo= (ImageButton) findViewById(R.id.imgExitFull);
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

        position = checkLink.CheckLinkURL(check);
        if (position == 1) {
            if (didIndex == 5) {
                listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                didIndex--;
                listItem.get(didIndex).setBackgroundResource(R.drawable.border_videopick);
            }
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

            case R.id.imgVolumeOn_video:
                if (mute == true) {
                    vh.ibtVolumeOnVideo.setImageResource(R.drawable.ic_volumeon);
                    volume.UnMuteAudio(this);
                    mute = false;
                } else {
                    vh.ibtVolumeOnVideo.setImageResource(R.drawable.ic_volumeoff);
                    volume.MuteAudio(this);
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
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                layoutControl.setVisibility(View.VISIBLE);
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (didIndex < 6 && didIndex > 0) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    if (didIndex == 4 && position == 1) {
                        didIndex--;
                    }
                    didIndex--;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_videopick);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (didIndex < 5) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    if (didIndex == 2 && position == 1) {
                        didIndex++;
                    }
                    didIndex++;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_videopick);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                listItem.get(didIndex).callOnClick();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
