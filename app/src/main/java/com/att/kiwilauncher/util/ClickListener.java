package com.att.kiwilauncher.util;

import android.view.View;

/**
 * Created by mac on 6/27/17.
 */

public interface ClickListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
