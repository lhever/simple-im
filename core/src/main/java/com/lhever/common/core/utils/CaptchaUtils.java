/**
 * 
 */
package com.lhever.common.core.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author dengyishi
 *
 *         2017年11月7日 下午2:05:12
 */
public class CaptchaUtils {

	private CaptchaUtils() {
	}

	/*
	 * 随机数
	 */

	/*
	 * 获取随机数颜色
	 */
	private static Color getRandomColor() {
		Random random = new Random(System.currentTimeMillis());
		return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
	}

	/*
	 * 返回某颜色的反色
	 */
	private static Color getReverseColor(Color c) {
		return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
	}

	public static void outputCaptcha(int width, int height, String  randomString, OutputStream os) throws IOException {


		Color color = getRandomColor();
		Color reverse = getReverseColor(color);

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		g.setColor(color);
		g.fillRect(0, 0, width, height);
		g.setColor(reverse);
		g.drawString(randomString, 15, 20);
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0, n = random.nextInt(100); i < n; i++) {
			g.drawRect(random.nextInt(width), random.nextInt(height), 1, 1);
		}

		// 转成JPEG格式
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
		encoder.encode(bi);
		os.flush();
	}
}
