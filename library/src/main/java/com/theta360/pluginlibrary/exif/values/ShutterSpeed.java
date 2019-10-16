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
 * ShutterSpeed
 */
public enum ShutterSpeed {
    SHUTTER_SPEED_AUTO(0, -1),                  // Auto
    SHUTTER_SPEED_0_00004(0.00004, 0),          // 1/25000 sec
    SHUTTER_SPEED_0_00005(0.00005, 1),
    SHUTTER_SPEED_0_0000625(0.0000625, 2),
    SHUTTER_SPEED_0_00008(0.00008, 3),
    SHUTTER_SPEED_0_0001(0.0001, 4),
    SHUTTER_SPEED_0_000125(0.000125, 5),
    SHUTTER_SPEED_0_00015625(0.00015625, 6),
    SHUTTER_SPEED_0_0002(0.0002, 7),
    SHUTTER_SPEED_0_00025(0.00025, 8),
    SHUTTER_SPEED_0_0003125(0.0003125, 9),
    SHUTTER_SPEED_00004(0.0004, 10),
    SHUTTER_SPEED_0_0005(0.0005, 11),
    SHUTTER_SPEED_0_000625(0.000625, 12),
    SHUTTER_SPEED_0_0008(0.0008, 13),
    SHUTTER_SPEED_0_001(0.001, 14),
    SHUTTER_SPEED_0_00125(0.00125, 15),
    SHUTTER_SPEED_0_0015625(0.0015625, 16),
    SHUTTER_SPEED_0_002(0.002, 17),
    SHUTTER_SPEED_0_0025(0.0025, 18),
    SHUTTER_SPEED_0_003125(0.003125, 19),
    SHUTTER_SPEED_0_004(0.004, 20),
    SHUTTER_SPEED_0_005(0.005, 21),
    SHUTTER_SPEED_0_00625(0.00625, 22),
    SHUTTER_SPEED_0_008(0.008, 23),
    SHUTTER_SPEED_0_01(0.01, 24),
    SHUTTER_SPEED_0_0125(0.0125, 25),
    SHUTTER_SPEED_0_01666666(0.01666666, 26),
    SHUTTER_SPEED_0_02(0.02, 27),
    SHUTTER_SPEED_0_025(0.025, 28),
    SHUTTER_SPEED_0_03333333(0.03333333, 29),
    SHUTTER_SPEED_0_04(0.04, 30),
    SHUTTER_SPEED_0_05(0.05, 31),
    SHUTTER_SPEED_0_06666666(0.06666666, 32),
    SHUTTER_SPEED_0_07692307(0.07692307, 33),
    SHUTTER_SPEED_0_1(0.1, 34),
    SHUTTER_SPEED_0_125(0.125, 35),             // 1/8 sec
    SHUTTER_SPEED_0_16666666(0.16666666, 36),
    SHUTTER_SPEED_0_2(0.2, 37),
    SHUTTER_SPEED_0_25(0.25, 38),
    SHUTTER_SPEED_0_33333333(0.33333333, 39),
    SHUTTER_SPEED_0_4(0.4, 40),
    SHUTTER_SPEED_0_5(0.5, 41),
    SHUTTER_SPEED_0_625(0.625, 42),
    SHUTTER_SPEED_0_76923076(0.76923076, 43),
    SHUTTER_SPEED_1(1, 44),
    SHUTTER_SPEED_1_3(1.3, 45),
    SHUTTER_SPEED_1_6(1.6, 46),
    SHUTTER_SPEED_2(2, 47),
    SHUTTER_SPEED_2_5(2.5, 48),
    SHUTTER_SPEED_3_2(3.2, 49),
    SHUTTER_SPEED_4(4, 50),
    SHUTTER_SPEED_5(5, 51),
    SHUTTER_SPEED_6(6, 52),
    SHUTTER_SPEED_8(8, 53),
    SHUTTER_SPEED_10(10, 54),
    SHUTTER_SPEED_13(13, 55),
    SHUTTER_SPEED_15(15, 56),
    SHUTTER_SPEED_20(20, 57),
    SHUTTER_SPEED_25(25, 58),
    SHUTTER_SPEED_30(30, 59),
    SHUTTER_SPEED_60(60, 62),;                  // 60 sec

    private final Number mShutterSpeed;
    private final int mShutterSpeedIndex;

    ShutterSpeed(final Number shutterSpeed, final int shutterSpeedIndex) {
        this.mShutterSpeed = shutterSpeed;
        this.mShutterSpeedIndex = shutterSpeedIndex;
    }

    public static ShutterSpeed getValue(final Number shutterSpeed) {
        if (shutterSpeed != null) {
            for (ShutterSpeed iterator : ShutterSpeed.values()) {
                if (iterator.getFloatValue() == shutterSpeed.floatValue()) {
                    return iterator;
                }
            }
        }

        return null;
    }

    public static ShutterSpeed getValueFromIndex(final int shutterSpeedIndex) {
        for (ShutterSpeed iterator : ShutterSpeed.values()) {
            if (iterator.getIndexValue() == shutterSpeedIndex) {
                return iterator;
            }
        }

        return null;
    }

    public int getIntValue() {
        return this.mShutterSpeed.intValue();
    }

    public float getFloatValue() {
        return this.mShutterSpeed.floatValue();
    }

    public Number getNumber() {
        return this.mShutterSpeed;
    }

    public int getIndexValue() {
        return this.mShutterSpeedIndex;
    }
}
