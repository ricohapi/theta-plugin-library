/**
 * Copyright 2018 Ricoh Company, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.theta360.pluginlibrary.exif.values;

/**
 * Gain
 */
public enum Gain {
    NORMAL("normal", 1, "RicMicSurroundVolumeLevelNormal"), // MicGain Normal
    MEGAVOLUME("megavolume", 2, "RicMicSurroundVolumeLevelLarge"),; // MicGain Large

    private final String mGain;
    private final int mGainExif;
    private final String mGainIndex;

    Gain(final String gain, final int gainExif, final String gainIndex) {
        this.mGain = gain;
        this.mGainExif = gainExif;
        this.mGainIndex = gainIndex;
    }

    public static Gain getValueFromIndex(final String gainIndex) {
        for (Gain gain : values()) {
            if (gain.getIndexValue() == gainIndex) {
                return gain;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.mGain;
    }

    public int getExifValue() {
        return this.mGainExif;
    }

    public String getIndexValue() {
        return this.mGainIndex;
    }
}
