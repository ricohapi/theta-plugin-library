/**
 * Copyright 2018 Ricoh Company, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.theta360.pluginlibrary.exif;

import android.support.annotation.NonNull;

import com.theta360.pluginlibrary.exif.utils.Buffer;
import com.theta360.pluginlibrary.exif.values.ExposureProgram;
import com.theta360.pluginlibrary.exif.values.WhiteBalance;
import com.theta360.pluginlibrary.exif.values.exif.IFD;
import com.theta360.pluginlibrary.exif.values.exif.Tag;

/**
 * ExifRMKN class
 */
public class ExifRMKN extends Exif {
    /**
     * Analyzes the Exif tag and the MakerNote in the presented data.
     *
     * @param data Exif data in `RMKN` box in video file
     */
    public ExifRMKN(byte[] data) {
        this(data, false);
    }

    /**
     * Analyzes the Exif tag and the MakerNote in the presented data, and holds the result in the object.
     *
     * @param data Exif data in `RMKN` box in video file
     * @param dataBeforeModify Metadata operation flag<br>
     *                         true: Before operation (eg. When `data` is data obtained by takePicture method.)<br>
     *                         false: After operation (eg. When `data` is read from a saved image file.)
     */
    public ExifRMKN(@NonNull byte[] data, boolean dataBeforeModify) {
        super(data, dataBeforeModify);
        loadAttributes();
    }

    /**
     * Set zenith correction disabled in MP4 file metadata
     */
    @Override
    public void setExifSphere() {
        setExifSphere(false);
    }

    private boolean parseHeader() {
        mSegment.base = 0;
        return mSegment.parseTIFFHeader();
    }

    private void loadAttributes() {
        if (!parseHeader()) {
            return;
        }
        parseTagPos();
    }

    protected Buffer getBuffer() {
        return mBuffer;
    }

    public byte[] replaceRMKN() {
        setAttribute(IFD.MAPP1_IFDM, Tag.TAG_RM_0005, String.format("%016X", CameraSettings.getThetaSerialNumber()));

        setAttribute(IFD.MAPP1_IFDM, Tag.TAG_RM_1000, (short) 2);

        setAttribute(IFD.MAPP1_IFDM, Tag.TAG_RM_1307, CameraSettings.getColorTemperature().intValue());

        WhiteBalance whiteBalance = CameraSettings.getWhiteBalance();
        setAttribute(IFD.MAPP1_IFDM, Tag.TAG_RM_1003,
                (short) whiteBalance.getMakerValue());
        if (whiteBalance == WhiteBalance.AUTO) {
            setAttribute(IFD.MAPP1_EXIF, Tag.TAG_WHITEBALANCE, (short) 0);
        } else {
            setAttribute(IFD.MAPP1_EXIF, Tag.TAG_WHITEBALANCE, (short) 1);
        }

        ExposureProgram exposureProgram = CameraSettings.getExposureProgram();
        if (exposureProgram == ExposureProgram.ISO_PRIORITY) {
            setAttribute(IFD.MAPP1_EXIF, Tag.TAG_EXPOSUREPROGRAM,
                    (short) ExposureProgram.NORMAL_PROGRAM.getInt());
        } else {
            setAttribute(IFD.MAPP1_EXIF, Tag.TAG_EXPOSUREPROGRAM,
                    (short) exposureProgram.getInt());
        }

        if (exposureProgram == ExposureProgram.MANUAL) {
            setAttribute(IFD.MAPP1_EXIF, Tag.TAG_EXPOSUREMODE, (short) 1);
        } else {
            setAttribute(IFD.MAPP1_EXIF, Tag.TAG_EXPOSUREMODE, (short) 0);
        }

        setExifGPS();

        setExifSphere();

        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0108, (short) 0x01);

        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_1014, (short) 0x00);

        return getExif();
    }
}
