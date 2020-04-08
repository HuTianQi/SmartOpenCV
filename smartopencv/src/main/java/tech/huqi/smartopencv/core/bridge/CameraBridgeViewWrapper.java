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

import static org.opencv.android.CameraBridgeViewBase.CAMERA_ID_BACK;
import static org.opencv.android.CameraBridgeViewBase.CAMERA_ID_FRONT;
import static tech.huqi.smartopencv.core.preview.CameraConfiguration.DEFAULT_BITMAP_CONFIG;

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
    protected boolean isUseFrontCamera = false;
    protected boolean isSetLandscape;
    protected boolean isUsbCamera;
    protected Bitmap.Config mBitmapConfig = DEFAULT_BITMAP_CONFIG;
    private Mat mConvertRgbaMat;
    private Mat mConvertGrayMat;
    private int mCameraId;

    public CameraBridgeViewWrapper(CameraBridgeViewBase base, SurfaceHolder holder) {
        mBase = base;
        mSurfaceHolder = holder;
    }


    @Override
    public void setCameraIndex(int cameraIndex) {
        if (cameraIndex == CAMERA_ID_FRONT) {
            isUseFrontCamera = true;
            mCameraId = CameraHelper.getFrontCameraId();
        } else if (cameraIndex == CAMERA_ID_BACK) {
            mCameraId = CameraHelper.getBackCameraId();
        } else {
            mCameraId = cameraIndex;
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
        // USB摄像头默认安装取景方向是设备竖屏时方向，而手机摄像头默认安装取景方向是设备横屏时的方向
        if (Util.isLandscape(mBase) || isUsbCamera) {
            mCacheBitmap = Bitmap.createBitmap(frameWidth, frameHeight, mBitmapConfig);
            if (!isSetLandscape && !isUsbCamera) {
                Util.printErrorLog("[Warning]:You've called \".landscape(false)\", but the actual direction of the screen is horizontal," +
                        "please set the 'android:screenOrientation = \"portrait\"' in your Activity of AndroidManifest.xml");
                isSetLandscape = true;
            }
        } else {
            mCacheBitmap = Bitmap.createBitmap(frameHeight, frameWidth, mBitmapConfig);
            if (isSetLandscape) {
                Util.printErrorLog("[Warning]:You set the horizontal direction, but the actual direction of the screen is not horizontal," +
                        "please set the 'android:screenOrientation = \"landscape\" \"in your Activity of AndroidManifest.xml");
                isSetLandscape = false;
            }
        }
        Util.printDebugLog("bitmap config:" + mBitmapConfig.toString());
    }

    private CvCameraViewFrame adjustImageOrientation(CvCameraViewFrame frame) {
        Context context = mBase.getContext();
        CameraHelper.adjustImageOrientation(context, frame.rgba(), mConvertRgbaMat, isUseFrontCamera, isSetLandscape, mCameraId);
        CameraHelper.adjustImageOrientation(context, frame.gray(), mConvertGrayMat, isUseFrontCamera, isSetLandscape, mCameraId);
        return JavaCameraFrame.getInstance().setRgba(mConvertRgbaMat).setGray(mConvertGrayMat);
    }

    public void updateCameraBridgeViewBaseMembers(CameraBridgeViewBaseMembers members) {
        mScale = members.scale;
        mFrameWidth = members.frameWidth;
        mFrameHeight = members.frameHeight;
        mFpsMeter = members.fpsMeter;
    }

    public static class CameraBridgeViewBaseMembers {
        float scale;
        int frameWidth;
        int frameHeight;
        FpsMeter fpsMeter;

        public CameraBridgeViewBaseMembers(int frameWidth, int frameHeight, float scale,
                                           FpsMeter fpsMeter) {
            this.scale = scale;
            this.frameWidth = frameWidth;
            this.frameHeight = frameHeight;
            this.fpsMeter = fpsMeter;
        }
    }
}
