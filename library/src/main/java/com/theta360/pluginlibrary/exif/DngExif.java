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

import java.nio.ByteBuffer;
import java.util.Arrays;

import android.support.annotation.NonNull;

import com.theta360.pluginlibrary.exif.values.exif.IFD;
import com.theta360.pluginlibrary.exif.values.exif.Tag;

/**
 * DngExif
 */
public class DngExif extends Exif {
    /**
     * Analyzes the Exif tag and the MakerNote in the captured DNG data, and holds the result in the object.
     *
     * @param data Image data
     * @param dataBeforeModify Metadata operation flag<br>
     *                         true: Before operation (eg. When `data` is DNG data obtained by takePicture method.)<br>
     *                         false: After operation (eg. When `data` is read from a saved image file.)
     */
    public DngExif(@NonNull byte[] data, boolean dataBeforeModify) {
        super(data, dataBeforeModify);
        loadAttributes();
    }

    /**
     * Set zenith correction disabled in DNG file metadata
     */
    @Override
    public void setExifSphere() {
        setExifSphere(false);
    }

    @Override
    int getMaxSegmentLen() {
        return Integer.MAX_VALUE;
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

    /**
     * Returns the JPEG image data held by the DngExif object.
     *
     * @return Image data
     */
    public byte[] getJpeg() {
        int offset = ByteBuffer.wrap(getAttribute(IFD.MAPP1_SUBIFD1, Tag.TAG_STRIPOFFSETS))
                .getInt();
        int length = ByteBuffer.wrap(getAttribute(IFD.MAPP1_SUBIFD1, Tag.TAG_STRIPBYTECOUNTS))
                .getInt();
        return Arrays.copyOfRange(getExif(), offset, offset + length);
    }

    public void replaceJpeg(byte[] jpeg) {
        int jpegOffset = ByteBuffer.wrap(getAttribute(IFD.MAPP1_SUBIFD1, Tag.TAG_STRIPOFFSETS))
                .getInt();
        int jpegOldLength = ByteBuffer
                .wrap(getAttribute(IFD.MAPP1_SUBIFD1, Tag.TAG_STRIPBYTECOUNTS))
                .getInt();
        int thumbnailOldOffset = ByteBuffer.wrap(getAttribute(IFD.MAPP1_IFD1, Tag.TAG_JPEGICFORMAT))
                .getInt();
        mBuffer.seek(jpegOffset);
        mBuffer.replace(jpegOldLength, jpeg);
        setAttribute(IFD.MAPP1_SUBIFD1, Tag.TAG_STRIPBYTECOUNTS, jpeg.length);
        setAttribute(IFD.MAPP1_IFD1, Tag.TAG_JPEGICFORMAT,
                thumbnailOldOffset + (jpeg.length - jpegOldLength));
    }
}
