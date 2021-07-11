package com.lhever.common.core.test;

import com.lhever.common.core.utils.IOUtils;
import com.lhever.common.core.utils.ImgUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/9/18 17:55
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/9/18 17:55
 * @modify by reason:{方法名}:{原因}
 */
public class ImgFormatTest {

    private static int size = 1000;
    public void convertFormat(String input, String outPut, String targetFormat) throws IOException {
        FileInputStream fis = new FileInputStream(input);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        IOUtils.copyLarge(fis, os);

        byte[] jpgs = ImgUtils.formatOrResize(os.toByteArray(), 2880, 2160, true, targetFormat);
//        byte[] jpgs = ImgUtils.formatOrScale(os.toByteArray(), 1d, true, targetFormat);


        ByteBuffer src = ByteBuffer.wrap(jpgs);
        FileOutputStream  fos = new FileOutputStream(outPut);
        FileChannel channel = null;
        try {
            channel = fos.getChannel();
            // 字节缓冲的容量和limit会随着数据长度变化，不是固定不变的
            //LOG.info("初始化position:{}, limit:{}, 容量:{}", src.position(), src.limit(), src.capacity());
            int length = 0;
            while ((length = channel.write(src)) != 0) {
                //注意，这里不需要clear，将缓冲中的数据写入到通道中后 第二次接着上一次的顺序往下读
                //LOG.info("写入长度:{}", length);
            }
        } finally {
            IOUtils.closeQuietly(channel);
        }

        System.out.println(new Tika().detect(new File(input)));
        System.out.println(new Tika().detect(new File(outPut)));
    }


    @Test
    public void testConvertFormat() throws IOException {
        convertFormat("F:/imgs/deep.gif", "F:/imgs/deep_1.jpg", "jpg");
        convertFormat("F:/imgs/disconnect.bmp", "F:/imgs/disconnect_1.jpg", "jpg");
        convertFormat("F:/imgs/person2.jpg", "F:/imgs/person2_1.jpg", "jpg");
        convertFormat("F:/imgs/face2.jpg", "F:/imgs/face2_1.jpg", "jpg");

    }
    @Test
    public void testConvertFormatlyc() throws IOException {
        convertFormatlyc("F:/linshi/20201013144118.jpg", "F:/linshi/new1.jpg","jpg");
        convertFormatlyc("F:/linshi/20201013144131.jpg", "F:/linshi/new2.jpg","jpg");
        convertFormatlyc("F:/linshi/timg.jpg", "F:/linshi/new3.jpg","jpg");
    }
    @Test
    public void testConvertFormatlyc2() throws IOException {
        File pdfFile = new File("F:/linshi/20201013144118.jpg");
        FileInputStream fileInputStream = new FileInputStream(pdfFile);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(fileInputStream, bos);


        byte[] bytes = ImgUtils.resizeAndFormat(bos.toByteArray(), false, "jpg");
        ByteBuffer src = ByteBuffer.wrap(bytes);
        FileOutputStream  fos = new FileOutputStream("F:/linshi/new110.jpg");
        FileChannel channel = null;
        try {
            channel = fos.getChannel();
            // 字节缓冲的容量和limit会随着数据长度变化，不是固定不变的
            //LOG.info("初始化position:{}, limit:{}, 容量:{}", src.position(), src.limit(), src.capacity());
            int length = 0;
            while ((length = channel.write(src)) != 0) {
                //注意，这里不需要clear，将缓冲中的数据写入到通道中后 第二次接着上一次的顺序往下读
                //LOG.info("写入长度:{}", length);
            }
        } finally {
            IOUtils.closeQuietly(channel);
        }
    }

    public void convertFormatlyc(String input, String outPut, String targetFormat) throws IOException {
        File picture = new File(input);
        FileInputStream fis = new FileInputStream(picture);
        BufferedImage read = ImageIO.read(fis);
        System.out.println(String.format("%.1f",picture.length()/1024.0));
        double v = picture.length() / 1024.0;
        if (v<200&&v>20){
            System.out.println("大小在20~200Kb之间,不处理");
            return;
        }
        if (read.getHeight()==0||read.getWidth()==0){
            return;
        }
        System.out.println(read.getHeight());
        System.out.println(read.getWidth());
        int width = 0;
        int height = 0;
        if (read.getHeight()>read.getWidth()){
            double wh = (double)read.getWidth()/read.getHeight();
            height = size;
            width = (int) (size*wh);
        }else{
            double hw = (double)read.getHeight()/read.getWidth();
            width = size;
            height = (int) (size*hw);
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(read,targetFormat,os);
        byte[] jpgs = ImgUtils.formatOrResize(os.toByteArray(), width, height, true, targetFormat);
//        byte[] jpgs = ImgUtils.formatOrScale(os.toByteArray(), 1d, true, targetFormat);


        ByteBuffer src = ByteBuffer.wrap(jpgs);
        FileOutputStream  fos = new FileOutputStream(outPut);
        FileChannel channel = null;
        try {
            channel = fos.getChannel();
            // 字节缓冲的容量和limit会随着数据长度变化，不是固定不变的
            //LOG.info("初始化position:{}, limit:{}, 容量:{}", src.position(), src.limit(), src.capacity());
            int length = 0;
            while ((length = channel.write(src)) != 0) {
                //注意，这里不需要clear，将缓冲中的数据写入到通道中后 第二次接着上一次的顺序往下读
                //LOG.info("写入长度:{}", length);
            }
        } finally {
            IOUtils.closeQuietly(channel);
        }

        System.out.println(new Tika().detect(new File(input)));
        System.out.println(new Tika().detect(new File(outPut)));
    }


    private static void generateOutputstream(){
        try(OutputStream outputStream = new FileOutputStream("data/2016010208_outputstream.png")) { //自动关闭流
            Thumbnails.of("data/2016010208.jpg").
                    size(500,500).
                    outputFormat("png"). // 转换格式
                    toOutputStream(outputStream); // 写入输出流
        } catch (IOException e) {
            System.out.println("原因: " + e.getMessage());
        }
    }
}
