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

import java.util.Locale;

/**
 * Xmp
 */
public class Xmp {
    private static final String XML_START = "<?xml version=\"1.0\"?>";
    private static final String RDF_START = "  <rdf:SphericalVideo xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:GSpherical=\"http://ns.google.com/videos/1.0/spherical/\">";
    private static final String RDF_END = "  </rdf:SphericalVideo>";
    private static final String SPHERICAL = "    <GSpherical:Spherical>true</GSpherical:Spherical>";
    private static final String STITCHED = "    <GSpherical:Stitched>true</GSpherical:Stitched>";
    private static final String STITCHING_SOFTWARE_START = "    <GSpherical:StitchingSoftware>";
    private static final String STITCHING_SOFTWARE_END = "</GSpherical:StitchingSoftware>";
    private static final String PROJECTION_TYPE = "    <GSpherical:ProjectionType>equirectangular</GSpherical:ProjectionType>";
    private static final String SOURCE_COUNT = "    <GSpherical:SourceCount>1</GSpherical:SourceCount>";
    private static final String TIMESTAMP_START = "    <GSpherical:Timestamp>";
    private static final String TIMESTAMP_END = "</GSpherical:Timestamp>";

    private static final String LF = "\n";

    public static byte[] getXmp(String modelName, String verName, long timestamp) {
        StringBuilder sb = new StringBuilder();
        sb.append(XML_START);
        sb.append(LF);
        sb.append(RDF_START);
        sb.append(LF);
        sb.append(SPHERICAL);
        sb.append(LF);
        sb.append(STITCHED);
        sb.append(LF);
        sb.append(String.format(Locale.ENGLISH, "%s%s%s%s%s", STITCHING_SOFTWARE_START, modelName, " ", verName,
                STITCHING_SOFTWARE_END));
        sb.append(LF);
        sb.append(PROJECTION_TYPE);
        sb.append(LF);
        sb.append(SOURCE_COUNT);
        sb.append(LF);
        sb.append(
                String.format(Locale.ENGLISH, "%s%d%s", TIMESTAMP_START, timestamp, TIMESTAMP_END));
        sb.append(LF);
        sb.append(RDF_END);

        return sb.toString().getBytes();
    }
}
