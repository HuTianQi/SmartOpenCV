package tech.huqi.smartopencv.core.preview;

import android.graphics.Bitmap;

/**
 * Created by hzhuqi on 2019/9/17
 */
public abstract class CameraPreviewAdapter implements ICameraPreview {
    @Override
    public void setUseFrontCamera(boolean isUseFrontCamera) {
        // empty
    }

    public void setIsUsbCamera() {
        // empty
    }

    @Override
    public void setLandscape(boolean isAllowedScreenSwitch) {
        // empty
    }

    @Override
    public void setCameraIndex(int cameraIndex) {
        // empty
    }

    @Override
    public void setBitmapConfig(Bitmap.Config bitmapConfig) {
        // empty
    }
}
