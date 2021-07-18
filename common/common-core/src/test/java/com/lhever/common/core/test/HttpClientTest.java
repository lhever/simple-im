package com.lhever.common.core.test;

import com.lhever.common.core.support.http.CustomHttpClient;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/11/24 15:08
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/11/24 15:08
 * @modify by reason:{方法名}:{原因}
 */
public class HttpClientTest {

    @Test
    public void download() {
        CustomHttpClient client = new CustomHttpClient();
        byte[] bytes = client.download("https://open.ys7.com/api/lapp/mq/downloadurl?appKey=e37a6936b00a47a2b86a99491246aa39&fileKey=ISAPI_FILES/E28958552_1/20201124150514640-E28958552-1-10000$encrypt=2,2020-11-24T15:05:13,a04e5b7d0a26e411d50ae580dfa6184f", null);
        writeFile(bytes, "F:/", "lalal.jpg");

    }

    @Test
    public void download2() throws FileNotFoundException {
        CustomHttpClient client = new CustomHttpClient();
        String url = "https://open.ys7.com/api/lapp/mq/downloadurl?appKey=e37a6936b00a47a2b86a99491246aa39&fileKey=ISAPI_FILES/E28958552_1/20201124150514640-E28958552-1-10000$encrypt=2,2020-11-24T15:05:13,a04e5b7d0a26e411d50ae580dfa6184f";
        client.download(null, url, null, new FileOutputStream("F:/la2.jpg"), true);


    }

    public static void writeFile(byte[] content, String path, String fileName) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(path + fileName);
            fos.write(content);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
