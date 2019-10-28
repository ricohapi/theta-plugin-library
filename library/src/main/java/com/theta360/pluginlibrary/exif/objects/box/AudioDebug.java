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

import com.theta360.pluginlibrary.exif.CameraSettings;
import com.theta360.pluginlibrary.values.ThetaModel;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * AudioDebug
 */
public class AudioDebug {
    public static final int AUDIO_DEBUG_SIZE = 4;

    private static final String AUDIO_PATH_Z1 = "/temp/debug/audio_debug.bin";
    private static final String AUDIO_PATH_V = "/param/debug/audio_debug.bin";

    private RandomAccessFile mAudioFile = null;

    public AudioDebug() throws IOException {
        if (CameraSettings.getThetaModel() == ThetaModel.THETA_Z1) {
            File fAudio = new File(AUDIO_PATH_Z1);
            mAudioFile = new RandomAccessFile(fAudio, "r");
        } else if (CameraSettings.getThetaModel() == ThetaModel.THETA_V) {
            File fAudio = new File(AUDIO_PATH_V);
            mAudioFile = new RandomAccessFile(fAudio, "r");
        } else {
            chkAudioFile();
        }
    }

    public short getMicSelect() throws IOException {
        if (mAudioFile != null) {
            return readLittle2Short(0L, mAudioFile);
        }
        return (short) 0;
    }

    public short getAudioLevel() throws IOException {
        if (mAudioFile != null) {
            return readLittle2Short(2L, mAudioFile);
        }
        return (short) -1;
    }

    public void close() throws IOException {
        mAudioFile.close();
    }

    private short readLittle2Short(long offset, RandomAccessFile raf) throws IOException {
        mAudioFile.seek(offset);

        byte b0 = raf.readByte();
        byte b1 = raf.readByte();

        byte[] bValue = {b1, b0};

        short sValue = ByteBuffer.wrap(bValue).getShort();

        return sValue;
    }

    private void chkAudioFile() throws IOException {
        File zFile = new File(AUDIO_PATH_Z1);
        File vFile = new File(AUDIO_PATH_V);
        if (zFile.exists()) {
            mAudioFile = new RandomAccessFile(new File(AUDIO_PATH_Z1), "r");
        } else if (vFile.exists()) {
            mAudioFile = new RandomAccessFile(new File(AUDIO_PATH_V), "r");
        }
    }
}
