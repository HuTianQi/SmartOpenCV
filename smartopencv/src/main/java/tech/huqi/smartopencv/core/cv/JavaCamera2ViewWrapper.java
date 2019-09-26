package tech.huqi.smartopencv.core.cv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

import org.opencv.android.JavaCamera2View;

import tech.huqi.smartopencv.core.bridge.CameraBridgeViewWrapper.CameraBridgeViewBaseUnit;
import tech.huqi.smartopencv.core.bridge.CameraViewBridgeImpl;
import tech.huqi.smartopencv.core.preview.Camera2Preview;
import tech.huqi.smartopencv.core.preview.CameraConfiguration;
import tech.huqi.smartopencv.draw.IDrawStrategy;

/**
 * Created by hzhuqi on 2019/9/3
 */
public class JavaCamera2ViewWrapper extends JavaCamera2View implements ICameraView {
    private static final String TAG = "JavaCamera2ViewWrapper";
    private CameraViewBridgeImpl mCameraViewBridgeImpl;

    public JavaCamera2ViewWrapper(Context context, int cameraId) {
        super(context, cameraId);
        init();
    }

    public JavaCamera2ViewWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        CameraBridgeViewBaseUnit unit = new CameraBridgeViewBaseUnit(mFrameWidth, mFrameHeight, mScale, mFpsMeter, getHolder());
        mCameraViewBridgeImpl = new CameraViewBridgeImpl(this, unit);
    }

    @Override
    public void setCvCameraViewListener(CvCameraViewListener2 listener) {
        super.setCvCameraViewListener(listener);
        mCameraViewBridgeImpl.setCvCameraViewListener(listener);
    }

    @Override
    public void setCameraIndex(int cameraIndex) {
        this.mCameraIndex = cameraIndex;
        mCameraViewBridgeImpl.setCameraIndex(cameraIndex);
    }

    // NOTE: On Android 4.1.x the function must be called before SurfaceTexture constructor!
    @Override
    protected void AllocateCache() {
        mCameraViewBridgeImpl.AllocateCache(mFrameWidth, mFrameHeight);
    }

    /**
     * This method shall be called by the subclasses when they have valid
     * object and want it to be delivered to external client (via callback) and
     * then displayed on the screen.
     *
     * @param frame - the current frame to be delivered
     */
    @Override
    protected void deliverAndDrawFrame(CvCameraViewFrame frame) {
        mCameraViewBridgeImpl.deliverAndDrawFrame(frame);
    }

    // -------------------------------------internal method-------------------------------------

    protected void setUseFrontCamera(boolean isUseFrontCamera) {
        mCameraViewBridgeImpl.setUseFrontCamera(isUseFrontCamera);
    }

    protected void setLandscape(boolean isLandscape) {
        mCameraViewBridgeImpl.setLandscape(isLandscape);
    }

    /**
     * 默认实现为OpenCV官方绘制算法，如果想要使用自己的绘制策略，
     * 可通过{@link CameraConfiguration.Builder#drawStrategy(IDrawStrategy)}接口修改
     * {@link Camera2Preview#drawBitmap(Canvas, Bitmap)}
     *
     * @param canvas
     * @param frameBitmap
     */
    public void drawBitmap(Canvas canvas, Bitmap frameBitmap) {
        mCameraViewBridgeImpl.drawBitmap(canvas, frameBitmap);
    }
}
