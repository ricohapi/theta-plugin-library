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
 * Aperture
 */
public enum Aperture {
    APERTURE_AUTO(0, 0),
    APERTURE_2_0(2.0, -1),  // F2.0 (THETA V Only)
    APERTURE_2_1(2.1, 0),   // F2.1 (THETA Z1 Only)
    APERTURE_3_5(3.5, 1),   // F3.5 (THETA Z1 Only)
    APERTURE_5_6(5.6, 2),;   // F5.5 (THETA Z1 Only)

    private final Number mAperture;
    private final int mApertureIndex;

    Aperture(final Number aperture, final int apertureIndex) {
        this.mAperture = aperture;
        this.mApertureIndex = apertureIndex;
    }

    public static Aperture getValue(final Number aperture) {
        for (Aperture iterator : Aperture.values()) {
            if (iterator.getFloatValue() == aperture.floatValue()) {
                return iterator;
            }
        }

        return null;
    }

    public static Aperture getValueFromIndex(final int apertureIndex) {
        for (Aperture iterator : Aperture.values()) {
            if (iterator.getIndexValue() == apertureIndex) {
                return iterator;
            }
        }

        return null;
    }

    public float getFloatValue() {
        return this.mAperture.floatValue();
    }

    public Number getNumber() {
        return this.mAperture;
    }

    public int getIndexValue() {
        return this.mApertureIndex;
    }
}
