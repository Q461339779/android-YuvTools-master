package cc.rome753.yuvtools;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;

import java.nio.ByteBuffer;

public class YUVTools {

    /******************************* YUV420旋转算法 *******************************/

    public static final String TAG = "YUVTools_";
    // I420或YV12顺时针旋转
    public static void rotateP(byte[] src, byte[] dest, int w, int h, int rotation) {
        switch (rotation) {
            case 0:
                System.arraycopy(src, 0, dest, 0, src.length);
                break;
            case 90:
                rotateP90(src, dest, w, h);
                break;
            case 180:
                rotateP180(src, dest, w, h);
                break;
            case 270:
                rotateP270(src, dest, w, h);
                break;
        }
    }

    // NV21或NV12顺时针旋转
    public static void rotateSP(byte[] src, byte[] dest, int w, int h, int rotation) {
        switch (rotation) {
            case 0:
                System.arraycopy(src, 0, dest, 0, src.length);
                break;
            case 90:
                rotateSP90(src, dest, w, h);
                break;
            case 180:
                rotateSP180(src, dest, w, h);
                break;
            case 270:
                rotateSP270(src, dest, w, h);
                break;
        }
    }

    // NV21或NV12顺时针旋转90度
    public static void rotateSP90(byte[] src, byte[] dest, int w, int h) {
        int pos = 0;
        int k = 0;
        for (int i = 0; i <= w - 1; i++) {
            for (int j = h - 1; j >= 0; j--) {
                dest[k++] = src[j * w + i];
            }
        }

        pos = w * h;
        for (int i = 0; i <= w - 2; i += 2) {
            for (int j = h / 2 - 1; j >= 0; j--) {
                dest[k++] = src[pos + j * w + i];
                dest[k++] = src[pos + j * w + i + 1];
            }
        }
    }

    // NV21或NV12顺时针旋转270度
    public static void rotateSP270(byte[] src, byte[] dest, int w, int h) {
        int pos = 0;
        int k = 0;
        for (int i = w - 1; i >= 0; i--) {
            for (int j = 0; j <= h - 1; j++) {
                dest[k++] = src[j * w + i];
            }
        }

        pos = w * h;
        for (int i = w - 2; i >= 0; i -= 2) {
            for (int j = 0; j <= h / 2 - 1; j++) {
                dest[k++] = src[pos + j * w + i];
                dest[k++] = src[pos + j * w + i + 1];
            }
        }
    }

    // NV21或NV12顺时针旋转180度
    public static void rotateSP180(byte[] src, byte[] dest, int w, int h) {
        int pos = 0;
        int k = w * h - 1;
        while (k >= 0) {
            dest[pos++] = src[k--];
        }

        k = src.length - 2;
        while (pos < dest.length) {
            dest[pos++] = src[k];
            dest[pos++] = src[k + 1];
            k -= 2;
        }
    }

    // I420或YV12顺时针旋转90度
    public static void rotateP90(byte[] src, byte[] dest, int w, int h) {
        int pos = 0;
        //旋转Y
        int k = 0;
        for (int i = 0; i < w; i++) {
            for (int j = h - 1; j >= 0; j--) {
                dest[k++] = src[j * w + i];
            }
        }
        //旋转U
        pos = w * h;
        for (int i = 0; i < w / 2; i++) {
            for (int j = h / 2 - 1; j >= 0; j--) {
                dest[k++] = src[pos + j * w / 2 + i];
            }
        }

        //旋转V
        pos = w * h * 5 / 4;
        for (int i = 0; i < w / 2; i++) {
            for (int j = h / 2 - 1; j >= 0; j--) {
                dest[k++] = src[pos + j * w / 2 + i];
            }
        }
    }

    // I420或YV12顺时针旋转270度
    public static void rotateP270(byte[] src, byte[] dest, int w, int h) {
        int pos = 0;
        //旋转Y
        int k = 0;
        for (int i = w - 1; i >= 0; i--) {
            for (int j = 0; j < h; j++) {
                dest[k++] = src[j * w + i];
            }
        }
        //旋转U
        pos = w * h;
        for (int i = w / 2 - 1; i >= 0; i--) {
            for (int j = 0; j < h / 2; j++) {
                dest[k++] = src[pos + j * w / 2 + i];
            }
        }

        //旋转V
        pos = w * h * 5 / 4;
        for (int i = w / 2 - 1; i >= 0; i--) {
            for (int j = 0; j < h / 2; j++) {
                dest[k++] = src[pos + j * w / 2 + i];
            }
        }
    }

    // I420或YV12顺时针旋转180度
    public static void rotateP180(byte[] src, byte[] dest, int w, int h) {
        int pos = 0;
        int k = w * h - 1;
        while (k >= 0) {
            dest[pos++] = src[k--];
        }

        k = w * h * 5 / 4;
        while (k >= w * h) {
            dest[pos++] = src[k--];
        }

        k = src.length - 1;
        while (pos < dest.length) {
            dest[pos++] = src[k--];
        }
    }

    /******************************* YUV420格式相互转换算法 *******************************/

    // i420 -> nv12, yv12 -> nv21
    public static void pToSP(byte[] src, byte[] dest, int w, int h) {
        int pos = w * h;
        int u = pos;
        int v = pos + (pos >> 2);
        System.arraycopy(src, 0, dest, 0, pos);
        while (pos < src.length) {
            dest[pos++] = src[u++];
            dest[pos++] = src[v++];
        }
    }

    // i420 -> nv21, yv12 -> nv12
    public static void pToSPx(byte[] src, byte[] dest, int w, int h) {
        int pos = w * h;
        int u = pos;
        int v = pos + (pos >> 2);
        System.arraycopy(src, 0, dest, 0, pos);
        while (pos < src.length) {
            dest[pos++] = src[v++];
            dest[pos++] = src[u++];
        }
    }

    // nv12 -> i420, nv21 -> yv12
    public static void spToP(byte[] src, byte[] dest, int w, int h) {
        int pos = w * h;
        int u = pos;
        int v = pos + (pos >> 2);
        System.arraycopy(src, 0, dest, 0, pos);
        while (pos < src.length) {
            dest[u++] = src[pos++];
            dest[v++] = src[pos++];
        }
    }

    // nv12 -> yv12, nv21 -> i420
    public static void spToPx(byte[] src, byte[] dest, int w, int h) {
        int pos = w * h;
        int u = pos;
        int v = pos + (pos >> 2);
        System.arraycopy(src, 0, dest, 0, pos);
        while (pos < src.length) {
            dest[v++] = src[pos++];
            dest[u++] = src[pos++];
        }
    }

    // i420 <-> yv12
    public static void pToP(byte[] src, byte[] dest, int w, int h) {
        int pos = w * h;
        int off = pos >> 2;
        System.arraycopy(src, 0, dest, 0, pos);
        System.arraycopy(src, pos, dest, pos + off, off);
        System.arraycopy(src, pos + off, dest, pos, off);
    }

    // nv12 <-> nv21
    public static void spToSP(byte[] src, byte[] dest, int w, int h) {
        int pos = w * h;
        System.arraycopy(src, 0, dest, 0, pos);
        for (; pos < src.length; pos += 2) {
            dest[pos] = src[pos + 1];
            dest[pos + 1] = src[pos];
        }
    }


    /******************************* YUV420转换Bitmap算法 *******************************/

    // 此方法虽然是官方的，但是耗时是下面方法的两倍
//    public static Bitmap nv21ToBitmap(byte[] data, int w, int h) {
//        final YuvImage image = new YuvImage(data, ImageFormat.NV21, w, h, null);
//        ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
//        if (image.compressToJpeg(new Rect(0, 0, w, h), 100, os)) {
//            byte[] tmp = os.toByteArray();
//            return BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
//        }
//        return null;
//    }

    public static Bitmap nv12ToBitmap(byte[] data, int w, int h) {
        return spToBitmap(data, w, h, 0, 1);
    }

    public static Bitmap nv21ToBitmap(byte[] data, int w, int h) {
        return spToBitmap(data, w, h, 1, 0);
    }

    private static Bitmap spToBitmap(byte[] data, int w, int h, int uOff, int vOff) {
        int plane = w * h;
        int[] colors = new int[plane];
        int yPos = 0, uvPos = plane;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                // YUV byte to RGB int
                final int y1 = data[yPos] & 0xff;
                final int u = (data[uvPos + uOff] & 0xff) - 128;
                final int v = (data[uvPos + vOff] & 0xff) - 128;
                final int y1192 = 1192 * y1;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                r = (r < 0) ? 0 : ((r > 262143) ? 262143 : r);
                g = (g < 0) ? 0 : ((g > 262143) ? 262143 : g);
                b = (b < 0) ? 0 : ((b > 262143) ? 262143 : b);
                colors[yPos] = ((r << 6) & 0xff0000) |
                        ((g >> 2) & 0xff00) |
                        ((b >> 10) & 0xff);

                if((yPos++ & 1) == 1) uvPos += 2;
            }
            if((j & 1) == 0) uvPos -= w;
        }
        return Bitmap.createBitmap(colors, w, h, Bitmap.Config.RGB_565);
    }

    public static Bitmap i420ToBitmap(byte[] data, int w, int h) {
        return pToBitmap(data, w, h, true);
    }

    public static Bitmap yv12ToBitmap(byte[] data, int w, int h) {
        return pToBitmap(data, w, h, false);
    }

    private static Bitmap pToBitmap(byte[] data, int w, int h, boolean uv) {
        int plane = w * h;
        int[] colors = new int[plane];
        int off = plane >> 2;
        int yPos = 0, uPos = plane + (uv ? 0 : off), vPos = plane + (uv ? off : 0);
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                // YUV byte to RGB int
                final int y1 = data[yPos] & 0xff;
                final int u = (data[uPos] & 0xff) - 128;
                final int v = (data[vPos] & 0xff) - 128;
                final int y1192 = 1192 * y1;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                r = (r < 0) ? 0 : ((r > 262143) ? 262143 : r);
                g = (g < 0) ? 0 : ((g > 262143) ? 262143 : g);
                b = (b < 0) ? 0 : ((b > 262143) ? 262143 : b);
                colors[yPos] = ((r << 6) & 0xff0000) |
                        ((g >> 2) & 0xff00) |
                        ((b >> 10) & 0xff);

                if((yPos++ & 1) == 1) {
                    uPos++;
                    vPos++;
                }
            }
            if((j & 1) == 0) {
                uPos -= (w >> 1);
                vPos -= (w >> 1);
            }
        }
        return Bitmap.createBitmap(colors, w, h, Bitmap.Config.RGB_565);
    }

    public static int[] planesToColors(Image.Plane[] planes, int height) {
        ByteBuffer yPlane = planes[0].getBuffer();
        ByteBuffer uPlane = planes[1].getBuffer();
        ByteBuffer vPlane = planes[2].getBuffer();

        int bufferIndex = 0;
        final int total = yPlane.capacity();
        final int uvCapacity = uPlane.capacity();
        final int width = planes[0].getRowStride();

        int[] rgbBuffer = new int[width * height];

        int yPos = 0;
        for (int i = 0; i < height; i++) {
            int uvPos = (i >> 1) * width;

            for (int j = 0; j < width; j++) {
                if (uvPos >= uvCapacity - 1)
                    break;
                if (yPos >= total)
                    break;

                final int y1 = yPlane.get(yPos++) & 0xff;

            /*
              The ordering of the u (Cb) and v (Cr) bytes inside the planes is a
              bit strange. The _first_ byte of the u-plane and the _second_ byte
              of the v-plane build the u/v pair and belong to the first two pixels
              (y-bytes), thus usual YUV 420 behavior. What the Android devs did
              here (IMHO): just copy the interleaved NV21 U/V data to two planes
              but keep the offset of the interleaving.
             */
                final int u = (uPlane.get(uvPos) & 0xff) - 128;
                final int v = (vPlane.get(uvPos) & 0xff) - 128;
                if ((j & 1) == 1) {
                    uvPos += 2;
                }

                // This is the integer variant to convert YCbCr to RGB, NTSC values.
                // formulae found at
                // https://software.intel.com/en-us/android/articles/trusted-tools-in-the-new-android-world-optimization-techniques-from-intel-sse-intrinsics-to
                // and on StackOverflow etc.
                final int y1192 = 1192 * y1;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                r = (r < 0) ? 0 : ((r > 262143) ? 262143 : r);
                g = (g < 0) ? 0 : ((g > 262143) ? 262143 : g);
                b = (b < 0) ? 0 : ((b > 262143) ? 262143 : b);

                rgbBuffer[bufferIndex++] = ((r << 6) & 0xff0000) |
                        ((g >> 2) & 0xff00) |
                        ((b >> 10) & 0xff);
            }
        }
        return rgbBuffer;
    }

    /**
     * 从ImageReader中获取byte[]数据
     */
    public static ImageBytes getBytesFromImageReader(ImageReader imageReader) {
        try (Image image = imageReader.acquireNextImage()) {
//            Log.i(TAG,"image format: " +image.getFormat());
//            Image.Plane[] planes = image.getPlanes();
//            for (int i = 0; i < planes.length; i++) {
//                ByteBuffer iBuffer = planes[i].getBuffer();
//                int iSize = iBuffer.remaining();
//                Log.i(TAG, "i_pixelStride                       " + planes[i].getPixelStride());
//                Log.i(TAG, "i_rowStride                         " + planes[i].getRowStride());
//                Log.i(TAG, "i_width                             " + image.getWidth());
//                Log.i(TAG, "i_height                            " + image.getHeight());
//                Log.i(TAG, "i_buffer size                       " + iSize);
//                Log.i(TAG, "i_Finished reading data from plane  " + i);
//            }

            return getBytesFromImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param image
     * @return
     */
    public static ImageBytes getBytesFromImage(Image image) {
        final Image.Plane[] planes = image.getPlanes();

        Image.Plane p0 = planes[0];
        Image.Plane p1 = planes[1];
        Image.Plane p2 = planes[2];

        ByteBuffer b0 = p0.getBuffer();
        ByteBuffer b1 = p1.getBuffer();
        ByteBuffer b2 = p2.getBuffer();

        int r0 = b0.remaining();
        int r1 = b1.remaining();
        int r2 = b2.remaining();

        int w0 = p0.getRowStride();
        int h0 = r0 / w0;
        if(r0 % w0 > 0) h0++;
        int w1 = p1.getRowStride();
        int h1 = r1 / w1;
        if(r1 % w1 > 1) h1++;
        int w2 = p2.getRowStride();
        int h2 = r2 / w2;
        if(r2 % w2 > 2) h2++;

        int y = w0 * h0;
        int u = w1 * h1;
        int v = w2 * h2;

        byte[] bytes = new byte[y + u + v];

        b0.get(bytes, 0, r0);
        b1.get(bytes, y, r1); // u
        b2.get(bytes, y + u, r2); // v

        return new ImageBytes(bytes, w0, h0);
    }

    /**
     * 将 YUV 420 的 YV12 格式 转为 nv21 格式
     *
     * @param yv12bytes 转换之前的YV12格式数据
     * @param nv21bytes 转换之后的NV21格式数据
     * @param width     数据 宽
     * @param height    数据 高
     */
    public static void YV12toNV21(byte[]yv12bytes, byte[] nv21bytes, int width, int height) {
        int totle = width * height; //Y数据的长度
        int nLen = totle / 4;  //U、V数据的长度
        System.arraycopy(yv12bytes, 0, nv21bytes, 0, width * height);
        for (int i = 0; i < nLen; i++) {
            nv21bytes[totle + 2 * i + 1] = yv12bytes[totle  + nLen + i];
            nv21bytes[totle + 2 * i] = yv12bytes[totle + i];
        }
    }

    /**
     * 将 YUV 420 的 YV12 格式 转为 I420 格式
     *
     * @param yv12bytes 转换之前的YV12格式数据
     * @param i420bytes 转换之后的i420格式数据
     * @param width     数据 宽
     * @param height    数据 高
     */
    private void YV12toI420(byte[]yv12bytes, byte[] i420bytes, int width, int height) {
        int totle = width * height; //Y数据的长度
        System.arraycopy(yv12bytes, 0, i420bytes, 0,totle);
        System.arraycopy(yv12bytes, totle + totle/4, i420bytes, totle,totle/4);
        System.arraycopy(yv12bytes, totle, i420bytes, totle + totle/4,totle/4);
    }


    /**
     * 将 YUV 420 的 NV21 格式 转为 I420 格式
     * @param nv21bytes 转换之前的NV21格式数据
     * @param i420bytes 转换之后的i420格式数据
     * @param width     数据 宽
     * @param height    数据 高
     *
     * NV21 is a 4:2:0 YCbCr, For 1 NV21 pixel: YYYYYYYY VUVU I420YUVSemiPlanar
     * is a 4:2:0 YUV, For a single I420 pixel: YYYYYYYY UVUV Apply NV21 to
     * I420YUVSemiPlanar(NV12) Refer to https://wiki.videolan.org/YUV/
     */
    private void NV21toI420SemiPlanar(byte[] nv21bytes, byte[] i420bytes,
                                      int width, int height) {
        int totle = width * height; //Y数据的长度
        int nLen = totle / 4;  //U、V数据的长度
        System.arraycopy(nv21bytes, 0, i420bytes, 0, totle);
        for (int i = 0; i < nLen; i++) {
            i420bytes[totle + i] = nv21bytes[totle + 2 * i];
            i420bytes[totle + nLen + i] = nv21bytes[totle + 2 * i + 1];
        }
    }
    static {
        System.loadLibrary("yuv420");
    }

    public static native void i420ToNv21cpp(byte[] src, byte[] dest, int width, int height);

    public static native void yv12ToNv21cpp(byte[] src, byte[] dest, int width, int height);

    public static native void nv12ToNv21cpp(byte[] src, byte[] dest, int width, int height);
}
