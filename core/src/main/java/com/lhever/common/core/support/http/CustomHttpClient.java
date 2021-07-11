package com.lhever.common.core.support.http;

import com.lhever.common.core.utils.IOUtils;
import com.lhever.common.core.utils.JsonUtils;
import com.lhever.common.core.utils.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/10/13 16:33
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/10/13 16:33
 * @modify by reason:{方法名}:{原因}
 */
public class CustomHttpClient {

    private static  final Logger logger = LoggerFactory.getLogger(CustomHttpClient.class);


    private static final int DEFAULT_POOL_MAX_TOTAL = 250;
    private static final int DEFAULT_POOL_MAX_PER_ROUTE = 250;

    private static final int DEFAULT_CONNECT_TIMEOUT = 25000;
    private static final int DEFAULT_CONNECT_REQUEST_TIMEOUT = 25000;
    private static final int DEFAULT_SOCKET_TIMEOUT = 180000;

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String OPEN = "(";
    private static final String CLOSE = "): ";
    private static final String URL_TIP = "request url: ";
    private static final String TIME_TIP = "take time: ";
    private static final String PARAM_TIP = "with param: ";
    private static final String HEADER_TIP = "header: ";
    private static final String RESPONSE_TIP =  "get response: ";

    private PoolingHttpClientConnectionManager gcm = null;

    private CloseableHttpClient httpClient = null;

    private IdleConnectionMonitorThread idleThread = null;

    // 连接池的最大连接数
    private final int maxTotal;
    // 连接池按route配置的最大连接数
    private final int maxPerRoute;

    // tcp connect的超时时间
    private final int connectTimeout;
    // 从连接池获取连接的超时时间
    private final int connectRequestTimeout;
    // tcp io的读写超时时间
    private final int socketTimeout;


    private ThreadLocal<Boolean> logFlag = new ThreadLocal() {
        @Override
        protected Boolean initialValue() {
            return Boolean.TRUE;
        }
    };

    public void offLog() {
        logFlag.set(false);
    }

    public void onLog() {
        logFlag.set(true);
    }


    /**
     * 该方法不能随意调用的，调用该方法就达不到链接池化的作用了
     * @author lihong10 2020/11/24 16:02
     * @param
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2020/11/24 16:02
     * @modify by reason:{原因}
     */
    public void close() {
        IOUtils.closeQuietly(httpClient);
        idleThread.shutdown();
    }


    public CustomHttpClient() {
        this(CustomHttpClient.DEFAULT_POOL_MAX_TOTAL,
                CustomHttpClient.DEFAULT_POOL_MAX_PER_ROUTE,
                CustomHttpClient.DEFAULT_CONNECT_TIMEOUT,
                CustomHttpClient.DEFAULT_CONNECT_REQUEST_TIMEOUT,
                CustomHttpClient.DEFAULT_SOCKET_TIMEOUT);
    }

    public CustomHttpClient(int maxTotal, int maxPerRoute, int connectTimeout, int connectRequestTimeout, int socketTimeout) {

        this.maxTotal = maxTotal;
        this.maxPerRoute = maxPerRoute;
        this.connectTimeout = connectTimeout;
        this.connectRequestTimeout = connectRequestTimeout;
        this.socketTimeout = socketTimeout;

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
//                .register("https", new SSLConnectionSocketFactory(defaultSSLContext(), new TrustAnyHostnameVerifier()))
                .build();

        this.gcm = new PoolingHttpClientConnectionManager(registry);
        this.gcm.setMaxTotal(this.maxTotal);
        this.gcm.setDefaultMaxPerRoute(this.maxPerRoute);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(this.connectTimeout)                     // 设置连接超时
                .setSocketTimeout(this.socketTimeout)                       // 设置读取超时
                .setConnectionRequestTimeout(this.connectRequestTimeout)    // 设置从连接池获取连接实例的超时
                .build();

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        this.httpClient = httpClientBuilder
                .setConnectionManager(this.gcm)
                .setDefaultRequestConfig(requestConfig)
                .build();

        this.idleThread = new IdleConnectionMonitorThread(this.gcm);
        this.idleThread.start();

    }

    public String doGet(String url, Map<String, String> reqHeader, Map<String, Object> params) {
        return doGet(null, url, reqHeader, params, null);
    }
    public String doGet(RequestConfig config, String url, Map<String, String> reqHeader, Map<String, Object> params) {
        return doGet(config, url, reqHeader, params, null);
    }

    public String doGet(RequestConfig config, String url, Map<String, String> reqHeader, Map<String, Object> params, Map<String, String> respHeader) {
        long startTime = System.currentTimeMillis();
        CloseableHttpResponse response = null;
        try {
            // *) 构建GET请求头
            String apiUrl = getUrlWithParams(url, params);
            HttpGet httpGet = new HttpGet(apiUrl);
            if (config != null) {
                httpGet.setConfig(config);
            }

            // *) 设置header信息
            setRequestHeader(httpGet, reqHeader);

            response = httpClient.execute(httpGet);
            if (response == null) {
                return null;
            }
            StatusLine statusLine = response.getStatusLine();

            setResponseHeader(respHeader, response.getAllHeaders());

            //int statusCode = response.getStatusLine().getStatusCode();
            //if (statusCode == HttpStatus.SC_OK) {}

            String res = readEntity(response.getEntity());

            logRespMsg(url, startTime, reqHeader, JsonUtils.object2Json(params), statusLine, res, false);
            return res;
        } catch (Throwable e) {
            String tip = "调用get方法出错:";
            logger.error(getReqMsg(tip, url, startTime, reqHeader, JsonUtils.object2Json(params)), e);
            throwError(tip, e);
        } finally {
            IOUtils.closeQuietly(response);
        }
        return null;
    }


    public byte[] download(String url, Map<String, String> reqHeader) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
        download(null, url, reqHeader, os, true);
        byte[] bytes = os.toByteArray();
        return bytes;
    }

    public byte[] download(RequestConfig config, String url, Map<String, String> reqHeader) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(4096);
        download(config, url, reqHeader, os, true);
        byte[] bytes = os.toByteArray();
        return bytes;
    }


    public void download(RequestConfig config, String url, Map<String, String> reqHeader, OutputStream os, boolean closeOs) {
        long startTime = System.currentTimeMillis();
        CloseableHttpResponse response = null;
        InputStream is = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            if (config != null) {
                httpGet.setConfig(config);
            }
            setRequestHeader(httpGet, reqHeader);
            response = httpClient.execute(httpGet);
            if (response == null) {
                return;
            }

            HttpEntity entity = response.getEntity();
            if (entity != null && (is = entity.getContent()) != null) {
                IOUtils.copyLarge(is, os);
            }
        } catch (Throwable e) {
            String tip = "调用get方法下载文件出错:";
            logger.error(getReqMsg(tip, url, startTime, reqHeader, ""), e);
            throwError(tip, e);
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(response);
            if (closeOs) {
                IOUtils.closeQuietly(os);
            }
        }
    }



    public String doPostForm(String url, Map<String, String> reqHeader, Map<String, Object> params) {
        return doPostForm(null, url, reqHeader, params, null);
    }
    public String doPostForm(RequestConfig config, String url, Map<String, String> reqHeader, Map<String, Object> params) {
        return doPostForm(config, url, reqHeader, params, null);
    }

    public String doPostForm(RequestConfig config, String url, Map<String, String> reqHeader, Map<String, Object> params, Map<String, String> respHeader) {
        long startTime = System.currentTimeMillis();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            if (config != null) {
                httpPost.setConfig(config);
            }
            // *) 配置请求headers
            setRequestHeader(httpPost, reqHeader);

            // *) 配置请求参数
            if (params != null && params.size() > 0) {
                HttpEntity entityReq = getUrlEncodedFormEntity(params);
                httpPost.setEntity(entityReq);
            }

            response = httpClient.execute(httpPost);
            if (response == null) {
                return null;
            }
            StatusLine statusLine = response.getStatusLine();

            setResponseHeader(respHeader, response.getAllHeaders());

            String res = readEntity(response.getEntity());

            logRespMsg(url, startTime, reqHeader, JsonUtils.object2Json(params), statusLine, res, false);
            return res;
        } catch (Throwable e) {
            String tip = "调用post方法提交表单参数出错:";
            logger.error(getReqMsg(tip, url, startTime, reqHeader, JsonUtils.object2Json(params)), e);
            throwError(tip, e);
        } finally {
            IOUtils.closeQuietly(response);
        }
        return null;
    }

    public String doPostBody(String url, Map<String, String> reqHeader, String body) {
        return doPostBody(null, url, reqHeader, body, null);

    }

    public String doPostBody(RequestConfig config, String url, Map<String, String> reqHeader, String body) {
        return doPostBody(config, url, reqHeader, body, null);

    }

    public String doPostBody(RequestConfig config, String url, Map<String, String> reqHeader, String body, Map<String, String> respHeader) {
        long startTime = System.currentTimeMillis();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            if (config != null) {
                httpPost.setConfig(config);
            }
            // *) 配置请求headers
            setRequestHeader(httpPost, reqHeader);

            if (body != null) {
                StringEntity entity = new StringEntity(body, Charset.forName("UTF-8"));
                setContentType(entity, reqHeader);
                httpPost.setEntity(entity);
            }

            response = httpClient.execute(httpPost);
            if (response == null) {
                return null;
            }
            StatusLine statusLine = response.getStatusLine();

            setResponseHeader(respHeader, response.getAllHeaders());

            String res = readEntity(response.getEntity());
            logRespMsg(url, startTime, reqHeader, body, statusLine, res, false);
            return res;
        } catch (Throwable e) {
            String tip = "调用post方法提交请求体出错:";
            logger.error(getReqMsg(tip, url, startTime, reqHeader,  body), e);
            throwError(tip, e);
        } finally {
            IOUtils.closeQuietly(response);
        }
        return null;
    }

    public String doPostBytes(String url, Map<String, String> reqHeader, byte[] body) {
        return doPostBytes(null, url, reqHeader, body, null);
    }

    public String doPostBytes(RequestConfig config, String url, Map<String, String> reqHeader, byte[] body) {
        return doPostBytes(config, url, reqHeader, body, null);
    }


    public String doPostBytes(RequestConfig config, String url, Map<String, String> reqHeader, byte[] body, Map<String, String> respHeader) {
        long startTime = System.currentTimeMillis();
        String bodyStr = "byte[" + (body == null ? 0 : body.length) + "]";
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            if (config != null) {
                httpPost.setConfig(config);
            }
            // *) 配置请求headers
            setRequestHeader(httpPost, reqHeader);

            if (body != null) {
                ByteArrayEntity entity = new ByteArrayEntity(body);
                setContentType(entity, reqHeader);
                httpPost.setEntity(entity);
            }

            response = httpClient.execute(httpPost);
            if (response == null) {
                return null;
            }
            StatusLine statusLine = response.getStatusLine();

            setResponseHeader(respHeader, response.getAllHeaders());

            String res = readEntity(response.getEntity());
            logRespMsg(url, startTime, reqHeader, bodyStr, statusLine, res, false);
            return res;
        } catch (Throwable e) {
            String tip = "调用post方法提交请求体出错:";
            logger.error(getReqMsg(tip, url, startTime, reqHeader,  bodyStr), e);
            throwError(tip, e);
        } finally {
            IOUtils.closeQuietly(response);
        }
        return null;
    }

    public String doPutBody(String url, Map<String, String> reqHeader, String body) {
        return doPutBody(null, url, reqHeader, body, null);

    }

    public String doPutBody(RequestConfig config, String url, Map<String, String> reqHeader, String body) {
        return doPutBody(config, url, reqHeader, body, null);

    }

    public String doPutBody(RequestConfig config, String url, Map<String, String> reqHeader, String body, Map<String, String> respHeader) {
        long startTime = System.currentTimeMillis();
        CloseableHttpResponse response = null;
        try {
            HttpPut httpPut = new HttpPut(url);
            if (config != null) {
                httpPut.setConfig(config);
            }
            // *) 配置请求headers
            setRequestHeader(httpPut, reqHeader);

            if (body != null) {
                StringEntity entity = new StringEntity(body, Charset.forName("UTF-8"));
                setContentType(entity, reqHeader);
                httpPut.setEntity(entity);
            }

            response = httpClient.execute(httpPut);
            if (response == null) {
                return null;
            }
            StatusLine statusLine = response.getStatusLine();
            setResponseHeader(respHeader, response.getAllHeaders());

            String res = readEntity(response.getEntity());
            logRespMsg(url, startTime, reqHeader, body, statusLine, res, false);
            return res;
        } catch (Throwable e) {
            String tip = "调用put方法提交请求体出错:";
            logger.error(getReqMsg(tip, url, startTime, reqHeader, body), e);
            throwError(tip, e);
        } finally {
            IOUtils.closeQuietly(response);
        }
        return null;
    }


    private static void setContentType(AbstractHttpEntity entity, Map<String, String> reqHeader) {
        if (reqHeader == null || entity == null) {
            return;
        }
        String contentType = reqHeader.get(CONTENT_TYPE);
        if (StringUtils.isNotBlank(contentType)) {
            entity.setContentType(contentType);
        }
    }

    private static String readEntity(HttpEntity entity) throws IOException {
        if (entity != null) {
            String res =  EntityUtils.toString(entity, "UTF-8");
            return res;
        }
        return null;
    }


    private static void setRequestHeader(HttpRequestBase httpReq, Map<String, String> reqHeader) {
        if (reqHeader != null && reqHeader.size() > 0) {
            for (Map.Entry<String, String> entry : reqHeader.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (key != null && value != null) {
                    httpReq.addHeader(key, value);
                }
            }
        }
    }

    private static void setResponseHeader(Map<String, String> respHeader, Header[] rspHeader) {
        if (respHeader == null || rspHeader == null) {
            return;
        }
        if (respHeader.size() > 0) {
            for (Header h : rspHeader) {
                if (respHeader.containsKey(h.getName())) {
                    respHeader.put(h.getName(), h.getValue());
                }
            }
        } else {
            for (Header h : rspHeader) {
                respHeader.put(h.getName(), h.getValue());
            }
        }
    }


    private HttpEntity getUrlEncodedFormEntity(Map<String, Object> params) {
        List<NameValuePair> pairList = new ArrayList<>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            Object value = entry.getValue();
            NameValuePair pair = new BasicNameValuePair(entry.getKey(), value == null ? null : value.toString());
            pairList.add(pair);
        }
        return new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8"));
    }

    private String getUrlWithParams(String url, Map<String, Object> params) {
        if (params == null) {
            return url;
        }
        boolean first = true;
        StringBuilder sb = new StringBuilder(url);
        for (String key : params.keySet()) {
            char ch = '&';
            if (first == true) {
                if (url.indexOf('?') > 0) { //url可能已经携带了部分参数
                   ch = '&';
                } else {
                    ch = '?';
                }
                first = false;
            }
            Object value = params.get(key);

            if (value != null) {
                try {
                    String sval = URLEncoder.encode(value.toString(), "UTF-8");
                    sb.append(ch).append(key).append("=").append(sval);
                } catch (UnsupportedEncodingException e) {
                    logger.error("", e);
                    sb.append(ch).append(key).append("=").append(value.toString());
                }
            } else {
                sb.append(ch).append(key).append("=").append("");
            }
        }
        return sb.toString();
    }


    public void shutdown() {
        idleThread.shutdown();
    }


    /**
     * 部分参数太长，打印日志时候不打印该参数值
     * @author lihong10 2020/9/2 17:19
     * @param params
     * @return
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2020/9/2 17:19
     * @modify by reason:{原因}
     */
    public static String modifyParam(String params) {
        if (StringUtils.isEmpty(params)) {
            return params;
        }
        //补充一点点优化，包含关键字base64FaceImageFile才替换
        if (params.contains("base64FaceImageFile")) {
            return params.replaceAll("\"base64FaceImageFile\":\"[[\\s\\S]&&[^\"]]*\"","\"base64FaceImageFile\":\"...\"");
        }
        return params;
    }

    private void throwError(String msg, Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        RuntimeException runEx = new RuntimeException(msg);
        runEx.setStackTrace(stackTrace);
        throw  runEx;
    }


    private String getReqMsg(String mark, String url, long startTime, Map<String, String> header, String param) {
        StringBuilder builder = new StringBuilder();
        builder.append(mark)
                .append("request url: ")
                .append(url)
                .append(", take time: ")
                .append(System.currentTimeMillis() - startTime)
                .append(", header: ")
                .append(JsonUtils.object2Json(header))
                .append(", request param: ")
                .append(modifyParam(param));
        return builder.toString();
    }

    //打印日志， 明确指定容量，目前发现打印日志的速度，有时候比调用接口的速度慢太多。所以做了一点优化
    public void logRespMsg(String url, long startTime, Map<String, String> header, String params, StatusLine statusLine, String resp, boolean printHeader) {
        if (!logFlag.get()) {
            logFlag.set(true);
            return;
        }
        String headerValue = null;//printHeader 为true再复制
        String paramValue = modifyParam(params);
        String respTip = RESPONSE_TIP;
        String statusStr = null;
        boolean failed = (statusLine != null && (statusLine.getStatusCode() != 200));
        if (failed) {
            respTip = "get response ";
            statusStr = statusLine.toString();
        }
        String lfcr = "\n\r";
        //19是Long型转字符串的最大长度
        int len = URL_TIP.length() + url.length()
                + TIME_TIP.length() + 19 +
                PARAM_TIP.length() + (paramValue == null ? 0 : paramValue.length()) +
                respTip.length() + (statusStr != null ? (statusStr.length() + OPEN.length() + CLOSE.length()) : 0) + (resp == null ? 0 : resp.length())
                + (lfcr.length() * 4);
        if (printHeader) { //如果打印请求头
            headerValue = JsonUtils.object2Json(header);
            len += (HEADER_TIP.length() + (headerValue == null ? 0 : headerValue.length()) + lfcr.length());
        }

        //明确指定容量，目前发现打印日志的速度，有时候比调用接口的速度慢太多。
        StringBuilder builder = new StringBuilder(len);
        builder.append(URL_TIP)
                .append(url)
                .append(lfcr)
                .append(TIME_TIP)
                .append(System.currentTimeMillis() - startTime)
                .append(lfcr);
        if (printHeader) {
            builder.append(HEADER_TIP)
                    .append(headerValue)
                    .append(lfcr);
        }
        builder.append(PARAM_TIP)
                .append(paramValue)
                .append(lfcr);
        if (failed) {
            builder.append(respTip).append(OPEN).append(statusStr).append(CLOSE);
        } else {
            builder.append(respTip);
        }
        builder.append(resp)
                .append(lfcr);
        logger.debug(builder.toString());
    }


    // 监控有异常的链接
    private class IdleConnectionMonitorThread extends Thread {

        private final HttpClientConnectionManager connMgr;
        private volatile boolean exitFlag = false;

        public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
            this.connMgr = connMgr;
            setDaemon(true);
        }

        @Override
        public void run() {
            while (!this.exitFlag) {
                synchronized (this) {
                    try {
                        this.wait(2000);
                    } catch (Throwable e) {
                        logger.error("", e);
                    }
                    close();
                }
            }
        }

        private void close() {
            try {
                // 关闭失效的连接
                connMgr.closeExpiredConnections();
                // 可选的, 关闭30秒内不活动的连接
                connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
            } catch (Throwable e) {
                logger.error("", e);
            }
        }

        public void shutdown() {
            this.exitFlag = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }



    public static SSLContext defaultSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
            final SSLContext ssl = SSLContext.getInstance("SSL");
            ssl.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new SecureRandom());
            return ssl;
    }


    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }


}
