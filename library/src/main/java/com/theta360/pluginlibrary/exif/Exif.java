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

import com.theta360.pluginlibrary.exif.objects.box.AudioDebug;
import com.theta360.pluginlibrary.exif.objects.exif.ImageQualityDebug;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

import org.joda.time.DateTime;

import com.theta360.pluginlibrary.exif.utils.Buffer;
import com.theta360.pluginlibrary.exif.utils.DateTimeFormats;
import com.theta360.pluginlibrary.exif.values.ExposureProgram;
import com.theta360.pluginlibrary.exif.values.Filter;
import com.theta360.pluginlibrary.exif.values.Gain;
import com.theta360.pluginlibrary.exif.values.SphereType;
import com.theta360.pluginlibrary.exif.values.WhiteBalance;
import com.theta360.pluginlibrary.exif.values.exif.ExifType;
import com.theta360.pluginlibrary.exif.values.exif.IFD;
import com.theta360.pluginlibrary.exif.values.exif.Tag;

/**
 * Exif class
 */
public class Exif {
    private static final int SENSOR_COEFFICIENT = 100;
    private static final String MAKER_ID = "Ricoh\0\0\0";
    private static final int MAKER_ID_LEN = 8;
    private static final int TAG_ID_LEN = 2;
    private static final int TAG_TYPE_LEN = 2;
    private static final int TAG_NUM_LEN = 4;
    private static final int TAG_VALUE_LEN = 4;
    private static final int TAG_LEN = TAG_ID_LEN + TAG_TYPE_LEN + TAG_NUM_LEN + TAG_VALUE_LEN;

    private static final int MARKER_LEN = 2;
    private static final byte[] SOI_MARKER = {(byte) 0xFF, (byte) 0xD8};
    private static final byte[] APP1_MARKER = {(byte) 0xFF, (byte) 0xE1};
    private static final byte[] APP2_MARKER = {(byte) 0xFF, (byte) 0xE2};
    private static final byte[] DQT_MARKER = {(byte) 0xFF, (byte) 0xDB};
    private static final String EXIF_CODE = "Exif\0";
    private static final String ANALYZE_ID = "[Ricoh Camera Info]\0";

    Buffer mBuffer;
    Segment mSegment;

    private TagSpec[] MAPP1_IFD0ParseTags = new TagSpec[]{
            new TagSpec(Tag.TAG_IMAGEDESCRIPTION),
            new TagSpec(Tag.TAG_MAKE),
            new TagSpec(Tag.TAG_MODEL),
            new TagSpec(Tag.TAG_SUBIFDS),
            new TagSpec(Tag.TAG_COPYRIGHT),
            new TagSpec(Tag.TAG_EXIFIFDPOINTER),
            new TagSpec(Tag.TAG_GPSINFOIFDPOINTER),
    };
    private TagSpec[] MAPP1_ExifParseTags = new TagSpec[]{
            new TagSpec(Tag.TAG_EXPOSURETIME),
            new TagSpec(Tag.TAG_FNUMBER),
            new TagSpec(Tag.TAG_EXPOSUREPROGRAM),
            new TagSpec(Tag.TAG_ISOSPEEDRATINGS),
            new TagSpec(Tag.TAG_STANDARDOUTPUTSENSITIVITY),
            new TagSpec(Tag.TAG_APERTUREVALUE),
            new TagSpec(Tag.TAG_EXPOSUREBIASVALUE),
            new TagSpec(Tag.TAG_PIXELXDIMENSION),
            new TagSpec(Tag.TAG_PIXELYDIMENSION),
            new TagSpec(Tag.TAG_MAXAPERTUREVALUE),
            new TagSpec(Tag.TAG_LIGHTSOURCE),
            new TagSpec(Tag.TAG_WHITEBALANCE),
            new TagSpec(Tag.TAG_EXPOSUREMODE),
            new TagSpec(Tag.TAG_MAKERNOTE),
    };
    private TagSpec[] MAPP1_GPSParseTags = new TagSpec[]{
            new TagSpec(Tag.TAG_GPSVERSIONID),
            new TagSpec(Tag.TAG_GPSLATITUDEREF),
            new TagSpec(Tag.TAG_GPSLATITUDE),
            new TagSpec(Tag.TAG_GPSLONGITUDEREF),
            new TagSpec(Tag.TAG_GPSLONGITUDE),
            new TagSpec(Tag.TAG_GPSALTITUDEREF),
            new TagSpec(Tag.TAG_GPSALTITUDE),
            new TagSpec(Tag.TAG_GPSTIMESTAMP),
            new TagSpec(Tag.TAG_GPSIMGDIRECTIONREF),
            new TagSpec(Tag.TAG_GPSIMGDIRECTION),
            new TagSpec(Tag.TAG_GPSMAPDATUM),
            new TagSpec(Tag.TAG_GPSDATESTAMP),
    };
    private TagSpec[] MAPP1_IFDMParseTags = new TagSpec[]{
            new TagSpec(Tag.TAG_RM_0001),
            new TagSpec(Tag.TAG_RM_0002),
            new TagSpec(Tag.TAG_RM_0003),
            new TagSpec(Tag.TAG_RM_0005),
            new TagSpec(Tag.TAG_RM_0006),
            new TagSpec(Tag.TAG_RM_0007),
            new TagSpec(Tag.TAG_RM_1000),
            new TagSpec(Tag.TAG_RM_1001),
            new TagSpec(Tag.TAG_RM_1003),
            new TagSpec(Tag.TAG_RM_1307),
            new TagSpec(Tag.TAG_RM_4001),
            new TagSpec(Tag.TAG_RM_4002),
            new TagSpec(Tag.TAG_RM_4003),
            new TagSpec(Tag.TAG_RM_4004),
            new TagSpec(Tag.TAG_RM_4005),
            new TagSpec(Tag.TAG_RM_1900),
            new TagSpec(Tag.TAG_RM_1901),
            new TagSpec(Tag.TAG_RM_1902),
            new TagSpec(Tag.TAG_RM_2001),
    };
    private TagSpec[] MAPP1_SphereParseTags = new TagSpec[]{
            new TagSpec(Tag.TAG_R_0001),
            new TagSpec(Tag.TAG_R_0002),
            new TagSpec(Tag.TAG_R_0003),
            new TagSpec(Tag.TAG_R_0004),
            new TagSpec(Tag.TAG_R_0005),
            new TagSpec(Tag.TAG_R_0006),
            new TagSpec(Tag.TAG_R_0007),
            new TagSpec(Tag.TAG_R_0008),
            new TagSpec(Tag.TAG_R_0009),
            new TagSpec(Tag.TAG_R_000A),
            new TagSpec(Tag.TAG_R_000E),
            new TagSpec(Tag.TAG_R_0101),
            new TagSpec(Tag.TAG_R_0102),
            new TagSpec(Tag.TAG_R_0103),
            new TagSpec(Tag.TAG_R_0104),
            new TagSpec(Tag.TAG_R_0105),
            new TagSpec(Tag.TAG_R_0106),
            new TagSpec(Tag.TAG_R_0107),
            new TagSpec(Tag.TAG_R_0108),
            new TagSpec(Tag.TAG_R_0109),
            new TagSpec(Tag.TAG_R_1001),
            new TagSpec(Tag.TAG_R_1002),
            new TagSpec(Tag.TAG_R_1003),
            new TagSpec(Tag.TAG_R_1004),
            new TagSpec(Tag.TAG_R_1005),
            new TagSpec(Tag.TAG_R_1006),
            new TagSpec(Tag.TAG_R_1007),
            new TagSpec(Tag.TAG_R_1008),
            new TagSpec(Tag.TAG_R_1009),
            new TagSpec(Tag.TAG_R_100A),
            new TagSpec(Tag.TAG_R_100B),
            new TagSpec(Tag.TAG_R_100C),
            new TagSpec(Tag.TAG_R_100D),
            new TagSpec(Tag.TAG_R_100E),
            new TagSpec(Tag.TAG_R_100F),
            new TagSpec(Tag.TAG_R_1010),
            new TagSpec(Tag.TAG_R_1011),
            new TagSpec(Tag.TAG_R_1012),
            new TagSpec(Tag.TAG_R_1013),
            new TagSpec(Tag.TAG_R_1014),
            new TagSpec(Tag.TAG_R_1015),
    };

    private TagSpec[] MAPP1_RicohAnalyzeParseTags = new TagSpec[]{
            new TagSpec(Tag.TAG_RA_9001),
            new TagSpec(Tag.TAG_RA_9002),
    };
    private TagSpec[] MAPP1_IFD1ParseTags = new TagSpec[]{
            new TagSpec(Tag.TAG_JPEGICFORMAT),
            new TagSpec(Tag.TAG_JPEGICFORMATLENGTH),
    };
    private TagSpec[] MAPP1_SUBIFDParseTags = new TagSpec[]{
            new TagSpec(Tag.TAG_STRIPOFFSETS),
            new TagSpec(Tag.TAG_STRIPBYTECOUNTS),
    };
    private TagSpec[] MAPP1_SUBIFD1ParseTags = new TagSpec[]{
            new TagSpec(Tag.TAG_STRIPOFFSETS),
            new TagSpec(Tag.TAG_STRIPBYTECOUNTS),
    };

    private IFDSpec[] IFDS_MAPP1Parse = new IFDSpec[]{
            new IFDSpec(IFD.MAPP1_IFD0, MAPP1_IFD0ParseTags),
            new IFDSpec(IFD.MAPP1_EXIF, MAPP1_ExifParseTags),
            new IFDSpec(IFD.MAPP1_GPS, MAPP1_GPSParseTags),
            new IFDSpec(IFD.MAPP1_IFDM, MAPP1_IFDMParseTags),
            new IFDSpec(IFD.MAPP1_SPHERE, MAPP1_SphereParseTags),
            new IFDSpec(IFD.MAPP1_ANALYZE, MAPP1_RicohAnalyzeParseTags),
            new IFDSpec(IFD.MAPP1_IFD1, MAPP1_IFD1ParseTags),
            new IFDSpec(IFD.MAPP1_SUBIFD, MAPP1_SUBIFDParseTags),
            new IFDSpec(IFD.MAPP1_SUBIFD1, MAPP1_SUBIFD1ParseTags),
    };
    private int mApp1Len;
    private int mZerothOffset;
    private boolean mShiftTagOffset = true;

    /**
     * Analyzes the Exif tag and the MakerNote in the captured image data, and holds the result in the object.
     *
     * @param data Image data
     * @param dataBeforeModify Metadata operation flag<br>
     *                         true: Before operation (eg. When `data` is JPEG data obtained by takePicture method.)<br>
     *                         false: After operation (eg. When `data` is read from a saved image file.)
     */
    public Exif(@NonNull byte[] data, boolean dataBeforeModify) {
        byte[] buffer = Arrays.copyOf(data, data.length);
        this.mBuffer = new Buffer(buffer);

        setSegment(dataBeforeModify);
    }

    /**
     * Update the MakerNote and some Exif tags in the captured image data held in the Exif object.
     */
    public void setExifMaker() {
        setAttribute(IFD.MAPP1_IFDM, Tag.TAG_RM_0005,
                String.format("%016X", CameraSettings.getThetaSerialNumber()));

        String verNumber = makeVersionNumber(CameraSettings.getThetaFirmwareVersion());
        setAttribute(IFD.MAPP1_IFDM, Tag.TAG_RM_0002, verNumber + "\0");

        WhiteBalance whiteBalance = CameraSettings.getWhiteBalance();
        byte bWhiteBalance = whiteBalance.getExifValue();
        byte bWhiteBalanceMode = whiteBalance.getMakerValue();
        byte[] bWB = {0x00, 0x00};
        byte[] bLight = {0x00, 0x00};
        byte[] bWBmode = {0x00, 0x00};
        if (whiteBalance != WhiteBalance.AUTO) {
            bWB[1] = 1;
            bLight[1] = bWhiteBalance;
            bWBmode[1] = bWhiteBalanceMode;
        }
        setAttribute(IFD.MAPP1_IFDM, Tag.TAG_RM_1003, bWBmode);
        setAttribute(IFD.MAPP1_EXIF, Tag.TAG_WHITEBALANCE, bWB);
        setAttribute(IFD.MAPP1_EXIF, Tag.TAG_LIGHTSOURCE, bLight);

        removeIntervalTags();
        removeBracketTags();
        removeShootingModeTag();

        int iColorTemp = CameraSettings.getColorTemperature().intValue();
        setAttribute(IFD.MAPP1_IFDM, Tag.TAG_RM_1307, iColorTemp);

        ExposureProgram ep = CameraSettings.getExposureProgram();
        if (ep == ExposureProgram.ISO_PRIORITY) {
            setAttribute(IFD.MAPP1_EXIF, Tag.TAG_EXPOSUREPROGRAM, (0x02 << 16));
        } else {
            setAttribute(IFD.MAPP1_EXIF, Tag.TAG_EXPOSUREPROGRAM, (ep.getInt() << 16));
        }

        if (ep == ExposureProgram.MANUAL) {
            setAttribute(IFD.MAPP1_EXIF, Tag.TAG_EXPOSUREMODE, (short) 1);
        } else {
            setAttribute(IFD.MAPP1_EXIF, Tag.TAG_EXPOSUREMODE, (short) 0);
        }
    }

    public int calcRoll() {
        double ax = 0.0;
        double ay = 0.0;
        double az = 0.0;

        byte xyz[] = getAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0109);
        if (xyz != null) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 0, 4));
            int xNumer = byteBuffer.getInt();
            byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 4, 8));
            int xDenom = byteBuffer.getInt();
            byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 8, 12));
            int yNumer = byteBuffer.getInt();
            byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 12, 16));
            int yDenom = byteBuffer.getInt();
            byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 16, 20));
            int zNumer = byteBuffer.getInt();
            byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 20, 24));
            int zDenom = byteBuffer.getInt();

            ax = (double) xNumer / xDenom;
            ay = (double) yNumer / yDenom;
            az = (double) zNumer / zDenom;
        }
        return calcRoll(ax, ay, az);
    }

    private int calcRoll(double ax, double ay, double az) {
        double roll = 0.0;

        double sqsum = ax * ax + az * az;
        if (sqsum < 0.0 * 0.0) {
            if (ax >= 0) {
                return 27000;
            } else {
                return 9000;
            }
        }

        if (ay == 0 && az == 0) {
            if (ax >= 0) {
                return 27000;
            } else {
                return 9000;
            }
        }

        roll = Math.toDegrees(Math.atan2(-ax, az));

        if (roll >= 0) {
            return (int) (roll * 100);
        } else {
            return (int) ((360 + roll) * 100);
        }
    }

    public int calcPitch() {
        double ax = 0.0;
        double ay = 0.0;
        double az = 0.0;

        byte xyz[] = getAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0109);
        if (xyz != null) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 0, 4));
            int xNumer = byteBuffer.getInt();
            byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 4, 8));
            int xDenom = byteBuffer.getInt();
            byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 8, 12));
            int yNumer = byteBuffer.getInt();
            byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 12, 16));
            int yDenom = byteBuffer.getInt();
            byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 16, 20));
            int zNumer = byteBuffer.getInt();
            byteBuffer = ByteBuffer.wrap(Arrays.copyOfRange(xyz, 20, 24));
            int zDenom = byteBuffer.getInt();

            ax = (double)xNumer/xDenom;
            ay = (double)yNumer/yDenom;
            az = (double)zNumer/zDenom;
        }
        double pitch = 0.0;

        double sqsum = ax * ax + ay * ay + az * az;
        if (sqsum < 0.0 * 0.0) {
            return 0;
        }

        if(ay == 0 && az == 0) {
            return 0;
        }

        pitch = Math.toDegrees(Math.acos(-ay / Math.sqrt(sqsum))) - 90.0;
        return (int)(pitch * 100);
    }

    /**
     * Set the sphere information to the captured image data held in the Exif object.
     */
    public void setExifSphere() {
        setExifSphere(CameraSettings.isZenith());
    }

    /**
     * Set the sphere information to the captured image data held in the Exif object.
     *
     * @param isZenith true: Jpeg file processing and zenith correction enabled
     *                 false: When processing other than Jpeg files or zenith correction is disabled
     */
    protected void setExifSphere(boolean isZenith) {
        SphereType sphereType = CameraSettings.getSphereType();
        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0001, (short) sphereType.getInt());

        Filter filter = CameraSettings.getFilter();
        final byte[] filteroff = {0x00, 0x00};
        final byte[] filteron = {0x00, 0x01};
        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0006, filteroff);
        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0002, filteroff);
        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0008, filteroff);
        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_000E, filteroff);
        if (filter == Filter.NOISE_REDUCTION) {
            setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0006, filteron);
        } else if (filter == Filter.HDR) {
            setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0002, filteron);
        } else if (filter == Filter.DR_COMP) {
            setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0008, filteron);
        } else if (filter == Filter.HH_HDR) {
            setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_000E, filteron);
        }

        final byte[] abnormal = {0x00, 0x00};
        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0005, abnormal);

        byte[] zenith;
        if (isZenith) {
            zenith = new byte[]{0x00, 0x01};
        } else {
            zenith = new byte[]{0x00, 0x00};
        }
        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0007, zenith);

        final byte[] composite = {0x00, 0x00};
        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0009, composite);

        int timezoneLength = getAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_000A).length;
        String timezone = "";
        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_000A, new byte[timezoneLength]);
        if (!CameraSettings.isEmptyTimeZone()) {
            timezone = CameraSettings.getTimeZone();
        }
        if (timezoneLength >= timezone.getBytes().length) {
            setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_000A, timezone);
        }

        try {
            ImageQualityDebug iqd = new ImageQualityDebug();
            setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0102, iqd.getSphereFNumber());
            iqd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0104, 0x00);
        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0105, 0x00);

        int pitch = 0;
        int roll = 0;
        if (!isZenith) {
            pitch = calcPitch();
            roll = calcRoll();
        }

        final int denom100 = 100;
        byte[] bPitchRoll = new byte[16];
        System.arraycopy(ByteBuffer.allocate(4).putInt(roll).array(), 0, bPitchRoll, 0, 4);
        System.arraycopy(ByteBuffer.allocate(4).putInt(denom100).array(), 0, bPitchRoll, 4, 4);
        System.arraycopy(ByteBuffer.allocate(4).putInt(pitch).array(), 0, bPitchRoll, 8, 4);
        System.arraycopy(ByteBuffer.allocate(4).putInt(denom100).array(), 0, bPitchRoll, 12, 4);
        setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0003, bPitchRoll);

        if (CameraSettings.getSensorValues().getCompassAccuracy()) {
            int compass = calcCompass(CameraSettings.getSensorValues());
            byte[] bCompass = new byte[8];
            System.arraycopy(ByteBuffer.allocate(4).putInt(compass).array(), 0, bCompass, 0, 4);
            System.arraycopy(ByteBuffer.allocate(4).putInt(denom100).array(), 0, bCompass, 4, 4);
            setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_0004, bCompass);
        } else {
            removeSphereCompass();
        }

        try {
            AudioDebug audioDebug = new AudioDebug();
            short sMicSelect = audioDebug.getMicSelect();
            short sAudioLevel = audioDebug.getAudioLevel();
            audioDebug.close();
            Gain sGain = CameraSettings.getGain();

            setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_1011, sMicSelect);
            setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_1012, sAudioLevel);
            setAttribute(IFD.MAPP1_SPHERE, Tag.TAG_R_1013, (short) sGain.getExifValue());
        } catch (IOException e) {
            removeSphereMicrophone();
        }

        removeSphereDebugs();
    }

    /**
     * Set GPS information to the captured image data held in the Exif object.
     */
    public void setExifGPS() {
        GpsInfo gpsInfo = CameraSettings.getGpsInfo();
        SensorValues sensorValues = CameraSettings.getSensorValues();
        final byte[] versionid = {0x02, 0x03, 0x00, 0x00};
        setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSVERSIONID, versionid);

        if (gpsInfo.getLat().intValue() == GpsInfo.INVALID) {
            removeGPSlatTags();
        } else {
            final byte[] latNorth = {(byte) 'N', 0x00};
            final byte[] latSouth = {(byte) 'S', 0x00};
            double gpslat = gpsInfo.getLat().doubleValue();

            if (gpslat > 0) {
                setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSLATITUDEREF, latNorth);
            } else {
                setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSLATITUDEREF, latSouth);
                gpslat = -gpslat;
            }

            int latdegree = (int) gpslat;
            double dminutes = (gpslat - (double) latdegree) * 60.0;
            int latminutes = (int) dminutes;
            double dseconds = (dminutes - (double) latminutes) * 60.0;
            int latseconds = (int) (dseconds * 100);
            int[] lat = new int[6];
            lat[0] = (int) gpslat;
            lat[1] = 1;
            lat[2] = latminutes;
            lat[3] = 1;
            lat[4] = latseconds;
            lat[5] = 100;
            setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSLATITUDE, lat);
        }

        if (gpsInfo.getLng().intValue() == GpsInfo.INVALID) {
            removeGPSlngTags();
        } else {
            final byte[] lngEast = {(byte) 'E', 0x00};
            final byte[] lngWest = {(byte) 'W', 0x00};
            double gpslng = gpsInfo.getLng().doubleValue();

            if (gpslng > 0) {
                setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSLONGITUDEREF, lngEast);
            } else {
                setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSLONGITUDEREF, lngWest);
                gpslng = -gpslng;
            }
            int lngDegree = (int) gpslng;
            double dlngMinutes = (gpslng - (double) lngDegree) * 60.0;
            int lngMinutes = (int) dlngMinutes;
            double dlngSeconds = (dlngMinutes - (double) lngMinutes) * 60.0;
            int lngSeconds = (int) (dlngSeconds * 100);

            int[] lon = new int[6];
            lon[0] = (int) gpslng;
            lon[1] = 1;
            lon[2] = lngMinutes;
            lon[3] = 1;
            lon[4] = lngSeconds;
            lon[5] = 100;
            setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSLONGITUDE, lon);
        }

        if (gpsInfo.getAltitude().intValue() == GpsInfo.ALT_INVALID) {
            removeGPSaltTags();
        } else {
            float gpsalt = gpsInfo.getAltitude().floatValue();

            if (gpsalt > 0) {
                setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSALTITUDEREF, (byte) 0x00);
            } else {
                setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSALTITUDEREF, (byte) 0x01);
                gpsalt = -gpsalt;
            }

            int[] alt = new int[2];
            alt[0] = (int) (gpsalt * 100);

            alt[1] = 100;
            setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSALTITUDE, alt);
        }

        String gpsDateTime = gpsInfo.getDateTimeZone();
        if (gpsDateTime.isEmpty()) {
            removeGPSDateTimeTags();
        } else {
            DateTime dateTime = DateTimeFormats.getDateTimeZone(gpsDateTime);
            int hour = dateTime.getHourOfDay();
            int minute = dateTime.getMinuteOfHour();
            int second = dateTime.getSecondOfMinute();

            int[] timestamp = {0, 1, 0, 1, 0, 1};
            timestamp[0] = hour;
            timestamp[2] = minute;
            timestamp[4] = second;
            setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSTIMESTAMP, timestamp);

            String date = DateTimeFormats.getDate(dateTime);
            setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSDATESTAMP, date + "\0");
        }

        if (gpsInfo.getDatum().isEmpty()) {
            removeGPSDatumTag();
        } else {
            setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSMAPDATUM, "WGS-84\0");
        }

        if (!sensorValues.getCompassAccuracy()) {
            removeGPSImagedirection();
        } else {
            setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSIMGDIRECTIONREF, "M\0");

            int compass = calcCompass(sensorValues);
            int[] compassRational = {compass, SENSOR_COEFFICIENT};
            setAttribute(IFD.MAPP1_GPS, Tag.TAG_GPSIMGDIRECTION, compassRational);
        }
    }

    /**
     * Returns the captured image data held by the Exif object.
     *
     * @return Image data
     */
    public byte[] getExif() {
        return this.mBuffer.getByte();
    }

    private String makeVersionNumber(@NonNull final String versionName) {
        String versionNumber = versionName.replace("-", "");
        String[] versions = versionNumber.split(Pattern.quote("."), 0);
        if (versions.length > 2) {
            versionNumber = String.format(Locale.ENGLISH, "%1d.%1d%1d",
                    Integer.parseInt(versions[0]),
                    Integer.parseInt(versions[1]),
                    Integer.parseInt(versions[2]));
        }

        return versionNumber;
    }

    private int calcMakerRollAngle(@NonNull SensorValues sensorValues) {
        float[] attitude = sensorValues.getAttitudeRadian();
        double receptRoll = Math.toDegrees(attitude[2]);
        if (receptRoll < 0) {
            receptRoll = 360 + receptRoll;
        }
        return (int) (receptRoll * 100);
    }

    private int calcMakerPitchAngle(@NonNull SensorValues sensorValues) {
        float[] attitude = sensorValues.getAttitudeRadian();
        double receptPitch = Math.toDegrees(attitude[1]);
        return (int) (receptPitch * -100);

    }

    private int calcCompass(@NonNull SensorValues sensorValues) {
        float[] attitude = sensorValues.getAttitudeRadian();
        double receptYaw = Math.toDegrees(attitude[0]);
        if (receptYaw < 0) {
            receptYaw = 360 + receptYaw;
        }
        return (int) (receptYaw * 100);
    }

    protected Buffer.Endian getEndian() {
        return mBuffer.getEndian();
    }

    protected byte[] getAttribute(IFD ifd, Tag tag) {
        return mSegment.getTagValue(ifd, tag);
    }

    protected void setAttribute(IFD ifd, Tag tag, byte[] value) {
        if (!mSegment.skipToTagPos(ifd, tag)) {
            return;
        }
        mBuffer.put(value);
    }

    protected void setAttribute(IFD ifd, Tag tag, String value) {
        if (!mSegment.skipToTagPos(ifd, tag)) {
            return;
        }
        mBuffer.put(value);
    }

    protected void setAttribute(IFD ifd, Tag tag, int value) {
        if (!mSegment.skipToTagPos(ifd, tag)) {
            return;
        }
        mBuffer.put32(value);
    }

    protected void setAttribute(IFD ifd, Tag tag, int[] values) {
        if (!mSegment.skipToTagPos(ifd, tag)) {
            return;
        }
        for (int value : values) {
            mBuffer.put32(value);
        }
    }

    protected void setAttribute(IFD ifd, Tag tag, short value) {
        if (!mSegment.skipToTagPos(ifd, tag)) {
            return;
        }
        mBuffer.put32(value << 16);
    }

    protected void setAttribute(IFD ifd, Tag tag, byte value) {
        if (!mSegment.skipToTagPos(ifd, tag)) {
            return;
        }
        mBuffer.put(value);
    }

    private void setSegment(boolean shiftTagOffset) {
        mShiftTagOffset = shiftTagOffset;

        mSegment = new Segment(IFDS_MAPP1Parse);
        loadAttributes();
    }

    private void removeShootingModeTag() {
        Segment seg = mSegment;
        int offset = seg.getTagOffset(IFD.MAPP1_EXIF, Tag.TAG_MAKERNOTE);
        if (!seg.validOffset(offset)) {
            return;
        }

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);
        if (!mBuffer.verify(MAKER_ID)) {
            return;
        }

        int pos = mBuffer.getCursor();
        seg.removeTag(pos, IFD.MAPP1_IFDM, Tag.TAG_RM_1001);
    }

    private void removeSphereCompass() {
        Segment seg = mSegment;
        int offset = getSphereStartOffset(seg);

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        int pos = mBuffer.getCursor();
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_0004);
    }

    private void removeSphereMicrophone() {
        Segment seg = mSegment;
        int offset = getSphereStartOffset(seg);

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        int pos = mBuffer.getCursor();
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1011);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1012);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1013);
    }

    private void removeSphereDebugs() {
        Segment seg = mSegment;
        int offset = getSphereStartOffset(seg);

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        int pos = mBuffer.getCursor();

        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1001);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1003);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1002);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1004);

        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1010);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1005);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1006);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1007);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1008);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_100F);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_0101);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_0103);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1009);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_100B);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_100D);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_100A);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_100C);
        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_100E);

        seg.removeTag(pos, IFD.MAPP1_SPHERE, Tag.TAG_R_1015);
    }

    private int getSphereStartOffset(Segment seg) {
        int offset = seg.getTagOffset(IFD.MAPP1_IFDM, Tag.TAG_RM_4001);

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);
        int spherePoint = mBuffer.get32();

        offset = spherePoint;

        if (!seg.validOffset(offset)) {
            return 0;
        }

        return spherePoint;
    }

    private void removeGPSImagedirection() {
        Segment seg = mSegment;
        int offset = getGpsStartOffset(seg);

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        int pos = mBuffer.getCursor();
        seg.removeTag(pos, IFD.MAPP1_GPS, Tag.TAG_GPSIMGDIRECTIONREF);
        seg.removeTag(pos, IFD.MAPP1_GPS, Tag.TAG_GPSIMGDIRECTION);
    }

    private void removeGPSDateTimeTags() {
        Segment seg = mSegment;
        int offset = getGpsStartOffset(seg);

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        int pos = mBuffer.getCursor();
        seg.removeTag(pos, IFD.MAPP1_GPS, Tag.TAG_GPSTIMESTAMP);
        seg.removeTag(pos, IFD.MAPP1_GPS, Tag.TAG_GPSDATESTAMP);
    }

    private void removeGPSDatumTag() {
        Segment seg = mSegment;
        int offset = getGpsStartOffset(seg);

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        int pos = mBuffer.getCursor();
        seg.removeTag(pos, IFD.MAPP1_GPS, Tag.TAG_GPSMAPDATUM);
    }

    private void removeGPSaltTags() {
        Segment seg = mSegment;
        int offset = getGpsStartOffset(seg);

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        int pos = mBuffer.getCursor();
        seg.removeTag(pos, IFD.MAPP1_GPS, Tag.TAG_GPSALTITUDE);
        seg.removeTag(pos, IFD.MAPP1_GPS, Tag.TAG_GPSALTITUDEREF);
    }

    private void removeGPSlatTags() {
        Segment seg = mSegment;
        int offset = getGpsStartOffset(seg);

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        int pos = mBuffer.getCursor();
        seg.removeTag(pos, IFD.MAPP1_GPS, Tag.TAG_GPSLATITUDE);
        seg.removeTag(pos, IFD.MAPP1_GPS, Tag.TAG_GPSLATITUDEREF);
    }

    private void removeGPSlngTags() {
        Segment seg = mSegment;
        int offset = getGpsStartOffset(seg);

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        int pos = mBuffer.getCursor();
        seg.removeTag(pos, IFD.MAPP1_GPS, Tag.TAG_GPSLONGITUDE);
        seg.removeTag(pos, IFD.MAPP1_GPS, Tag.TAG_GPSLONGITUDEREF);
    }

    private int getGpsStartOffset(Segment seg) {
        int offset = seg.getTagOffset(IFD.MAPP1_IFD0, Tag.TAG_GPSINFOIFDPOINTER);
        mBuffer.seek(seg.base);
        mBuffer.skip(offset);
        int gpsPoint = mBuffer.get32();

        offset = gpsPoint;
        if (!seg.validOffset(offset)) {
            return 0;
        }

        return gpsPoint;
    }

    private void loadAttributes() {
        if (!parseHeader()) {
            return;
        }
        parseTagPos();
    }

    private void removeIntervalTags() {
        Segment seg = mSegment;
        int offset = seg.getTagOffset(IFD.MAPP1_EXIF, Tag.TAG_MAKERNOTE);

        if (!seg.validOffset(offset)) {
            return;
        }

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);
        if (!mBuffer.verify(MAKER_ID)) {
            return;
        }

        int pos = mBuffer.getCursor();

        seg.removeTag(pos, IFD.MAPP1_IFDM, Tag.TAG_RM_4002);
        seg.removeTag(pos, IFD.MAPP1_IFDM, Tag.TAG_RM_4003);
        seg.removeTag(pos, IFD.MAPP1_IFDM, Tag.TAG_RM_4004);
        seg.removeTag(pos, IFD.MAPP1_IFDM, Tag.TAG_RM_4005);
    }

    private void removeBracketTags() {
        Segment seg = mSegment;
        int offset = seg.getTagOffset(IFD.MAPP1_EXIF, Tag.TAG_MAKERNOTE);

        if (!seg.validOffset(offset)) {
            return;
        }

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);
        if (!mBuffer.verify(MAKER_ID)) {
            return;
        }

        int pos = mBuffer.getCursor();

        seg.removeTag(pos, IFD.MAPP1_IFDM, Tag.TAG_RM_1900);
        seg.removeTag(pos, IFD.MAPP1_IFDM, Tag.TAG_RM_1901);
        seg.removeTag(pos, IFD.MAPP1_IFDM, Tag.TAG_RM_1902);
    }

    private boolean parseAPP1Header() {
        if (!mBuffer.verify(APP1_MARKER)) {
            return false;
        }

        mApp1Len = mBuffer.get16();
        if (getMaxSegmentLen() < mApp1Len) {
            return false;
        }
        if (!mBuffer.verify(EXIF_CODE)) {
            return false;
        }

        mBuffer.skip(1);
        return true;
    }

    private boolean skipAPPn() {
        mBuffer.seek(MARKER_LEN + MARKER_LEN + mApp1Len);

        for (int i = 0; i < 10; i++) {
            if (mBuffer.verify(APP2_MARKER)) {
                mBuffer.skip(MARKER_LEN * -1);
                return true;
            } else if (mBuffer.verify(DQT_MARKER)) {
                return true;
            } else if (mBuffer.verifyExifMarker()) {
                int len = mBuffer.get16();
                mBuffer.skip(MARKER_LEN * -1);
                mBuffer.skip(len);
            } else {
                return false;
            }
        }

        return true;
    }

    private boolean parseHeader() {
        if (!mBuffer.verify(SOI_MARKER)) {
            return false;
        }
        if (!parseAPP1Header()) {
            return false;
        }
        mSegment.base = mBuffer.getCursor();

        return mSegment.parseTIFFHeader() && skipAPPn();
    }

    private boolean parseTagPosGPS(int[] next) {
        Segment seg = mSegment;

        int offset = seg.get32(IFD.MAPP1_IFD0, Tag.TAG_GPSINFOIFDPOINTER);

        if (!seg.validOffset(offset)) {
            return false;
        }

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        return seg.parseTagOffset(IFD.MAPP1_GPS, offset, next);
    }

    private boolean parseTagPosEXIF(int[] next) {
        Segment seg = mSegment;

        int offset = seg.get32(IFD.MAPP1_IFD0, Tag.TAG_EXIFIFDPOINTER);

        if (!seg.validOffset(offset)) {
            return false;
        }
        mBuffer.seek(seg.base);
        mBuffer.skip(offset);
        if (!seg.parseTagOffset(IFD.MAPP1_EXIF, offset, next)) {
            return false;
        }

        offset = seg.getTagOffset(IFD.MAPP1_EXIF, Tag.TAG_MAKERNOTE);

        if (!seg.validOffset(offset)) {
            return false;
        }

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);
        if (!mBuffer.verify(MAKER_ID)) {
            return true;
        }

        int makernoteHead = offset;
        if (mShiftTagOffset) {
            seg.shiftTagOffset(makernoteHead);
        }

        offset += MAKER_ID_LEN;
        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        if (!seg.parseTagOffset(IFD.MAPP1_IFDM, offset, next)) {
            return false;
        }

        offset = seg.getTagOffset(IFD.MAPP1_IFDM, Tag.TAG_RM_4001);

        if (!seg.validOffset(offset)) {
            return false;
        }

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        offset = mBuffer.get32();

        if (mShiftTagOffset) {
            offset += makernoteHead;

            mBuffer.skip(-4);
            mBuffer.put32(offset);

            mBuffer.seek(seg.base);
            mBuffer.skip(offset);
            seg.shiftTagOffset(makernoteHead);
        }

        mBuffer.seek(seg.base);
        mBuffer.skip(offset);
        if (!seg.parseTagOffset(IFD.MAPP1_SPHERE, offset, next)) {
            return false;
        }

        offset = seg.getTagOffset(IFD.MAPP1_IFDM, Tag.TAG_RM_2001);

        if (seg.validOffset(offset)) {
            mBuffer.seek(seg.base);
            mBuffer.skip(offset);

            offset = mBuffer.get32();

            if (mShiftTagOffset) {
                offset += makernoteHead;

                mBuffer.skip(-4);
                mBuffer.put32(offset);

                mBuffer.seek(seg.base);
                mBuffer.skip(offset + ANALYZE_ID.length());
                seg.shiftTagOffset(makernoteHead);
            }

            mBuffer.seek(seg.base);
            mBuffer.skip(offset + ANALYZE_ID.length());
            if (!seg.parseTagOffset(IFD.MAPP1_ANALYZE, offset, next)) {
                return false;
            }
        }

        return true;
    }

    private void parseTagPosSubIfds(int[] next) {
        Segment seg = mSegment;

        byte[] subIfdOffsets = seg.getTagValue(IFD.MAPP1_IFD0, Tag.TAG_SUBIFDS);
        if (subIfdOffsets == null || subIfdOffsets.length < 8) {
            return;
        }

        int subIfdOffset = ByteBuffer.wrap(subIfdOffsets).getInt();
        int subIfd1Offset = ByteBuffer.wrap(subIfdOffsets).getInt(4);

        if (!seg.validOffset(subIfdOffset)) {
            return;
        }
        if (!seg.validOffset(subIfd1Offset)) {
            return;
        }

        mBuffer.seek(seg.base);
        mBuffer.skip(subIfdOffset);
        if (!seg.parseTagOffset(IFD.MAPP1_SUBIFD, subIfdOffset, next)) {
            return;
        }

        mBuffer.seek(seg.base);
        mBuffer.skip(subIfd1Offset);
        seg.parseTagOffset(IFD.MAPP1_SUBIFD1, subIfd1Offset, next);
    }

    protected boolean parseTagPos() {
        int[] next = {0};
        Segment seg = mSegment;

        int offset = mZerothOffset;
        mBuffer.seek(seg.base);
        mBuffer.skip(offset);

        if (!seg.parseTagOffset(IFD.MAPP1_IFD0, offset, next)) {
            return false;
        }

        offset = next[0];
        if (!seg.validOffset(offset)) {
            return false;
        }
        mBuffer.seek(seg.base);
        mBuffer.skip(offset);
        if (!seg.parseTagOffset(IFD.MAPP1_IFD1, offset, next)) {
            return false;
        }

        parseTagPosSubIfds(next);

        if (!parseTagPosEXIF(next)) {
            return false;
        }

        return parseTagPosGPS(next);
    }

    int getMaxSegmentLen() {
        return Segment.MAX_SEGMENT_LEN;
    }

    private class IFDSpec {
        IFD mIFD;
        TagSpec[] mTagSpecs;

        IFDSpec(IFD ifd, TagSpec[] tagSpecs) {
            mIFD = ifd;
            mTagSpecs = tagSpecs;
        }
    }

    private class TagSpec {
        Tag mTag;
        int mOffset;
        int mLength;
        int mPos;

        TagSpec(Tag tag) {
            mTag = tag;
        }
    }

    protected class Segment {
        private static final int TIFF_ID = 0x002a;
        private static final int MAX_SEGMENT_LEN = 1024 * 64;

        IFDSpec[] ifdSpecs;
        int base;

        Segment(IFDSpec[] ifdSpecs) {
            this.ifdSpecs = ifdSpecs;
        }

        private int getValueLen(ExifType type, int num) {
            switch (type) {
                case TIFF_TYPE_RAW:
                case TIFF_TYPE_BYTE:
                case TIFF_TYPE_ASCII:
                case TIFF_TYPE_UNDEF:
                    return num;
                case TIFF_TYPE_SHORT:
                    return 2 * num;
                case TIFF_TYPE_LONG:
                    return 4 * num;
                case TIFF_TYPE_RATIONAL:
                case TIFF_TYPE_SRATIONAL:
                    return 8 * num;
                default:
                    break;
            }
            return 0;
        }

        private TagSpec containTag(IFD ifd, int id) {
            for (IFDSpec ifdSpec : ifdSpecs) {
                if (ifdSpec.mIFD.equals(ifd)) {
                    for (TagSpec tagSpec : ifdSpec.mTagSpecs) {
                        if (tagSpec.mTag.getTagID() == id) {
                            return tagSpec;
                        }
                    }
                }
            }
            return null;
        }

        protected int getTagOffset(IFD ifd, Tag tag) {
            for (IFDSpec ifdSpec : ifdSpecs) {
                if (ifdSpec.mIFD.equals(ifd)) {
                    for (TagSpec tagSpec : ifdSpec.mTagSpecs) {
                        if (tagSpec.mTag.equals(tag)) {
                            return tagSpec.mOffset;
                        }
                    }
                }
            }
            return 0;
        }

        protected int getTagPos(IFD ifd, Tag tag) {
            for (IFDSpec ifdSpec : ifdSpecs) {
                if (ifdSpec.mIFD.equals(ifd)) {
                    for (TagSpec tagSpec : ifdSpec.mTagSpecs) {
                        if (tagSpec.mTag.equals(tag)) {
                            return tagSpec.mPos;
                        }
                    }
                }
            }
            return 0;
        }

        private int get32(IFD ifd, Tag tag) {
            int offset = getTagOffset(ifd, tag);
            if (offset == 0) {
                return 0;
            }
            mBuffer.seek(base);
            mBuffer.skip(offset);
            return mBuffer.get32();
        }

        private boolean parseTagOffset(IFD ifd, int offset, int[] next) {
            int pos = offset;
            int count = mBuffer.get16();

            int cur = mBuffer.getCursor();

            if (100 < count) {
                return false;
            }
            pos += 2;
            for (int i = 0; i < count; i++) {
                int tagId = mBuffer.get16();
                TagSpec tagSpec = containTag(ifd, tagId);
                if (tagSpec == null) {
                    mBuffer.skip(10);
                    pos += 12;
                    continue;
                }
                int typeId = mBuffer.get16();
                int num = mBuffer.get32();
                int value = mBuffer.get32();

                tagSpec.mLength = getValueLen(ExifType.getType(typeId), num);
                if (tagSpec.mLength <= 4) {
                    tagSpec.mOffset = pos + 8;
                    tagSpec.mPos = pos + 8;
                } else {
                    tagSpec.mOffset = value;
                    tagSpec.mPos = pos + 8;
                }

                pos += 12;
            }
            next[0] = mBuffer.get32();
            return true;
        }

        private boolean validOffset(int offset) {
            return ((0 < offset) && (offset < getMaxSegmentLen()));
        }

        boolean parseTIFFHeader() {
            if (mBuffer.verify("MM")) {
                mBuffer.setEndian(Buffer.Endian.BIG);
            } else if (mBuffer.verify("II")) {
                mBuffer.setEndian(Buffer.Endian.LITTLE);
            } else {
                return false;
            }
            if (mBuffer.get16() != TIFF_ID) {
                return false;
            }
            mZerothOffset = mBuffer.get32();
            return getMaxSegmentLen() >= mZerothOffset;
        }

        private boolean shiftTagOffset(int offset) {
            int count = mBuffer.get16();
            if (100 < count) {
                return false;
            }
            for (int i = 0; i < count; i++) {
                int tagId = mBuffer.get16();
                int typeId = mBuffer.get16();
                int num = mBuffer.get32();
                int value = mBuffer.get32();

                if (getValueLen(ExifType.getType(typeId), num) > 4) {
                    mBuffer.skip(-4);
                    mBuffer.put32(offset + value);
                }
            }
            return true;
        }

        private void removeTag(int startPos, IFD ifd, Tag tag) {
            mBuffer.seek(startPos);

            int count = mBuffer.get16();

            if (100 < count) {
                return;
            }
            int i;
            for (i = 0; i < count; i++) {
                int tagId = mBuffer.get16();
                if (tagId == tag.getTagID()) {
                    mBuffer.skip(-TAG_ID_LEN);
                    TagSpec tagSpec = containTag(ifd, tagId);
                    if (tagSpec != null) {
                        tagSpec.mOffset = 0;
                    }
                    break;
                } else {
                    mBuffer.skip(TAG_TYPE_LEN + TAG_NUM_LEN + TAG_VALUE_LEN);
                }
            }

            if (i == count) {
                return;
            }

            count -= 1;
            mBuffer.remove(TAG_LEN, (count - i) * TAG_LEN);

            for (; i < count; i++) {
                int tagId = mBuffer.get16();
                TagSpec tagSpec = containTag(ifd, tagId);
                if (tagSpec == null) {
                    mBuffer.skip(TAG_TYPE_LEN + TAG_NUM_LEN + TAG_VALUE_LEN);
                    continue;
                }
                int typeId = mBuffer.get16();
                int num = mBuffer.get32();
                int value = mBuffer.get32();

                if (getValueLen(ExifType.getType(typeId), num) <= TAG_VALUE_LEN) {
                    tagSpec.mOffset -= TAG_LEN;
                }
            }

            mBuffer.seek(startPos);
            mBuffer.put16(count);
        }

        private boolean skipToTagPos(IFD ifd, Tag tag) {
            int offset = getTagOffset(ifd, tag);

            if (!validOffset(offset)) {
                return false;
            }
            mBuffer.seek(base);
            mBuffer.skip(offset);
            return true;
        }

        private byte[] getTagValue(IFD ifd, Tag tag) {
            TagSpec tagSpec = containTag(ifd, tag.getTagID());
            if (tagSpec == null) {
                return null;
            }
            if (!skipToTagPos(ifd, tag)) {
                return null;
            }
            return mBuffer.getN(tagSpec.mLength);
        }
    }
}
