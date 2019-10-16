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

package com.theta360.pluginlibrary.exif.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Buffer
 */
public class Buffer {
    public static final String CHARSET = "UTF-8";

    private byte[] mBuffer;
    private int mCursor;
    private Endian mEndian = Endian.BIG;

    public Buffer(byte[] buffer) {
        this.mBuffer = buffer;
        mCursor = 0;
    }

    public byte[] getByte() {
        return this.mBuffer;
    }

    public void setEndian(Endian endian) {
        this.mEndian = endian;
    }

    public Endian getEndian() {
        return this.mEndian;
    }

    public int getCursor() {
        return mCursor;
    }

    public void seek(int pos) {
        mCursor = pos;
    }

    public void skip(long n) {
        mCursor += n;
    }

    public void put(byte[] p) {
        System.arraycopy(p, 0, mBuffer, mCursor, p.length);
        mCursor += p.length;
    }

    public void put(String str) {
        try {
            byte[] b = str.getBytes(CHARSET);
            put(b);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void put16(int value) {
        if (mEndian == Endian.BIG) {
            mBuffer[mCursor] = (byte) ((value & 0xff00) >> 8);
            mBuffer[mCursor + 1] = (byte) ((value & 0x00ff));
        } else {
            mBuffer[mCursor] = (byte) ((value & 0x00ff));
            mBuffer[mCursor + 1] = (byte) ((value & 0xff00) >> 8);
        }
        mCursor += 2;
    }

    public void put32(int value) {
        if (mEndian == Endian.BIG) {
            mBuffer[mCursor] = (byte) ((value & 0xff000000) >> 24);
            mBuffer[mCursor + 1] = (byte) ((value & 0x00ff0000) >> 16);
            mBuffer[mCursor + 2] = (byte) ((value & 0x0000ff00) >> 8);
            mBuffer[mCursor + 3] = (byte) ((value & 0x000000ff));
        } else {
            mBuffer[mCursor] = (byte) ((value & 0x000000ff));
            mBuffer[mCursor + 1] = (byte) ((value & 0x0000ff00) >> 8);
            mBuffer[mCursor + 2] = (byte) ((value & 0x00ff0000) >> 16);
            mBuffer[mCursor + 3] = (byte) ((value & 0xff000000) >> 24);
        }
        mCursor += 4;
    }

    public void put(byte value) {
        mBuffer[mCursor] = value;
        mCursor += 1;
    }

    public void insert(byte[] p) {
        System.arraycopy(mBuffer, mCursor, mBuffer, mCursor + p.length,
                mBuffer.length - (mCursor + p.length));
        put(p);
    }

    public void replace(int removeLen, byte[] p) {
        byte[] src = mBuffer;
        mBuffer = new byte[src.length - removeLen + p.length];
        System.arraycopy(src, 0, mBuffer, 0, mCursor);
        System.arraycopy(src, mCursor + removeLen, mBuffer, mCursor + p.length,
                src.length - mCursor - removeLen);
        put(p);
    }

    public void remove(int removeLen, int shiftLen) {
        System.arraycopy(mBuffer, mCursor + removeLen, mBuffer, mCursor, shiftLen);
        System.arraycopy(new byte[removeLen], 0, mBuffer, mCursor + shiftLen, removeLen);
    }

    public boolean verifyExifMarker() {
        if (mBuffer[mCursor] != (byte) 0xff) {
            return false;
        }
        if ((mBuffer[mCursor + 1] & (byte) 0xe0) == (byte) 0xe0) {
            mCursor += 2;
            return true;
        }
        mCursor += 2;
        return true;
    }

    public boolean verify(byte[] p) {
        byte[] b = Arrays.copyOfRange(mBuffer, mCursor, mCursor + p.length);
        if (Arrays.equals(b, p)) {
            mCursor += p.length;
            return true;
        } else {
            return false;
        }
    }

    public boolean verify(String p) {
        try {
            byte[] pb = p.getBytes(CHARSET);
            return verify(pb);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int get16() {
        int value = 0;
        byte[] b = Arrays.copyOfRange(mBuffer, mCursor, mCursor + 2);
        value = (mEndian == Endian.BIG)
                ? (((b[0] & 0xff) << 8) | (b[1] & 0xff))
                : (((b[1] & 0xff) << 8) | (b[0] & 0xff));
        mCursor += b.length;
        return value;
    }

    public int get32() {
        int value = 0;
        byte[] b = Arrays.copyOfRange(mBuffer, mCursor, mCursor + 4);
        value = (mEndian == Endian.BIG)
                ? (((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16) | ((b[2] & 0xff) << 8) | (b[3]
                & 0xff))
                : (((b[3] & 0xff) << 24) | ((b[2] & 0xff) << 16) | ((b[1] & 0xff) << 8) | (b[0]
                & 0xff));
        mCursor += b.length;
        return value;
    }

    public byte[] getN(int len) {
        byte[] value = Arrays.copyOfRange(mBuffer, mCursor, mCursor + len);
        mCursor += len;
        return value;
    }

    public int length() {
        return mBuffer.length;
    }

    public enum Endian {
        BIG,
        LITTLE,
        UNDEF,
    }
}
