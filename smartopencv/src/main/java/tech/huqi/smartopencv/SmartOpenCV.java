package tech.huqi.smartopencv;

import org.opencv.android.CameraBridgeViewBase;

import tech.huqi.smartopencv.core.preview.Camera2Preview;
import tech.huqi.smartopencv.core.preview.CameraConfiguration;
import tech.huqi.smartopencv.core.preview.CameraPreview;

/**
 * Created by hzhuqi on 2019/9/5
 */
public class SmartOpenCV {
    public static final String TAG = "SmartOpenCV";
    public static final String SDK_VERSION = "1.0.0";

    private static SmartOpenCV sInstance;
    private CameraBridgeViewBase mPreview;
    private CameraConfiguration mConfiguration;

    /**
     * Returns singleton class instance
     */
    public static SmartOpenCV getInstance() {
        if (sInstance == null) {
            synchronized (SmartOpenCV.class) {
                if (sInstance == null) {
                    sInstance = new SmartOpenCV();
                }
            }
        }
        return sInstance;
    }

    private SmartOpenCV() {
    }

    public void init(CameraBridgeViewBase preview, CameraConfiguration cameraConfiguration) {
        if (preview instanceof Camera2Preview) {
            ((Camera2Preview) preview).init(cameraConfiguration);
        } else if (preview instanceof CameraPreview) {
            ((CameraPreview) preview).init(cameraConfiguration);
        } else {
            throw new IllegalArgumentException("[preview] must be an instance of CameraPreview or Camera2Preview");
        }
        mPreview = preview;
        mConfiguration = cameraConfiguration;
    }

    /**
     * 屏幕方向发生切换，通知OpenCV重新创建预览帧Bitmap
     * 仅在允许屏幕方向切换开关打开时有效，{@link CameraConfiguration.Builder#allowedScreenOrientationSwitch(boolean)}
     */
    @Deprecated
    public void notifyScreenOrientationSwitch() {
        if (mConfiguration.isAllowedScreenOrientationSwitch()) {
            mPreview.disableView();
            mPreview.enableView();
        }
    }

    public boolean isUsbCamera() {
        if (mConfiguration != null) {
            return mConfiguration.isUsbCamera();
        }
        return false;
    }
}
