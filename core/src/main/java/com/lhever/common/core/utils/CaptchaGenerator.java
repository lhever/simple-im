package com.lhever.common.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/2/27 16:23
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/2/27 16:23
 * @modify by reason:{方法名}:{原因}
 */
public class CaptchaGenerator {

    private final static Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);

    /**
     * 获得字体
     */
    private Font getFont() {
        return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
    }

    /**
     * 获得颜色
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random(System.currentTimeMillis());
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc - 16);
        int g = fc + random.nextInt(bc - fc - 14);
        int b = fc + random.nextInt(bc - fc - 18);
        return new Color(r, g, b);
    }

    /**
     * 生成随机图片
     */
    public byte[] getCaptchaBytes(int width, int height, String  randomString) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

        //产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
        Graphics g = image.getGraphics();
        //图片大小
        g.fillRect(0, 0, width, height);
        //字体大小
        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
        //字体颜色
        g.setColor(getRandColor(110, 133));
        // 绘制干扰线
        for (int i = 0; i <= 40; i++) {
            drowLine(g, width, height);
        }
        // 绘制随机字符
        for (int i = 0; i < randomString.length(); i++) {
            drowString(g, new String(new char[] {randomString.charAt(i)}), i);
        }
        LOG.info(randomString);
        g.dispose();
        try {
            // 将内存中的图片通过流动形式输出到客户端
            ImageIO.write(image, "JPEG", out);
        } catch (Exception e) {
            LOG.error("将内存中的图片通过流动形式输出到客户端失败>>>>   ", e);
        }

        return out.toByteArray();
    }

    /**
     * 绘制字符串
     */
    private void drowString(Graphics g, String randomString, int i) {
        Random random = new Random(System.currentTimeMillis());
        g.setFont(getFont());
        g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
                .nextInt(121)));
        g.translate(random.nextInt(3), random.nextInt(3));
        g.drawString(randomString, 13 * i, 16);
    }

    /**
     * 绘制干扰线
     */
    private void drowLine(Graphics g, int width, int height) {
        Random random = new Random(System.currentTimeMillis());
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        int xl = random.nextInt(13);
        int yl = random.nextInt(15);
        g.drawLine(x, y, x + xl, y + yl);
    }


}