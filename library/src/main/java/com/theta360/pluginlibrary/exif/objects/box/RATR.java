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

import com.theta360.pluginlibrary.exif.values.box.BoxType;
import java.nio.ByteBuffer;

/**
 * RATR
 */
public class RATR {
    public static final int STSZ_SAMPLE_SIZE = 8;
    public static final int STSZ_SAMPLE_PER_CHUNK = 0x600;

    private static final int BOX_HEADER_LEN = 8;
    private static final int MDHD_TIMESCALE = 48000;

    private final byte[] bTkhd = {
            0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x22, (byte) 0x84, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x01, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x40, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    private final byte[] bEdts = {
            0x00, 0x00, 0x00, 0x1C, 0x65, 0x6C, 0x73, 0x74,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x22, (byte) 0x84, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00};

    private final byte[] bMdiaHdlr = {
            0x00, 0x00, 0x00, 0x00, 0x6D, 0x68, 0x6C, 0x72,
            0x73, 0x6F, 0x75, 0x6E, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x0C, 0x53, 0x6F, 0x75, 0x6E, 0x64, 0x48, 0x61, 0x6E, 0x64, 0x6C, 0x65,
            0x72};

    private final byte[] bSmhd = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
    };

    private final byte[] bMinfHdlr = {
            0x00, 0x00, 0x00, 0x00, 0x64, 0x68, 0x6C, 0x72,
            0x75, 0x72, 0x6C, 0x20, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x0B, 0x44, 0x61, 0x74, 0x61, 0x48, 0x61, 0x6E, 0x64, 0x6C, 0x65, 0x72};
    private final byte[] bDinf = {
            0x00, 0x00, 0x00, 0x1C, 0x64, 0x72, 0x65, 0x66,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x0C, 0x75, 0x72,
            0x6C, 0x20, 0x00, 0x00, 0x00, 0x01};

    private final byte[] bStsd = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
            0x00, 0x00, 0x00, 0x60, 0x73, 0x6F, 0x77, 0x74, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x10,
            0x00, 0x00, 0x00, 0x00, (byte) 0xBB, (byte) 0x80, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18,
            0x63, 0x68, 0x61, 0x6E, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x07, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x24, 0x53, 0x41, 0x33, 0x44,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x01};

    private byte[] bMdhd = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xBB, (byte) 0x80, 0x00, 0x06, 0x78,
            (byte) 0xA1, 0x00, 0x00, 0x00, 0x00};

    private byte[] bStts = {
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
            0x00, 0x06, 0x78, (byte) 0xA1, 0x00, 0x00, 0x00, 0x01};

    private byte[] bStsc = {
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x02,
            0x00, 0x00, 0x00, 0x01,
            0x00, 0x00, 0x06, 0x00,
            0x00, 0x00, 0x00, 0x01,
            0x00, 0x00, 0x00, 0x01,
            0x00, 0x00, 0x06, 0x00,
            0x00, 0x00, 0x00, 0x01,
    };

    private byte[] bStsz = {
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x08,
            0x00, 0x06, 0x78, (byte) 0xA1
    };

    private int mWavDataSize = 0;

    private int mRATRsize = 0;
    private int mMdiaSize = 0;
    private int mMinfSize = 0;
    private int mStblSize = 0;
    private int mCo64Size = 0;

    public RATR(int wavDataSize) {
        mWavDataSize = wavDataSize;
    }

    /**
     * Calculate the size of the RATR box
     */
    public int size() {
        int iRATRsize = 0;

        iRATRsize += BOX_HEADER_LEN;
        iRATRsize += bTkhd.length + BOX_HEADER_LEN;
        iRATRsize += bEdts.length + BOX_HEADER_LEN;
        iRATRsize += BOX_HEADER_LEN;
        iRATRsize += bMdhd.length + BOX_HEADER_LEN;
        iRATRsize += bMdiaHdlr.length + BOX_HEADER_LEN;
        iRATRsize += BOX_HEADER_LEN;
        iRATRsize += bSmhd.length + BOX_HEADER_LEN;
        iRATRsize += bMinfHdlr.length + BOX_HEADER_LEN;
        iRATRsize += bDinf.length + BOX_HEADER_LEN;
        iRATRsize += BOX_HEADER_LEN;
        iRATRsize += bStsd.length + BOX_HEADER_LEN;
        iRATRsize += bStts.length + BOX_HEADER_LEN;
        iRATRsize += bStsc.length + BOX_HEADER_LEN;
        iRATRsize += bStsz.length + BOX_HEADER_LEN;

        int co64count = (mWavDataSize / (STSZ_SAMPLE_SIZE * STSZ_SAMPLE_PER_CHUNK)) + 1;
        mCo64Size = BOX_HEADER_LEN + 8 + (8 * co64count);

        iRATRsize += mCo64Size;

        mStblSize = BOX_HEADER_LEN +
                bStsd.length + BOX_HEADER_LEN +
                bStts.length + BOX_HEADER_LEN +
                bStsc.length + BOX_HEADER_LEN +
                bStsz.length + BOX_HEADER_LEN +
                mCo64Size;

        mMinfSize = BOX_HEADER_LEN +
                bSmhd.length + BOX_HEADER_LEN +
                bMinfHdlr.length + BOX_HEADER_LEN +
                bDinf.length + BOX_HEADER_LEN +
                mStblSize;

        mMdiaSize = BOX_HEADER_LEN +
                bMdhd.length + BOX_HEADER_LEN +
                bMdiaHdlr.length + BOX_HEADER_LEN +
                mMinfSize;

        mRATRsize = BOX_HEADER_LEN +
                bTkhd.length + BOX_HEADER_LEN +
                bEdts.length + BOX_HEADER_LEN +
                mMdiaSize;

        return iRATRsize;
    }

    /**
     * Generate RATR metadata
     */
    public byte[] getData(long RADToffset) {
        int pos = 0;
        int iRATRsize = this.size();
        byte[] bRadt = new byte[iRATRsize - BOX_HEADER_LEN];

        System.arraycopy(ByteBuffer.allocate(4).putInt(bTkhd.length + BOX_HEADER_LEN).array(), 0,
                bRadt, pos, 4);
        System.arraycopy(BoxType.RATRtkhd.getValue(), 0, bRadt, pos + 4, 4);
        System.arraycopy(bTkhd, 0, bRadt, pos + 8, bTkhd.length);
        pos += (bTkhd.length + BOX_HEADER_LEN);

        System.arraycopy(ByteBuffer.allocate(4).putInt(bEdts.length + BOX_HEADER_LEN).array(), 0,
                bRadt, pos, 4);
        System.arraycopy(BoxType.RATRedts.getValue(), 0, bRadt, pos + 4, 4);
        System.arraycopy(bEdts, 0, bRadt, pos + 8, bEdts.length);
        pos += (bEdts.length + BOX_HEADER_LEN);

        System.arraycopy(ByteBuffer.allocate(4).putInt(mMdiaSize).array(), 0, bRadt, pos,
                4);
        System.arraycopy(BoxType.RATRmdia.getValue(), 0, bRadt, pos + 4, 4);
        pos += BOX_HEADER_LEN;

        int duration_sec = mWavDataSize / (8 * MDHD_TIMESCALE);
        int duration_sample = mWavDataSize / 8;
        System.arraycopy(ByteBuffer.allocate(4).putInt(duration_sample).array(), 0, bMdhd, 16, 4);
        System.arraycopy(ByteBuffer.allocate(4).putInt(bMdhd.length + BOX_HEADER_LEN).array(), 0,
                bRadt, pos, 4);
        System.arraycopy(BoxType.RATRmdhd.getValue(), 0, bRadt, pos + 4, 4);
        System.arraycopy(bMdhd, 0, bRadt, pos + 8, bMdhd.length);
        pos += (bMdhd.length + BOX_HEADER_LEN);

        System.arraycopy(ByteBuffer.allocate(4).putInt(bMdiaHdlr.length + BOX_HEADER_LEN).array(),
                0, bRadt, pos, 4);
        System.arraycopy(BoxType.RATRhdlr.getValue(), 0, bRadt, pos + 4, 4);
        System.arraycopy(bMdiaHdlr, 0, bRadt, pos + 8, bMdiaHdlr.length);
        pos += (bMdiaHdlr.length + BOX_HEADER_LEN);

        System.arraycopy(ByteBuffer.allocate(4).putInt(mMinfSize).array(), 0, bRadt, pos,
                4);
        System.arraycopy(BoxType.RATRminf.getValue(), 0, bRadt, pos + 4, 4);
        pos += BOX_HEADER_LEN;

        System.arraycopy(ByteBuffer.allocate(4).putInt(bSmhd.length + BOX_HEADER_LEN).array(), 0,
                bRadt, pos, 4);
        System.arraycopy(BoxType.RATRsmhd.getValue(), 0, bRadt, pos + 4, 4);
        System.arraycopy(bSmhd, 0, bRadt, pos + 8, bSmhd.length);
        pos += (bSmhd.length + BOX_HEADER_LEN);

        System.arraycopy(ByteBuffer.allocate(4).putInt(bMinfHdlr.length + BOX_HEADER_LEN).array(),
                0, bRadt, pos, 4);
        System.arraycopy(BoxType.RATRminfhdlr.getValue(), 0, bRadt, pos + 4, 4);
        System.arraycopy(bMinfHdlr, 0, bRadt, pos + 8, bMinfHdlr.length);
        pos += (bMinfHdlr.length + BOX_HEADER_LEN);

        System.arraycopy(ByteBuffer.allocate(4).putInt(bDinf.length + BOX_HEADER_LEN).array(), 0,
                bRadt, pos, 4);
        System.arraycopy(BoxType.RATRdinf.getValue(), 0, bRadt, pos + 4, 4);
        System.arraycopy(bDinf, 0, bRadt, pos + 8, bDinf.length);
        pos += (bDinf.length + BOX_HEADER_LEN);

        System.arraycopy(ByteBuffer.allocate(4).putInt(mStblSize).array(), 0, bRadt, pos,
                4);
        System.arraycopy(BoxType.RATRstbl.getValue(), 0, bRadt, pos + 4, 4);
        pos += BOX_HEADER_LEN;

        System.arraycopy(ByteBuffer.allocate(4).putInt(bStsd.length + BOX_HEADER_LEN).array(), 0,
                bRadt, pos, 4);
        System.arraycopy(BoxType.RATRstsd.getValue(), 0, bRadt, pos + 4, 4);
        System.arraycopy(bStsd, 0, bRadt, pos + 8, bStsd.length);
        pos += (bStsd.length + BOX_HEADER_LEN);

        System.arraycopy(ByteBuffer.allocate(4).putInt(bStts.length + BOX_HEADER_LEN).array(), 0,
                bRadt, pos, 4);
        System.arraycopy(BoxType.RATRstts.getValue(), 0, bRadt, pos + 4, 4);
        int stts_sample_count = mWavDataSize / 8;
        System.arraycopy(ByteBuffer.allocate(4).putInt(stts_sample_count).array(), 0, bStts, 8, 4);
        System.arraycopy(bStts, 0, bRadt, pos + 8, bStts.length);
        pos += (bStts.length + BOX_HEADER_LEN);

        int stsc_first_chunk =
                (mWavDataSize / (STSZ_SAMPLE_SIZE * STSZ_SAMPLE_PER_CHUNK)) + 1;
        System.arraycopy(ByteBuffer.allocate(4).putInt(stsc_first_chunk).array(), 0, bStsc, 0x14,
                4);
        int stsc_sample_per_chunk = (mWavDataSize / STSZ_SAMPLE_SIZE) % STSZ_SAMPLE_PER_CHUNK;
        System.arraycopy(ByteBuffer.allocate(4).putInt(stsc_sample_per_chunk).array(), 0, bStsc,
                0x18, 4);

        System.arraycopy(ByteBuffer.allocate(4).putInt(bStsc.length + BOX_HEADER_LEN).array(), 0,
                bRadt, pos, 4);
        System.arraycopy(BoxType.RATRstsc.getValue(), 0, bRadt, pos + 4, 4);
        System.arraycopy(bStsc, 0, bRadt, pos + 8, bStsc.length);
        pos += (bStsc.length + BOX_HEADER_LEN);

        int stsz_sample_count = mWavDataSize / STSZ_SAMPLE_SIZE;
        System.arraycopy(ByteBuffer.allocate(4).putInt(stsz_sample_count).array(), 0, bStsz, 0x08,
                4);

        System.arraycopy(ByteBuffer.allocate(4).putInt(bStsz.length + BOX_HEADER_LEN).array(), 0,
                bRadt, pos, 4);
        System.arraycopy(BoxType.RATRstsz.getValue(), 0, bRadt, pos + 4, 4);
        System.arraycopy(bStsz, 0, bRadt, pos + 8, bStsz.length);
        pos += (bStsz.length + BOX_HEADER_LEN);

        System.arraycopy(ByteBuffer.allocate(4).putInt(mCo64Size).array(), 0, bRadt, pos,
                4);
        System.arraycopy(BoxType.RATRco64.getValue(), 0, bRadt, pos + 4, 4);
        System.arraycopy(ByteBuffer.allocate(4).putInt(0x00).array(), 0, bRadt, pos + 8,
                4);

        int co64count = (mWavDataSize / (STSZ_SAMPLE_SIZE * STSZ_SAMPLE_PER_CHUNK)) + 1;

        System.arraycopy(ByteBuffer.allocate(4).putInt(co64count).array(), 0, bRadt, pos + 12, 4);
        pos += 16;
        for (int i = 0; i < co64count; i++) {
            long chunkOffset = RADToffset + BOX_HEADER_LEN + (long) (i * STSZ_SAMPLE_SIZE
                    * STSZ_SAMPLE_PER_CHUNK);
            System.arraycopy(ByteBuffer.allocate(8).putLong(chunkOffset).array(), 0, bRadt,
                    pos + (i * 8), 8);
        }

        return bRadt;
    }
}
