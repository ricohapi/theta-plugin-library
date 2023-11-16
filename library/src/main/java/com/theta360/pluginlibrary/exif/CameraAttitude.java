/**
 * Copyright 2018 Ricoh Company, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.theta360.pluginlibrary.exif;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.annotation.NonNull;

/**
 * CameraAttitude
 */
public class CameraAttitude implements SensorEventListener {
    private final float[] mAccelerometer;
    private final float[] mMagnetic;
    private final float[] mGyroscope;
    private SensorManager mSensorManager;
    private boolean mAccuracy;

    private float[] mAttitudeSnupshot;
    private boolean mAccuracySnupshot;

    public CameraAttitude(@NonNull Context context) {
        mAccelerometer = new float[3];
        mMagnetic = new float[3];
        mGyroscope = new float[3];
        mAccuracy = true;

        mAttitudeSnupshot = new float[3];
        mAccuracySnupshot = true;

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void register() {
        if (mSensorManager != null) {
            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                    SensorManager.SENSOR_DELAY_FASTEST);
            mSensorManager.registerListener(this,
                    mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                    SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void unregister() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, mAccelerometer, 0, mAccelerometer.length);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, mMagnetic, 0, mMagnetic.length);
                break;
            case Sensor.TYPE_GYROSCOPE:
                System.arraycopy(event.values, 0, mGyroscope, 0, mGyroscope.length);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        switch (sensor.getType()) {
            case Sensor.TYPE_MAGNETIC_FIELD:
                switch (accuracy) {
                    case SensorManager.SENSOR_STATUS_UNRELIABLE:
                        mAccuracy = false;
                        break;
                    case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                        mAccuracy = true;
                        break;
                    default:
                        break;
                }
            default:
                break;
        }
    }

    public boolean getAccuracy() {
        return mAccuracy;
    }

    public float[] getAttitudeRadian() {
        float[] inR = new float[16];
        float[] outR = new float[16];
        float[] I = new float[16];
        float[] attitude = new float[3];

        SensorManager.getRotationMatrix(inR, I, mAccelerometer, mMagnetic);
        SensorManager.remapCoordinateSystem(inR, SensorManager.AXIS_X, SensorManager.AXIS_Y, outR);
        SensorManager.getOrientation(outR, attitude);

        return attitude;
    }

    public void snapshot() {
        mAccuracySnupshot = getAccuracy();
        mAttitudeSnupshot = getAttitudeRadian();
    }

    public boolean getAccuracySnapshot() {
        return mAccuracySnupshot;
    }

    public float[] getAttitudeRadianSnapshot() {
        return mAttitudeSnupshot;
    }
}
