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
 * SphereType
 */
public enum SphereType {
    EQUIRECTANGULAR(1),         // Equirectangular
    DUALFISHEYE(2),             // Dual fish-eye
    DUALFISHEYE_UPRIGHT(3),;    // Dual fish-eye (THETA Z1 DNG Only)

    private final int mSphereType;

    SphereType(final int sphereType) {
        this.mSphereType = sphereType;
    }

    public static SphereType getValue(int sphereType) {
        for (SphereType iterator : SphereType.values()) {
            if (iterator.getInt() == sphereType) {
                return iterator;
            }
        }

        return null;
    }

    public int getInt() {
        return this.mSphereType;
    }
}
