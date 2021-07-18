package com.lhever.common.core.test;

import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.utils.FileTypeUtils;
import com.lhever.common.core.utils.IOUtils;
import org.apache.tika.Tika;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2019/7/23 10:16
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2019/7/23 10:16
 * @modify by reason:{方法名}:{原因}
 */
public class FileTypeUtilsTest {
    private static Logger logger = LoggerFactory.getLogger(FileTypeUtilsTest.class);



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



    public String getMime(String path) {
        MediaType mediaType = getMediaType(new File(path));
        String type = mediaTypeString(mediaType);
        logger.info("type: " + type);
        return type;
    }


    @Test
    public void testGetMime() {
        getMime("D:/BBB.xlsx");
        getMime("C:/imgs/深色底.gif");
        getMime("C:/imgs/深色底.png");
        getMime("C:/imgs/aaa.bmp");
        getMime("C:/imgs/Apps.icns");
        getMime("C:/imgs/Cinema Blue Screen.ico");
        getMime("C:/imgs/ico.ico");
        getMime("C:/imgs/1064480.png");
        getMime("C:/imgs/disconnect_network_folder24.bmp");
        getMime("C:/imgs/active_network_connection24.bmp");
        getMime("C:/imgs/11.jpeg");
        getMime("C:/imgs/oauth-boot-master.zip");
        getMime("C:/imgs/表格.rar");
        getMime("C:/imgs/studio3T脚本.rar");
        getMime("C:/imgs/jemalloc.pdf");
        getMime("C:/imgs/test.rar");
        getMime("C:/imgs/新建文件夹.7z");

        String s = FileTypeUtils.subType(FileTypeUtils.getMediaType(new File("C:/imgs/oauth-boot-master.zip")));
        System.out.println(s);

        String s1 = FileTypeUtils.subType(FileTypeUtils.getMediaType(new File("C:/imgs/studio3T脚本.rar")));
        System.out.println(s1);
    }


    @Test
    public void testGetType() throws IOException {
        Tika tika = new Tika();
        File file = new File("D:/BBB.xlsx");
        String mimeType = tika.detect(file);
        System.out.println(mimeType);
    }

    @Test
    public void test() {
        Path path = Paths.get("D:/BBB.xlsx");
        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File content type is : " + contentType);
    }

    @Test
    public void test1() {
        String contentType = new MimetypesFileTypeMap().getContentType(new File("D:/BBB.xlsx"));
        System.out.println(contentType);
    }


}
