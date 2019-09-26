package tech.huqi.smartopencv.core.cv;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

/**
 * Created by hzhuqi on 2019/9/4
 */
public final class JavaCameraFrame implements CameraBridgeViewBase.CvCameraViewFrame {
    private static JavaCameraFrame sInstance;
    private Mat mRgba;
    private Mat mGray;

    private JavaCameraFrame() {
    }

    public static JavaCameraFrame getInstance() {
        if (sInstance == null) {
            synchronized (JavaCameraFrame.class) {
                if (sInstance == null) {
                    sInstance = new JavaCameraFrame();
                }
            }
        }
        return sInstance;
    }

    public JavaCameraFrame setRgba(Mat mRgba) {
        this.mRgba = mRgba;
        return this;
    }

    public JavaCameraFrame setGray(Mat mGray) {
        this.mGray = mGray;
        return this;
    }


    @Override
    public Mat rgba() {
        return mRgba;
    }

    @Override
    public Mat gray() {
        return mGray;
    }

    public void release() {
        mRgba.release();
    }
}
