package com.lhever.common.core.utils;

import com.lhever.common.core.consts.CommonConsts;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileTypeUtils {

    private static Logger logger = LoggerFactory.getLogger(FileTypeUtils.class);


    public static MediaType getMediaType(File file) {
        MediaType mediaType = null;
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            mediaType = getMediaType(is, false);
        } catch (IOException e) {
            logger.error("get media type error, file:" + file.getAbsolutePath(), e);
        } finally {
            IOUtils.closeQuietly(is);
        }

        return mediaType;
    }


    public static MediaType getMediaType(InputStream is, boolean close) {
        MediaType mediaType = null;
        try {
            AutoDetectParser parser = new AutoDetectParser();
            Detector detector = parser.getDetector();
            Metadata md = new Metadata();
            if (!is.markSupported()) {
                BufferedInputStream bis = new BufferedInputStream(is);
                mediaType = detector.detect(bis, md);
            } else {
                mediaType = detector.detect(is, md);
            }
        } catch (IOException e) {
            logger.error("get media type error", e);
        } finally {
            if (close) {
                IOUtils.closeQuietly(is);
            }
        }
        return mediaType;
    }


    public static String mediaTypeString(MediaType mediaType) {
        String result = (mediaType == null ? CommonConsts.EMPTY : mediaType.toString());
        return result;
    }

    public static String type(MediaType mediaType) {
        String result = (mediaType == null ? CommonConsts.EMPTY : mediaType.getType());
        return result;
    }

    public static String subType(MediaType mediaType) {
        String result = (mediaType == null ? CommonConsts.EMPTY : mediaType.getSubtype());
        return result;
    }


}
