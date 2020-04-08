package tech.huqi.smartopencv.core.preview;

import android.graphics.Bitmap;

import org.opencv.android.CameraBridgeViewBase;

import tech.huqi.smartopencv.draw.IDrawStrategy;
import tech.huqi.smartopencv.draw.IPreviewSizeCalculator;
import tech.huqi.smartopencv.utils.Util;

/**
 * Created by hzhuqi on 2019/9/5
 */
public class CameraConfiguration {
    public static final Bitmap.Config DEFAULT_BITMAP_CONFIG = Bitmap.Config.RGB_565;
    public static final int NOT_SET_VALUE = -1000;
    final int maxWidth;
    final int maxHeight;
    final int cameraIndex;
    final boolean landscape;
    final boolean frontCamera;
    final boolean usbCamera;
    final boolean keepScreenOn;
    final boolean enableFpsMeter;
    final boolean openCvDefaultDrawStrategy;
    final boolean openCvDefaultPreviewCalculator;
    final boolean allowedScreenOrientationSwitch;
    final Bitmap.Config bitmapConfig;
    final IDrawStrategy drawStrategy;
    final IPreviewSizeCalculator previewSizeCalculator;
    final CameraBridgeViewBase.CvCameraViewListener cvCameraViewListener;
    final CameraBridgeViewBase.CvCameraViewListener2 cvCameraViewListener2;

    public CameraConfiguration(final Builder builder) {
        this.maxWidth = builder.maxWidth;
        this.maxHeight = builder.maxHeight;
        this.cameraIndex = builder.cameraIndex;
        this.landscape = builder.landscape;
        this.frontCamera = builder.frontCamera;
        this.usbCamera = builder.usbCamera;
        this.keepScreenOn = builder.keepScreenOn;
        this.enableFpsMeter = builder.enableFpsMeter;
        this.openCvDefaultDrawStrategy = builder.openCvDefaultDrawStrategy;
        this.openCvDefaultPreviewCalculator = builder.openCvDefaultPreviewCalculator;
        this.allowedScreenOrientationSwitch = builder.allowedScreenOrientationSwitch;
        this.bitmapConfig = builder.bitmapConfig;
        this.drawStrategy = builder.drawStrategy;
        this.previewSizeCalculator = builder.previewSizeCalculator;
        this.cvCameraViewListener = builder.cvCameraViewListener;
        this.cvCameraViewListener2 = builder.cvCameraViewListener2;
        Util.isEnableDebugMode(builder.debug);
    }

    public boolean isUsbCamera() {
        return usbCamera;
    }

    public boolean isAllowedScreenOrientationSwitch() {
        return allowedScreenOrientationSwitch;
    }

    public static class Builder {
        private boolean debug;
        private boolean landscape;
        private boolean frontCamera;
        private boolean usbCamera;
        private boolean keepScreenOn;
        private boolean enableFpsMeter;
        private boolean openCvDefaultDrawStrategy;
        private boolean openCvDefaultPreviewCalculator;
        private boolean allowedScreenOrientationSwitch;
        private int maxWidth = NOT_SET_VALUE;
        private int maxHeight = NOT_SET_VALUE;
        private int cameraIndex = NOT_SET_VALUE;
        private Bitmap.Config bitmapConfig = DEFAULT_BITMAP_CONFIG;
        private IDrawStrategy drawStrategy;
        private IPreviewSizeCalculator previewSizeCalculator;
        private CameraBridgeViewBase.CvCameraViewListener cvCameraViewListener;
        private CameraBridgeViewBase.CvCameraViewListener2 cvCameraViewListener2;

        public Builder debug(boolean isEnableDebug) {
            this.debug = isEnableDebug;
            return this;
        }

        /**
         * 是否横屏显示，如果设置为true，请在Manifest的Activity中设置{android:screenOrientation="landscape"
         *
         * @param isLandscape
         * @return
         */
        public Builder landscape(boolean isLandscape) {
            this.landscape = isLandscape;
            return this;
        }

        public Builder frontCamera(boolean isUseFrontCamera) {
            this.frontCamera = isUseFrontCamera;
            return this;
        }

        /**
         * 是否是USB摄像头
         * USB摄像头作为外设接入设备，因此不会像手机那样当旋转设备时摄像头会跟随旋转
         * 另一方面USB摄像头安装默认取景方向是设备竖屏，而手机摄像头一般以手机横屏时为默认安装取景方向
         *
         * @param isUsbCamera
         * @return
         */
        public Builder usbCamera(boolean isUsbCamera) {
            this.usbCamera = isUsbCamera;
            return this;
        }

        public Builder keepScreenOn(boolean isKeepScreenOn) {
            this.keepScreenOn = isKeepScreenOn;
            return this;
        }

        /**
         * 设置摄像头索引,主要用于多摄像头设备中控制使用哪一个摄像头
         * 优先级低于{@link Builder#frontCamera(boolean)}
         *
         * @param cameraIndex
         * @return
         */
        public Builder cameraIndex(int cameraIndex) {
            this.cameraIndex = cameraIndex;
            return this;
        }

        public Builder openCvDefaultPreviewCalculator(boolean isUseOpenCvDefaultPreviewCalculator) {
            this.openCvDefaultPreviewCalculator = isUseOpenCvDefaultPreviewCalculator;
            return this;
        }

        public Builder openCvDefaultDrawStrategy(boolean isUseOpenCvDefaultDrawStrategy) {
            this.openCvDefaultDrawStrategy = isUseOpenCvDefaultDrawStrategy;
            return this;
        }

        /**
         * 该方法已经废弃，不推荐运行过程中旋转屏幕
         * 默认竖屏，如果要横屏显示可调用{@link Builder#landscape(boolean)}
         *
         * @param isAllowedScreenOrientationSwitch
         * @return
         */
        @Deprecated
        public Builder allowedScreenOrientationSwitch(boolean isAllowedScreenOrientationSwitch) {
            this.allowedScreenOrientationSwitch = isAllowedScreenOrientationSwitch;
            return this;
        }

        public Builder maxFrameSize(int maxWidth, int maxHeight) {
            if (maxWidth != NOT_SET_VALUE) this.maxWidth = maxWidth;
            if (maxHeight != NOT_SET_VALUE) this.maxHeight = maxHeight;
            return this;
        }

        public Builder enableFpsMeter(boolean isEnableFpsMeter) {
            this.enableFpsMeter = isEnableFpsMeter;
            return this;
        }

        public Builder bitmapConfig(Bitmap.Config bitmapConfig) {
            this.bitmapConfig = bitmapConfig;
            return this;
        }

        public Builder cvCameraViewListener(CameraBridgeViewBase.CvCameraViewListener cvCameraViewListener) {
            this.cvCameraViewListener = cvCameraViewListener;
            return this;
        }

        public Builder cvCameraViewListener(CameraBridgeViewBase.CvCameraViewListener2 cvCameraViewListener) {
            this.cvCameraViewListener2 = cvCameraViewListener;
            return this;
        }

        public Builder drawStrategy(IDrawStrategy drawStrategy) {
            this.drawStrategy = drawStrategy;
            return this;
        }

        public Builder previewSizeCalculator(IPreviewSizeCalculator previewSizeCalculator) {
            this.previewSizeCalculator = previewSizeCalculator;
            return this;
        }

        public CameraConfiguration build() {
            return new CameraConfiguration(this);
        }
    }
}
