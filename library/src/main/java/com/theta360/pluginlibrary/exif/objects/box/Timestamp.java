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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Timestamp
 */
public class Timestamp {
    private static final int BOX_SIZE_LEN = 4;
    private static final int BOX_TYPE_LEN = 4;
    private static final int BOX_HEADER_LEN = BOX_SIZE_LEN + BOX_TYPE_LEN;
    private RandomAccessFile mRandomAccessFile;

    private long mPosition = 0;
    private long mCreation = 0;
    private long mModification = 0;

    public void init(RandomAccessFile file, long pos) throws IOException {
        mRandomAccessFile = file;
        mPosition = pos;
        mRandomAccessFile.seek(pos + BOX_HEADER_LEN + 4);
        long creationTime = mRandomAccessFile.readInt() & 0x00000000ffffffffL;
        mCreation = creationTime;
        long modificationTime = mRandomAccessFile.readInt() & 0x00000000ffffffffL;
        mModification = modificationTime;
        mRandomAccessFile.seek(pos);
    }

    public void shift(String sTimezone) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("Z", Locale.US);
        long timezone = 0;
        try {
            timezone = sdf.parse(sTimezone).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mPosition == 0L) {
            return;
        }
        mRandomAccessFile.seek(mPosition + BOX_HEADER_LEN + 4);
        mRandomAccessFile.writeInt((int) (mCreation + timezone));
        mRandomAccessFile.writeInt((int) (mModification + timezone));
    }
}
