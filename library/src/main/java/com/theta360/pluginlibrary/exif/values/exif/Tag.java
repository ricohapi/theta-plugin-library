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

package com.theta360.pluginlibrary.exif.values.exif;

/**
 * Tag
 */
public enum Tag {
    TAG_IMAGEDESCRIPTION(0x010E),
    TAG_MAKE(0x010f),
    TAG_MODEL(0x0110),
    TAG_STRIPOFFSETS(0x0111),
    TAG_STRIPBYTECOUNTS(0x0117),
    TAG_ORIENTATION(0x0112),
    TAG_SOFTWARE(0x0131),
    TAG_SUBIFDS(0x14A),
    TAG_COPYRIGHT(0x8298),
    TAG_EXIFIFDPOINTER(0x8769),
    TAG_GPSINFOIFDPOINTER(0x8825),

    TAG_EXPOSURETIME(0x829A),
    TAG_FNUMBER(0x829D),
    TAG_EXPOSUREPROGRAM(0x8822),
    TAG_ISOSPEEDRATINGS(0x8827),
    TAG_SENSITIVITYTYPE(0x8830),
    TAG_STANDARDOUTPUTSENSITIVITY(0x8831),
    TAG_COMPRESSEDBITSPERPIXEX(0x9102),
    TAG_APERTUREVALUE(0x9202),
    TAG_EXPOSUREBIASVALUE(0x9204),
    TAG_MAXAPERTUREVALUE(0x9205),
    TAG_LIGHTSOURCE(0x9208),
    TAG_MAKERNOTE(0x927C),
    TAG_USERCOMMENT(0x9286),
    TAG_PIXELXDIMENSION(0xA002),
    TAG_PIXELYDIMENSION(0xA003),
    TAG_EXPOSUREMODE(0xA402),
    TAG_WHITEBALANCE(0xA403),
    TAG_SCENECAPTURETYPE(0xA406),
    TAG_SHARPNESS(0xA40A),

    TAG_GPSVERSIONID(0x0000),
    TAG_GPSLATITUDEREF(0x0001),
    TAG_GPSLATITUDE(0x0002),
    TAG_GPSLONGITUDEREF(0x0003),
    TAG_GPSLONGITUDE(0x0004),
    TAG_GPSALTITUDEREF(0x0005),
    TAG_GPSALTITUDE(0x0006),
    TAG_GPSTIMESTAMP(0x0007),
    TAG_GPSIMGDIRECTIONREF(0x0010),
    TAG_GPSIMGDIRECTION(0x0011),
    TAG_GPSMAPDATUM(0x0012),
    TAG_GPSDATESTAMP(0x001d),

    TAG_RM_0001(0x0001),
    TAG_RM_0002(0x0002),
    TAG_RM_0003(0x0003),
    TAG_RM_0005(0x0005),
    TAG_RM_0006(0x0006),
    TAG_RM_0007(0x0007),
    TAG_RM_1000(0x1000),
    TAG_RM_1001(0x1001),
    TAG_RM_1003(0x1003),
    TAG_RM_1307(0x1307),
    TAG_RM_4001(0x4001),
    TAG_RM_4002(0x4002),
    TAG_RM_4003(0x4003),
    TAG_RM_4004(0x4004),
    TAG_RM_4005(0x4005),
    TAG_RM_1900(0x1900),
    TAG_RM_1901(0x1901),
    TAG_RM_1902(0x1902),
    TAG_RM_2001(0x2001),

    TAG_RA_9001(0x9001),
    TAG_RA_9002(0x9002),

    TAG_R_0001(0x0001),
    TAG_R_0002(0x0002),
    TAG_R_0003(0x0003),
    TAG_R_0004(0x0004),
    TAG_R_0005(0x0005),
    TAG_R_0006(0x0006),
    TAG_R_0007(0x0007),
    TAG_R_0008(0x0008),
    TAG_R_0009(0x0009),
    TAG_R_000A(0x000A),
    TAG_R_0101(0x0101),
    TAG_R_0102(0x0102),
    TAG_R_0103(0x0103),
    TAG_R_0104(0x0104),
    TAG_R_0105(0x0105),
    TAG_R_0106(0x0106),
    TAG_R_0107(0x0107),
    TAG_R_0108(0x0108),
    TAG_R_0109(0x0109),
    TAG_R_1001(0x1001),
    TAG_R_1002(0x1002),
    TAG_R_1003(0x1003),
    TAG_R_1004(0x1004),
    TAG_R_1005(0x1005),
    TAG_R_1006(0x1006),
    TAG_R_1007(0x1007),
    TAG_R_1008(0x1008),
    TAG_R_1009(0x1009),
    TAG_R_100A(0x100A),
    TAG_R_100B(0x100B),
    TAG_R_100C(0x100C),
    TAG_R_100D(0x100D),
    TAG_R_100E(0x100E),
    TAG_R_100F(0x100F),
    TAG_R_1010(0x1010),
    TAG_R_1011(0x1011),
    TAG_R_1012(0x1012),
    TAG_R_1013(0x1013),
    TAG_R_1014(0x1014),
    TAG_R_1015(0x1015),

    TAG_JPEGICFORMAT(0x0201),
    TAG_JPEGICFORMATLENGTH(0x0202),;

    private final int mTagID;

    Tag(final int tagID) {
        this.mTagID = tagID;
    }

    public int getTagID() {
        return mTagID;
    }
}
