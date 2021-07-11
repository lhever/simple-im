package com.lhever.common.core.utils;


import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.support.logger.LogFactory;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private static Logger logger = LogFactory.getLogger(ZipUtils.class);

    /**
     * 方法的功能说明
     *
     * @param inputFile      输入文件夹|文件
     * @param inputFileAlias 输入文件夹别名，可以为null， 如果null，则使用文件|文件夹名称
     * @param outputFile     输出的zip压缩包的名称
     * @return
     * @author lihong10 2019/9/6 11:18
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/9/6 11:18
     * @modify by reason:{原因}
     */
    public static void zip(String inputFile, String inputFileAlias, String outputFile) {
        //创建zip输出流
        ZipOutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(outputFile));
            //创建缓冲输出流
            bos = new BufferedOutputStream(out);
            File input = new File(inputFile);
            if (FileUtils.dirExists(input) && ArrayUtils.isEmpty(input.listFiles()) && StringUtils.isBlank(inputFileAlias)) {
                return;
            }
            compress(out, bos, input, inputFileAlias);
        } catch (Exception e) {
            logger.error("压缩文件失败!!", e);
        } finally {
            FileUtils.close(bos, out);
        }
    }

    public static void zip(String inputFile, String outputFile) {
        zip(inputFile, null, outputFile);
    }

    /**
     * @param name 压缩文件名，可以写为null保持默认
     */
    //递归压缩
    public static void compress(ZipOutputStream out, BufferedOutputStream bos, File input, String name) throws IOException {
        if (name == null) {
            name = input.getName();
        }
        //如果路径为目录（文件夹）
        if (input.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = input.listFiles();

            if (flist.length == 0)//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入
            {
                out.putNextEntry(new ZipEntry(name + "/"));
            } else//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            {
                for (int i = 0; i < flist.length; i++) {
                    String compressName = StringUtils.isBlank(name) ? flist[i].getName() : name + "/" + flist[i].getName();
                    logger.info("压缩:{}", compressName);
                    compress(out, bos, flist[i], compressName);
                }
            }
        } else//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        {
            FileInputStream fis = new FileInputStream(input);
            BufferedInputStream bis = new BufferedInputStream(fis);
            try {
                out.putNextEntry(new ZipEntry(name));
                int len;
                //将源文件写入到zip文件中
                byte[] buf = new byte[1024];
                while ((len = bis.read(buf)) != -1) {
                    bos.write(buf, 0, len);
                }
            } catch (Exception e) {
                logger.error("解析失败!!!", e);
            } finally {
                FileUtils.close(bis, fis);
            }

        }
    }

    //压缩
    public static void compress(ZipOutputStream out, List<InputStream> input, List<String> names) throws IOException {
        if (CollectionUtils.isEmpty(input)){
            return;
        }
        for (int i = 0; i < input.size(); i++) {
            BufferedInputStream bis = new BufferedInputStream(input.get(i));
            try {
                out.putNextEntry(new ZipEntry(names.get(i)));
                int len;
                //将源文件写入到zip文件中
                byte[] buf = new byte[1024];
                while ((len = bis.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
            } catch (Exception e) {
                logger.error("打包失败!!!", e);
            } finally {
                FileUtils.close(bis);
            }
        }
    }

    public static void main(String[] args) {
        List<String> doorIds = new ArrayList<>(Arrays.asList("fag第三发放","范德萨发","的放散阀ddd"));
        List<String> names = new ArrayList<>(Arrays.asList("fag第三发放.jpg","范德萨发.jpg","的放散阀ddd.jpg"));
//        List<String> doorIds = new ArrayList<>(Arrays.asList("fag第三发放","asd"));
//        List<String> names = new ArrayList<>(Arrays.asList("fag第三发放.jpg","ddd.jpg"));
        //创建zip输出流
        ZipOutputStream out = null;
        BufferedOutputStream bos = null;
        List<InputStream> input = new ArrayList<>();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");//设置日期格式
            String name = df.format(new Date());
            out = new ZipOutputStream(new FileOutputStream("E:\\"+name+".zip"));
            //生成文件输入流
            for (String doorById : doorIds) {
                ByteArrayOutputStream osTemp = new ByteArrayOutputStream();
                QRCodeUtil.creatImage(doorById,osTemp,"的撒发范德萨范德萨范德萨发反反复复方法付付等待");
                input.add(new ByteArrayInputStream(osTemp.toByteArray()));
            }
            ZipUtils.compress(out, input, names);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (CollectionUtils.isNotEmpty(input)){
                for (InputStream inputStream : input) {
                    FileUtils.close(inputStream);
                }
            }
            FileUtils.close(out);
        }
    }
    /**
     * 不是文件不解压
     *
     * @param srcFile
     * @return
     * @author lihong10 2019/4/11 11:36
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/11 11:36
     * @modify by reason:{原因}
     */
    private static void check(File srcFile) {
        // 判断源文件是否存在
        if (srcFile == null || !srcFile.exists()) {
            throw new RuntimeException("待解压缩的文件" + srcFile.getPath() + "不存在");
        }
        if (srcFile.isDirectory()) {
            throw new RuntimeException("文件" + srcFile.getPath() + "是目录，无需解压");
        }
    }


    /**
     * 获取文件名，移除后缀
     *
     * @param srcFile
     * @return
     * @author lihong10 2019/4/11 11:37
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/11 11:37
     * @modify by reason:{原因}
     */
    public static String nameNoPostfix(File srcFile) {
        String name = srcFile.getName();
        int i = name.lastIndexOf(CommonConsts.DOT);
        name = (i > -1) ? name.substring(0, i) : name;
        return name;

    }


    /**
     * 移除最后的/ 或 \
     *
     * @param path
     * @return
     * @author lihong10 2019/4/11 11:36
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/11 11:36
     * @modify by reason:{原因}
     */
    private static String trimTail(String path) {
        if (path == null) {
            return path;
        }
        path = path.trim();
        path = (path.endsWith(CommonConsts.SLASH) || path.endsWith(CommonConsts.BACK_SLASH))
                ? path.substring(0, path.length() - 1) : path;
        return path;
    }

    /**
     * 解压缩文件
     *
     * @param filePath    压缩包所在路径
     * @param destDirPath 解压基路径
     * @return
     * @author lihong10 2019/4/11 11:37
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/11 11:37
     * @modify by reason:{原因}
     */
    public static void unZip(String filePath, String destDirPath) throws RuntimeException {
        File srcFile = new File(filePath);
        unZip(srcFile, destDirPath);

    }


    /**
     * 解压缩文件
     *
     * @param srcFile     压缩包
     * @param destDirPath 解压目标路径
     * @return
     * @author lihong10 2019/4/11 11:37
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/11 11:37
     * @modify by reason:{原因}
     */
    public static void unZip(File srcFile, String destDirPath) {

        long start = System.currentTimeMillis();

        check(srcFile);

        destDirPath = FileUtils.trimTail(destDirPath);
        FileUtils.createDir(destDirPath);

        // 开始解压
        ZipFile zipFile = null;
        try {
            //压缩文件含有中文，编码必须要gbk
            zipFile = new ZipFile(srcFile, Charset.forName("gbk"));
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                logger.info("解压:" + entry.getName());

                String unzipPath = destDirPath + CommonConsts.SLASH + entry.getName();
                // 如果是文件夹，就创建个文件夹
                if (entry.isDirectory()) {
                    FileUtils.createDir(unzipPath);
                } else {
                    doUnzip(zipFile, entry, unzipPath);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("解压缩文件失败", e);
        } finally {
            // 关流顺序，先打开的后关闭
            FileUtils.close(zipFile);
        }


        long end = System.currentTimeMillis();
        logger.debug("解压完成,耗时{}ms", end - start);
    }

    /**
     * 将压缩包中的文件拷贝到目录路径, 达到解压目的
     *
     * @param zipFile
     * @param entry
     * @param destDirPath
     * @return
     * @author lihong10 2019/4/11 11:38
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/11 11:38
     * @modify by reason:{原因}
     */
    public static void doUnzip(ZipFile zipFile, ZipEntry entry, String destDirPath) throws Exception {
        InputStream is = null;
        FileOutputStream os = null;

        try {
            // 如果是文件，就先创建一个文件，然后用io流把内容copy过去
            File targetFile = new File(destDirPath);
            // 保证这个文件的父文件夹必须要存在
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            targetFile.createNewFile();
            // 将压缩文件内容写入到这个文件中
            is = zipFile.getInputStream(entry);
            os = new FileOutputStream(targetFile);
            int len;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } finally {
            IOUtils.closeQuietly(is, os);
        }
    }

    public static void fastUnZip(String inputFile, String destDirPath) {
        if (StringUtils.isBlank(inputFile)) {
            throw new RuntimeException("未指定待解压缩的文件");
        }
        File srcFile = new File(inputFile);
        fastUnZip(srcFile, destDirPath);

    }


    public static void fastUnZip(File srcFile, String destDirPath) {

        check(srcFile);//判断源文件是否存在

        destDirPath = FileUtils.trimTail(destDirPath);

        //开始解压
        //构建解压输入流
        ZipInputStream zIn = null;
        ZipEntry entry = null;
        try {
            zIn = new ZipInputStream(new FileInputStream(srcFile), Charset.forName("gbk"));
            while ((entry = zIn.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    File file = new File(destDirPath, entry.getName());
                    if (!file.exists()) {
                        new File(file.getParent()).mkdirs();//创建此文件的上级目录
                    }
                    logger.info("创建" + file.getAbsolutePath());
                    file.createNewFile();
                    OutputStream out = new FileOutputStream(file);
                    BufferedOutputStream bos = new BufferedOutputStream(out);
                    int len = -1;
                    byte[] buf = new byte[1024];

                    while ((len = zIn.read(buf)) != -1) {
                        bos.write(buf, 0, len);
                    }
                    // 关流顺序，先打开的后关闭
                    IOUtils.closeQuietly(out);
                } else {
                    String dirPath = destDirPath + "/" + entry.getName();
                    logger.info("创建" + dirPath);
                    FileUtils.createDir(dirPath);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("解压缩文件" + entry.getName() + "失败", e);
        } finally {
            IOUtils.closeQuietly(zIn);
        }
    }


}