package com.lhever.common.core.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Timestamp;
import java.util.*;

public class YamlUtils {

    private static YAMLFactory yamlFactory;
    private static ObjectMapper mapper;
    private final static Logger log = LoggerFactory.getLogger(YamlUtils.class);

    static {
        yamlFactory = new YAMLFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper = objectMapper;
    }

    /**
     * yml文件转JsonNode
     *
     * @param fileName
     * @param outside  true:从文件系统读取， false:从classpath读取
     * @return
     * @author lihong10 2019/1/28 15:47
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 15:47
     * @modify by reason:{原因}
     */
    public static JsonNode yaml2JsonNode(String fileName, boolean outside) {
        JsonNode node = null;

        InputStream inputStream = null;
        try {
            Resource res = getResource(fileName, outside);
            inputStream = res.getInputStream();
            YAMLParser yamlParser = yamlFactory.createParser(inputStream);
            node = mapper.readTree(yamlParser);
        } catch (Exception e) {
            log.error("get jsonNode error", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return node;
    }

    /**
     * yml文件转JsonNode, 文件从classpath读取
     *
     * @param fileName
     * @return
     * @author lihong10 2019/1/28 15:47
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 15:47
     * @modify by reason:{原因}
     */
    public static JsonNode yaml2JsonNode(String fileName) {
        return yaml2JsonNode(fileName, false);
    }


    /**
     * yml文件转clss类型的对象
     *
     * @param fileName
     * @param outside  true:从文件系统读取， false:从classpath读取
     * @return
     * @author lihong10 2019/1/28 15:47
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 15:47
     * @modify by reason:{原因}
     */
    public static <T> T yaml2Class(String fileName, boolean outside, Class<? extends T> clss) {

        T obj = null;
        try {
            JsonNode node = yaml2JsonNode(fileName, outside);
            TreeTraversingParser treeTraversingParser = new TreeTraversingParser(node);
            obj = mapper.readValue(treeTraversingParser, clss);
        } catch (Exception e) {
            log.error("yaml convert to class error", e);
        }

        return obj;
    }

    /**
     * yml文件转clss类型的对象, 文件从classpath读取
     *
     * @param fileName
     * @return
     * @author lihong10 2019/1/28 15:47
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 15:47
     * @modify by reason:{原因}
     */
    public static <T> T yaml2Class(String fileName, Class<? extends T> clss) {
        return yaml2Class(fileName, false, clss);
    }


    /**
     * 读取Resource
     *
     * @param fileName
     * @param outside  true：从文件系统读取， false:从classpath读取
     * @return
     * @author lihong10 2019/1/28 16:00
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 16:00
     * @modify by reason:{原因}
     */
    private static Resource getResource(String fileName, boolean outside) {
        Resource resource = outside ? (new FileSystemResource(fileName)) : (new ClassPathResource(fileName));
        return resource;
    }

    /**
     * yml转换为map
     *
     * @param fileName
     * @param outside
     * @return
     * @author lihong10 2019/1/28 15:49
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 15:49
     * @modify by reason:{原因}
     */
    public static Map<String, Object> yaml2Map(String fileName, boolean outside) {
        try {
            YamlMapFactoryBean yaml = new YamlMapFactoryBean();
            yaml.setResources(getResource(fileName, outside));
            return yaml.getObject();
        } catch (Exception e) {
            log.error("Cannot read yaml", e);
            return null;
        }
    }

    /**
     * yml转换为map
     *
     * @param fileName
     * @return
     * @author lihong10 2019/1/28 15:49
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 15:49
     * @modify by reason:{原因}
     */
    public static Map<String, Object> yaml2Map(String fileName) {
        try {
            YamlMapFactoryBean yaml = new YamlMapFactoryBean();
            yaml.setResources(getResource(fileName, false));
            return yaml.getObject();
        } catch (Exception e) {
            log.error("Cannot read yaml", e);
            return null;
        }
    }

    /**
     * 将时间戳对象转换为字符串格式
     *
     * @param date 时间戳
     * @return
     * @author lihong10 2018/12/21 16:23
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/21 16:23
     * @modify by reason:{原因}
     */
    private static String asDateString(Timestamp date) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.toISO8601DateString(date);
    }

    /**
     * 将Date对象转换为字符串格式
     *
     * @param date
     * @return
     * @author lihong10 2018/12/21 16:23
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/21 16:23
     * @modify by reason:{原因}
     */
    private static String asDateString(Date date) {
        if (date == null) {
            return null;
        }
        return DateFormatUtils.toISO8601DateString(date);
    }

    private static String convert(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Timestamp) {
            return asDateString((Timestamp) value);
        }

        if (value instanceof Date) {
            return asDateString((Date) value);
        }
        return value.toString();
    }

    /**
     * 创建并定制YamlPropertiesFactoryBean，使用YamlPropertiesFactoryBean读取yaml文件得到的Properties对象，
     * 如果调用get(key)方法得到的值是Date类型，则调用getProperty(key)得到的日期字符串不符合iso-8601日期格式，
     * 所以需要定制YamlPropertiesFactoryBean
     *
     * @param
     * @return
     * @author lihong10 2019/2/20 16:14
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/2/20 16:14
     * @modify by reason:{原因}
     */
    public static YamlPropertiesFactoryBean getDefaultPropertiesFactory() {
        Properties prop = new Properties() {
            @Override
            public String getProperty(String key) {
                Object value = get(key);
                return convert(value);
            }
        };

        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean() {
            @Override
            protected Properties createProperties() {
                process((properties, map) -> prop.putAll(properties));
                return prop;
            }
        };

        return factory;
    }

    public static Properties yaml2Properties(String fileName, boolean outside, YamlPropertiesFactoryBean factory) {
        try {
            factory.setResources(getResource(fileName, outside));
            return factory.getObject();
        } catch (Exception e) {
            log.error("Cannot read yaml", e);
            return null;
        }
    }

    public static Properties yaml2Properties(String fileName, boolean outside) {
        YamlPropertiesFactoryBean factory = getDefaultPropertiesFactory();
        return yaml2Properties(fileName, outside, factory);
    }

    public static Properties yaml2Properties(String fileName) {
        YamlPropertiesFactoryBean factory = getDefaultPropertiesFactory();
        return yaml2Properties(fileName, false, factory);
    }

    /**
     * yml转换为摊平的map
     *
     * @param fileName
     * @param outsideFile
     * @return
     * @author lihong10 2019/1/28 15:50
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 15:50
     * @modify by reason:{原因}
     */
    public static Map<String, Object> yaml2FlattenedMap(String fileName, boolean outsideFile) {
        return yaml2FlattenedMap(fileName, outsideFile, false);
    }

    /**
     * yml转换为摊平的map
     *
     * @param fileName
     * @return
     * @author lihong10 2019/1/28 15:50
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 15:50
     * @modify by reason:{原因}
     */
    public static Map<String, Object> yaml2FlattenedMap(String fileName) {
        return yaml2FlattenedMap(fileName, false, false);
    }

    /**
     * yml转换为摊平的map
     *
     * @param fileName
     * @param outsideFile
     * @param firstFound  true:只转换文件中的第一个文档，false:转换所有文档
     * @return
     * @author lihong10 2019/1/28 15:50
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 15:50
     * @modify by reason:{原因}
     */
    public static Map<String, Object> yaml2FlattenedMap(String fileName, boolean outsideFile, boolean firstFound) {
        Map<String, Object> result = new HashMap<>();
        Yaml yaml = createYaml();
        Reader reader = null;
        try {
            Resource resource = getResource(fileName, outsideFile);
            reader = new UnicodeReader(resource.getInputStream());
            int count = 0;
            Iterable<Object> objects = yaml.loadAll(reader);
            for (Object object : objects) {
                if (object == null) {
                    continue;
                }
                count++;
                Map<String, Object> flattenedMap = getFlattenedMap(asMap(object));
                result.putAll(flattenedMap);

                if (firstFound) {
                    break;
                }

            }
            if (log.isDebugEnabled()) {
                log.debug("Loaded " + count + " document" + (count > 1 ? "s" : "") + " from YAML resource: " + resource);
            }
        } catch (Exception e) {
            String msg = "error when read file:" + fileName;
            throw new RuntimeException(msg, e);
        } finally {
            IOUtils.closeQuietly(reader);
        }

        return result;
    }

    /**
     * 创建yaml对象
     *
     * @param
     * @return Yaml
     * @author lihong10 2019/2/20 16:13
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/2/20 16:13
     * @modify by reason:{原因}
     */
    public static Yaml createYaml() {
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        return new Yaml(options);
    }

    public static final Map<String, Object> getFlattenedMap(Map<String, Object> source) {
        Map<String, Object> result = new LinkedHashMap<>();
        buildFlattenedMap(result, source, null);
        return result;
    }

    /**
     * 对象转换为map
     *
     * @param object
     * @return
     * @author lihong10 2019/1/28 15:52
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 15:52
     * @modify by reason:{原因}
     */
    private static Map<String, Object> asMap(Object object) {
        // YAML can have numbers as keys
        Map<String, Object> result = new LinkedHashMap<>();
        if (!(object instanceof Map)) {
            // A document can be a text literal
            result.put("document", object);
            return result;
        }

        Map<Object, Object> map = (Map<Object, Object>) object;
        map.forEach((key, value) -> {
            if (value instanceof Map) {
                value = asMap(value);
            }
            if (key instanceof CharSequence) {
                result.put(key.toString(), value);
            } else {
                // It has to be a map key in this case
                result.put("[" + key.toString() + "]", value);
            }
        });
        return result;
    }

    /**
     * 将map摊平的递归方法
     *
     * @param result
     * @param source
     * @param path
     * @return
     * @author lihong10 2019/1/28 15:52
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/28 15:52
     * @modify by reason:{原因}
     */
    private static void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, @Nullable String path) {
        source.forEach((key, value) -> {
            if (StringUtils.isNotBlank(path)) {
                if (key.startsWith("[")) {
                    key = path + key;
                } else {
                    key = path + '.' + key;
                }
            }
            if (value instanceof String) {
                result.put(key, value);
            } else if (value instanceof Map) {
                // Need a compound key
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) value;
                buildFlattenedMap(result, map, key);
            } else if (value instanceof Collection) {
                // Need a compound key
                @SuppressWarnings("unchecked")
                Collection<Object> collection = (Collection<Object>) value;
                if (CollectionUtils.isEmpty(collection)) {
                    result.put(key, "");
                } else {
                    int count = 0;
                    for (Object object : collection) {
                        buildFlattenedMap(result, Collections.singletonMap(
                                "[" + (count++) + "]", object), key);
                    }
                }
            } else {
                result.put(key, (value != null ? value : ""));
            }
        });
    }


}


















