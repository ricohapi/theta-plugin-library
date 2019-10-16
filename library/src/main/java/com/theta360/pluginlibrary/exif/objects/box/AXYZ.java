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

package com.theta360.pluginlibrary.exif.objects.box;

import com.theta360.pluginlibrary.exif.GpsInfo;
import com.theta360.pluginlibrary.exif.MovieSettings;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

public class AXYZ {
    public byte[] getData() {
        byte[] bAxyz = null;
        GpsInfo gpsInfo = MovieSettings.getGpsInfo();
        if (gpsInfo != null && gpsInfo.hasValue()) {
            if (gpsInfo.getLat().intValue() != GpsInfo.INVALID &&
                    gpsInfo.getLng().intValue() != GpsInfo.INVALID &&
                    gpsInfo.getAltitude().intValue() != GpsInfo.ALT_INVALID) {
                float gpslat = gpsInfo.getLat().floatValue();
                float gpslng = gpsInfo.getLng().floatValue();
                float gpsalt = gpsInfo.getAltitude().floatValue();

                StringBuffer sbGPS = new StringBuffer();
                DecimalFormat df = new DecimalFormat("#.#");
                if (gpslat > 0) {
                    sbGPS.append("+");
                    df.setMinimumFractionDigits(4);
                    df.setMaximumFractionDigits(4);
                    df.setMinimumIntegerDigits(2);
                    df.setMaximumIntegerDigits(2);
                    BigDecimal bd = new BigDecimal(gpslat);
                    sbGPS.append(df.format(bd));
                } else {
                    sbGPS.append("-");
                    df.setMinimumFractionDigits(4);
                    df.setMaximumFractionDigits(4);
                    df.setMinimumIntegerDigits(2);
                    df.setMaximumIntegerDigits(2);
                    BigDecimal bd = new BigDecimal(gpslat * (-1));
                    sbGPS.append(df.format(bd));
                }
                if (gpslng > 0) {
                    sbGPS.append("+");
                    df.setMinimumFractionDigits(4);
                    df.setMaximumFractionDigits(4);
                    df.setMinimumIntegerDigits(3);
                    df.setMaximumIntegerDigits(3);
                    BigDecimal bd = new BigDecimal(gpslng);
                    sbGPS.append(df.format(bd));
                } else {
                    sbGPS.append("-");
                    df.setMinimumFractionDigits(4);
                    df.setMaximumFractionDigits(4);
                    df.setMinimumIntegerDigits(3);
                    df.setMaximumIntegerDigits(3);
                    BigDecimal bd = new BigDecimal(gpslng * (-1));
                    sbGPS.append(df.format(bd));
                }
                if (gpsalt > 0) {
                    sbGPS.append("+");
                    sbGPS.append(String.format(Locale.US, "%d", (int) gpsalt));
                } else {
                    sbGPS.append("-");
                    sbGPS.append(String.format(Locale.US, "%d", (int) gpsalt * (-1)));
                }
                sbGPS.append("CRSWGS_84/");

                bAxyz = sbGPS.toString().getBytes();
            }
        }
        return bAxyz;
    }
}
