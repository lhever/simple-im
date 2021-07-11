package com.lhever.common.core.utils;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/9/19 18:40
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/9/19 18:40
 * @modify by reason:{方法名}:{原因}
 */
public class ImgUtils {
    private static int size = 1000;
    public static byte[] formatOrScale(byte[] img, double scale, Boolean format, String targetFormat) throws IOException {
        if (ArrayUtils.isEmpty(img)) {
            return new byte[0];
        }
        ByteArrayInputStream is = new ByteArrayInputStream(img);
        ByteArrayOutputStream os = new ByteArrayOutputStream(img.length);
        Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(is).scale(scale);
        if (format) {
                builder.outputFormat(targetFormat).toOutputStream(os);
        } else {
            builder.useOriginalFormat().toOutputStream(os);
        }

        return os.toByteArray();
    }

    public static byte[] formatOrResize(byte[] img, int width, int height, Boolean format, String targetFormat) throws IOException {
        if (ArrayUtils.isEmpty(img)) {
            return new byte[0];
        }
        ByteArrayInputStream is = new ByteArrayInputStream(img);
        ByteArrayOutputStream os = new ByteArrayOutputStream(img.length);
        Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(is).forceSize(width, height);
        if (format) {
            builder.outputFormat(targetFormat).toOutputStream(os);
        } else {
            builder.useOriginalFormat().toOutputStream(os);
        }
        return os.toByteArray();
    }

    public static byte[] formatOrScale(InputStream is, double scale, Boolean format, String targetFormat, boolean close) throws IOException {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream(512);
            Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(is).scale(scale);
            if (format) {
                builder.outputFormat(targetFormat).toOutputStream(os);
            } else {
                builder.useOriginalFormat().toOutputStream(os);
            }
            return os.toByteArray();
        } finally {
            if (close) {
                IOUtils.closeQuietly(is);
            }
        }
    }

    /**
    * @Description: 图片超过200kb或小于20Kb则调整图片大小,format为ture则转化图片格式
    * @Author: liuyichi
    * @Date: 2020/10/14
    */
    public static byte[] resizeAndFormat(byte[] fileBytes, Boolean format, String targetFormat) throws IOException {
        BufferedImage read = ImageIO.read(new ByteArrayInputStream(fileBytes));
        double kb = fileBytes.length / 1024.0;
        if (kb > 20 && kb < 200) { //大小在20~200Kb之间,不处理
            if (format) {
                byte[] jpgs = ImgUtils.formatOrScale(fileBytes, 1d, true, targetFormat);
                return jpgs;
            }
            return fileBytes;
        }
        if (read.getHeight() == 0 || read.getWidth() == 0) {
            return fileBytes;
        }
        int width = 0;
        int height = 0;
        if (read.getHeight() > read.getWidth()) {
            double wh = (double)read.getWidth() / read.getHeight();
            height = size;
            width = (int) (size * wh);
        } else {
            double hw = (double)read.getHeight() / read.getWidth();
            width = size;
            height = (int) (size * hw);
        }
        byte[] jpgs = ImgUtils.formatOrResize(fileBytes, width, height, format, targetFormat);
        return jpgs;
    }
}
