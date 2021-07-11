package com.lhever.common.core.utils;

import com.lhever.common.core.consts.CommonConsts;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileUtils {

    private static Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    /**
     * @param filePath
     * @param encoding
     * @return
     */
    public static List<String> readLinesByPath(String filePath, String encoding) {
        encoding = StringUtils.isBlank(encoding) ? "UTF-8" : encoding;
        InputStream inputStream = null;
        File file = new File(filePath);

        if (file.exists() && file.isFile()) { // 判断文件是否存在
            try {
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                LOG.error("", e);
            }
        }

        List<String> lines = readLines(inputStream, encoding);
        return lines;
    }

    public static String readByClassLoader(String fileName) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        return read(inputStream, StandardCharsets.UTF_8);
    }

    public static String readByPath(String path) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            LOG.error("read file error", e);
        }
        if (inputStream == null) {
            return null;
        }
        return read(inputStream, StandardCharsets.UTF_8);
    }


    public static String readFile(File file, Charset charset) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LOG.error("read file error", e);
        }
        return read(inputStream, charset);
    }

    public static String readFile(File file, String charset) {
        return readFile(file, Charset.forName(charset));
    }

    public static String readFile(File file) {
        return readFile(file, StandardCharsets.UTF_8);
    }

    /**
     * 该工具类用于解决spring boot 打包后读取文件内容为空的问题
     *
     * @param fileName
     * @return
     */
    public static String readByClassPathResource(String fileName) {
        ClassPathResource cpr = new ClassPathResource(fileName);
        InputStream inputStream = null;
        try {
            inputStream = cpr.getInputStream();
        } catch (IOException e) {
            LOG.error("", e);
        }
        return read(inputStream, StandardCharsets.UTF_8);
    }

    public static InputStream getInputStreamByClassPathResource(String fileName) {
        ClassPathResource cpr = new ClassPathResource(fileName);
        InputStream inputStream = null;
        try {
            inputStream = cpr.getInputStream();
        } catch (IOException e) {
            LOG.error("", e);
        }
        return inputStream;
    }

    public static String read(InputStream inputStream, Charset charset) {
        if (inputStream == null) {
            return null;
        }
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        String data = null;
        try {
            byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
            data = new String(bdata, charset);
        } catch (Exception e) {
            LOG.error("", e);
        } finally {
            close(inputStream);
        }
        return data;
    }


    public static List<String> readLinesByClassLoader(String fileName) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        List<String> lines = readLines(inputStream, "UTF-8");
        return lines;
    }

    public static List<String> readLinesByClassPathResource(String fileName) {
        ClassPathResource cpr = new ClassPathResource(fileName);
        InputStream inputStream = null;
        try {
            inputStream = cpr.getInputStream();
        } catch (IOException e) {
            LOG.error("", e);
        }
        List<String> lines = readLines(inputStream, "UTF-8");
        return lines;
    }


    public static List<String> readLines(InputStream inputStream, String encoding) {
        List<String> lines = new ArrayList<String>();
        if (inputStream == null) {
            return lines;
        }
        InputStreamReader reader = null;
        encoding = StringUtils.isBlank(encoding) ? "UTF-8" : encoding;
        try {
            reader = new InputStreamReader(inputStream, encoding);// 考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (StringUtils.isNotBlank(line)) {
                    lines.add(line.trim());
                }
            }

        } catch (Exception e) {
            LOG.error("读取文件内容出错", e);
        } finally {
            close(reader);
        }

        return lines;
    }


    public void copyByBio(File fromFile, File toFile) throws FileNotFoundException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(fromFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(toFile));
            byte[] bytes = new byte[1024];
            int i;
            //读取到输入流数据，然后写入到输出流中去，实现复制
            while ((i = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, i);
            }
            outputStream.flush();
        } catch (Exception e) {
            LOG.error("copy error", e);
        } finally {
            close(inputStream);
            close(outputStream);
        }
    }


    /**
     * 用filechannel进行文件复制
     *
     * @param fromFile 源文件
     * @param toFile   目标文件
     */
    public static void copyByFileChannel(File fromFile, File toFile) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannelInput = null;
        FileChannel fileChannelOutput = null;
        try {
            fileInputStream = new FileInputStream(fromFile);
            fileOutputStream = new FileOutputStream(toFile);
            //得到fileInputStream的文件通道
            fileChannelInput = fileInputStream.getChannel();
            //得到fileOutputStream的文件通道
            fileChannelOutput = fileOutputStream.getChannel();
            //将fileChannelInput通道的数据，写入到fileChannelOutput通道
            fileChannelInput.transferTo(0, fileChannelInput.size(), fileChannelOutput);
        } catch (IOException e) {
            LOG.error("IOException", e);
        } finally {
            close(fileInputStream, fileOutputStream, fileChannelInput, fileChannelOutput);
        }
    }


    public static void copyByFileChannelEx(File fromFile, File toFile) {

        if (!fromFile.exists() || fromFile.isDirectory()) {
            throw new IllegalArgumentException("source file not exists nor source file is a directory");
        }

        try (FileInputStream fileInputStream = new FileInputStream(fromFile);
             FileOutputStream fileOutputStream = new FileOutputStream(toFile);
             FileChannel fileChannelInput = fileInputStream.getChannel();
             FileChannel fileChannelOutput = fileOutputStream.getChannel()) {

            fileChannelInput.transferTo(0, fileChannelInput.size(), fileChannelOutput);

        } catch (IOException e) {
            LOG.error("copy error", e);
        }
    }


    public static void copyByFileChannel(String from, String to) {

        File fileFrom = new File(from);
        if (!fileFrom.exists() || fileFrom.isDirectory()) {
            throw new IllegalArgumentException("source file not exists nor source file is a directory");
        }

        File fileTo = new File(to);

        copyByFileChannel(fileFrom, fileTo);
    }

    /**
     * 方法的功能说明:同时关闭多个Closeable对象
     *
     * @param closeables
     * @return
     * @author lihong10 2018/12/14 12:25
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/14 12:25
     * @modify by reason:{原因}
     */
    public static void close(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            close(closeable);
        }
    }


    /**
     * 方法的功能说明:关闭Closeable对象
     *
     * @param closeable
     * @return
     * @author lihong10 2018/12/14 12:25
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/14 12:25
     * @modify by reason:{原因}
     */
    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            LOG.error("", e);
        }
    }


    /**
     * 删除单个文件, 如果参数是目录， 不删除目录，返回false
     * 文件不存在，返回true
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     * @author lihong10 2018/12/27 16:31
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/27 16:31
     * @modify by reason:{原因}
     */
    public static boolean deleteFile(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return true;
        }
        File file = new File(fileName);
        return deleteFile(file);
    }

    /**
     * 删除单个文件, 如果参数是目录， 不删除目录，返回false
     * 文件不存在，返回true
     *
     * @param file 要删除的文件
     * @return 单个文件删除成功返回true，否则返回false
     * @author lihong10 2018/12/27 16:31
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/27 16:31
     * @modify by reason:{原因}
     */
    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.isFile()) {
            return file.delete();
        }

        LOG.info("delete failed, {} is not a file, maybe a directory", file.getName());
        return false;
    }

    /**
     * 删除目录, 如果传入的不是目录，不删除，返回false
     * 删除不存在的目录，返回true;
     *
     * @param dir
     * @return
     * @author lihong10 2018/12/27 16:42
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/27 16:42
     * @modify by reason:{原因}
     */
    public static boolean deleteDirectory(String dir) {
        if (StringUtils.isBlank(dir)) {
            return true;
        }
        File file = new File(dir);
        if (file.isDirectory()) {
            return deleteDirectory(file);
        }
        return false;
    }

    /**
     * 删除目录, 如果传入的不是目录，不删除，返回false
     * 删除不存在的目录，返回true;
     *
     * @param file
     * @return
     * @author lihong10 2018/12/27 16:42
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/27 16:42
     * @modify by reason:{原因}
     */
    public static boolean deleteDirectory(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
           return delete(file);
        }
        return false;
    }

    /**
     * 删除目录及目录下的文件, 返回的布尔值表示是否删除成功;
     * 只要有一个文件删除失败，就返回false;
     * 删除不存在的目录，返回true;
     *
     * @param fileName
     * @return
     * @author lihong10 2018/12/27 16:42
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/27 16:42
     * @modify by reason:{原因}
     */
    public static boolean delete(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return true;
        }
        File file = new File(fileName);
        return delete(file);
    }

    /**
     * 删除目录及目录下的文件, 返回的布尔值表示是否删除成功;
     * 只要有一个文件删除失败，就返回false；
     * 删除不存在的目录，返回true;
     *
     * @param file
     * @return
     * @author lihong10 2018/12/27 16:42
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/27 16:42
     * @modify by reason:{原因}
     */
    public static boolean delete(File file) {
        boolean success = true;
        if (file == null || !file.exists()) {
            return success;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subfile : files) {
                    success &= delete(subfile);
                }
            }
            success &= file.delete();
        } else {
            success &= file.delete();
        }
        return success;
    }

    /**
     * 将文本(content)写入fullFileName指定的文件中，文件不存在会新建, 写入格式是UTF-8
     *
     * @param fullFileName 文件的全路径名称
     * @param content      待写入到文件中的字符串
     * @return
     * @author lihong10 2018/12/26 15:44
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/26 15:44
     * @modify by reason:{原因}
     */
    public static void writeToFileByNio(String fullFileName, String content) {
        writeToFileByNio(fullFileName, content, CommonConsts.CHARSET_UTF8);

    }

    /**
     * 将文本(content)写入fullFileName指定的文件中，文件不存在会新建
     *
     * @param fullFileName 文件的全路径名称
     * @param content      待写入到文件中的字符串
     * @param charset      字符编码
     * @return
     * @author lihong10 2018/12/26 15:44
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/26 15:44
     * @modify by reason:{原因}
     */
    public static void writeToFileByNio(String fullFileName, String content, String charset) {
        FileOutputStream fos = null;
        try {

            File file = new File(fullFileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            FileChannel channel = fos.getChannel();
            ByteBuffer src = Charset.forName(charset).encode(content);
            // 字节缓冲的容量和limit会随着数据长度变化，不是固定不变的
            //LOG.info("初始化position:{}, limit:{}, 容量:{}", src.position(), src.limit(), src.capacity());
            int length = 0;

            while ((length = channel.write(src)) != 0) {
                //注意，这里不需要clear，将缓冲中的数据写入到通道中后 第二次接着上一次的顺序往下读
                //LOG.info("写入长度:{}", length);
            }

        } catch (Exception e) {
            LOG.error("write to file error: ", e);
        } finally {
            close(fos);
        }
    }

    /**
     * 判断某路径下是否有某文件夹,没有则创建
     *
     * @return
     * @author jianghaitao6 2019/3/2 9:37
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/2 9:37
     * @modify by reason:{原因}
     */
    public static boolean createDir(String dirPath, String dirName) {
        return createDir(dirPath + File.separator + dirName);
    }

    public static boolean createDir(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            return true;
        }
        return file.mkdirs();
    }

    /**
     * 情况目录下的所有内容
     * @return
     * @author jianghaitao6 2019/3/2 15:31
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/2 15:31
     * @modify by reason:{原因}
     */
    public static boolean clearDir(String path) {
        File file = new File(path);
        return clearDir(file);
    }

    public static boolean clearDir(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            //该方法的参数需要是一个目录,如果是文件不删除, 返回false
            return false;
        }
        boolean success = true;
        //列出子文件
        File[] children = file.listFiles();
        for (int i = 0; i < children.length; i++) {
            success &= delete(children[i]);
        }
        return success;
    }

    public static boolean fileExists(String path) {
        File file = new File(path);
        return exists(file) && file.isFile();
    }

    public static boolean fileExists(File file) {
        return exists(file) && file.isFile();
    }

    public static boolean dirExists(String path) {
        File file = new File(path);
        return exists(file) && file.isDirectory();
    }

    public static boolean dirExists(File dir) {
        return exists(dir) && dir.isDirectory();
    }

    public static boolean exists(File file) {
        return file != null && file.exists();
    }

    public static String trimTail(String fileName) {
        if (fileName == null) {
            return fileName;
        }
        fileName = fileName.trim();
        while (fileName.endsWith(CommonConsts.SLASH) || fileName.endsWith(CommonConsts.BACK_SLASH)) {
            fileName = fileName.substring(0, fileName.length() - 1);
        }
        return fileName;
    }

    public static String trimHead(String fileName) {
        if (fileName == null) {
            return fileName;
        }
        fileName = fileName.trim();
        while (fileName.startsWith(CommonConsts.SLASH) || fileName.startsWith(CommonConsts.BACK_SLASH)) {
            fileName = fileName.substring(1);
        }
        return fileName;
    }

    public static String trim(String fileName) {
        fileName = trimHead(fileName);
        fileName = trimTail(fileName);
        return fileName;
    }


    public static int copy(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[1024];
        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

    /**
     * Tests if the specified {@code File} is newer than the specified time reference.
     *
     * @param file       the {@code File} of which the modification date must be compared
     * @param timeMillis the time reference measured in milliseconds since the
     *                   epoch (00:00:00 GMT, January 1, 1970)
     * @return true if the {@code File} exists and has been modified after the given time reference.
     * @throws NullPointerException if the file is {@code null}
     */
    public static boolean isFileNewer(final File file, final long timeMillis) {
        Objects.requireNonNull(file, "file");
        if (!file.exists()) {
            return false;
        }
        return file.lastModified() > timeMillis;
    }


    @Test
    public void test() throws Exception {
        String temp_file_path = "d:/temp";
        clearDir(temp_file_path);
    }


}
