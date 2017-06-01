package com.xiaoantech.electrombile.widget.Waveform;

import android.graphics.Canvas;

/**
 * Created by yangxu on 2017/1/15.
 */

public interface WaveformRender {
    void render(Canvas canvas, byte[] waveform);
}
