package com.lhever.common.core.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * @program: scos
 * @description: 二维码相关工具类
 * @author: liuyichi
 * @create: 2019-11-22 14:37
 **/
public class QRCodeUtil {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int width = 300;
    private static final int height = 300;
    private static final int charlongthMax = 14;
    private static final int charSize = 18;
    private QRCodeUtil() {
    }
    /**
     * @Description: 创建图片
     * @Author: liuyichi
     * @Date: 2019/11/22
     */
    public static void creatImage(String text,OutputStream out,String orderText) throws Exception
    {
        // text = "http://www.baidu.com"; // 二维码内容
        String format = "jpg";// 二维码的图片格式
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
                BarcodeFormat.QR_CODE, width, height, hints);
        writeToStream(bitMatrix, format, out,orderText);
    }

    public static void main(String[] args) {
        try {
            File file = new File("E:\\二维码.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            creatImage("hello你好",outputStream,"访客微信扫码进门，住户{APP名称}扫码进门");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix,0);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format "
                    + format + " to " + file);
        }
    }
    public static void writeToStream(BitMatrix matrix, String format,
                                     OutputStream stream,String text) throws IOException {
        BufferedImage image;
        if (StringUtils.isNotBlank(text)) {
            int line = text.length()%charlongthMax!=0?text.length()/charlongthMax:text.length()/charlongthMax-1;
            image = toBufferedImage(matrix,line<0?0:line);
            pressText(text, image);
        }else{
            image = toBufferedImage(matrix,0);
        }
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix,int line) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height+line*(charSize+2),
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
            for (int y = height; y < height+line*(charSize+2); y++) {
                image.setRGB(x, y, WHITE);
            }
        }
        return image;
    }

    /**
     * 给二维码图片加上文字
     */
    public static void pressText(String pressText, BufferedImage image) throws IOException {
        if (StringUtils.isEmpty(pressText)){
            return;
        }
        pressText = new String(pressText.getBytes(), "utf-8");
        Graphics g = image.createGraphics();
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.PLAIN, charSize);
        FontMetrics metrics = g.getFontMetrics(font);
        int line = pressText.length()%charlongthMax!=0?pressText.length()/charlongthMax:pressText.length()/charlongthMax-1;
        int startY = height - 15;
        g.setFont(font);
        for (int i = 0; i <= line; i++) {
            if ((i+1)*charlongthMax<=pressText.length()){
                int pressTextSize = metrics.stringWidth(pressText.substring(i*charlongthMax,(i+1)*charlongthMax));
                g.drawString(pressText.substring(i*charlongthMax,(i+1)*charlongthMax),(width - pressTextSize) / 2 , startY+i*(charSize+2));
            }else{
                int pressTextSize = metrics.stringWidth(pressText.substring(i*charlongthMax,pressText.length()));
                g.drawString(pressText.substring(i*charlongthMax,pressText.length()), (width - pressTextSize) / 2 , startY+i*(charSize+2));
            }
        }
        g.dispose();
    }
}
