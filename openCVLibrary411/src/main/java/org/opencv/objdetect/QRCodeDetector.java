//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import org.opencv.core.Mat;

// C++: class QRCodeDetector
/**
 * Groups the object candidate rectangles.
 *     rectList  Input/output vector of rectangles. Output vector includes retained and grouped rectangles. (The Python list is not modified in place.)
 *     weights Input/output vector of weights of rectangles. Output vector includes weights of retained and grouped rectangles. (The Python list is not modified in place.)
 *     groupThreshold Minimum possible number of rectangles minus 1. The threshold is used in a group of rectangles to retain it.
 *     eps Relative difference between sides of the rectangles to merge them into a group.
 */
public class QRCodeDetector {

    protected final long nativeObj;
    protected QRCodeDetector(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static QRCodeDetector __fromPtr__(long addr) { return new QRCodeDetector(addr); }

    //
    // C++:   cv::QRCodeDetector::QRCodeDetector()
    //

    public QRCodeDetector() {
        nativeObj = QRCodeDetector_0();
    }


    //
    // C++:  bool cv::QRCodeDetector::detect(Mat img, Mat& points)
    //

    /**
     * Detects QR code in image and returns the quadrangle containing the code.
     *      @param img grayscale or color (BGR) image containing (or not) QR code.
     *      @param points Output vector of vertices of the minimum-area quadrangle containing the code.
     * @return automatically generated
     */
    public boolean detect(Mat img, Mat points) {
        return detect_0(nativeObj, img.nativeObj, points.nativeObj);
    }


    //
    // C++:  string cv::QRCodeDetector::decode(Mat img, Mat points, Mat& straight_qrcode = Mat())
    //

    /**
     * Decodes QR code in image once it's found by the detect() method.
     *      Returns UTF8-encoded output string or empty string if the code cannot be decoded.
     *
     *      @param img grayscale or color (BGR) image containing QR code.
     *      @param points Quadrangle vertices found by detect() method (or some other algorithm).
     *      @param straight_qrcode The optional output image containing rectified and binarized QR code
     * @return automatically generated
     */
    public String decode(Mat img, Mat points, Mat straight_qrcode) {
        return decode_0(nativeObj, img.nativeObj, points.nativeObj, straight_qrcode.nativeObj);
    }

    /**
     * Decodes QR code in image once it's found by the detect() method.
     *      Returns UTF8-encoded output string or empty string if the code cannot be decoded.
     *
     *      @param img grayscale or color (BGR) image containing QR code.
     *      @param points Quadrangle vertices found by detect() method (or some other algorithm).
     * @return automatically generated
     */
    public String decode(Mat img, Mat points) {
        return decode_1(nativeObj, img.nativeObj, points.nativeObj);
    }


    //
    // C++:  string cv::QRCodeDetector::detectAndDecode(Mat img, Mat& points = Mat(), Mat& straight_qrcode = Mat())
    //

    /**
     * Both detects and decodes QR code
     *
     *      @param img grayscale or color (BGR) image containing QR code.
     *      @param points opiotnal output array of vertices of the found QR code quadrangle. Will be empty if not found.
     *      @param straight_qrcode The optional output image containing rectified and binarized QR code
     * @return automatically generated
     */
    public String detectAndDecode(Mat img, Mat points, Mat straight_qrcode) {
        return detectAndDecode_0(nativeObj, img.nativeObj, points.nativeObj, straight_qrcode.nativeObj);
    }

    /**
     * Both detects and decodes QR code
     *
     *      @param img grayscale or color (BGR) image containing QR code.
     *      @param points opiotnal output array of vertices of the found QR code quadrangle. Will be empty if not found.
     * @return automatically generated
     */
    public String detectAndDecode(Mat img, Mat points) {
        return detectAndDecode_1(nativeObj, img.nativeObj, points.nativeObj);
    }

    /**
     * Both detects and decodes QR code
     *
     *      @param img grayscale or color (BGR) image containing QR code.
     * @return automatically generated
     */
    public String detectAndDecode(Mat img) {
        return detectAndDecode_2(nativeObj, img.nativeObj);
    }


    //
    // C++:  void cv::QRCodeDetector::setEpsX(double epsX)
    //

    /**
     * sets the epsilon used during the horizontal scan of QR code stop marker detection.
     *      @param epsX Epsilon neighborhood, which allows you to determine the horizontal pattern
     *      of the scheme 1:1:3:1:1 according to QR code standard.
     */
    public void setEpsX(double epsX) {
        setEpsX_0(nativeObj, epsX);
    }


    //
    // C++:  void cv::QRCodeDetector::setEpsY(double epsY)
    //

    /**
     * sets the epsilon used during the vertical scan of QR code stop marker detection.
     *      @param epsY Epsilon neighborhood, which allows you to determine the vertical pattern
     *      of the scheme 1:1:3:1:1 according to QR code standard.
     */
    public void setEpsY(double epsY) {
        setEpsY_0(nativeObj, epsY);
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::QRCodeDetector::QRCodeDetector()
    private static native long QRCodeDetector_0();

    // C++:  bool cv::QRCodeDetector::detect(Mat img, Mat& points)
    private static native boolean detect_0(long nativeObj, long img_nativeObj, long points_nativeObj);

    // C++:  string cv::QRCodeDetector::decode(Mat img, Mat points, Mat& straight_qrcode = Mat())
    private static native String decode_0(long nativeObj, long img_nativeObj, long points_nativeObj, long straight_qrcode_nativeObj);
    private static native String decode_1(long nativeObj, long img_nativeObj, long points_nativeObj);

    // C++:  string cv::QRCodeDetector::detectAndDecode(Mat img, Mat& points = Mat(), Mat& straight_qrcode = Mat())
    private static native String detectAndDecode_0(long nativeObj, long img_nativeObj, long points_nativeObj, long straight_qrcode_nativeObj);
    private static native String detectAndDecode_1(long nativeObj, long img_nativeObj, long points_nativeObj);
    private static native String detectAndDecode_2(long nativeObj, long img_nativeObj);

    // C++:  void cv::QRCodeDetector::setEpsX(double epsX)
    private static native void setEpsX_0(long nativeObj, double epsX);

    // C++:  void cv::QRCodeDetector::setEpsY(double epsY)
    private static native void setEpsY_0(long nativeObj, double epsY);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
