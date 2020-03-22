package tech.huqi.smartopencv.draw;

import org.opencv.core.Size;

import java.util.List;

/**
 * Created by hzhuqi on 2019/9/3
 */
public interface IPreviewSizeCalculator {
    /**
     * @param supportedSizes 当前设备摄像头所支持的预览大小列表
     * @param surfaceWidth   预览控件的宽度
     * @param surfaceHeight  预览控件的高度
     * @return 设置给摄像头的预览宽高Size
     */
    Size calculateCameraFrameSize(List<Size> supportedSizes, int surfaceWidth, int surfaceHeight);
}
