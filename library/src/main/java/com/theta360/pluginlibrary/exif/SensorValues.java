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

package com.theta360.pluginlibrary.exif;

import androidx.annotation.NonNull;

/**
 * SensorValues class
 */
public class SensorValues {
    private boolean mCompassAccuracy = false;
    private float[] mAttitude = {0.0F, 0.0F, 0.0F};

    /**
     * Hold the compass accuracy value.
     *
     * @param compassAccuracy Accuracy of electronic compass<br>
     *                        true: High accuracy<br>
     *                        false: Low accuracy (It is not reliable. Required calibration.)
     */
    public void setCompassAccuracy(boolean compassAccuracy) {
        mCompassAccuracy = compassAccuracy;
    }

    /**
     * Returns the compass accuracy value that is held
     *
     * @return Accuracy of electronic compass
     */
    public boolean getCompassAccuracy() {
        return mCompassAccuracy;
    }

    /**
     * Hold the attitude values.
     *
     * @param attitude Attitude infomation of the unit. (Radian)<br>
     *                 [0]: Yaw<br>
     *                 [1]: Pitch<br>
     *                 [2]: Roll
     */
    public void setAttitudeRadian(@NonNull final float[] attitude) {
        mAttitude[0] = attitude[0];
        mAttitude[1] = attitude[1];
        mAttitude[2] = attitude[2];
    }

    /**
     * Returns the attitude values that is held
     *
     * @return Attitude infomation of the unit. (Radian)
     */
    public float[] getAttitudeRadian() {
        return mAttitude;
    }
}
