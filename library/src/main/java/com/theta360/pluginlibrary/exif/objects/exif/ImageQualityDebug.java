package com.theta360.pluginlibrary.exif.objects.exif;

import com.theta360.pluginlibrary.exif.CameraSettings;
import com.theta360.pluginlibrary.values.ThetaModel;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * ImageQualityDebug 画質情報をexifに埋め込むためにbinファイルから取り出す
 */
public class ImageQualityDebug {
    private static final String AE_PATH_V = "/param/debug/ae_debug.bin";
    private static final String AE_PATH_Z1 = "/temp/debug/ae_debug.bin";
    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    private RandomAccessFile mAeFile = null;

    public ImageQualityDebug() throws IOException {
        File fAE = null;
        if (CameraSettings.getThetaModel() == ThetaModel.THETA_Z1) {
            fAE = new File(AE_PATH_Z1);
        } else {
            fAE = new File(AE_PATH_V);
        }

        mAeFile = new RandomAccessFile(fAE, "r");
    }

    public void close() throws IOException {
        mAeFile.close();
    }

    public int[] getSphereFNumber() throws IOException {
        mAeFile.seek(12800);

        byte[] buf = new byte[2];
        mAeFile.read(buf);
        int camera1Numerator = ByteBuffer.wrap(buf).order(BYTE_ORDER).getShort();
        mAeFile.read(buf);
        int camera1Denominator = ByteBuffer.wrap(buf).order(BYTE_ORDER).getShort();
        mAeFile.read(buf);
        int camera2Numerator = ByteBuffer.wrap(buf).order(BYTE_ORDER).getShort();
        mAeFile.read(buf);
        int camera2Denominator = ByteBuffer.wrap(buf).order(BYTE_ORDER).getShort();

        return new int[]{camera1Numerator, camera1Denominator, camera2Numerator,
                camera2Denominator};
    }
}
