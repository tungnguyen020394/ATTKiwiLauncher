package com.att.kiwilauncher.util;

/**
 * Created by tienh on 5/31/2017.
 */

public interface MediaPlayerControl {

    void    start();
    void    pause();
    int     getDuration();
    int     getCurrentPosition();
    void    seekTo(int pos);
    boolean isPlaying();
    int     getBufferPercentage();
    boolean canPause();
    boolean canSeekBackward();
    boolean canSeekForward();
    boolean isFullScreen();
    void    toggleFullScreen();


}
