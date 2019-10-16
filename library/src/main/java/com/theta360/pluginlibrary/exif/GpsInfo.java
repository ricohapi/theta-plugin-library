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

import com.theta360.pluginlibrary.exif.utils.DateTimeFormats;

/**
 * GpsInfo class
 */
public class GpsInfo {
    public static final int INVALID = 65535;
    public static final int ALT_INVALID = 0;
    public static final String DEFAULT_DATE_TIME_ZONE = "1582:01:01 00:00:00+00:00";
    public static final String DATUM = "WGS84";

    private Number mLat = INVALID;
    private Number mLng = INVALID;
    private Number mAltitude = ALT_INVALID;
    private String mDateTimeZone = "";
    private String mDatum = "";

    public boolean hasValue() {
        if (mLat.intValue() == INVALID && mLng.intValue() == INVALID) {
            this.mLat = INVALID;
            this.mLng = INVALID;
            this.mAltitude = 0;
            this.mDateTimeZone = "";
            this.mDatum = "";
        } else {
            if (mLat.intValue() == INVALID || mLng.intValue() == INVALID) {
                return false;
            }

            double lat = mLat.doubleValue();
            if (lat < -90.0 || lat > 90.0) {
                return false;
            }

            double lng = mLng.doubleValue();
            if (lng < -180.0 || lng > 180.0) {
                return false;
            }

            double altitude = mAltitude.doubleValue();
            if (altitude < -9999.0 || altitude > 9999.0) {
                return false;
            }

            if (!mDateTimeZone.isEmpty()) {
                if (!DateTimeFormats.isDateTimeZone(mDateTimeZone)) {
                    return false;
                }
            }

            if (mDatum.isEmpty() || !mDatum.equals(DATUM)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Hold the latitude value.
     *
     * @param lat Latitude (Range: -90.000000 ... 90.000000 degree)
     */
    public void setLat(Number lat) {
        this.mLat = INVALID;
        if (lat.floatValue() >= -90.000000 && lat.floatValue() <= 90.000000 ) {
            this.mLat = lat;
        }
    }

    /**
     * Returns the latitude value that is held
     *
     * @return Latitude (degree)
     */
    public Number getLat() {
        return this.mLat;
    }

    /**
     * Hold the longitude value.
     *
     * @param lng Longtitude (Range: -180.000000 ... 180.000000 degree)
     */
    public void setLng(Number lng) {
        this.mLng = INVALID;
        if (lng.floatValue() >= -180.000000 && lng.floatValue() <= 180.000000) {
            this.mLng = lng;
        }
    }

    /**
     * Returns the longitude value that is held
     *
     * @return Longtitude (degree)
     */
    public Number getLng() {
        return this.mLng;
    }

    /**
     * Hold the altitude value.
     *
     * @param altitude Altitude (Range: -9999.0 ... 9999.0 Meter)
     */
    public void setAltitude(Number altitude) {
        this.mAltitude = ALT_INVALID;
        if (altitude.floatValue() >= -9999.0 && altitude.floatValue() <= 9999.0) {
            this.mAltitude = altitude;
        }
    }

    /**
     * Returns the altitude value that is held
     *
     * @return Altitude (Meter)
     */
    public Number getAltitude() {
        return this.mAltitude;
    }

    /**
     * Hold the date, time and timezone value.
     *
     * @param dateTimeZone A date, time and timezone string of a predetermined format<br>
     *                     (Format: `"YYYY:MM:DD hh:mm:ss+(-)hh:mm"`)
     */
    public void setDateTimeZone(String dateTimeZone) {
        this.mDateTimeZone = "";
        if (!dateTimeZone.isEmpty()) {
            if (DateTimeFormats.isDateTimeZone(dateTimeZone)) {
                this.mDateTimeZone = dateTimeZone;
            }
        }
    }

    /**
     * Returns the date, time and timezone value that is held
     *
     * @return A date, time and timezone string
     */
    public String getDateTimeZone() {
        return this.mDateTimeZone;
    }

    /**
     * Hold the datum value.
     *
     * @param datum Stringof Geodetic system (eg. "WGS84")
     */
    public void setDatum(String datum) {
        this.mDatum = datum;
    }

    /**
     * Returns the datum value that is held
     *
     * @return
     */
    public String getDatum() {
        return this.mDatum;
    }
}
