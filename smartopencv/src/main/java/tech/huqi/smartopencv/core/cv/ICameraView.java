package tech.huqi.smartopencv.core.cv;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by hzhuqi on 2019/9/9
 */
public interface ICameraView {
    void drawBitmap(Canvas canvas, Bitmap frameBitmap);
}
