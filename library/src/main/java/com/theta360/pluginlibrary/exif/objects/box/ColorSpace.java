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

package com.theta360.pluginlibrary.exif.objects.box;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * ColorSpace
 */
public class ColorSpace {
    private static final int BOX_SIZE_LEN = 4;
    private static final int BOX_TYPE_LEN = 4;
    private static final int BOX_HEADER_LEN = BOX_SIZE_LEN + BOX_TYPE_LEN;

    // Movie color space Full range
    private static byte[] AVCC_H264_4K_FULL = {
            0x01, 0x42, (byte) 0x80, 0x33, (byte) 0xFF, (byte) 0xE1, 0x00, 0x12,
            0x67, 0x42, (byte) 0x80, 0x33, (byte) 0xDA, 0x00, (byte) 0xF0, 0x03,
            (byte) 0xC6, (byte) 0x9B, (byte) 0x80, (byte) 0x83, 0x03, 0x03, 0x68, 0x50,
            (byte) 0x9A, (byte) 0x80, 0x01, 0x00, 0x04, 0x68, (byte) 0xCE, 0x06,
            (byte) 0xE2
    };

    // Movie color space Full range SPS.Constrained_set1_flag ON
    private static byte[] AVCC_H264_4K_FULL_CONSTRAINED_FLAG_ON = {
            0x01, 0x42, (byte) 0xC0, 0x33, (byte) 0xFF, (byte) 0xE1, 0x00, 0x12,
            0x67, 0x42, (byte) 0xC0, 0x33, (byte) 0xDA, 0x00, (byte) 0xF0, 0x03,
            (byte) 0xC6, (byte) 0x9B, (byte) 0x80, (byte) 0x83, 0x03, 0x03, 0x68, 0x50,
            (byte) 0x9A, (byte) 0x80, 0x01, 0x00, 0x04, 0x68, (byte) 0xCE, 0x06,
            (byte) 0xE2
    };

    // Movie color space Full range
    private static byte[] AVCC_H264_2K_FULL = {
            0x01, 0x42, (byte) 0x80, 0x28, (byte) 0xFF, (byte) 0xE1, 0x00, 0x11,
            0x67, 0x42, (byte) 0x80, 0x28, (byte) 0xDA, 0x01, (byte) 0xE0, 0x1E,
            (byte) 0x69, (byte) 0xB8, 0x08, 0x30, 0x30, 0x36, (byte) 0x85, 0x09,
            (byte) 0xA8, 0x01, 0x00, 0x04, 0x68, (byte) 0xCE, 0x06, (byte) 0xE2
    };

    // Color space tag
    private static byte[] COLR_BT709_FULL = {
            0x6E, 0x63, 0x6C, 0x78, 0x00, 0x01, 0x00, 0x01,
            0x00, 0x06, (byte) 0x80
    };

    private static byte[] H_354b = {0x6E, 0x02};
    private static byte[] H_344b = {(byte) 0xE6, (byte) 0xE0, 0x20};
    private static byte[] H_324b = {0x6E, 0x02};

    public void replaceColorSpace(RandomAccessFile file, long stsdOffset, int width, int height) {
        try {
            file.seek(stsdOffset + 16);

            int size = file.readInt();
            byte[] type = new byte[BOX_TYPE_LEN];
            file.read(type);
            String sType = new String(type, "UTF-8");

            if (!sType.equals("avc1") && !sType.equals("hvc1")) {
                return;
            }

            if (size == 0xA2) {
                file.skipBytes(0x56);
                file.write(AVCC_H264_4K_FULL_CONSTRAINED_FLAG_ON);
                file.skipBytes(16 + 8);
                file.write(COLR_BT709_FULL);
            } else if (size == 0xA1) {
                file.skipBytes(0x56);
                file.write(AVCC_H264_2K_FULL);
                file.skipBytes(16 + 8);
                file.write(COLR_BT709_FULL);
            } else if (size == 0xEB && width == 5376 && height == 2688) {
                file.skipBytes(0xAC);
                file.write(H_354b);
                file.skipBytes(0x2A);
                file.write(COLR_BT709_FULL);
            } else if (size == 0xEB) {
                file.skipBytes(0xAB);
                file.write(H_344b);
                file.skipBytes(0x2A);
                file.write(COLR_BT709_FULL);
            } else if (size == 0xEA) {
                file.skipBytes(0xAB);
                file.write(H_324b);
                file.skipBytes(0x2A);
                file.write(COLR_BT709_FULL);
            }
        } catch (IOException e) {
        }
    }
}
