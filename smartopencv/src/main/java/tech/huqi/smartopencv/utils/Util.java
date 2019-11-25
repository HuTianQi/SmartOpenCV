package tech.huqi.smartopencv.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import tech.huqi.smartopencv.SmartOpenCV;

/**
 * Created by hzhuqi on 2019/9/3
 */
public class Util {
    private static final String TAG = SmartOpenCV.TAG;
    private static boolean DEBUG;

    public static void printDebugLog(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void printErrorLog(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void isEnableDebugMode(boolean debug) {
        DEBUG = debug;
    }

    /**
     * 获取屏幕宽度，单位像素
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕高度，单位像素
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

    }

    public static boolean isPortrait(View view) {
        return view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isLandscape(View view) {
        return view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static int getScreenRotateDegree(Context context) {
        int degree = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        if (degree == Surface.ROTATION_0) return 0;
        if (degree == Surface.ROTATION_90) return 90;
        if (degree == Surface.ROTATION_180) return 180;
        if (degree == Surface.ROTATION_270) return 270;
        return 0;
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static boolean writeBytes2File(byte[] bytes, File file) throws IOException {
        if (bytes == null || bytes.length == 0 || file == null) return false;
        FileChannel fileChannel = new FileOutputStream(file).getChannel();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        fileChannel.write(buffer);
        fileChannel.close();
        return true;
    }
}
