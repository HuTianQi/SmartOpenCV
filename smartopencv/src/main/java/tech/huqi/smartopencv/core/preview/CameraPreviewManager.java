package tech.huqi.smartopencv.core.preview;

import android.content.Context;

import org.opencv.android.CameraBridgeViewBase;

import tech.huqi.smartopencv.draw.DefaultDrawStrategy;
import tech.huqi.smartopencv.draw.DefaultFrameSizeCalculator;
import tech.huqi.smartopencv.draw.IDrawStrategy;
import tech.huqi.smartopencv.draw.IPreviewSizeCalculator;

import static tech.huqi.smartopencv.core.preview.CameraConfiguration.DEFAULT_BITMAP_CONFIG;

/**
 * Created by hzhuqi on 2019/9/9
 */
public class CameraPreviewManager extends CameraPreviewAdapter {
    boolean isUseOpenCvDefaultDrawStrategy;
    boolean isUseOpenCvDefaultFrameSizeCalculator;

    CameraBridgeViewBase mBase;
    IDrawStrategy drawStrategy = new DefaultDrawStrategy();
    IPreviewSizeCalculator cameraFrameSizeCalculator;


    public CameraPreviewManager(Context context, CameraBridgeViewBase base) {
        mBase = base;
        cameraFrameSizeCalculator = new DefaultFrameSizeCalculator(context);
    }

    public void init(CameraConfiguration cameraConfiguration) {
        if (cameraConfiguration.cameraIndex != CameraConfiguration.NOT_SET_VALUE) {
            ((ICameraPreview) mBase).setCameraIndex(cameraConfiguration.cameraIndex);
        }
        if (cameraConfiguration.landscape) {
            ((ICameraPreview) mBase).setLandscape(true);
        }
        if (cameraConfiguration.frontCamera) {
            ((ICameraPreview) mBase).setUseFrontCamera(true);
        }
        if (cameraConfiguration.usbCamera) {
            ((ICameraPreview) mBase).setIsUsbCamera();
        }
        if (cameraConfiguration.bitmapConfig != DEFAULT_BITMAP_CONFIG) {
            ((ICameraPreview) mBase).setBitmapConfig(cameraConfiguration.bitmapConfig);
        }
        if (cameraConfiguration.drawStrategy != null) {
            ((ICameraPreview) mBase).setDrawStrategy(cameraConfiguration.drawStrategy);
        }
        if (cameraConfiguration.previewSizeCalculator != null) {
            ((ICameraPreview) mBase).setCameraFrameSizeCalculator(cameraConfiguration.previewSizeCalculator);
        }
        if (cameraConfiguration.cvCameraViewListener != null) {
            mBase.setCvCameraViewListener(cameraConfiguration.cvCameraViewListener);
        }
        if (cameraConfiguration.cvCameraViewListener2 != null) {
            mBase.setCvCameraViewListener(cameraConfiguration.cvCameraViewListener2);
        }
        if (cameraConfiguration.keepScreenOn) {
            mBase.setKeepScreenOn(true);
        }
        if (cameraConfiguration.enableFpsMeter) {
            mBase.enableFpsMeter();
        }
        if (cameraConfiguration.maxWidth != CameraConfiguration.NOT_SET_VALUE &&
                cameraConfiguration.maxHeight != CameraConfiguration.NOT_SET_VALUE) {
            mBase.setMaxFrameSize(cameraConfiguration.maxWidth, cameraConfiguration.maxHeight);
        }
        isUseOpenCvDefaultDrawStrategy = cameraConfiguration.openCvDefaultDrawStrategy;
        isUseOpenCvDefaultFrameSizeCalculator = cameraConfiguration.openCvDefaultPreviewCalculator;
    }


    @Override
    public void setDrawStrategy(IDrawStrategy mDrawStrategy) {
        this.drawStrategy = mDrawStrategy;
        isUseOpenCvDefaultDrawStrategy = false;
    }

    @Override
    public void setCameraFrameSizeCalculator(IPreviewSizeCalculator mCameraFrameSizeCalculator) {
        this.cameraFrameSizeCalculator = mCameraFrameSizeCalculator;
    }
}
