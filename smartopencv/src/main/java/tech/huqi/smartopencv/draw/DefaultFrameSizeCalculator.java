package tech.huqi.smartopencv.draw;

import android.content.Context;
import android.content.res.Configuration;

import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tech.huqi.smartopencv.SmartOpenCV;
import tech.huqi.smartopencv.utils.Util;

/**
 * Created by hzhuqi on 2019/9/3
 */

/**
 * SmartOpenCV默认预览帧大小计算策略类
 * 从硬件设备支持的预览大小中选择出宽/高都 >= SurfaceView控件宽/高的最小的预览
 */
public class DefaultFrameSizeCalculator implements IPreviewSizeCalculator {
    private Context mContext;
    private boolean isPortrait;

    public DefaultFrameSizeCalculator(Context context) {
        mContext = context;
        isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    @Override
    public Size calculateCameraFrameSize(List<Size> supportedSizes, int surfaceWidth, int surfaceHeight) {
        Util.printDebugLog("surfaceWidth:" + surfaceWidth + " surfaceHeight:" + surfaceHeight);
        final List<Size> descSupportedSizes = new ArrayList<>(supportedSizes);
        // 按照分辨率降序排序
        Collections.sort(descSupportedSizes, new Comparator<Size>() {
            @Override
            public int compare(Size s1, Size s2) {
                int num = (int) (s2.width - s1.width);
                if (num == 0) {
                    return (int) (s2.height - s1.height);
                }
                return num;
            }
        });
        printSupportSize(descSupportedSizes);

        int targetWidth = surfaceWidth;
        int targetHeight = surfaceHeight;
        /**
         * USB摄像头默认取景方向和设备竖屏方向一致，因此USB摄像头设备无需调整宽高
         */
        if (!SmartOpenCV.getInstance().isUsbCamera() && isPortrait) {
            targetWidth = surfaceHeight;
            targetHeight = surfaceWidth;
        }
        int calcWidth = Integer.MAX_VALUE;
        int calcHeight = Integer.MAX_VALUE;

        // 从支持的预览大小中选择出 >= SurfaceView控件大小集合中最小的预览
        for (Size size : descSupportedSizes) {
            if (size.width >= targetWidth && size.height >= targetHeight) {
                calcWidth = (int) (size.width);
                calcHeight = (int) (size.height);
            }
        }
        // 如果没找到合适预览大小，则选择分辨率最大的
        if (calcWidth == Integer.MAX_VALUE || calcHeight == Integer.MAX_VALUE && descSupportedSizes.size() > 0) {
            calcWidth = (int) (descSupportedSizes.get(0).width);
            calcHeight = (int) (descSupportedSizes.get(0).height);
        }
        Util.printDebugLog("calcWidth:" + calcWidth + " calcHeight:" + calcHeight +
                " screenWidth:" + Util.getScreenWidth(mContext) +
                " screenHeight" + Util.getScreenHeight(mContext));
        return new Size(calcWidth, calcHeight);
    }

    private void printSupportSize(List<Size> supportedSizes) {
        for (Size size : supportedSizes) {
            Util.printDebugLog("support width:" + size.width + " support height:" + size.height);
        }
    }
}
