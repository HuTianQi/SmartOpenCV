package tech.huqi.smartopencv.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;

import org.opencv.core.Core;
import org.opencv.core.Mat;

/**
 * Created by hzhuqi on 2019/6/1
 */

public class CameraHelper {

    public static void adjustImageOrientation(Context context, Mat srcMat, Mat dstMat,
                                              boolean isFrontCamera, boolean isSetLandscape, int cameraId) {
        int degree = getCameraRotateDegree(cameraId);
        if (isFrontCamera) {
            if (isSetLandscape) {
                if (Util.isPortrait(context)) {
                    rotateFrontCameraMat(srcMat, dstMat, degree);
                } else {
                    Core.flip(srcMat, dstMat, 1); // 沿Y轴进行水平翻转
                }
            } else {
                rotateFrontCameraMat(srcMat, dstMat, degree);
            }
        } else {
            if (isSetLandscape) {
                if (Util.isPortrait(context)) {
                    rotateBackCameraMat(srcMat, dstMat, degree);
                } else {
                    Core.rotate(srcMat, dstMat, Core.ROTATE_180);
                    Core.flip(dstMat, dstMat, -1);
                }
            } else {
                rotateBackCameraMat(srcMat, dstMat, degree);
            }
        }
    }

    private static void rotateFrontCameraMat(Mat srcMat, Mat dstMat, int degree) {
        switch (degree) {
            case 0:
                Core.flip(srcMat, dstMat, 1); // 沿Y轴进行水平翻转
                break;
            case 90:
                Core.rotate(srcMat, dstMat, Core.ROTATE_180);
                Core.flip(dstMat, dstMat, 1); // 沿Y轴进行水平翻转
                break;
            case 270:
                Core.rotate(srcMat, dstMat, Core.ROTATE_90_COUNTERCLOCKWISE);
                Core.flip(dstMat, dstMat, 1); // 沿Y轴进行水平翻转
                break;
        }
    }

    private static void rotateBackCameraMat(Mat srcMat, Mat dstMat, int degree) {
        if (degree == 90) {
            Core.rotate(srcMat, dstMat, Core.ROTATE_90_CLOCKWISE);
        } else {
            Core.rotate(srcMat, dstMat, Core.ROTATE_180);
        }
    }

    private static int getFrontCameraRotateDegree() {
        return getCameraRotateDegree(1);
    }

    private static int getBackCameraRotateDegree() {
        return getCameraRotateDegree(0);
    }

    private static int getCameraRotateDegree(int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        return info.orientation;
    }

    public static Bitmap scaleImage(Bitmap image, int desireWidth, int desireHeight) {
        if (image == null) return null;

        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) desireWidth) / width;
        float scaleHeight = ((float) desireHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap result = Bitmap.createBitmap(image, 0, 0, width, height, matrix, true);
        return result;
    }

    public static int getBackCameraId() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                return camIdx;
            }
        }
        return 0;
    }

    public static int getFrontCameraId() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return camIdx;
            }
        }
        return 1;
    }
}
