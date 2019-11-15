package tech.huqi.smartopencv.core.preview;

import tech.huqi.smartopencv.draw.IDrawStrategy;
import tech.huqi.smartopencv.draw.IPreviewSizeCalculator;

/**
 * Created by hzhuqi on 2019/9/17
 */
public interface ICameraPreview {
    void setUseFrontCamera(boolean isUseFrontCamera);

    void setCameraIndex(int cameraIndex);

    void setLandscape(boolean isAllowedScreenSwitch);

    void setDrawStrategy(IDrawStrategy mDrawStrategy);

    void setCameraFrameSizeCalculator(IPreviewSizeCalculator mCameraFrameSizeCalculator);
}
