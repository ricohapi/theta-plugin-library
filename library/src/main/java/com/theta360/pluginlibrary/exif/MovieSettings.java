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

/**
 * MovieSettings class
 */
public class MovieSettings {
    private static String sManufacturer;
    private static String sModel;
    private static String sSerialNumber;
    private static String sFirmwareVersion;
    private static GpsInfo sGpsInfo;
    private static SensorValues sSensorValues;

    private static final String DEFAULT_MANUFACTURER = "RICOH";
    private static final String DEFAULT_MODEL = "RICOH THETA";
    private static final String DEFAULT_SERIAL_NUMBER = "00000000";
    private static final String DEFAULT_FIRMWARE_VERSION = ".";

    /**
     * Initialize the movie settings held by the object.
     */
    public static void initialize() {
        sManufacturer = DEFAULT_MANUFACTURER;
        sModel = DEFAULT_MODEL;
        sSerialNumber = DEFAULT_SERIAL_NUMBER;
        sFirmwareVersion = DEFAULT_FIRMWARE_VERSION;
        sGpsInfo = new GpsInfo();
        sSensorValues = new SensorValues();
    }

    /**
     * Set manufacturer name to give Box
     *
     * @param manufacturer manufacturer name("RICOH")
     */
    public static void setManufacturer(String manufacturer){
        sManufacturer = manufacturer;
    }

    /**
     * Acquire the manufacturer name to give Box
     * @return manufacturer name("RICOH")
     */
    public static String getManufacturer(){
        return (sManufacturer != null) ? sManufacturer : DEFAULT_MANUFACTURER;
    }

    /**
     * Set serial number to give Box
     *
     * @param serialNumber
     */
    public static void setSerialNumber(String serialNumber){
        sSerialNumber = serialNumber;
    }

    /**
     * Get serial number to give Box
     *
     * @return serialNumber
     */
    public static String getSerialNumber(){
        return (sSerialNumber != null) ? sSerialNumber : DEFAULT_SERIAL_NUMBER;
    }

    /**
     * Set the model name to give Box
     *
     * @param model Model name("RICOH THETA 〇")
     */
    public static void setModel(String model){
        sModel = model;
    }

    /**
     * Get the model name to give Box
     *
     * @return Model name("RICOH THETA 〇")
     */
    public static String getModel(){
        return (sModel != null) ? sModel : DEFAULT_MODEL;
    }

    /**
     * Set the firmware version to be given Box
     *
     * @param firmwareVersion
     */
    public static void setFirmwareVersion(String firmwareVersion){
        sFirmwareVersion = firmwareVersion;
    }

    /**
     * Get the firmware version to give Box
     *
     * @return firmwareVersion
     */
    public static String getFirmwareVersion(){
        return (sFirmwareVersion != null) ? sFirmwareVersion : DEFAULT_FIRMWARE_VERSION;
    }

    /**
     * Set GPS information to be given to Box
     *
     * @param gpsInfo GPS information
     */
    public static void setGpsInfo(GpsInfo gpsInfo){
        sGpsInfo = gpsInfo;
    }

    /**
     * Get GPS information to give Box
     *
     * @return GPS information
     */
    public static GpsInfo getGpsInfo(){
        if(sGpsInfo == null){
            sGpsInfo = new GpsInfo();
        }
        return sGpsInfo;
    }

    /**
     * Set sensor information to be given to Box
     *
     * @param sensorValues sensor information
     */
    public static void setSensorValues(SensorValues sensorValues){
        sSensorValues = sensorValues;
    }

    /**
     * Get sensor information to give Box
     *
     * @return sensor information
     */
    public static SensorValues getSensorValues(){
        if(sSensorValues == null){
            sSensorValues = new SensorValues();
        }
        return sSensorValues;
    }
}
