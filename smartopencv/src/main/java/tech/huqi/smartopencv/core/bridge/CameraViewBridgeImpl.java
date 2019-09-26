package tech.huqi.smartopencv.core.bridge;

import org.opencv.android.CameraBridgeViewBase;

import static org.opencv.android.CameraBridgeViewBase.CAMERA_ID_BACK;
import static org.opencv.android.CameraBridgeViewBase.CAMERA_ID_FRONT;

/**
 * Created by hzhuqi on 2019/9/6
 */
public class CameraViewBridgeImpl extends CameraBridgeViewWrapper {
    public CameraViewBridgeImpl(CameraBridgeViewBase base, CameraBridgeViewBaseUnit unit) {
        super(base, unit);
    }

    public void setUseFrontCamera(boolean isUseFrontCamera) {
        this.isUseFrontCamera = isUseFrontCamera;
        if (isUseFrontCamera) {
            mBase.setCameraIndex(CAMERA_ID_FRONT);
        } else {
            mBase.setCameraIndex(CAMERA_ID_BACK);
        }
    }

    public void setLandscape(boolean isLandscape) {
        this.isSetLandscape = isLandscape;
    }

}
