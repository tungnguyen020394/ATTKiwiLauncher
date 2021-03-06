package com.att.kiwilauncher.util;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

/**
 * Created by tienh on 6/14/2017.
 */

public class Volume {
    public void MuteAudio(Context context){
        AudioManager mAlramMAnager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
//            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
//            mAlramMAnager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

//            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
//            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
        } else {
//            mAlramMAnager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
//            mAlramMAnager.setStreamMute(AudioManager.STREAM_ALARM, true);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, true);
//            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, true);
//            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
        }
    }

    public void UnMuteAudio(Context context,int i){
        AudioManager mAlramMAnager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
//        int i =mAlramMAnager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, i);
//            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, i);
//            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE,i);
            mAlramMAnager.setStreamVolume(AudioManager.STREAM_MUSIC, i, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

//            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, i);
//            mAlramMAnager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, i);
        } else {
//            mAlramMAnager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
//            mAlramMAnager.setStreamMute(AudioManager.STREAM_ALARM, false);
            mAlramMAnager.setStreamMute(AudioManager.STREAM_MUSIC, false);
//            mAlramMAnager.setStreamMute(AudioManager.STREAM_RING, false);
//            mAlramMAnager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
    }
}
