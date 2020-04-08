package tech.huqi.smartopencv.core.preview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

import tech.huqi.smartopencv.core.cv.JavaCamera2ViewWrapper;
import tech.huqi.smartopencv.draw.IDrawStrategy;
import tech.huqi.smartopencv.draw.IPreviewSizeCalculator;

/**
 * Created by hzhuqi on 2019/9/4
 */
public class Camera2Preview extends JavaCamera2ViewWrapper implements ICameraPreview {
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    private CameraPreviewManager mPreviewManager;

    public Camera2Preview(Context context, int cameraId) {
        super(context, cameraId);
        mPreviewManager = new CameraPreviewManager(context, this);
    }

    public Camera2Preview(Context context, AttributeSet attrs) {
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
    public void setIsUsbCamera() {
        super.setIsUsbCamera();
    }

    @Override
    public void setBitmapConfig(Bitmap.Config bitmapConfig) {
        super.setBitmapConfig(bitmapConfig);
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
        if (mPreviewManager.isUseOpenCvDefaultFrameSizeCalculator) {
            return super.calculateCameraFrameSize(supportedSizes, accessor, surfaceWidth, surfaceHeight);
        }
        final List<Size> sizes = new ArrayList<>();
        for (Object size : supportedSizes) {
            Size size2 = new Size(accessor.getWidth(size), accessor.getHeight(size));
            sizes.add(size2);
        }
        return mPreviewManager.cameraFrameSizeCalculator.calculateCameraFrameSize(sizes, surfaceWidth, surfaceHeight);
    }
}
