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
 * WhiteBalance
 */
public enum WhiteBalance {
    AUTO("RicWbAuto", (byte) 0, (byte) 0), // AUTO (Unknown）
    DAYLIGHT("RicWbPrefixDaylight", (byte) 9, (byte) 2), // Daylight
    SHADE("RicWbPrefixShade", (byte) 11, (byte) 12), // Shade
    CLOUDY_DAYLIGHT("RicWbPrefixCloudyDaylight", (byte) 10, (byte) 3), // Cloudy daylight
    INCANDESCENT("RicWbPrefixIncandescent", (byte) 3, (byte) 4), // Incandescent (tungsten)
    WARM_WHITE_FLUORESCENT("RicWbPrefixFluorescentWW", (byte) 15, (byte) 5), // Incandescent (Warm white fluorescent)
    DAY_LIGHT_FLUORESCENT("RicWbPrefixFluorescentD", (byte) 12, (byte) 6), // Fluorescent (Daylight）
    DAY_WHITE_FLUORESCENT("RicWbPrefixFluorescentN", (byte) 13, (byte) 7), // Fluorescent (Daylight white fluorescent)
    FLUORESCENT("RicWbPrefixFluorescentW", (byte) 14, (byte) 8), // Fluorescent (White fluorescent）
    BULB_FLUORESCENT("RicWbPrefixFluorescentL", (byte) 16, (byte) 9), // Fluorescent (Light bulb color fluorescent）
    COLOR_TEMPERATURE("RicWbPrefixTemperature", (byte) 0, (byte) 11),; // Color temperature specification

    private final String mWhiteBalanceIndex;
    private final byte mWhiteBalanceExif;  // Exif: Lightsource
    private final byte mWhiteBalanceMaker;

    WhiteBalance(final String whiteBalanceIndex, final byte whiteBalanceExif, final byte whiteBalanceMaker) {
        this.mWhiteBalanceIndex = whiteBalanceIndex;
        this.mWhiteBalanceExif = whiteBalanceExif;
        this.mWhiteBalanceMaker = whiteBalanceMaker;
    }

    public static WhiteBalance getValue(final String whiteBalance) {
        for (WhiteBalance iterator : WhiteBalance.values()) {
            if (iterator.toString().equals(whiteBalance)) {
                return iterator;
            }
        }

        return null;
    }

    public static WhiteBalance getValueFromIndex(final String whiteBalanceIndex) {
        for (WhiteBalance iterator : WhiteBalance.values()) {
            if (iterator.getIndexValue().equals(whiteBalanceIndex)) {
                return iterator;
            }
        }

        return null;
    }

    public String getIndexValue() {
        return this.mWhiteBalanceIndex;
    }

    public byte getExifValue() {
        return this.mWhiteBalanceExif;
    }

    public byte getMakerValue() {
        return this.mWhiteBalanceMaker;
    }
}
