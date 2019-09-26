package tech.huqi.smartopencv.core.bridge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

import org.opencv.BuildConfig;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.FpsMeter;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import tech.huqi.smartopencv.core.cv.ICameraView;
import tech.huqi.smartopencv.core.cv.JavaCameraFrame;
import tech.huqi.smartopencv.utils.CameraHelper;
import tech.huqi.smartopencv.utils.Util;

import static org.opencv.android.CameraBridgeViewBase.CAMERA_ID_FRONT;

/**
 * Created by hzhuqi on 2019/9/3
 */

/**
 * CameraBridgeViewBase的装饰类，提供了改写OpenCV{@link CameraBridgeViewBase}类的关键函数的实现
 */
public abstract class CameraBridgeViewWrapper implements ICameraViewBridge, ICameraView {
    private static final String TAG = "CameraBridgeViewWrapper";

    private float mScale;
    private int mFrameWidth;
    private int mFrameHeight;
    private FpsMeter mFpsMeter;
    private SurfaceHolder mSurfaceHolder;
    /**
     * 预览帧图像，宽与高等于{@link CameraBridgeViewBase#mFrameWidth}或{@link CameraBridgeViewBase#mFrameHeight}
     * 最大值为相机支持的最大预览宽高
     */
    private Bitmap mCacheBitmap;
    private CvCameraViewListener2 mListener;
    protected CameraBridgeViewBase mBase;
    protected boolean isUseFrontCamera;
    protected boolean isSetLandscape;
    private Mat mConvertRgbaMat;
    private Mat mConvertGrayMat;

    public CameraBridgeViewWrapper(CameraBridgeViewBase base, CameraBridgeViewBaseUnit unit) {
        mBase = base;
        mScale = unit.scale;
        mFrameWidth = unit.frameWidth;
        mFrameHeight = unit.frameHeight;
        mFpsMeter = unit.fpsMeter;
        mSurfaceHolder = unit.surfaceHolder;
    }


    @Override
    public void setCameraIndex(int cameraIndex) {
        if (cameraIndex == CAMERA_ID_FRONT) {
            isUseFrontCamera = true;
        } else {
            isUseFrontCamera = false;
        }
    }

    @Override
    public void setCvCameraViewListener(CvCameraViewListener2 listener) {
        mListener = listener;
    }

    @Override
    public void disableView() {
        if (mCacheBitmap != null) {
            mCacheBitmap.recycle();
        }
    }

    /**
     * Mock and enhance OpenCV{@link CameraBridgeViewBase#deliverAndDrawFrame(CvCameraViewFrame)}实现
     *
     * @param frame
     */
    @Override
    public void deliverAndDrawFrame(CvCameraViewFrame frame) {
        Mat modified;
        if (mListener != null) {
            frame = adjustImageOrientation(frame);
            modified = mListener.onCameraFrame(frame);
        } else {
            modified = frame.rgba();
        }

        boolean bmpValid = true;
        if (modified != null) {
            try {
                Utils.matToBitmap(modified, mCacheBitmap);
            } catch (Exception e) {
                Util.printErrorLog("===================[matToBitmap]===================");
                Util.printErrorLog("Mat type: " + modified);
                Util.printErrorLog("frameWidth:" + mFrameWidth + " frameHeight:" + mFrameHeight +
                        " surfaceWidth:" + mBase.getWidth() + " surfaceHeight:" + mBase.getHeight());
                Util.printErrorLog("Bitmap type: " + mCacheBitmap.getWidth() + "*" + mCacheBitmap.getHeight());
                Util.printErrorLog("Utils.matToBitmap() throws an exception: " + e.getMessage());
                Util.printErrorLog("===================[matToBitmap]===================");
                bmpValid = false;
            }
        }

        if (bmpValid && mCacheBitmap != null) {
            Canvas canvas = mSurfaceHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "mStretch value: " + mScale);

                if (mBase instanceof ICameraView) {
                    ((ICameraView) mBase).drawBitmap(canvas, mCacheBitmap);
                } else {
                    drawBitmap(canvas, mCacheBitmap);
                }

                if (mFpsMeter != null) {
                    mFpsMeter.measure();
                    mFpsMeter.draw(canvas, 20, 30);
                }
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * OpenCV 默认的绘制策略
     *
     * @param canvas
     * @param frameBitmap
     */
    @Override
    public void drawBitmap(Canvas canvas, Bitmap frameBitmap) {
        if (mScale != 0) {
            canvas.drawBitmap(frameBitmap, new Rect(0, 0, frameBitmap.getWidth(), frameBitmap.getHeight()),
                    new Rect((int) ((canvas.getWidth() - mScale * frameBitmap.getWidth()) / 2),
                            (int) ((canvas.getHeight() - mScale * frameBitmap.getHeight()) / 2),
                            (int) ((canvas.getWidth() - mScale * frameBitmap.getWidth()) / 2 + mScale * frameBitmap.getWidth()),
                            (int) ((canvas.getHeight() - mScale * frameBitmap.getHeight()) / 2 + mScale * frameBitmap.getHeight())), null);
        } else {
            canvas.drawBitmap(frameBitmap, new Rect(0, 0, frameBitmap.getWidth(), frameBitmap.getHeight()),
                    new Rect((canvas.getWidth() - frameBitmap.getWidth()) / 2,
                            (canvas.getHeight() - frameBitmap.getHeight()) / 2,
                            (canvas.getWidth() - frameBitmap.getWidth()) / 2 + frameBitmap.getWidth(),
                            (canvas.getHeight() - frameBitmap.getHeight()) / 2 + frameBitmap.getHeight()), null);
        }
    }

    /**
     * Mock and enhance OpenCV{@link CameraBridgeViewBase#AllocateCache()}
     *
     * @param frameWidth  预览宽，在允许横竖屏切换且横屏情况下等于{@link CameraBridgeViewBase#mFrameWidth}
     *                    其余情况等于{@link CameraBridgeViewBase#mFrameHeight}
     * @param frameHeight 预览高，在允许横竖屏切换且横屏情况下等于{@link CameraBridgeViewBase#mFrameHeight}
     *                    其余情况等于{@link CameraBridgeViewBase#mFrameWidth}
     */
    public void AllocateCache(int frameWidth, int frameHeight) {
        mConvertRgbaMat = new Mat();
        mConvertGrayMat = new Mat();
        if (isSetLandscape && Util.isLandscape(mBase)) {
            mCacheBitmap = Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888);
            return;
        }
        if (isSetLandscape && !Util.isLandscape(mBase)) {
            Util.printErrorLog("You set the horizontal direction, but the actual direction of the screen is not horizontal," +
                    "please set the 'android:screenOrientation = \"landscape\" \"in your AndroidManifest.xml");
        }
        mCacheBitmap = Bitmap.createBitmap(frameHeight, frameWidth, Bitmap.Config.ARGB_8888);
    }

    private CvCameraViewFrame adjustImageOrientation(CvCameraViewFrame frame) {
        Context context = mBase.getContext();
        CameraHelper.adjustImageOrientation(context, frame.rgba(), mConvertRgbaMat, isUseFrontCamera, isSetLandscape);
        CameraHelper.adjustImageOrientation(context, frame.gray(), mConvertGrayMat, isUseFrontCamera, isSetLandscape);
        return JavaCameraFrame.getInstance().setRgba(mConvertRgbaMat).setGray(mConvertGrayMat);
    }

    public static class CameraBridgeViewBaseUnit {
        float scale;
        int frameWidth;
        int frameHeight;
        FpsMeter fpsMeter;
        SurfaceHolder surfaceHolder;

        public CameraBridgeViewBaseUnit(int frameWidth, int frameHeight, float scale,
                                        FpsMeter fpsMeter, SurfaceHolder surfaceHolder) {
            this.scale = scale;
            this.frameWidth = frameWidth;
            this.frameHeight = frameHeight;
            this.fpsMeter = fpsMeter;
            this.surfaceHolder = surfaceHolder;
        }
    }
}
