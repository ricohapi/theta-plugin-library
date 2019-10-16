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
 * ColorTemperature
 */
public enum ColorTemperature {
    DEFAULT(5000),
    MIN(2500),
    MAX(10000),
    STEP_SIZE(100),;

    private final Number mColorTemperature;

    ColorTemperature(final Number colorTemperature) {
        this.mColorTemperature = colorTemperature;
    }

    public int getInt() {
        return this.mColorTemperature.intValue();
    }

    public Number getNumber() {
        return this.mColorTemperature;
    }
}
