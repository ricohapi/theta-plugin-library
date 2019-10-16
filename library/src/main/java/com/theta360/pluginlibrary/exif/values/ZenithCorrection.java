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

package com.theta360.pluginlibrary.exif.values;

/**
 * ZenithCorrection
 */
public enum ZenithCorrection {
    RIC_ZENITH_CORRECTION_ON_AUTO("RicZenithCorrectionOnAuto", true),
    RIC_ZENITH_CORRECTION_OFF("RicZenithCorrectionOff", false),
    RIC_ZENITH_CORRECTION_ON_SAVE("RicZenithCorrectionOnSave", true),
    RIC_ZENITH_CORRECTION_ON_LOAD("RicZenithCorrectionOnLoad", true),
    ;

    private final String mZenithIndex;
    private final boolean mIsZenith;

    ZenithCorrection(final String zenithIndex, final boolean isZenith) {
        this.mZenithIndex = zenithIndex;
        this.mIsZenith = isZenith;
    }

    public static ZenithCorrection getValueFromIndex(final String zenithIndex) {
        for (ZenithCorrection iterator : ZenithCorrection.values()) {
            if (iterator.getIndexValue().equals(zenithIndex)) {
                return iterator;
            }
        }

        return null;
    }

    public String getIndexValue() {
        return this.mZenithIndex;
    }

    public boolean isZenith() {
        return this.mIsZenith;
    }
}