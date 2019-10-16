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

/**
 * BoxSpec
 */
public class BoxSpec {
    private BoxType mBoxType;
    private long mOffset = 0L;

    private long mBoxSize = 0;
    private long lBoxSize = 0L;

    public BoxSpec(BoxType boxType) {
        mBoxType = boxType;
    }

    public BoxType getType() {
        return mBoxType;
    }

    public long getOffset() {
        return mOffset;
    }

    public void setOffset(long offset) {
        mOffset = offset;
    }

    public long getBoxSize() {
        return mBoxSize;
    }

    public void setBoxSize(int boxSize) {
        mBoxSize = boxSize;
    }

    public long getlBoxSize() {
        return lBoxSize;
    }

    public void setlBoxSize(long lBoxSize) {
        this.lBoxSize = lBoxSize;
    }
}
