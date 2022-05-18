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

import java.util.Date;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.theta360.pluginlibrary.exif.utils.DateTimeFormats;
import com.theta360.pluginlibrary.exif.values.Aperture;
import com.theta360.pluginlibrary.exif.values.ColorTemperature;
import com.theta360.pluginlibrary.exif.values.ExposureCompensation;
import com.theta360.pluginlibrary.exif.values.ExposureProgram;
import com.theta360.pluginlibrary.exif.values.Filter;
import com.theta360.pluginlibrary.exif.values.Gain;
import com.theta360.pluginlibrary.exif.values.Iso;
import com.theta360.pluginlibrary.exif.values.ShutterSpeed;
import com.theta360.pluginlibrary.exif.values.SphereType;
import com.theta360.pluginlibrary.exif.values.WhiteBalance;
import com.theta360.pluginlibrary.exif.values.ZenithCorrection;
import com.theta360.pluginlibrary.values.ThetaModel;
import com.theta360.pluginlibrary.factory.Camera;

/**
 * CameraSettings class
 */
public class CameraSettings {
    private static final String DEFAULT_MANUFACTURER = "RICOH";
    private static final String DEFAULT_FIRMWARE_VERSION = ".";

    private static String mManufacturer = null;
    private static ThetaModel mThetaModel = null;
    private static long mThetaSerialNumber = -1;
    private static String mThetaFirmwareVersion = null;
    private static GpsInfo mGpsInfo = null;
    private static SensorValues mSensorValues = null;

    private static ExposureProgram mExposureProgram = null;
    private static Aperture mAperture = null;
    private static ShutterSpeed mShutterSpeed = null;
    private static Iso mIso = null;
    private static ExposureCompensation mExposureCompensation = null;
    private static WhiteBalance mWhiteBalance = null;
    private static Number mColorTemperature = null;
    private static Filter mFilter = null;
    private static SphereType mSphereType = null;
    private static long mDateTime = -1;
    private static int mTimeZone = -1;
    private static Gain mGain = null;
    private static ZenithCorrection mZenithCorrection = null;
    private static boolean mDngOutput = false;

    /**
     * Initialize the camera settings held by the object.
     */
    public static void initialize() {
        mManufacturer = DEFAULT_MANUFACTURER;
        mThetaModel = ThetaModel.THETA_DEF;
        mThetaSerialNumber = 0;
        mThetaFirmwareVersion = DEFAULT_FIRMWARE_VERSION;
        mGpsInfo = new GpsInfo();
        mSensorValues = new SensorValues();

        mFilter = Filter.OFF;
        mExposureProgram = ExposureProgram.NORMAL_PROGRAM;
        mAperture = Aperture.APERTURE_2_0;
        mShutterSpeed = ShutterSpeed.SHUTTER_SPEED_AUTO;
        mIso = Iso.ISO_AUTO;
        mExposureCompensation = ExposureCompensation.EXPOSURE_COMPENSATION_0;
        mWhiteBalance = WhiteBalance.AUTO;
        mColorTemperature = ColorTemperature.DEFAULT.getNumber();
        setSphereType(SphereType.EQUIRECTANGULAR);
        setDateTime(-1);
        setTimeZone(-1);
        mGain = Gain.NORMAL;
        mZenithCorrection = ZenithCorrection.RIC_ZENITH_CORRECTION_OFF;
        mDngOutput = false;
    }

    /**
     * Returns the manufacturer name that is held
     *
     * @return manufacturer name("RICOH")
     */
    public static String getManufacturer() {
        return (mManufacturer != null) ? mManufacturer : DEFAULT_MANUFACTURER;
    }

    /**
     * Hold the manufacturer name
     *
     * @param manufacturer manufacturer name("RICOH")
     */
    public static void setManufacturer(String manufacturer) {
        mManufacturer = manufacturer;
    }

    /**
     * Returns the THETA serial number that is held
     *
     * @return serialNumber
     */
    public static long getThetaSerialNumber() {
        return mThetaSerialNumber;
    }

    /**
     * Hold the THETA serial number
     */
    public static void setThetaSerialNumber(long serialNumber) {
        mThetaSerialNumber = serialNumber;
    }

    /**
     * Returns the THETA model that is held
     *
     * @return ThetaModel
     */
    public static ThetaModel getThetaModel() {
        return (mThetaModel != null) ? mThetaModel : ThetaModel.THETA_DEF;
    }

    /**
     * Returns the THETA model that is held
     *
     * @param model ThetaModel
     */
    public static void setThetaModel(ThetaModel model) {
        mThetaModel = model;
    }

    /**
     * Returns the THETA firmware version that is held
     *
     * @return firmwareVersion
     */
    public static String getThetaFirmwareVersion() {
        return (mThetaFirmwareVersion != null) ? mThetaFirmwareVersion : DEFAULT_FIRMWARE_VERSION;
    }

    /**
     * Hold the THETA firmware version
     */
    public static void setThetaFirmwareVersion(String firmwareVersion) {
        mThetaFirmwareVersion = firmwareVersion;
    }

    /**
     * Returns the GPS information that is held
     *
     * @return GPS information
     */
    public static GpsInfo getGpsInfo() {
        if (mGpsInfo == null) {
            mGpsInfo = new GpsInfo();
        }
        return mGpsInfo;
    }

    /**
     * Hold the GPS information
     *
     * @param gpsInfo GPS information
     */
    public static void setGpsInfo(GpsInfo gpsInfo) {
        mGpsInfo = gpsInfo;
    }

    /**
     * Returns the sensor information that is held
     *
     * @return sensor information
     */
    public static SensorValues getSensorValues() {
        if (mSensorValues == null) {
            mSensorValues = new SensorValues();
        }
        return mSensorValues;
    }

    /**
     * Hold the sensor information
     *
     * @param sensorValues sensor information
     */
    public static void setSensorValues(SensorValues sensorValues) {
        mSensorValues = sensorValues;
    }

    /**
     * Update the camera setting value held by the object based on the setting value of Camera.Parameters.
     *
     * @param parameters Camera parameter object obtained by Camera.getParameters()
     */
    public static void setCameraParameters(@NonNull Camera.Parameters parameters) {
        mFilter = Filter.OFF;
        mExposureProgram = ExposureProgram.NORMAL_PROGRAM;
        mAperture = Aperture.APERTURE_2_0;
        mShutterSpeed = ShutterSpeed.SHUTTER_SPEED_AUTO;
        mIso = Iso.ISO_AUTO;
        mExposureCompensation = ExposureCompensation.EXPOSURE_COMPENSATION_0;
        mWhiteBalance = WhiteBalance.AUTO;
        mColorTemperature = ColorTemperature.DEFAULT.getNumber();
        mZenithCorrection = ZenithCorrection.RIC_ZENITH_CORRECTION_OFF;
        mDngOutput = false;

        Filter filter = CameraParameters.getFilter(parameters);
        if (filter != null) {
            mFilter = filter;
        }

        ExposureProgram exposureProgram = CameraParameters.getExposureProgram(parameters);
        if (exposureProgram != null) {
            mExposureProgram = exposureProgram;
        }

        Aperture aperture = CameraParameters.getAperture(parameters);
        if (aperture != null) {
            mAperture = aperture;
        } else {
            mAperture = Aperture.APERTURE_2_0;
        }

        ShutterSpeed shutterSpeed = CameraParameters.getShutterSpeed(parameters);
        if (shutterSpeed != null) {
            mShutterSpeed = shutterSpeed;
        }

        Iso iso = CameraParameters.getIso(parameters);
        if (iso != null) {
            mIso = iso;
        }

        ExposureCompensation exposureCompensation = CameraParameters.getExposureCompensation(parameters);
        if (exposureCompensation != null) {
            mExposureCompensation = exposureCompensation;
        }

        WhiteBalance whiteBalance = CameraParameters.getWhiteBalance(parameters);
        if (whiteBalance != null) {
            mWhiteBalance = whiteBalance;
        }

        Number colorTemperature = CameraParameters.getColorTemperature(parameters);
        if (colorTemperature != null) {
            mColorTemperature = colorTemperature;
        }

        ZenithCorrection zenithCorrection = CameraParameters.getZenithCorrection(parameters);
        if (zenithCorrection != null) {
            mZenithCorrection = zenithCorrection;
        }

        mDngOutput = CameraParameters.getDngOutput(parameters);
    }

    /**
     * Returns the exposure mode that is held
     *
     * @return Exposure program value
     */
    public static ExposureProgram getExposureProgram() {
        return (mExposureProgram != null) ? mExposureProgram : ExposureProgram.NORMAL_PROGRAM;
    }

    /**
     * Returns the aperture value that is held
     *
     * @return Aperture value
     */
    public static Aperture getAperture() {
        return (mAperture != null) ? mAperture : Aperture.APERTURE_2_0;
    }

    /**
     * Returns the shutter speed value that is held
     *
     * @return Shutter speed value
     */
    public static ShutterSpeed getShutterSpeed() {
        return (mShutterSpeed != null) ? mShutterSpeed : ShutterSpeed.SHUTTER_SPEED_AUTO;
    }

    /**
     * Returns the ISO value that is held
     *
     * @return Sensitivity value
     */
    public static Iso getIso() {
        return (mIso != null) ? mIso : Iso.ISO_AUTO;
    }

    /**
     * Returns the exposure compensation value that is held
     *
     * @return Exposure compensation value
     */
    public static ExposureCompensation getExposureCompensation() {
        return (mExposureCompensation != null) ? mExposureCompensation : ExposureCompensation.EXPOSURE_COMPENSATION_0;
    }

    /**
     * Returns the white balance value that is held
     *
     * @return White balance value
     */
    public static WhiteBalance getWhiteBalance() {
        return (mWhiteBalance != null) ? mWhiteBalance : WhiteBalance.AUTO;
    }

    /**
     * Returns the color temperature value that is held
     *
     * @return Color temperature value
     */
    public static Number getColorTemperature() {
        return (mColorTemperature != null) ? mColorTemperature.intValue() : ColorTemperature.DEFAULT.getInt();
    }

    /**
     * Returns the filtre value that is held
     *
     * @return Filter value
     */
    public static Filter getFilter() {
        return (mFilter != null) ? mFilter : Filter.OFF;
    }

    /**
     * Hold the sphere type value.
     *
     * @param sphereType Sphere type
     */
    public static void setSphereType(@NonNull SphereType sphereType) {
        mSphereType = sphereType;
    }

    /**
     * Returns the sphere type value that is held
     *
     * @return Sphere type value
     */
    public static SphereType getSphereType() {
        return (mSphereType != null) ? mSphereType : SphereType.EQUIRECTANGULAR;
    }

    /**
     * Returns whether there is a zenith correction
     * @return true: With zenith correction
     *          false: Without zenith correction
     */
    public static boolean isZenith() {
        return mZenithCorrection.isZenith();
    }

    /**
     * Hold the date, time  and timezone value.
     *
     * @param dateTimeZone A date, time and timezone string of a predetermined format<br>
     *                     (Format: `"YYYY:MM:DD hh:mm:ss+(-)hh:mm"`)
     */
    public static void setDateTimeZone(@NonNull String dateTimeZone) {
        if (!dateTimeZone.isEmpty()) {
            if (DateTimeFormats.isDateTimeZone(dateTimeZone)) {
                try {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DateTimeFormats.DATE_TIME_ZONE);
                    LocalDateTime localDateTime = dateTimeFormatter.parseLocalDateTime(dateTimeZone);
                    DateTime dateTime = dateTimeFormatter.withOffsetParsed().parseDateTime(dateTimeZone);
                    DateTimeZone timeZone = dateTime.getZone();

                    if (timeZone != null && localDateTime != null) {
                        int year = localDateTime.getYear();
                        if (year > 2016 && year < 2035) {
                            setDateTime(localDateTime.toDateTime().getMillis());
                            mTimeZone = timeZone.toTimeZone().getRawOffset();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Returns the date, time and timezone value that is held
     *
     * @return A date, time and timezone string
     */
    public static String getDateTimeZone() {
        LocalDateTime localDateTime;

        if (getDateTime() != -1) {
            localDateTime = LocalDateTime.fromDateFields(new Date(getDateTime()));
        } else {
            localDateTime = LocalDateTime.fromDateFields(new Date(0L));
        }

        String timeZone = getTimeZone();
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DateTimeFormats.DATE_TIME);

        return dateTimeFormatter.print(localDateTime.toDateTime()) + timeZone;
    }

    /**
     * Returns the date, time and timezone value that is held
     *
     * @return A date, time and timezone string (ISO format)
     */
    public static String getDateTimeZoneISO() {
        LocalDateTime localDateTime;

        if (getDateTime() != -1) {
            localDateTime = LocalDateTime.fromDateFields(new Date(getDateTime()));
        } else {
            localDateTime = LocalDateTime.fromDateFields(new Date(0L));
        }

        String timeZone = getTimeZone();
        DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(DateTimeFormats.DATE_ISO);
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(DateTimeFormats.TIME_ISO);

        return dateFormatter.print(localDateTime.toDateTime()) + "T" +
                timeFormatter.print(localDateTime.toDateTime()) + timeZone;
    }

    /**
     * Hold the date and time value.
     *
     * @param dateTime Date and Time (milliseconds)
     */
    public static void setDateTime(long dateTime) {
        mDateTime = dateTime;
    }

    /**
     * Returns the date and time value that is held
     *
     * @return Date and Time (millisecond)
     */
    public static long getDateTime() {
        return mDateTime;
    }

    /**
     * Hold the timezone value.
     *
     * @param timeZone Time zone (milliseconds)
     */
    public static void setTimeZone(int timeZone) {
        mTimeZone = timeZone;
    }

    /**
     * Returns the timezone value that is held
     *
     * @return Time zone string (eg. "+00:00")
     */
    public static String getTimeZone() {
        int timeZone = mTimeZone;

        if (timeZone == -1) {
            timeZone = DateTimeZone.UTC.toTimeZone().getRawOffset();
        }

        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(timeZone);
        String timezone;
        if (dateTimeZone == DateTimeZone.UTC) {
            timezone = "+00:00";
        } else {
            timezone = dateTimeZone.toString();
        }

        return timezone;
    }

    /**
     * Checks whether it not holds valid timezone information.
     *
     * @return Result<br>
     *     true: Not have valid value<br>
     *     false: Have valid value
     */
    public static boolean isEmptyTimeZone() {
        int timeZone = mTimeZone;
        return timeZone == -1;
    }

    /**
     * Returns the held microphone gain value
     *
     * @return microphone gain value
     */
    public static Gain getGain() {
        return (mGain != null) ? mGain : Gain.NORMAL;
    }

    /**
     * Hold the microphone gain.
     * @param value
     */
    public static void setGain(String value) {
        Gain gain = Gain.getValueFromIndex(value);
        if (gain != null) {
            mGain = gain;
        }
    }

    /**
     * Returns the held DngOutput Flag
     * @return Dng output flag<br>
     *      true: DngFile output
     *      false: DngFile not output
     */
    public static boolean isDngOutput() {
        return mDngOutput;
    }

    /**
     * CameraParameters
     */
    static class CameraParameters {
        // RIC_SHOOTING_MODE
        static final String KEY_STITCH_USE_CASE = "RIC_SHOOTING_MODE";
        static final String RIC_STILL_CAPTURE_STD = "RicStillCaptureStd";
        static final String RIC_STILL_CAPTURE_WDR = "RicStillCaptureWDR";
        static final String RIC_STILL_CAPTURE_MULTI_RAW_NR = "RicStillCaptureMultiRawNR";
        static final String RIC_STILL_CAPTURE_MULTI_YUV_HDR = "RicStillCaptureMultiYuvHdr";
        static final String RIC_STILL_CAPTURE_RAW = "RicStillCaptureCompositeRAW";
        static final String RIC_STILL_CAPTURE_YUV = "RicStillCaptureCompositeYUV";

        // RIC_EXPOSURE_MODE
        private static final String KEY_RIC_EXP_MODE = "RIC_EXPOSURE_MODE";

        // RIC_MANUAL_EXPOSURE_AV
        private static final String KEY_RIC_MANUAL_EXPOSURE_AV_FRONT = "RIC_MANUAL_EXPOSURE_AV_FRONT";
        private static final String KEY_RIC_MANUAL_EXPOSURE_AV_REAR = "RIC_MANUAL_EXPOSURE_AV_REAR";

        // RIC_MANUAL_EXPOSURE_ISO
        private static final String KEY_RIC_MANUAL_EXPOSURE_ISO_PRIMARY = "RIC_MANUAL_EXPOSURE_ISO_REAR";
        private static final String KEY_RIC_MANUAL_EXPOSURE_ISO_SECONDARY = "RIC_MANUAL_EXPOSURE_ISO_FRONT";

        // RIC_MANUAL_EXPOSURE_TIME
        private static final String KEY_RIC_MANUAL_EXPOSURE_PRIMARY = "RIC_MANUAL_EXPOSURE_TIME_REAR";
        private static final String KEY_RIC_MANUAL_EXPOSURE_SECONDARY = "RIC_MANUAL_EXPOSURE_TIME_FRONT";

        // RIC_WB_MODE, RIC_WB_TEMPERATURE
        private static final String KEY_RIC_WB_MODE = "RIC_WB_MODE";
        private static final String KEY_RIC_WB_TEMPERATURE = "RIC_WB_TEMPERATURE";

        // RIC_PROC_ZENITH_CORRECTION
        private static final String KEY_RIC_PROC_ZENITH_CORRECTION = "RIC_PROC_ZENITH_CORRECTION";

        // RIC_DNG_OUTPUT_ENABLED
        private static final String KEY_RIC_DNG_OUTPUT_ENABLED = "RIC_DNG_OUTPUT_ENABLED";

        static ExposureProgram getExposureProgram(@NonNull Camera.Parameters parameters) {
            String exposureProgram = parameters.get(KEY_RIC_EXP_MODE);
            return (exposureProgram != null) ? ExposureProgram.getValueFromIndex(exposureProgram) : ExposureProgram.NORMAL_PROGRAM;
        }

        static Aperture getAperture(@NonNull Camera.Parameters parameters) {
            String apertureIndex = parameters.get(KEY_RIC_MANUAL_EXPOSURE_AV_REAR);
            if (apertureIndex == null) {
                apertureIndex = parameters.get(KEY_RIC_MANUAL_EXPOSURE_AV_FRONT);
            }
            return (apertureIndex != null) ? Aperture.getValueFromIndex(Integer.parseInt(apertureIndex)) : Aperture.APERTURE_2_0;
        }

        static ShutterSpeed getShutterSpeed(@NonNull Camera.Parameters parameters) {
            String shutterSpeed = parameters.get(KEY_RIC_MANUAL_EXPOSURE_PRIMARY);
            if (shutterSpeed == null) {
                shutterSpeed = parameters.get(KEY_RIC_MANUAL_EXPOSURE_SECONDARY);
            }
            return (shutterSpeed != null) ? ShutterSpeed.getValueFromIndex(Integer.parseInt(shutterSpeed)) : ShutterSpeed.SHUTTER_SPEED_AUTO;
        }

        static Iso getIso(@NonNull Camera.Parameters parameters) {
            String iso = parameters.get(KEY_RIC_MANUAL_EXPOSURE_ISO_PRIMARY);
            if (iso == null) {
                iso = parameters.get(KEY_RIC_MANUAL_EXPOSURE_ISO_SECONDARY);
            }
            return (iso != null) ? Iso.getValueFromIndex(Integer.parseInt(iso)) : Iso.ISO_AUTO;
        }

        static ExposureCompensation getExposureCompensation(@NonNull Camera.Parameters parameters) {
            int exposureCompensationIndex = parameters.getExposureCompensation();
            ExposureCompensation exposureCompensation = ExposureCompensation.getValueFromIndex(exposureCompensationIndex);
            return (exposureCompensation != null) ? exposureCompensation : ExposureCompensation.EXPOSURE_COMPENSATION_0;
        }

        static WhiteBalance getWhiteBalance(@NonNull Camera.Parameters parameters) {
            String whiteBalance = parameters.get(KEY_RIC_WB_MODE);
            return (whiteBalance != null) ? WhiteBalance.getValueFromIndex(whiteBalance) : WhiteBalance.AUTO;
        }

        static Number getColorTemperature(@NonNull Camera.Parameters parameters) {
            String colorTemperature = parameters.get(KEY_RIC_WB_TEMPERATURE);
            if (colorTemperature != null) {
                return Integer.parseInt(colorTemperature);
            } else {
                return ColorTemperature.DEFAULT.getInt();
            }
        }

        static Filter getFilter(@NonNull Camera.Parameters parameters) {
            String useCase = parameters.get(KEY_STITCH_USE_CASE);
            return (useCase != null) ? Filter.getValueFromIndex(useCase) : Filter.OFF;
        }

        static ZenithCorrection getZenithCorrection(@NonNull Camera.Parameters parameters) {
            String zenithCorrection = parameters.get(KEY_RIC_PROC_ZENITH_CORRECTION);
            return (zenithCorrection != null) ? ZenithCorrection.getValueFromIndex(zenithCorrection) : ZenithCorrection.RIC_ZENITH_CORRECTION_OFF;
        }

        static boolean getDngOutput(@NonNull Camera.Parameters parameters) {
            String dngOutput = parameters.get(KEY_RIC_DNG_OUTPUT_ENABLED);
            if (dngOutput != null && Integer.parseInt(dngOutput) == 1) {
                return true;
            } else {
                return false;
            }
        }
    }
}
