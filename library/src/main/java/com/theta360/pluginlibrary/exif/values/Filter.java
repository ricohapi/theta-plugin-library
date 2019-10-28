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
 * Filter
 */
public enum Filter {
    OFF("RicStillCaptureStd"),                          // Filter OFF
    NOISE_REDUCTION("RicStillCaptureMultiRawNR"),   // Noise reduction
    HDR("RicStillCaptureMultiYuvHdr"),                  // HDR
    DR_COMP("RicStillCaptureWDR"),                     // DR Composite
    HH_HDR("RicStillCaptureMultiYuvHhHdr");            // Hh HDR

    private final String mFilterIndex;

    Filter(final String filterIndex) {
        this.mFilterIndex = filterIndex;
    }

    public static Filter getValue(final String filter) {
        for (Filter iterator : Filter.values()) {
            if (iterator.toString().equals(filter)) {
                return iterator;
            }
        }

        return null;
    }

    public static Filter getValueFromIndex(final String filterIndex) {
        for (Filter iterator : Filter.values()) {
            if (iterator.getIndexValue().equals(filterIndex)) {
                return iterator;
            }
        }

        return null;
    }

    public String getIndexValue() {
        return this.mFilterIndex;
    }
}
