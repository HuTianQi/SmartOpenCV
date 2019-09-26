package tech.huqi.smartopencv.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import tech.huqi.smartopencv.utils.CameraHelper;
import tech.huqi.smartopencv.utils.Util;

/**
 * Created by hzhuqi on 2019/9/3
 */
public class DefaultDrawStrategy implements IDrawStrategy {
    private int logCount = 0;
    private static final int MAX_LOG_COUNT = 3;

    @Override
    public void drawBitmap(Canvas canvas, Bitmap frameBitmap, int surfaceWidth, int surfaceHeight, boolean isSetLandscape, boolean isPortrait) {
        if (frameBitmap.getWidth() < surfaceWidth || frameBitmap.getHeight() < surfaceHeight) {
            Bitmap newBitmap = CameraHelper.scaleImage(frameBitmap, surfaceWidth, surfaceHeight);
            canvas.drawBitmap(newBitmap, new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight()),
                    new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight()), new Paint());
            newBitmap.recycle();
        } else {
            canvas.drawBitmap(frameBitmap, new Rect(0, 0, frameBitmap.getWidth(), frameBitmap.getHeight()),
                    new Rect(0, 0, frameBitmap.getWidth(), frameBitmap.getHeight()), new Paint());
        }
        if (logCount < MAX_LOG_COUNT) {
            Util.printDebugLog(" BitmapWidth:" + frameBitmap.getWidth() + " BitmapHeight:" + frameBitmap.getHeight() +
                    " surfaceWidth:" + surfaceWidth + " surfaceHeight:" + surfaceHeight);
            logCount++;
        }
    }
}
