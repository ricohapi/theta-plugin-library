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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import android.support.annotation.NonNull;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.jpeg.xmp.JpegXmpRewriter;

/**
 * XMP class
 */
public class Xmp {
    private static final String XPACKET_START = "<?xpacket begin=\"\" id=\"W5M0MpCehiHzreSzNTczkc9d\"?>";
    private static final String XPACKET_END = "<?xpacket end=\"r\"?>";
    private static final String XMPMETA_START = "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\" xmptk=\"";
    private static final String XMPMETA_END = "</x:xmpmeta>";
    private static final String RDF_START = "  <rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">";
    private static final String RDF_END = "  </rdf:RDF>";
    private static final String DESCRIPTION_START = "    <rdf:Description rdf:about=\"\" xmlns:GPano=\"http://ns.google.com/photos/1.0/panorama/\">";
    private static final String DESCRIPTION_END = "    </rdf:Description>";
    private static final String PROJECTTION_TYPE_START = "      <GPano:ProjectionType>";
    private static final String PROJECTTION_TYPE_END = "</GPano:ProjectionType>";
    private static final String USE_PANORAMA_VIEWER_START = "      <GPano:UsePanoramaViewer>";
    private static final String USE_PANORAMA_VIEWER_END = "</GPano:UsePanoramaViewer>";
    private static final String CROPPED_AREA_IMAGE_WIDTH_PIXELS_START = "      <GPano:CroppedAreaImageWidthPixels>";
    private static final String CROPPED_AREA_IMAGE_WIDTH_PIXELS_END = "</GPano:CroppedAreaImageWidthPixels>";
    private static final String CROPPED_AREA_IMAGE_HEIGHT_PIXELS_START = "      <GPano:CroppedAreaImageHeightPixels>";
    private static final String CROPPED_AREA_IMAGE_HEIGHT_PIXELS_END = "</GPano:CroppedAreaImageHeightPixels>";
    private static final String FULL_PANO_WIDTH_PIXELS_START = "      <GPano:FullPanoWidthPixels>";
    private static final String FULL_PANO_WIDTH_PIXELS_END = "</GPano:FullPanoWidthPixels>";
    private static final String FULL_PANO_HEIGHT_PIXELS_START = "      <GPano:FullPanoHeightPixels>";
    private static final String FULL_PANO_HEIGHT_PIXELS_END = "</GPano:FullPanoHeightPixels>";
    private static final String CROPPED_AREA_LEFT_PIXELS_START = "      <GPano:CroppedAreaLeftPixels>";
    private static final String CROPPED_AREA_LEFT_PIXELS_END = "</GPano:CroppedAreaLeftPixels>";
    private static final String CROPPED_AREA_TOP_PIXELS_START = "      <GPano:CroppedAreaTopPixels>";
    private static final String CROPPED_AREA_TOP_PIXELS_END = "</GPano:CroppedAreaTopPixels>";
    private static final String POSE_HEADING_DEGREES_START = "      <GPano:PoseHeadingDegrees>";
    private static final String POSE_HEADING_DEGREES_END = "</GPano:PoseHeadingDegrees>";
    private static final String POSE_PITCH_DEGREES_START = "      <GPano:PosePitchDegrees>";
    private static final String POSE_PITCH_DEGREES_END = "</GPano:PosePitchDegrees>";
    private static final String POSE_ROLL_DEGREES_START = "      <GPano:PoseRollDegrees>";
    private static final String POSE_ROLL_DEGREES_END = "</GPano:PoseRollDegrees>";

    private static final String PROJECTTION_TYPE_VALUE = "equirectangular";
    private static final String USE_PANORAMA_VIEWER_VALUE = "True";
    private static final int CROPPED_AREA_LEFT_PIXELS_VALUE = 0;
    private static final int CROPPED_AREA_TOP_PIXELS_VALUE = 0;

    private static final String LF = "\n";

    /**
     * Adds XMP(APP1) to captured image data and outputs it to the stream.。
     *
     * @param data Image data
     * @param os Output stream object (eg. FileOutputStream)
     * @param width Image width (pixels)
     * @param height Image height (pixels)
     * @param sensorValues SensorValues object
     * @param pitch PitchRoll PitchValue
     * @param roll PitchRoll RollValue
     * @param modelName Model name
     * @param verName Version name
     */
    public static void setXmp(@NonNull byte[] data, @NonNull OutputStream os, int width, int height, @NonNull SensorValues sensorValues, int pitch, int roll,@NonNull String modelName, @NonNull String verName) {
        int compass = 0;

        if (sensorValues != null) {
            compass = calcCompass(sensorValues);
        }

        // Make XMPToolkit string
        String xmptk = modelName + " Ver" + verName;

        StringBuilder sb = new StringBuilder();
        sb.append(XPACKET_START);
        sb.append(LF);
        sb.append(XMPMETA_START);
        sb.append(xmptk);
        sb.append("\">");

        sb.append(LF);
        sb.append(RDF_START);
        sb.append(LF);
        sb.append(DESCRIPTION_START);
        sb.append(LF);
        sb.append(String.format(Locale.ENGLISH, "%s%s%s", PROJECTTION_TYPE_START,
                PROJECTTION_TYPE_VALUE, PROJECTTION_TYPE_END));
        sb.append(LF);
        sb.append(String.format(Locale.ENGLISH, "%s%s%s", USE_PANORAMA_VIEWER_START,
                USE_PANORAMA_VIEWER_VALUE, USE_PANORAMA_VIEWER_END));
        sb.append(LF);
        sb.append(String.format(Locale.ENGLISH, "%s%d%s", CROPPED_AREA_IMAGE_WIDTH_PIXELS_START,
                width, CROPPED_AREA_IMAGE_WIDTH_PIXELS_END));
        sb.append(LF);
        sb.append(String.format(Locale.ENGLISH, "%s%d%s", CROPPED_AREA_IMAGE_HEIGHT_PIXELS_START,
                height, CROPPED_AREA_IMAGE_HEIGHT_PIXELS_END));
        sb.append(LF);
        sb.append(String.format(Locale.ENGLISH, "%s%d%s", FULL_PANO_WIDTH_PIXELS_START,
                width, FULL_PANO_WIDTH_PIXELS_END));
        sb.append(LF);
        sb.append(String.format(Locale.ENGLISH, "%s%d%s", FULL_PANO_HEIGHT_PIXELS_START,
                height, FULL_PANO_HEIGHT_PIXELS_END));
        sb.append(LF);
        sb.append(String.format(Locale.ENGLISH, "%s%d%s", CROPPED_AREA_LEFT_PIXELS_START,
                CROPPED_AREA_LEFT_PIXELS_VALUE, CROPPED_AREA_LEFT_PIXELS_END));
        sb.append(LF);
        sb.append(String.format(Locale.ENGLISH, "%s%d%s", CROPPED_AREA_TOP_PIXELS_START,
                CROPPED_AREA_TOP_PIXELS_VALUE, CROPPED_AREA_TOP_PIXELS_END));
        sb.append(LF);
        // If the accuracy of the compass is low, the following information is not recorded.
        if (sensorValues != null && sensorValues.getCompassAccuracy()) {
            sb.append(String.format(Locale.ENGLISH, "%s%.1f%s", POSE_HEADING_DEGREES_START,
                    compass / 100.0, POSE_HEADING_DEGREES_END));
            sb.append(LF);
        }
        sb.append(String.format(Locale.ENGLISH, "%s%.1f%s", POSE_PITCH_DEGREES_START, pitch / 100.0,
                POSE_PITCH_DEGREES_END));
        sb.append(LF);
        sb.append(String.format(Locale.ENGLISH, "%s%.1f%s", POSE_ROLL_DEGREES_START,
                (roll > 18000 ? roll - 36000 : roll) / 100.0, POSE_ROLL_DEGREES_END));
        sb.append(LF);
        sb.append(DESCRIPTION_END);
        sb.append(LF);
        sb.append(RDF_END);
        sb.append(LF);
        sb.append(XMPMETA_END);
        sb.append(LF);
        sb.append(XPACKET_END);
        sb.append(LF);

        try {
            new JpegXmpRewriter().updateXmpXml(data, os, sb.toString());
        } catch (ImageReadException | IOException | ImageWriteException e) {
            e.printStackTrace();
        }
    }

    private static int calcCompass(@NonNull SensorValues sensorValues) {
        float[] attitude = sensorValues.getAttitudeRadian();
        double receptYaw = Math.toDegrees(attitude[0]);
        if (receptYaw < 0) {
            receptYaw = 360 + receptYaw;
        }
        return (int) (receptYaw * 100);
    }
}