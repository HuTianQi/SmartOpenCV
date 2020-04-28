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
    public void drawBitmap(Canvas canvas, Bitmap frameBitmap, int surfaceWidth, int surfaceHeight) {
        int bitmapWidth = frameBitmap.getWidth();
        int bitmapHeight = frameBitmap.getHeight();
        int left = 0;
        int top = 0;
        if (bitmapWidth > surfaceWidth) {
            left = (frameBitmap.getWidth() - surfaceWidth) / 2;
        }
        if (bitmapHeight > surfaceHeight) {
            top = (frameBitmap.getHeight() - surfaceHeight) / 2;
        }
        /**
         * 如果相机预览大小（宽或高或宽高）大于控件大小，则从预览上选取子区域展示到控件上，
         * 选取规则是让预览尽可能的居中显示到控件上
         */
        if (bitmapWidth <= surfaceWidth && bitmapHeight > surfaceHeight) {
            // 宽度拉伸
            canvas.drawBitmap(frameBitmap, new Rect(0, top, frameBitmap.getWidth(), frameBitmap.getHeight() - top),
                    new Rect(0, 0, surfaceWidth, surfaceHeight), new Paint());
        } else if (bitmapHeight <= surfaceHeight && bitmapWidth > surfaceWidth) {
            // 高度拉伸
            canvas.drawBitmap(frameBitmap, new Rect(left, 0, frameBitmap.getWidth() - left, frameBitmap.getHeight()),
                    new Rect(0, 0, surfaceWidth, surfaceHeight), new Paint());
        } else if (bitmapWidth <= surfaceWidth && bitmapHeight <= surfaceHeight) {
            // 宽高比缩放
            Bitmap newBitmap = CameraHelper.scaleImage(frameBitmap, surfaceWidth, surfaceHeight);
            canvas.drawBitmap(newBitmap, new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight()),
                    new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight()), new Paint());
            newBitmap.recycle();
        } else {
            canvas.drawBitmap(frameBitmap, new Rect(left, top, frameBitmap.getWidth() - left, frameBitmap.getHeight() - top),
                    new Rect(0, 0, surfaceWidth, surfaceHeight), new Paint());
        }
        if (logCount < MAX_LOG_COUNT) {
            Util.printDebugLog(" BitmapWidth:" + frameBitmap.getWidth() + " BitmapHeight:" + frameBitmap.getHeight() +
                    " surfaceWidth:" + surfaceWidth + " surfaceHeight:" + surfaceHeight);
            logCount++;
        }
    }
}
