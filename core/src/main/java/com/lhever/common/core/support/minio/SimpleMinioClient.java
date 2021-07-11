package com.lhever.common.core.support.minio;

import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.utils.FileTypeUtils;
import com.lhever.common.core.utils.IOUtils;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SimpleMinioClient {

    private MinioClient minioClient = null;

    private Logger logger = LoggerFactory.getLogger(SimpleMinioClient.class);

    private static final String MEDIA_OCTET = "application/octet-stream";

    /**
     * 获取minioClient
     *
     * @return
     * @author jianghaitao6 2019/3/4 16:34
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/4 16:34
     * @modify by reason:{原因}
     */
    public SimpleMinioClient(String host, Integer port, String username, String password) {
        try {
            minioClient = new MinioClient("http://" + host + CommonConsts.COLON + port, username, password);
        } catch (InvalidEndpointException | InvalidPortException e) {
            throw new RuntimeException("mino client init error", e);
        }
    }

    public String presignedGetObject(String bucketName, String objectName) {
        logger.info("presignedGetObject, bucketName:{}, objectName:{}", bucketName, objectName);
        try {
            return minioClient.presignedGetObject(bucketName, objectName, 60 * 60);
        } catch (Exception e) {
            throw new RuntimeException("获取下载资源路径失败", e);
        }
    }

    /**
     * 拷贝文件
     *
     * @return
     * @author jianghaitao6 2019/4/8 19:01
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/8 19:01
     * @modify by reason:{原因}
     */
    public void copyobject(String bucketName, String objectName, String destBucketName) {
        logger.info("copy object, original bucket name:{}, object name:{}, dest bucket name:{}", bucketName, objectName, destBucketName);
        try {
            minioClient.copyObject(bucketName, objectName, destBucketName);
        } catch (Exception e) {
            throw new RuntimeException("拷贝资源缩略图失败", e);
        }
    }

    public void removeObject(String bucketName, String objectName) {
        logger.info("remove object, bucket name:{}, object name:{}", bucketName, objectName);
        try {
            minioClient.removeObject(bucketName, objectName);
        } catch (Exception e) {
            throw new RuntimeException("拷贝资源缩略图失败", e);
        }
    }

    /**
     * 下载图片到文件夹
     *
     * @return
     * @author jianghaitao6 2019/4/17 16:23
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/17 16:23
     * @modify by reason:{原因}
     */
    public void downloadTo(String mybucket, String myobject, String targetDir) throws Exception {
        //Object object = minioClient.statObject(mybucket, myobject);
        //if (object == null) {
        //throw new BusinessException("未找到指定文件!!");
        //}
        minioClient.getObject(mybucket, myobject, targetDir + myobject);
    }

    /**
     * 下载minio中的文件到本地
     *
     * @return
     * @author jianghaitao6 2019/3/4 16:35
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/4 16:35
     * @modify by reason:{原因}
     */
    public void downLoad(String bucket, String objectName, OutputStream os) {
        byte[] buff = new byte[1024];
        InputStream is = null;
        try {
            logger.info("download, bucket:{}, object:{}", bucket, objectName);

            //获取要下载的文件输入流
            is = minioClient.getObject(bucket, objectName);

            //将输入流写入到buffer缓冲区
            int i = is.read(buff);
            while (i != -1) {
                //将缓冲区的数据转移到输出流
                os.write(buff, 0, i);
                i = is.read(buff);
            }
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException("从minio中下载资源文件失败", e);
        } finally {
            IOUtils.closeQuietly(is, os);
        }
    }


    /**
     * 上传文件到minio
     *
     * @return
     * @author jianghaitao6 2019/3/4 16:43
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/3/4 16:43
     * @modify by reason:{原因}
     */
    public void upLoad(InputStream is, String bucket, String objectName, String contentType) throws Exception {
        //使用Minio服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        createBucket(bucket);
        logger.info("bucket name:{}, fileName:{}, fileContentType:{}", bucket, objectName, contentType);
        minioClient.putObject(bucket, objectName, is, is.available(), contentType);
    }



    public void upLoad(File file, String bucket, String objectName) {
        FileInputStream fis = null;
        try {
            //使用Minio服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            createBucket(bucket);
            //将上传的文件转成流
            fis = new FileInputStream(file);
            minioClient.putObject(bucket, objectName, fis, fis.available(), MEDIA_OCTET);
        } catch (Exception e) {
            throw new RuntimeException("上传文件到minion异常!", e);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }


    /**
     * 将图片文件上传到指定的bucket中，上传到minio后，文件以 fileName命名
     * 图片上传到minio,想要在前端正确显示，需要给予正确的文件类型,代码中动态识别文件类型
     * @param file
     * @param bucket
     * @param objectName
     * @return
     * @author lihong10 2019/4/11 14:12
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/4/11 14:12
     * @modify by reason:{原因}
     */
    public void upLoadImage(File file, String bucket, String objectName) throws Exception {
        InputStream fis = null;
        BufferedInputStream bis = null;
        try {
            //使用Minio服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            createBucket(bucket);
            //将上传的文件转成流
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            String imageType = getImageType(bis);
            // 使用putObject上传一个文件到存储桶中。
            logger.info("file origin name:{}, bucket:{}, file store name:{}, fileType:{}",  file.getName(), bucket, objectName, imageType);
            minioClient.putObject(bucket, objectName, bis, bis.available(), imageType);
        } finally {
            IOUtils.closeQuietly(bis, fis);
        }
    }


    public void createBucket(String bucket) throws IOException,
            InvalidKeyException, NoSuchAlgorithmException,
            InsufficientDataException, InternalException,
            NoResponseException, InvalidBucketNameException,
            XmlPullParserException, ErrorResponseException, RegionConflictException {
        boolean isExist = minioClient.bucketExists(bucket);
        if (!isExist) {
            minioClient.makeBucket(bucket);
        }

    }

    /**
     * 推测图片流的具体类型，如果不以image开头（不是图片流），统一返回application/octet-stream
     * @author lihong10 2019/7/23 15:28
     * @param is
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/7/23 15:28
     * @modify by reason:{原因}
     */
    public String getImageType(InputStream is) {
        MediaType mediaType = FileTypeUtils.getMediaType(is, false);
        if (mediaType == null) {
            return MEDIA_OCTET;
        }

        //如果media type不以image/开头，统一识别为application/octet-stream
        String type = mediaType.toString();
        if (type.startsWith("image/")) {
            return type;
        } else {
            return MEDIA_OCTET;
        }
    }


    /**
     * 检测指定的桶是否存在名为fileName的文件
     * @author lihong10 2019/6/25 16:38
     * @param bucket 桶名
     * @param fileName 文件名
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/6/25 16:38
     * @modify by reason:{原因}
     */
    public MinioFileState fileExists(String bucket, String fileName) {
        try {
            minioClient.statObject(bucket, fileName);
            return MinioFileState.EXISTS;
        } catch (InsufficientDataException e) {
            logger.error("", e);
            return MinioFileState.NOT_EXISTS;
        } catch (InvalidBucketNameException | NoSuchAlgorithmException |
                IOException | InvalidKeyException |
                NoResponseException | XmlPullParserException |
                ErrorResponseException | InternalException e) {
            logger.error("", e);
            return MinioFileState.MAYBE_NOT_EXISTS;
        } catch (Exception e) {
            logger.error("", e);
            return MinioFileState.MAYBE_NOT_EXISTS;
        }
    }

}
