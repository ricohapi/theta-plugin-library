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
 * ExposureProgram
 */
public enum ExposureProgram {
    MANUAL(1, "RicManualExposure"),             // Manual
    NORMAL_PROGRAM(2, "RicAutoExposureP"),      // Normal / Auto
    APERTURE_PRIORITY(3, "RicAutoExposureA"),   // Aperture priority (THETA Z1 Only)
    SHUTTER_PRIORITY(4, "RicAutoExposureT"),    // Shutter speed priority
    ISO_PRIORITY(9, "RicAutoExposureS"),;       // ISO sensitivity priority

    private final Number mExposureProgram;
    private final String mExposureProgramIndex;

    ExposureProgram(final Number exposureProgram, final String exposureProgramIndex) {
        this.mExposureProgram = exposureProgram;
        this.mExposureProgramIndex = exposureProgramIndex;
    }

    public static ExposureProgram getValue(final Number exposureProgram) {
        if (exposureProgram != null && !exposureProgram.toString().contains(".")) {
            for (ExposureProgram iterator : ExposureProgram.values()) {
                if (iterator.getInt() == exposureProgram.intValue()) {
                    return iterator;
                }
            }
        }

        return null;
    }

    public static ExposureProgram getValueFromIndex(final String exposureProgramIndex) {
        if (exposureProgramIndex != null && !exposureProgramIndex.contains(".")) {
            for (ExposureProgram iterator : ExposureProgram.values()) {
                if (iterator.getIndexValue().equals(exposureProgramIndex)) {
                    return iterator;
                }
            }
        }

        return null;
    }

    public int getInt() {
        return this.mExposureProgram.intValue();
    }

    public Number getNumber() {
        return this.mExposureProgram;
    }

    public String getIndexValue() {
        return this.mExposureProgramIndex;
    }
}
