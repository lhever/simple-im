package com.lhever.common.core.utils;

import com.lhever.common.core.consts.CommonConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class NetUtils {
    private static Logger log = LoggerFactory.getLogger(NetUtils.class);
    private static final Pattern ipPattern = Pattern.compile("([0-9]{1,3}\\.){3}[0-9]{1,3}");
    private static final String LOCALHOST_STR = "localhost";
    private static final String LOCALHOST_NUM = "127.0.0.1";

    public static String getLocalIP() throws Exception {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr.getHostAddress();
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress.getHostAddress();
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return jdkSuppliedAddress.getHostAddress();
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }


    /**
     * 从X-Forwarded-For头部中获取客户端的真实IP。
     * X-Forwarded-For并不是RFC定义的标准HTTP请求Header，可以参考http://en.wikipedia.org/wiki/X-Forwarded-For
     *
     * @param xff X-Forwarded-For头部的值
     * @return 如果能够解析到client IP，则返回表示该IP的字符串，否则返回null
     */
    private static String resolveClientIPFromXFF(String xff) {
        if (StringUtils.isBlank(xff)) {
            return null;
        }
        if (!xff.contains(CommonConsts.COMMA)) {
            return xff;
        }
        String[] ss = xff.split(CommonConsts.COMMA);
        for (String ip : ss) {
            ip = ip.trim();
            if (isValidIP(ip)) {
                return ip;
            }
        }
        return null;
    }

    /**
     * long ip to string
     *
     * @param ipaddress return java.lang.String
     * @author lihong10 2018/11/24 14:39:00
     */
    public static String longIpToIp(long ipaddress) {
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf((ipaddress >>> 24)));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf((ipaddress & 0x000000FF)));
        return sb.toString();
    }

    //string ip to long
    public static long stringIpToLong(String ipaddress) {
        long[] ip = new long[4];
        int position1 = ipaddress.indexOf(".");
        int position2 = ipaddress.indexOf(".", position1 + 1);
        int position3 = ipaddress.indexOf(".", position2 + 1);
        ip[0] = Long.parseLong(ipaddress.substring(0, position1));
        ip[1] = Long.parseLong(ipaddress.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(ipaddress.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(ipaddress.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 检查是否是一个合格的ipv4 ip
     *
     * @param ip
     * @return
     */
    public static boolean isValidIP(String ip) {
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }
        return ipPattern.matcher(ip).matches();
    }

    /**
     * 从url中分析出端口号, 例如: http://hdfa:8181/login.action <br/>
     *
     * @param url
     * @return
     * @author lihong10 2018/12/18 17:05
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/18 17:05
     * @modify by reason:{原因}
     */
    public static Integer getPortFromURI(String url) {
        try {
            URI uri = new URI(url);
            //URI effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
            return uri.getPort();
        } catch (Exception e) {
            log.error("获取端口号错误，url = " + url, e);
        } catch (Throwable t) {
            log.info("获取端口号错误，url = " + url, t);
        }

        return null;
    }

    /**
     * 从url中分析出IP,支持ip使用主机名表示, 例如: http://hdfa:8181/login.action <br/>
     *
     * @param url
     * @return
     * @author lihong10 2018/12/18 17:04
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/18 17:04
     * @modify by reason:{原因}
     */
    public static String getIpFromURI(String url) {
        try {
            URI uri = new URI(url);
            //URI effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
            return uri.getHost();
        } catch (Exception e) {
            log.error("获取ip错误，url = " + url, e);
        } catch (Throwable t) {
            log.info("获取端口号错误，url = " + url, t);
        }

        return null;
    }


}



