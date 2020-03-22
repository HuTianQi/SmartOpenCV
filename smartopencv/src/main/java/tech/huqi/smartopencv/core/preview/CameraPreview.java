package tech.huqi.smartopencv.core.preview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

import tech.huqi.smartopencv.core.cv.JavaCameraViewWrapper;
import tech.huqi.smartopencv.draw.IDrawStrategy;
import tech.huqi.smartopencv.draw.IPreviewSizeCalculator;
import tech.huqi.smartopencv.utils.Util;

/**
 * Created by hzhuqi on 2019/9/3
 */
public class CameraPreview extends JavaCameraViewWrapper implements ICameraPreview {
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private CameraPreviewManager mPreviewManager;

    public CameraPreview(Context context, int cameraId) {
        super(context, cameraId);
        mPreviewManager = new CameraPreviewManager(context, this);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPreviewManager = new CameraPreviewManager(context, this);
    }

    public void init(CameraConfiguration cameraConfiguration) {
        mPreviewManager.init(cameraConfiguration);
    }

    @Override
    public void setUseFrontCamera(boolean isUseFrontCamera) {
        super.setUseFrontCamera(isUseFrontCamera);
    }

    @Override
    public void setIsUsbCamera() {
        super.setIsUsbCamera();
    }

    @Override
    public void setLandscape(boolean isSetLandscape) {
        super.setLandscape(isSetLandscape);
    }

    public void setDrawStrategy(IDrawStrategy drawStrategy) {
        mPreviewManager.setDrawStrategy(drawStrategy);
    }

    public void setCameraFrameSizeCalculator(IPreviewSizeCalculator cameraFrameSizeCalculator) {
        mPreviewManager.setCameraFrameSizeCalculator(cameraFrameSizeCalculator);
    }

    @Override
    public void drawBitmap(Canvas canvas, Bitmap frameBitmap) {
        if (mPreviewManager.isUseOpenCvDefaultDrawStrategy) {
            super.drawBitmap(canvas, frameBitmap);
        } else {
            mPreviewManager.drawStrategy.drawBitmap(canvas, frameBitmap, mSurfaceWidth, mSurfaceHeight);
        }
    }

    @Override
    protected Size calculateCameraFrameSize(List<?> supportedSizes, ListItemAccessor accessor, int surfaceWidth, int surfaceHeight) {
        mSurfaceWidth = surfaceWidth;
        mSurfaceHeight = surfaceHeight;
        Size size;
        if (mPreviewManager.isUseOpenCvDefaultFrameSizeCalculator) {
            size = super.calculateCameraFrameSize(supportedSizes, accessor, surfaceWidth, surfaceHeight);
        } else {
            final List<Size> sizes = new ArrayList<>();
            for (Object supportedSize : supportedSizes) {
                Size size2 = new Size(accessor.getWidth(supportedSize), accessor.getHeight(supportedSize));
                sizes.add(size2);
            }
            size = mPreviewManager.cameraFrameSizeCalculator.calculateCameraFrameSize(sizes, surfaceWidth, surfaceHeight);
        }
        Util.printDebugLog("====================[calculateCameraFrameSize]====================");
        Util.printDebugLog("calculate size.width:" + size.width + " calculate size.height:" + size.height);
        Util.printDebugLog("====================[calculateCameraFrameSize]====================");
        return size;
    }

}
