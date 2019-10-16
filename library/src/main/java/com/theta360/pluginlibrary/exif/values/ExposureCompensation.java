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
 * ExposureCompensation
 */
public enum ExposureCompensation {
    EXPOSURE_COMPENSATION_DOWN_2(-2.0, -6),     // -2.0
    EXPOSURE_COMPENSATION_DOWN_1_7(-1.7, -5),   // -1.7
    EXPOSURE_COMPENSATION_DOWN_1_3(-1.3, -4),   // -1.3
    EXPOSURE_COMPENSATION_DOWN_1(-1.0, -3),     // -1.0
    EXPOSURE_COMPENSATION_DOWN_0_7(-0.7, -2),   // -0.7
    EXPOSURE_COMPENSATION_DOWN_0_3(-0.3, -1),   // -0.3
    EXPOSURE_COMPENSATION_0(0.0, 0),            // 0.0
    EXPOSURE_COMPENSATION_UP_0_3(0.3, 1),       // +0.3
    EXPOSURE_COMPENSATION_UP_0_7(0.7, 2),       // +0.7
    EXPOSURE_COMPENSATION_UP_1(1.0, 3),         // +1.0
    EXPOSURE_COMPENSATION_UP_1_3(1.3, 4),       // +1.3
    EXPOSURE_COMPENSATION_UP_1_7(1.7, 5),       // +1.7
    EXPOSURE_COMPENSATION_UP_2(2.0, 6),;        // +2.0

    private final Number mExposureCompensation;
    private final int mExposureCompensationIndex;

    ExposureCompensation(final Number exposureCompensation, final int exposureCompensationIndex) {
        this.mExposureCompensation = exposureCompensation;
        this.mExposureCompensationIndex = exposureCompensationIndex;
    }

    public static ExposureCompensation getValue(final Number exposureCompensation) {
        if (exposureCompensation != null) {
            for (ExposureCompensation iterator : ExposureCompensation.values()) {
                if (iterator.getFloatValue() == exposureCompensation.floatValue()) {
                    return iterator;
                }
            }
        }

        return null;
    }

    public static ExposureCompensation getValueFromIndex(final int exposureCompensationIndex) {
        for (ExposureCompensation iterator : ExposureCompensation.values()) {
            if (iterator.getIndexValue() == exposureCompensationIndex) {
                return iterator;
            }
        }

        return null;
    }

    public float getFloatValue() {
        return this.mExposureCompensation.floatValue();
    }

    public Number getNumber() {
        return this.mExposureCompensation;
    }

    public int getIndexValue() {
        return this.mExposureCompensationIndex;
    }
}
