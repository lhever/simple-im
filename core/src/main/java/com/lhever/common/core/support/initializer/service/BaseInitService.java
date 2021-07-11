package com.lhever.common.core.support.initializer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.support.initializer.CryptoKeyProvider;
import com.lhever.common.core.support.initializer.ctx.InitContext;
import com.lhever.common.core.utils.CollectionUtils;
import com.lhever.common.core.utils.FileUtils;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.common.core.utils.YamlUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultAttribute;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 类说明
 * </p>
 *
 * @author lihong10 2020/5/15 11:39
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2020/5/15 11:39
 * @modify by reason:{方法名}:{原因}
 */
public abstract class BaseInitService {


    /**
     * true字符串
     */
    protected static final String TRUE_STRING = "true";
    /**
     * false字符串
     */
    protected static final String FALSE_STRING = "false";

    /**
     * 服务默认端口
     */
    protected static final Integer DEFAULT_PORT = 8888;

    /**
     * 资源目录名
     */
    protected static final String RESOURCES_DIR = "resources";

    protected static final String SALT = "jasypt.encryptor.password";

    protected static final String HOST_PROP = "spring.application.host";


    /**
     * 上下文信息
     */
    protected volatile InitContext ctx = null;
    protected volatile CryptoKeyProvider provider;


    public abstract void init(Class clazz, String[] args, CryptoKeyProvider cryptoKeyProvider);




    public static void println(Object obj) {
        System.out.println(obj);
    }

    public static void println(String prefix, Object obj) {
        if (obj != null && obj instanceof Exception) {
            System.out.print(prefix);
            ((Exception) obj).printStackTrace();
            return;
        }

        System.out.println(prefix + obj);
    }



    protected int getPort() {

        String profiles = System.getProperty("profiles");
        boolean profileNotBlank = StringUtils.isNotBlank(profiles);

        String bootstrapYml = null;
        String applicationYml = null;

        Integer port = null;

        if (profileNotBlank) {
            bootstrapYml = "/bootstrap-" + profiles + ".yml";
            applicationYml = "/application-" + profiles + ".yml";
            //按照优先级查找端口
            port = getPort("/bootstrap.yml", bootstrapYml, "/application.yml", applicationYml);
        } else {
            bootstrapYml = "/bootstrap.yml";
            applicationYml = "/application.yml";
            port = getPort(bootstrapYml, applicationYml);
        }

        println("the final port is: " + port);
        return port;
    }


    /**
     * 按照优先级查找端口号，参数越在左边，优先级越高。端口号找不到，则返回默认端口
     *
     * @param fileNames
     * @return
     * @author lihong10 2018/12/25 11:49
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/25 11:49
     * @modify by reason:{原因}
     */
    protected Integer getPort(String... fileNames) {

        if (fileNames == null || fileNames.length == 0) {
            return DEFAULT_PORT;
        }

        println("the port will be selected from these files: " + Arrays.toString(fileNames));

        Integer port = null;

        for (String fileName : fileNames) {

            println("find port from file: " + fileName);
            Exception error = null;
            try {
                JsonNode jsonNode = YamlUtils.yaml2JsonNode(fileName);
                if ((jsonNode != null)
                        && (jsonNode.get("server") != null)
                        && (jsonNode.get("server").get("port") != null)) {
                    port = jsonNode.get("server").get("port").asInt(-1);
                }
            } catch (Exception e) {
                error = e;
            }

            if ((port != null && port.intValue() != -1) && (error == null)) {
                return port;
            }

            if (error != null) {
                println("error occur when find port, contine to find from next file");
            }
        }

        if (port == null || port.intValue() == -1) {
            port = DEFAULT_PORT;
        }

        return port;
    }


    protected static Map<String, Object> toMap(Properties properties) {
        HashMap<String, Object> map = new HashMap<>();
        if (properties == null) {
            return map;
        }
        Enumeration<?> enu = properties.propertyNames();
        while (enu.hasMoreElements()) {
            Object key = enu.nextElement();
            if (key == null) {
                continue;
            }
            Object value = properties.get(key);

            map.put(key.toString().trim(), value);
        }

        return map;
    }

    protected static String trimFileName(String fileName) {
        if (fileName == null) {
            return fileName;
        }
        fileName = fileName.trim();
        fileName = (fileName.startsWith(CommonConsts.SLASH) || fileName.startsWith(CommonConsts.BACK_SLASH))
                ? fileName.substring(1) : fileName;
        return fileName;
    }


    protected static void remove(Element parent, List<Element> children) {
        if (parent == null || CollectionUtils.isEmpty(children)) {
            return;
        }

        for (Element child : children) {
            if (child == null) {
                continue;
            }
            parent.remove(child);
        }
    }

    protected static List<Element> getByNameValue(List<Element> elements, String elementName, Collection<String> nameValues) {
        List<Element> result = new ArrayList<>(8);

        if (CollectionUtils.isEmpty(elements)) {
            return result;
        }

        for (int i = 0; i < elements.size(); i++) {

            Element element = elements.get(i);

            if (testElement(element, elementName, nameValues)) {
                result.add(element);
            }
        }

        return result;
    }

    protected static boolean testElement(Element element, String elementName, Collection<String> nameValues) {
        if (element == null) {
            return false;
        }

        if (element.getName().equals(elementName)) {
            List<DefaultAttribute> attributes =
                    (List<DefaultAttribute>) element.attributes();

            if (CollectionUtils.isEmpty(attributes)) {
                if (CollectionUtils.isEmpty(nameValues)) {
                    return true;
                } else {
                    return false;
                }
            }

            Set<String> names = attributes
                    .stream()
                    .map(attri -> (attri.getQualifiedName() + "=" + attri.getValue()))
                    .collect(Collectors.toSet());

            if (names.containsAll(nameValues)) {
                return true;
            }
        }

        return false;
    }




    protected static Document getLogDoc(String logPATH) throws DocumentException {
        SAXReader xmlReader = new SAXReader();
        Document document = xmlReader.read(new File(logPATH));
        return document;
    }

    protected static void rewriteLogXml(Document doc, String path) {
        String xml = doc.asXML();
        FileUtils.writeToFileByNio(path, xml);
    }

    protected static void modify(Element element, String name, String... attr) {
        element.setName(name);
        element.setAttributes(new LinkedList());

        for (int i = 0; i < (attr.length / 2); i++) {
            element.addAttribute(attr[2 * i], attr[2 * i + 1]);
        }
    }



    protected boolean isModfifyLogDir() {
        String modifyLogDir = System.getProperty("modifyLogDir");
        //如果不需要加密，直接返回
        if (StringUtils.isNotBlank(modifyLogDir) && modifyLogDir.trim().equalsIgnoreCase(FALSE_STRING)) {
            return false;
        } else {
            return true;
        }
    }




    protected void modifyHost(Properties prop) {
        String forceWriteHost = System.getProperty("forceWriteHost");
        if (!"false".equalsIgnoreCase(forceWriteHost)) { //false以外的任何值，都表示需要修改
            forceModifyHost(prop);
        }
    }

    private void forceModifyHost(Properties prop) {
        String localIP = getLocalIP();
        if (StringUtils.isBlank(localIP) || "127.0.0.1".equals(localIP) || "localhost".equalsIgnoreCase(localIP)) {
            return;
        }
        String old = prop.getProperty(HOST_PROP);
        prop.put(HOST_PROP, localIP);
        if (!localIP.equals(old)) {
            println("old host: " + old + " is replaced to: " + localIP);
        }
    }

    public static final String getLocalIP() {
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
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }






}
