package tech.huqi.smartopencv.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.List;

/**
 * Created by hzhuqi on 2019/9/3
 */

/**
 * 图像帧绘制策略接口
 * 将实时获取的每一帧图像{@code frameBitmap}绘制到预览画布{@code canvas}上
 */
public interface IDrawStrategy {
    /**
     * 绘制每一帧图像
     *
     * @param canvas        画布
     * @param frameBitmap   待绘制图像帧的{@code Bitmap}对象，其宽高分别等同于预览帧大小计算接口
     *                      {@link IPreviewSizeCalculator#calculateCameraFrameSize(List, int, int)}
     *                      返回的Size宽和高
     * @param surfaceWidth  SurfaceView的宽度
     * @param surfaceHeight SurfaceView的高度
     */
    void drawBitmap(Canvas canvas, Bitmap frameBitmap, int surfaceWidth, int surfaceHeight);
}
