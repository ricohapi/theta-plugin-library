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

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import com.theta360.pluginlibrary.exif.objects.box.AXYZ;
import com.theta360.pluginlibrary.exif.objects.box.BoxSpec;
import com.theta360.pluginlibrary.exif.objects.box.ColorSpace;
import com.theta360.pluginlibrary.exif.objects.box.RATR;
import com.theta360.pluginlibrary.exif.objects.box.RMKN;
import com.theta360.pluginlibrary.exif.objects.box.Timestamp;
import com.theta360.pluginlibrary.exif.objects.box.Xmp;
import com.theta360.pluginlibrary.exif.values.SphereType;
import com.theta360.pluginlibrary.exif.values.box.BoxType;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.apache.sanselan.util.IOUtils;

/**
 * Box
 */
public class Box {
    public interface Callback {
        /**
         * Callback when Box granting process ends normally
         *
         * @param fileUrls param[0]:MP4 file path param[1]:WAV file path
         */
        void onCompleted(String[] fileUrls);

        /**
         * Callback when Box granting process ends abnormally
         */
        void onError();
    }

    private Callback mCallback;

    /**
     * BoxTask class
     * (Asynchronous execution)
     */
    private class BoxTask extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            try(BoxData boxData = new BoxData(params[0], params[1])) {
                boxData.write();
            } catch(IOException e) {
                return null;
            }
            return params;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (mCallback != null) {
                if (result != null) {
                    mCallback.onCompleted(result);
                } else {
                    mCallback.onError();
                }
            }
        }
    }

    /**
     * Form Box data. Call Callback when finished
     *
     * @param mp4Path MP4 file path
     * @param wavPath WAV file path
     * @param callback End callback
     */
    public void formBox(String mp4Path, String wavPath, Callback callback) {
        mCallback = callback;
        BoxTask task = new BoxTask();
        task.execute(mp4Path, wavPath);

    }

    /**
     * BoxData
     */
    private class BoxData implements AutoCloseable {
        private static final int BOX_SIZE_LEN = 4;
        private static final int BOX_TYPE_LEN = 4;
        private static final int BOX_HEADER_LEN = BOX_SIZE_LEN + BOX_TYPE_LEN;
        private static final int BOX_SIZE_LARGER = 1;
        private static final int WAV_HEADER_LEN = 44;

        private static final int CELLSIZE = 512 * 1024;

        private static final String MAKER_ID = "RICOH\0";

        private final byte[] UUID_USER_TYPE = {
                0x28, (byte) 0xF3, 0x11, (byte) 0xE2, (byte) 0xB7, (byte) 0x91, 0x4F, 0x6F,
                (byte) 0x94, (byte) 0xE2, 0x4F, 0x5D, (byte) 0xEA, (byte) 0xCB, 0x3C, 0x01};

        private final byte[] UUID_SPHERICAL = {
                (byte) 0xff, (byte) 0xcc, (byte) 0x82, 0x63, (byte) 0xf8, 0x55, 0x4a, (byte) 0x93,
                (byte) 0x88, 0x14, 0x58, 0x7a, 0x02, 0x52, 0x1f, (byte) 0xdd};

        private RandomAccessFile mRandomAccessFile;
        private String mMp4FilePath;
        private String mWavFilePath;
        private long mEndPos;
        private BoxSpec[] rootBoxSpecs;
        private BoxSpec[] moovBoxSpecs;
        private BoxSpec[] udtaBoxSpecs;
        private BoxSpec[] uuidBoxSpecs;

        private BoxSpec mBoxSpecSound;

        private Timestamp mTimeMVHD = new Timestamp();
        private Timestamp mTimeTKHD = new Timestamp();
        private Timestamp mTimeMDHD = new Timestamp();
        private Timestamp mTimeTKHDSound = new Timestamp();
        private Timestamp mTimeMDHDSound = new Timestamp();

        public BoxData(String mp4FilePath, String wavFilePath)
                throws IOException, RuntimeException {
            mMp4FilePath = mp4FilePath;
            mWavFilePath = wavFilePath;

            rootBoxSpecs = new BoxSpec[]{
                    new BoxSpec(BoxType.FTYP),
                    new BoxSpec(BoxType.MDAT),
                    new BoxSpec(BoxType.MOOV),
                    new BoxSpec(BoxType.FREE),
                    new BoxSpec(BoxType.UUID),
            };
            moovBoxSpecs = new BoxSpec[]{
                    new BoxSpec(BoxType.MOOVMVHD),
                    new BoxSpec(BoxType.MOOVMETA),
                    new BoxSpec(BoxType.MOOVTRAK),
                    new BoxSpec(BoxType.MOOVTKHD),
                    new BoxSpec(BoxType.MOOVMDIA),
                    new BoxSpec(BoxType.MOOVMDHD),
                    new BoxSpec(BoxType.MOOVMINF),
                    new BoxSpec(BoxType.MOOVSTBL),
                    new BoxSpec(BoxType.MOOVSTSD),
                    new BoxSpec(BoxType.MOOVAVC1),
                    new BoxSpec(BoxType.MOOVAVCC),
                    new BoxSpec(BoxType.MOOVPASP),
                    new BoxSpec(BoxType.MOOVCOLR),
                    new BoxSpec(BoxType.MOOVHVC1),
                    new BoxSpec(BoxType.MOOVHVCC),
                    new BoxSpec(BoxType.MOOVCO64),
                    new BoxSpec(BoxType.MOOVSTCO),
                    new BoxSpec(BoxType.MOOVTRAKSOUND),
                    new BoxSpec(BoxType.MOOVUUID),
                    new BoxSpec(BoxType.UDTA),
            };
            udtaBoxSpecs = new BoxSpec[]{
                    new BoxSpec(BoxType.RTHU),
                    new BoxSpec(BoxType.RMKN),
                    new BoxSpec(BoxType.RDT1),
                    new BoxSpec(BoxType.RDT2),
                    new BoxSpec(BoxType.RDT3),
                    new BoxSpec(BoxType.RDT4),
                    new BoxSpec(BoxType.RDT5),
                    new BoxSpec(BoxType.RDT6),
                    new BoxSpec(BoxType.RDT7),
                    new BoxSpec(BoxType.RDT8),
                    new BoxSpec(BoxType.RDT9),
                    new BoxSpec(BoxType.RDTA),
                    new BoxSpec(BoxType.RDTB),
                    new BoxSpec(BoxType.RDTC),
                    new BoxSpec(BoxType.RDTD),
                    new BoxSpec(BoxType.RDTG),
                    new BoxSpec(BoxType.RDTI),
                    new BoxSpec(BoxType.AMOD),
                    new BoxSpec(BoxType.ASWR),
                    new BoxSpec(BoxType.ADAY),
                    new BoxSpec(BoxType.AXYZ),
                    new BoxSpec(BoxType.AMAK),
                    new BoxSpec(BoxType.MANU),
                    new BoxSpec(BoxType.MODL),
            };
            uuidBoxSpecs = new BoxSpec[]{
                    new BoxSpec(BoxType.RADT),
                    new BoxSpec(BoxType.RATR),
                    new BoxSpec(BoxType.RATRtkhd),
                    new BoxSpec(BoxType.RATRedts),
                    new BoxSpec(BoxType.RATRelst),
                    new BoxSpec(BoxType.RATRmdia),
                    new BoxSpec(BoxType.RATRmdhd),
                    new BoxSpec(BoxType.RATRhdlr),
                    new BoxSpec(BoxType.RATRminf),
                    new BoxSpec(BoxType.RATRsmhd),
                    new BoxSpec(BoxType.RATRminfhdlr),
                    new BoxSpec(BoxType.RATRdinf),
                    new BoxSpec(BoxType.RATRdref),
                    new BoxSpec(BoxType.RATRstbl),
                    new BoxSpec(BoxType.RATRstsd),
                    new BoxSpec(BoxType.RATRsowt),
                    new BoxSpec(BoxType.RATRchan),
                    new BoxSpec(BoxType.RATRSA3D),
                    new BoxSpec(BoxType.RATRstts),
                    new BoxSpec(BoxType.RATRstsc),
                    new BoxSpec(BoxType.RATRstsz),
                    new BoxSpec(BoxType.RATRco64),
            };
            File file = new File(mMp4FilePath);
            mRandomAccessFile = new RandomAccessFile(file, "rw");
            mEndPos = file.length();

            parseMp4(mRandomAccessFile);
        }

        private double byteToInt(byte[] bytes) {
            int result = 0;
            result |= ((bytes[0] << 24) & 0xFF000000);
            result |= ((bytes[1] << 16) & 0xFF0000);
            result |= ((bytes[2] << 8) & 0xFF00);
            result |= ((bytes[3]) & 0xFF);

            return result / 65536;
        }

        @Override
        public void close() throws IOException {
            mRandomAccessFile.close();
        }

        private void parseMp4(RandomAccessFile raf) throws IOException {
            File file = new File(mMp4FilePath);
            mEndPos = file.length();
            parseBoxOffset(rootBoxSpecs, 0, mEndPos, raf);

            long offset = getBoxOffset(rootBoxSpecs, BoxType.MOOV);
            if (!validOffset(offset, mEndPos)) {
                return;
            }
            raf.seek(offset);
            int size = raf.readInt();
            if (!validSize(size)) {
                return;
            }
            raf.skipBytes(BOX_TYPE_LEN);
            parseBoxOffset(moovBoxSpecs, offset + BOX_HEADER_LEN, offset + size, raf);

            offset = getBoxOffset(moovBoxSpecs, BoxType.UDTA);
            if (!validOffset(offset, mEndPos)) {
                return;
            }
            raf.seek(offset);
            size = raf.readInt();
            if (!validSize(size)) {
                return;
            }
            raf.skipBytes(BOX_TYPE_LEN);
            parseBoxOffset(udtaBoxSpecs, offset + BOX_HEADER_LEN, offset + size, raf);

            offset = getBoxOffset(rootBoxSpecs, BoxType.FREE);
            raf.seek(offset);
            size = raf.readInt();

            offset = getBoxOffset(rootBoxSpecs, BoxType.MDAT);
            raf.seek(offset);
            size = raf.readInt();
            if (size == 1) {
                int iType = raf.readInt();
                long lSizeMdat = raf.readLong();
            }

            offset = getBoxOffset(rootBoxSpecs, BoxType.MOOV);
            size = (int) getBoxSize(rootBoxSpecs, BoxType.MOOV);
            parseMoov2tkhd(moovBoxSpecs, offset, offset + size);

            offset = getBoxOffset(rootBoxSpecs, BoxType.UUID);
            size = (int) getBoxSize(rootBoxSpecs, BoxType.UUID);
            if (offset == 0) {
            } else {
                parseBoxOffset(uuidBoxSpecs, offset + BOX_HEADER_LEN + UUID_USER_TYPE.length,
                        offset + size, raf);
            }
        }

        private void copyOrgFileIO(String filePath) throws IOException {
            if (filePath.endsWith(".MP4")) {
                File metaMovefile = new File(filePath);
                String orgFilePath = filePath.replace(".MP4", "org.MP4"); //★JT_ADD★
                File orgFile = new File(orgFilePath);
                long metaMovefileLen = metaMovefile.length();

                IOUtils.copyFileNio(metaMovefile, orgFile);
            }
        }

        private @NonNull
        String getVersionFullName(@NonNull String model, @NonNull String firmwareVersion) {
            if ((model != null) && (firmwareVersion != null)) {
                return model + " Ver " + firmwareVersion;
            } else {
                return "RICOH THETA Ver.";
            }
        }

        /**
         * Write metadata to video file
         */
        public void write() {
            try {
                copyOrgFileIO(mMp4FilePath);

                readTimestamp();
                shiftTimezone();

                String model = CameraSettings.getThetaModel().toString();
                byte[] bAmod = model.getBytes();

                byte[] bAswr = null;
                String firmwareVersion = CameraSettings.getThetaFirmwareVersion();
                bAswr = getVersionFullName(model, firmwareVersion).getBytes();

                byte[] bAday = null;
                String dateTimeZoneIso = CameraSettings.getDateTimeZoneISO();
                if (dateTimeZoneIso != null) {
                    bAday = dateTimeZoneIso.getBytes();
                }

                byte[] bAxyz = new AXYZ().getData();

                String manufacturer = CameraSettings.getManufacturer();
                byte[] bAmak = manufacturer.getBytes();

                byte[] bManu = new byte[6];
                byte[] bManu_resouce = manufacturer.getBytes();
                replaceByteArray(bManu, 0, bManu_resouce);

                byte[] bModl = model.getBytes();

                int newUdtaSize = BOX_HEADER_LEN;

                long sizeRTHU = getBoxSize(udtaBoxSpecs, BoxType.RTHU);
                newUdtaSize += sizeRTHU;

                long sizeRMKN = getBoxSize(udtaBoxSpecs,
                        BoxType.RMKN);
                newUdtaSize += sizeRMKN;

                newUdtaSize += getBoxSize(udtaBoxSpecs, BoxType.RDT1);
                newUdtaSize += getBoxSize(udtaBoxSpecs, BoxType.RDT2);
                newUdtaSize += getBoxSize(udtaBoxSpecs, BoxType.RDT3);
                newUdtaSize += getBoxSize(udtaBoxSpecs, BoxType.RDT4);
                newUdtaSize += getBoxSize(udtaBoxSpecs, BoxType.RDT6);
                newUdtaSize += getBoxSize(udtaBoxSpecs, BoxType.RDT7);
                newUdtaSize += getBoxSize(udtaBoxSpecs, BoxType.RDT8);

                int wavLength = 0;
                File wavFile = new File(mWavFilePath);
                RandomAccessFile wavAccessFile = null;
                boolean wavexist = wavFile.exists();
                if ((int) wavFile.length() == 0) {
                    wavexist = false;
                }

                if ((int) wavFile.length() == 0) {
                    wavexist = false;
                }

                if (wavexist) {
                    newUdtaSize += (BOX_HEADER_LEN + 16);
                } else {
                    newUdtaSize += BOX_HEADER_LEN;
                }

                long sizeRDTA = getBoxSize(udtaBoxSpecs, BoxType.RDTA);
                newUdtaSize += sizeRDTA;

                long sizeRDTB = getBoxSize(udtaBoxSpecs, BoxType.RDTB);
                newUdtaSize += sizeRDTB;

                long sizeRDTC = getBoxSize(udtaBoxSpecs, BoxType.RDTC);
                newUdtaSize += sizeRDTC;

                long sizeRDTD = getBoxSize(udtaBoxSpecs, BoxType.RDTD);
                newUdtaSize += sizeRDTD;

                long sizeRDTG = getBoxSize(udtaBoxSpecs, BoxType.RDTG);
                newUdtaSize += sizeRDTG;

                long sizeRDTI = getBoxSize(udtaBoxSpecs, BoxType.RDTI);
                newUdtaSize += sizeRDTI;

                newUdtaSize += BOX_HEADER_LEN + bAmod.length;

                newUdtaSize += BOX_HEADER_LEN + bAswr.length;

                if (bAday != null) {
                    newUdtaSize += BOX_HEADER_LEN + bAday.length;
                }

                if (bAxyz != null) {
                    newUdtaSize += BOX_HEADER_LEN + bAxyz.length;
                }

                newUdtaSize += BOX_HEADER_LEN + bAmak.length;

                newUdtaSize += BOX_HEADER_LEN + bManu.length;

                newUdtaSize += BOX_HEADER_LEN + bModl.length;

                long newUuidSize = 0;

                RATR boxRATR = null;
                if (wavexist) {
                    newUuidSize += BOX_HEADER_LEN + UUID_USER_TYPE.length;

                    wavAccessFile = new RandomAccessFile(wavFile, "r");
                    wavLength = (int) wavAccessFile.length();

                    newUuidSize += (BOX_HEADER_LEN + wavLength - WAV_HEADER_LEN);

                    boxRATR = new RATR(wavLength - WAV_HEADER_LEN);
                    newUuidSize += boxRATR.size();
                }

                byte[] bRTHU = getBoxData(BoxType.RTHU);
                byte[] bRMKN = getBoxData(BoxType.RMKN);
                byte[] bRdt1 = getBoxData(BoxType.RDT1);
                byte[] bRdt2 = getBoxData(BoxType.RDT2);
                byte[] bRdt3 = getBoxData(BoxType.RDT3);
                byte[] bRdt4 = getBoxData(BoxType.RDT4);
                byte[] bRdt6 = getBoxData(BoxType.RDT6);
                byte[] bRdt7 = getBoxData(BoxType.RDT7);
                byte[] bRdt8 = getBoxData(BoxType.RDT8);
                byte[] bRdta = getBoxData(BoxType.RDTA);
                byte[] bRdtb = getBoxData(BoxType.RDTB);
                byte[] bRdtc = getBoxData(BoxType.RDTC);
                byte[] bRdtd = getBoxData(BoxType.RDTD);
                byte[] bRdtg = getBoxData(BoxType.RDTG);
                byte[] bRdti = getBoxData(BoxType.RDTI);

                boolean foundStsd = parseMoov2stsd(moovBoxSpecs,
                        getBoxOffset(rootBoxSpecs, BoxType.MOOV), mRandomAccessFile.length());
                if (foundStsd) {
                    new ColorSpace().replaceColorSpace(mRandomAccessFile,
                            getBoxOffset(moovBoxSpecs, BoxType.MOOVSTSD), getWidth(), getHeight());
                }

                byte[] convMvhdData = getBoxData(BoxType.MOOVMVHD);
                byte[] convMoovTrakData = getBoxData(BoxType.MOOVTRAK);
                byte[] convMoovTrakDataSound = getBoxData(BoxType.MOOVTRAKSOUND);

                SphereType sphereType = CameraSettings.getSphereType();

                int sizeBoxXMP = 0;
                byte[] boxxmp = null;
                if (sphereType == SphereType.EQUIRECTANGULAR) {
                    boxxmp = Xmp.getXmp(model, firmwareVersion, getTimestamp());
                    sizeBoxXMP = BOX_HEADER_LEN + UUID_SPHERICAL.length + boxxmp.length;
                }

                if (bRMKN == null || bRMKN.length == 0) {
                    return;
                }

                bRMKN = new RMKN().getData(bRMKN);

                long newFileSize = extendFile(newUdtaSize, sizeBoxXMP, newUuidSize);

                if (sphereType == SphereType.EQUIRECTANGULAR) {
                    if (!insertXMP(newUdtaSize, sizeBoxXMP, boxxmp,
                            convMvhdData, convMoovTrakData, convMoovTrakDataSound)) {
                        return;
                    }
                } else {
                    if (!replaceMoovUdtaSize(newUdtaSize, 0,
                            convMvhdData, convMoovTrakData, convMoovTrakDataSound)) {
                        return;
                    }
                }

                BoxSpec udtaBoxSpec = containBox(moovBoxSpecs, BoxType.UDTA.getValue());
                mRandomAccessFile.seek(udtaBoxSpec.getOffset());
                addType(BoxType.UDTA.getValue(), (int) (newUdtaSize));

                addBox(BoxType.RTHU.getValue(), bRTHU);
                addBox(BoxType.RMKN.getValue(), bRMKN);
                addBox(BoxType.RDT1.getValue(), bRdt1);
                addBox(BoxType.RDT2.getValue(), bRdt2);
                addBox(BoxType.RDT3.getValue(), bRdt3);
                addBox(BoxType.RDT4.getValue(), bRdt4);
                addBox(BoxType.RDT6.getValue(), bRdt6);
                addBox(BoxType.RDT7.getValue(), bRdt7);
                addBox(BoxType.RDT8.getValue(), bRdt8);

                if (wavexist) {
                    addType(BoxType.RDT9.getValue(), BOX_HEADER_LEN + 16);
                    byte[] bWAVoffset = new byte[8];
                    byte[] bWAVlength = ByteBuffer.allocate(8)
                            .putLong(wavAccessFile.length() - WAV_HEADER_LEN).array();
                    mRandomAccessFile.write(bWAVoffset);
                    mRandomAccessFile.write(bWAVlength);

                } else {
                    addType(BoxType.RDT9.getValue());
                }

                addBox(BoxType.RDTA.getValue(), bRdta);
                addBox(BoxType.RDTB.getValue(), bRdtb);
                addBox(BoxType.RDTC.getValue(), bRdtc);
                addBox(BoxType.RDTD.getValue(), bRdtd);
                if (bRdtg != null) {
                    addBox(BoxType.RDTG.getValue(), bRdtg);
                }
                addBox(BoxType.RDTI.getValue(), bRdti);
                addBox(BoxType.AMOD.getValue(), bAmod);
                addBox(BoxType.ASWR.getValue(), bAswr);

                if (bAday != null) {
                    addBox(BoxType.ADAY.getValue(), bAday);
                }
                if (bAxyz != null) {
                    addBox(BoxType.AXYZ.getValue(), bAxyz);
                }
                addBox(BoxType.AMAK.getValue(), bAmak);
                addBox(BoxType.MANU.getValue(), bManu);
                addBox(BoxType.MODL.getValue(), bModl);

                if (wavexist) {
                    addType(BoxType.UUID.getValue(), (int) newUuidSize);
                    mRandomAccessFile.write(UUID_USER_TYPE);

                    long RADToffset = mRandomAccessFile.getFilePointer();

                    addType(BoxType.RADT.getValue(), wavLength - WAV_HEADER_LEN + BOX_HEADER_LEN);

                    mRandomAccessFile.skipBytes(wavLength - WAV_HEADER_LEN);

                    byte[] bRATR = boxRATR.getData(RADToffset);
                    addBox(BoxType.RATR.getValue(), bRATR);
                    BoxSpec ratrBoxSpec = containBox(uuidBoxSpecs, BoxType.RADT.getValue());
                    ratrBoxSpec.setOffset(RADToffset);
                }

                mEndPos = newFileSize;

                parseMp4(mRandomAccessFile);

                if (wavexist) {
                    replaceRDT9Offset();

                    long RADToffset = getBoxOffset(uuidBoxSpecs, BoxType.RADT);
                    copyLargeWAV(wavAccessFile, mRandomAccessFile, WAV_HEADER_LEN,
                            RADToffset + BOX_HEADER_LEN, wavLength - WAV_HEADER_LEN);

                    wavAccessFile.close();
                }

            } catch (IOException e) {
            }
        }

        private void replaceRDT9Offset() throws IOException {
            long lRADTOffset = getBoxOffset(uuidBoxSpecs, BoxType.RADT);
            long lRdt9Offset = getBoxOffset(udtaBoxSpecs, BoxType.RDT9);

            mRandomAccessFile.seek(lRdt9Offset + BOX_HEADER_LEN);
            mRandomAccessFile.writeLong(lRADTOffset);
        }

        private long extendFile(int newUdtaSize, int sizeBoxXMP, long newUuidSize)
                throws IOException {
            long convUdtaSize = getBoxSize(moovBoxSpecs, BoxType.UDTA);

            long oldFileSize = mRandomAccessFile.length();
            long newFileSize =
                    oldFileSize - convUdtaSize + newUdtaSize + sizeBoxXMP + newUuidSize;

            mRandomAccessFile.setLength(newFileSize);

            mEndPos = mRandomAccessFile.length();
            return newFileSize;
        }

        private boolean insertXMP(int newUdtaSize, int sizeBoxXMP, byte[] boxxmp
                , byte[] convMvhdData, byte[] convMoovTrakData,
                byte[] convMoovTrackDataSound) throws IOException {

            int newMoovSize = 0;

            long convMvhdSize = getBoxSize(moovBoxSpecs, BoxType.MOOVMVHD);

            long convMoovTrakSize = getBoxSize(moovBoxSpecs, BoxType.MOOVTRAK);

            long convMoovTrakSoundSize = mBoxSpecSound.getBoxSize();

            long moovOffset = 0;
            BoxSpec moovBoxSpec = containBox(rootBoxSpecs, BoxType.MOOV.getValue());
            moovOffset = moovBoxSpec.getOffset();
            if (!validOffset(moovOffset, mEndPos)) {
                return false;
            }

            long moovSize;
            moovSize = (int) (BOX_HEADER_LEN + convMvhdSize +
                    convMoovTrakSize + convMoovTrakSoundSize + newUdtaSize);

            newMoovSize = (int) moovSize + sizeBoxXMP;
            mRandomAccessFile.seek(moovOffset);
            addType(BoxType.MOOV.getValue(), newMoovSize);
            moovBoxSpec.setlBoxSize(newMoovSize);

            addBox(BoxType.MOOVMVHD.getValue(), convMvhdData);

            long tmpTrakPos = mRandomAccessFile.getFilePointer();
            addBox(BoxType.MOOVTRAK.getValue(), convMoovTrakData);

            long tmpPos = mRandomAccessFile.getFilePointer();
            int szBoxXMP = BOX_HEADER_LEN + UUID_SPHERICAL.length + boxxmp.length;

            mRandomAccessFile.seek(tmpPos);
            addType(BoxType.MOOVUUID.getValue(), szBoxXMP);
            mRandomAccessFile.write(UUID_SPHERICAL);
            mRandomAccessFile.write(boxxmp);
            tmpPos = mRandomAccessFile.getFilePointer();

            mRandomAccessFile.seek(tmpTrakPos);
            long trak_xmp_size = (int) (convMoovTrakData.length + BOX_HEADER_LEN + szBoxXMP);
            mRandomAccessFile.writeInt((int) trak_xmp_size);
            mRandomAccessFile.seek(tmpPos);

            addBox(BoxType.MOOVTRAKSOUND.getValue(), convMoovTrackDataSound);

            tmpPos = mRandomAccessFile.getFilePointer();
            BoxSpec udtaBoxSpec = containBox(moovBoxSpecs, BoxType.UDTA.getValue());

            addBox(BoxType.UDTA.getValue(), (int) newUdtaSize, null);
            udtaBoxSpec.setOffset(tmpPos);
            return true;
        }

        private void copyLargeWAV(RandomAccessFile srcRadomAccessFile,
                RandomAccessFile dstRandomAccessFile, long srcStartOffset, long dstStartOffset,
                long dataLength) throws IOException {
            int loopNum = (int) dataLength / CELLSIZE;
            byte[] cellByte = new byte[CELLSIZE];
            for (int i = 0; i < loopNum; i++) {
                srcRadomAccessFile.seek(srcStartOffset + (i * CELLSIZE));
                srcRadomAccessFile.read(cellByte);
                dstRandomAccessFile.seek(dstStartOffset + (i * CELLSIZE));
                dstRandomAccessFile.write(cellByte);
            }

            int remainSize = (int) dataLength - (loopNum * CELLSIZE);
            cellByte = new byte[remainSize];
            srcRadomAccessFile.seek(srcStartOffset + (loopNum * CELLSIZE));
            srcRadomAccessFile.read(cellByte);
            dstRandomAccessFile.seek(dstStartOffset + (loopNum * CELLSIZE));
            dstRandomAccessFile.write(cellByte);
        }

        private boolean replaceMoovUdtaSize(int newUdtaSize, int sizeBoxXMP,
                byte[] convMvhdData, byte[] convMoovTrakData, byte[] convMoovTrakDataSound)
                throws IOException {

            int newMoovSize = 0;

            BoxSpec moovBox = containBox(rootBoxSpecs, BoxType.MOOV.getValue());
            long moovOffset = moovBox.getOffset();

            if (!validOffset(moovOffset, mEndPos)) {
                return false;
            }
            long moovSize = 0;

            BoxSpec udtaBoxSpec = containBox(moovBoxSpecs, BoxType.UDTA.getValue());

            moovSize = moovBox.getlBoxSize();

            long udtaOffset = 0;
            udtaOffset = udtaBoxSpec.getOffset();
            if (!validOffset(udtaOffset, mEndPos)) {
                return false;
            }

            long udtaSize = 0;
            udtaSize = udtaBoxSpec.getBoxSize();

            newMoovSize = (int) (moovSize - udtaSize + newUdtaSize) + sizeBoxXMP;
            mRandomAccessFile.seek(moovOffset);
            addType(BoxType.MOOV.getValue(), newMoovSize);

            addBox(BoxType.MOOVMVHD.getValue(), convMvhdData);

            addBox(BoxType.MOOVTRAK.getValue(), convMoovTrakData);

            addBox(BoxType.MOOVTRAKSOUND.getValue(), convMoovTrakDataSound);

            long udtaBoxPos = mRandomAccessFile.getFilePointer();
            BoxSpec udtaBox = containBox(moovBoxSpecs, BoxType.UDTA.getValue());

            addBox(BoxType.UDTA.getValue(), (int) udtaSize, null);
            udtaBox.setOffset(udtaBoxPos);
            udtaBox.setBoxSize((int) udtaSize);

            return true;
        }

        public void addBox(byte[] typeValue, byte[] data) throws IOException {
            if (data == null) {
                mRandomAccessFile.writeInt(BOX_HEADER_LEN);
                mRandomAccessFile.write(typeValue);
            } else {
                mRandomAccessFile.writeInt(BOX_HEADER_LEN + data.length);
                mRandomAccessFile.write(typeValue);
                if (data.length > 0) {
                    mRandomAccessFile.write(data);
                }
            }
        }

        private void addBox(byte[] typeValue, int size, byte[] data) throws IOException {
            byte[] SizeArray = IntToByte(size);

            mRandomAccessFile.write(SizeArray);
            mRandomAccessFile.write(typeValue);
            if (data != null) {
                mRandomAccessFile.write(data);
            }
        }

        private byte[] IntToByte(int value) {
            int ArraySize = Integer.SIZE / Byte.SIZE;
            ByteBuffer buffer = ByteBuffer.allocate(ArraySize);
            return buffer.putInt(value).array();
        }

        private void addBox(byte[] typeValue, int[] data) throws IOException {
            mRandomAccessFile.writeInt(BOX_HEADER_LEN + data.length);
            mRandomAccessFile.write(typeValue);
            for (int i = 0; i < data.length; i++) {
                mRandomAccessFile.writeInt(data[0]);
            }
        }

        private void addType(byte[] typeValue) throws IOException {
            mRandomAccessFile.writeInt(BOX_HEADER_LEN);
            mRandomAccessFile.write(typeValue);
        }

        public void addType(byte[] typeValue, int size) throws IOException {
            mRandomAccessFile.writeInt(size);
            mRandomAccessFile.write(typeValue);
        }

        private BoxSpec containBox(BoxSpec[] boxSpecs, byte[] type) {
            for (BoxSpec boxSpec : boxSpecs) {
                if (Arrays.equals(boxSpec.getType().getValue(), type)) {
                    return boxSpec;
                }
            }

            return null;
        }

        private BoxSpec containBox(BoxSpec[] boxSpecs, int ordinal) {
            for (BoxSpec boxSpec : boxSpecs) {
                if (boxSpec.getType().ordinal() == ordinal) {
                    return boxSpec;
                }
            }

            return null;
        }

        private long getBoxOffset(BoxSpec[] boxSpecs, BoxType boxType) {
            for (BoxSpec boxSpec : boxSpecs) {
                if (boxSpec.getType() == boxType) {
                    return boxSpec.getOffset();
                }
            }

            return 0L;
        }

        private long getBoxSize(BoxSpec[] boxSpecs, BoxType boxType) {
            for (BoxSpec boxSpec : boxSpecs) {
                if (boxSpec.getType() == boxType) {
                    return boxSpec.getBoxSize();
                }
            }

            return 0L;
        }

        private long getlBoxSize(BoxSpec[] boxSpecs, BoxType boxType) {
            for (BoxSpec boxSpec : boxSpecs) {
                if (boxSpec.getType() == boxType) {
                    return boxSpec.getlBoxSize();
                }
            }

            return 0L;
        }

        private boolean parseBoxOffset(BoxSpec[] boxSpecs, long offset, long endPos,
                RandomAccessFile raf) throws IOException {
            long pos = offset;

            raf.seek(pos);

            while (pos < endPos) {
                long lsize;
                long size = raf.readInt();
                byte[] type = new byte[BOX_TYPE_LEN];
                raf.read(type);

                BoxSpec boxSpec = containBox(boxSpecs, type);
                if (size == BOX_SIZE_LARGER) {
                    lsize = raf.readLong();
                } else {
                    lsize = size;
                }

                if (!validSize(lsize)) {
                    return false;
                }
                if (boxSpec != null) {
                    BoxSpec TrakVideo = containBox(boxSpecs, BoxType.MOOVTRAK.ordinal());
                    BoxSpec TrakSound = containBox(boxSpecs, BoxType.MOOVTRAKSOUND.ordinal());
                    long offsetMoovTrak = 0;
                    if (TrakVideo != null) {
                        offsetMoovTrak = TrakVideo.getOffset();
                    }
                    if (boxSpec.getType() == BoxType.MOOVTRAK) {

                        if (offsetMoovTrak == 0 || pos == offsetMoovTrak) {
                            boxSpec.setOffset(pos);
                            boxSpec.setBoxSize((int) size);
                            boxSpec.setlBoxSize(lsize);
                        } else {
                            mBoxSpecSound = TrakSound;
                            mBoxSpecSound.setOffset(pos);
                            mBoxSpecSound.setBoxSize((int) size);
                            mBoxSpecSound.setlBoxSize(lsize);
                        }
                    } else {
                        boxSpec.setOffset(pos);
                        boxSpec.setBoxSize((int) size);
                        boxSpec.setlBoxSize(lsize);
                    }
                }

                raf.seek(pos + lsize);
                pos += lsize;
            }

            return true;
        }

        private boolean parseMoov2stsd(BoxSpec[] boxSpecs, long startOffset, long endOffset)
                throws IOException {
            long pos = startOffset + BOX_HEADER_LEN;

            mRandomAccessFile.seek(pos);

            while (pos < endOffset) {
                long size = mRandomAccessFile.readInt();
                byte[] type = new byte[BOX_TYPE_LEN];
                mRandomAccessFile.read(type);

                BoxSpec boxSpec = containBox(boxSpecs, type);

                if (!validSize(size)) {
                    return false;
                }
                if (boxSpec != null) {
                    boxSpec.setOffset(pos);
                    boxSpec.setBoxSize((int) size);

                    BoxType boxType = boxSpec.getType();

                    if (boxType == BoxType.MOOVSTSD) {
                         return true;
                    } else if (boxType == BoxType.MOOV || boxType == BoxType.MOOVTRAK
                            || boxType == BoxType.MOOVMDIA || boxType == BoxType.MOOVMINF
                            || boxType == BoxType.MOOVSTBL) {

                        mRandomAccessFile.seek(pos + BOX_HEADER_LEN);
                        pos += BOX_HEADER_LEN;
                    } else {
                        mRandomAccessFile.seek(pos + size);
                        pos += size;
                    }
                } else {
                    mRandomAccessFile.seek(pos + size);
                    pos += size;
                }
            }

            return false;
        }

        public void readTimestamp() throws IOException {
            BoxSpec[] boxSpecs = moovBoxSpecs;
            long startOffset = getBoxOffset(rootBoxSpecs, BoxType.MOOV);
            long endOffset = startOffset + (int) getBoxSize(rootBoxSpecs, BoxType.MOOV);

            long pos = startOffset + BOX_HEADER_LEN;
            boolean doSeekTrackSound = false;

            mRandomAccessFile.seek(pos);

            long MOOVTRAKSoundPos = 0;

            while (pos < endOffset) {
                long size = mRandomAccessFile.readInt();
                byte[] type = new byte[BOX_TYPE_LEN];

                mRandomAccessFile.read(type);

                BoxSpec boxSpec = containBox(boxSpecs, type);

                if (!validSize(size)) {
                    return;
                }
                if (boxSpec != null) {
                    if (!doSeekTrackSound) {
                        boxSpec.setOffset(pos);
                        boxSpec.setBoxSize((int) size);
                    }

                    BoxType boxType = boxSpec.getType();

                    if (boxType == BoxType.MOOVMDHD) {
                        if (doSeekTrackSound) {
                            mTimeMDHDSound.init(mRandomAccessFile, pos);
                            break;
                        } else {
                            mTimeMDHD.init(mRandomAccessFile, pos);
                            doSeekTrackSound = true;
                            pos = MOOVTRAKSoundPos;
                            mRandomAccessFile.seek(pos);
                        }
                    } else if (boxType == BoxType.MOOV
                            || boxType == BoxType.MOOVTRAK
                            || boxType == BoxType.MOOVMDIA) {

                        if (boxType == BoxType.MOOVTRAK && !doSeekTrackSound) {
                            mRandomAccessFile.seek(pos + size);
                            MOOVTRAKSoundPos = pos + size;

                            mRandomAccessFile.seek(pos);
                        }

                        mRandomAccessFile.seek(pos + BOX_HEADER_LEN);
                        pos += BOX_HEADER_LEN;
                    } else {
                        if (boxType == BoxType.MOOVMVHD) {
                            mTimeMVHD.init(mRandomAccessFile, pos);
                        } else if (boxType == BoxType.MOOVTKHD) {
                            if (!doSeekTrackSound) {
                                mTimeTKHD.init(mRandomAccessFile, pos);
                            } else {
                                mTimeTKHDSound.init(mRandomAccessFile, pos);
                            }
                        }
                        mRandomAccessFile.seek(pos + size);
                        pos += size;
                    }
                } else {
                    mRandomAccessFile.seek(pos + size);
                    pos += size;
                }
            }
        }

        public void shiftTimezone() throws IOException {
            String timeZone = CameraSettings.getTimeZone();
            if (timeZone != null) {
                mTimeMVHD.shift(timeZone);
                mTimeTKHD.shift(timeZone);
                mTimeMDHD.shift(timeZone);
                mTimeTKHDSound.shift(timeZone);
                mTimeMDHDSound.shift(timeZone);
            }
        }

        private boolean parseMoov2tkhd(BoxSpec[] boxSpecs, long startOffset, long endOffset)
                throws IOException {
            long pos = startOffset;

            mRandomAccessFile.seek(startOffset);

            while (pos < endOffset) {
                long size = mRandomAccessFile.readInt();
                byte[] type = new byte[BOX_TYPE_LEN];
                mRandomAccessFile.read(type);
                String sType = new String(type, "UTF-8");

                BoxSpec boxSpec = containBox(boxSpecs, type);

                if (!validSize(size)) {
                    return false;
                }
                if (boxSpec != null) {
                    boxSpec.setOffset(pos);
                    boxSpec.setBoxSize((int) size);

                }

                BoxType boxType = BoxType.getValue(sType);
                if (boxType != null) {
                    if (boxType == BoxType.MOOVTKHD) {
                        return true;
                    } else if (boxType == BoxType.MOOV || boxType == BoxType.MOOVTRAK) {
                        mRandomAccessFile.seek(pos + BOX_HEADER_LEN);
                        pos += BOX_HEADER_LEN;
                    } else {
                        mRandomAccessFile.seek(pos + size);
                        pos += size;
                    }
                } else {
                    mRandomAccessFile.seek(pos + size);
                    pos += size;
                }
            }

            return false;
        }

        private boolean validOffset(long offset, long endPos) {
            if ((0 < offset) && (offset < endPos)) {
                return true;
            }

            return false;
        }

        private boolean validSize(long size) {
            if (size >= BOX_HEADER_LEN) {
                return true;
            }

            return false;
        }

        private BoxSpec[] getBlock(BoxType boxType) throws IOException {
            for (BoxSpec rootBoxSpec : rootBoxSpecs) {
                if (rootBoxSpec.getType() == boxType) {
                    return rootBoxSpecs;
                }
            }

            for (BoxSpec moovBoxSpec : moovBoxSpecs) {
                if (moovBoxSpec.getType() == boxType) {
                    return moovBoxSpecs;
                }
            }

            for (BoxSpec udtaBoxSpec : udtaBoxSpecs) {
                if (udtaBoxSpec.getType() == boxType) {
                    return udtaBoxSpecs;
                }
            }

            return null;
        }

        public byte[] getBoxData(BoxType boxType) {
            byte[] bData = null;

            try {
                BoxSpec[] block = getBlock(boxType);

                if (block != null) {
                    long lOffset = getBoxOffset(block, boxType);
                    long lSize = getBoxSize(block, boxType);

                    if (lSize != 0) {
                        bData = new byte[(int) lSize - BOX_HEADER_LEN];
                        mRandomAccessFile.seek(lOffset + BOX_HEADER_LEN);
                        mRandomAccessFile.read(bData);
                    }
                }
            } catch (IOException e) {
            }

            if (bData != null && bData.length == 0) {
                bData = null;
            }

            return bData;
        }

        private void replaceByteArray(byte[] Bytes, int Pos, byte[] ReplaceBytes) {
            if ((Pos + ReplaceBytes.length) > Bytes.length) {
                return;
            }

            for (int i = 0; i < ReplaceBytes.length; i++) {
                Bytes[i + Pos] = ReplaceBytes[i];
            }
        }

        public int getWidth() {
            byte[] tkhd = getBoxData(BoxType.MOOVTKHD);
            byte[] bytes = Arrays.copyOfRange(tkhd, tkhd.length - 8, tkhd.length - 4);

            return (int) byteToInt(bytes);
        }

        public int getHeight() {
            byte[] tkhd = getBoxData(BoxType.MOOVTKHD);
            byte[] bytes = Arrays.copyOfRange(tkhd, tkhd.length - 4, tkhd.length);

            return (int) byteToInt(bytes);
        }

        private long getTimestamp() {
            byte[] tkhd = getBoxData(BoxType.MOOVTKHD);
            byte[] bytes = Arrays.copyOfRange(tkhd, 4, 4 + 4);

            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            long timestamp = buffer.getInt();
            if (timestamp < 0) {
                timestamp += 1L << 32;
            }

            return timestamp;
        }
    }
}
