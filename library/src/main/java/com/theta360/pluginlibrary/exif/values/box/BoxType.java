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

package com.theta360.pluginlibrary.exif.values.box;

import java.io.UnsupportedEncodingException;

/**
 * BoxType
 */
public enum BoxType {
    FTYP("ftyp"),
    MDAT("mdat"),
    FREE("free"),
    MOOV("moov"),
    MOOVMVHD("mvhd"),
    MOOVMETA("meta"),
    MOOVTRAK("trak"),
    MOOVTKHD("tkhd"),
    MOOVMDIA("mdia"),
    MOOVMDHD("mdhd"),
    MOOVMINF("minf"),
    MOOVSTBL("stbl"),
    MOOVSTSD("stsd"),
    MOOVAVC1("avc1"),
    MOOVAVCC("avcC"),
    MOOVPASP("pasp"),
    MOOVCOLR("colr"),
    MOOVHVC1("hvc1"),
    MOOVHVCC("hvcC"),
    MOOVCO64("co64"),
    MOOVSTCO("stco"),
    MOOVTRAKSOUND("trak"),
    MOOVUUID("uuid"),
    UDTA("udta"),
    RTHU("RTHU"),
    RMKN("RMKN"),
    RDT1("RDT1"),
    RDT2("RDT2"),
    RDT3("RDT3"),
    RDT4("RDT4"),
    RDT5("RDT5"),
    RDT6("RDT6"),
    RDT7("RDT7"),
    RDT8("RDT8"),
    RDT9("RDT9"),
    RDTA("RDTA"),
    RDTB("RDTB"),
    RDTC("RDTC"),
    RDTD("RDTD"),
    RDTG("RDTG"),
    RDTI("RDTI"),
    AMOD("@mod"),
    ASWR("@swr"),
    ADAY("@day"),
    AXYZ("@xyz"),
    AMAK("@mak"),
    MANU("manu"),
    MODL("modl"),
    RADT("RADT"),
    RATR("RATR"),
    RATRtkhd("tkhd"),
    RATRedts("edts"),
    RATRelst("elst"),
    RATRmdia("mdia"),
    RATRmdhd("mdhd"),
    RATRhdlr("hdlr"),
    RATRminf("minf"),
    RATRsmhd("smhd"),
    RATRminfhdlr("hdlr"),
    RATRdinf("dinf"),
    RATRdref("dref"),
    RATRstbl("stbl"),
    RATRstsd("stsd"),
    RATRsowt("sowt"),
    RATRchan("chan"),
    RATRSA3D("SA3D"),
    RATRstts("stts"),
    RATRstsc("stsc"),
    RATRstsz("stsz"),
    RATRco64("co64"),
    UUID("uuid"),;

    private final String mName;
    private byte[] mValue;

    BoxType(final String name) {
        mName = name;
        try {
            mValue = name.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
    }

    public static BoxType getValue(final String _boxType) {
        for (BoxType boxType : BoxType.values()) {
            if (boxType.toString().equals(_boxType)) {
                return boxType;
            }
        }

        return null;
    }

    public String getName() {
        return mName;
    }

    public byte[] getValue() {
        return mValue;
    }
}
