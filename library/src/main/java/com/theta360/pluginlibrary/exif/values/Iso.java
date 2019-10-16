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
 * Iso
 */
public enum Iso {
    ISO_AUTO(0, -1),        // Auto
    ISO_64(64, 1),          // ISO64
    ISO_80(80, 2),          // ISO80
    ISO_100(100, 3),        // ISO100
    ISO_125(125, 4),        // ISO125
    ISO_160(160, 5),        // ISO160
    ISO_200(200, 6),        // ISO200
    ISO_250(250, 7),        // ISO250
    ISO_320(320, 8),        // ISO320
    ISO_400(400, 9),        // ISO400
    ISO_500(500, 10),       // ISO500
    ISO_640(640, 11),       // ISO640
    ISO_800(800, 12),       // ISO800
    ISO_1000(1000, 13),     // ISO1000
    ISO_1250(1250, 14),     // ISO1250
    ISO_1600(1600, 15),     // ISO1600
    ISO_2000(2000, 16),     // ISO2000
    ISO_2500(2500, 17),     // ISO2500
    ISO_3200(3200, 18),     // ISO3200
    ISO_4000(4000, 19),     // ISO4000 (THETA Z1 Only)
    ISO_5000(5000, 20),     // ISO5000 (THETA Z1 Only)
    ISO_6400(6400, 21),;    // ISO6400 (THETA Z1 Only)

    private final Number mIso;
    private final int mIsoIndex;

    Iso(final Number iso, final int isoIndex) {
        this.mIso = iso;
        this.mIsoIndex = isoIndex;
    }

    public static Iso getValue(final Number iso) {
        if (iso != null && !iso.toString().contains(".")) {
            for (Iso iterator : Iso.values()) {
                if (iterator.getInt() == iso.intValue()) {
                    return iterator;
                }
            }
        }

        return null;
    }

    public static Iso getValueFromIndex(final int isoIndex) {
        for (Iso iterator : Iso.values()) {
            if (iterator.getIndexValue() == isoIndex) {
                return iterator;
            }
        }

        return null;
    }

    public int getInt() {
        return this.mIso.intValue();
    }

    public Number getNumber() {
        return this.mIso;
    }

    public int getIndexValue() {
        return this.mIsoIndex;
    }
}
