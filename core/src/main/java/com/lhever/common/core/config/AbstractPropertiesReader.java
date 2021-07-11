package com.lhever.common.core.config;

import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.support.parser.token.PropertyPlaceholderHelper;
import com.lhever.common.core.utils.IOUtils;
import com.lhever.common.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class AbstractPropertiesReader {

    protected final static Logger LOG = LoggerFactory.getLogger(AbstractPropertiesReader.class);
    private static final String LINE_RETURN = System.getProperty("line.separator");
    protected volatile Properties props;

    public AbstractPropertiesReader() {
    }

    public AbstractPropertiesReader(Properties properties) {
        this.props = properties;
    }

    public Properties getProps() {
        return props;
    }

    public boolean containsKey(String key) {
        return props.containsKey(key);
    }


    public String getProperty(String key) {
        return getProperty(key, true);
    }

    public String getProperty(String key, boolean hasPlaceHolder) {

        Properties copy = getProps();
        if (copy == null) {
            return null;
        }
        String property = copy.getProperty(key);
        if (property == null) {
            return null;
        }
        if (!hasPlaceHolder) {
            return property;
        }
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");
        return helper.replacePlaceholders(property, copy);
    }

    public void put(String key, String value) {
        props.put(key, value);
    }

    /**
     * 从yml中获取值，值为空（注意不是key不存在）则返回默认值
     *
     * @param key
     * @param defaultValue
     * @return
     * @author lihong10 2019/1/21 17:08
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2019/1/21 17:08
     * @modify by reason:{原因}
     */
    public String getProperty(String key, String defaultValue) {

        String value = getProperty(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }

    public Object setProperty(String key, String value) {
        Properties copy = getProps();
        if (copy == null) {
            return null;
        }
        return copy.setProperty(key, value);
    }


    public String getStringProperty(String propertyName) {
        return getProperty(propertyName);
    }

    public String getStringProperty(String propertyName, String dft) {
        return getProperty(propertyName, dft);
    }


    public Integer getIntProperty(String propertyName) {
        return getIntProperty(propertyName, null);
    }


    public Integer getIntProperty(String propertyName, Integer dft) {
        String raw = getProperty(propertyName);
        return getInt(raw, dft);
    }


    public Long getLongProperty(String propertyName) {
        return getLongProperty(propertyName, null);
    }


    public Long getLongProperty(String propertyName, Long dft) {
        String raw = getProperty(propertyName);
        return getLong(raw, dft);
    }


    public Boolean getBooleanProperty(String propertyName) {
        return getBooleanProperty(propertyName, null);
    }

    public Boolean getBooleanProperty(String propertyName, Boolean dft) {
        String raw = getProperty(propertyName);
        return getBoolean(raw, dft);
    }


    private Integer getInt(String str, Integer dft) {
        try {
            return Integer.parseInt(str.trim());
        } catch (Exception e) {
            LOG.error("error when parsing " + str + " to int, use default value: " + dft, e);
            return dft;
        }
    }

    private Long getLong(String str, Long dft) {
        try {
            return Long.parseLong(str.trim());
        } catch (Exception e) {
            LOG.error("error when parsing " + str + " to long, use default value: " + dft, e);
            return dft;
        }
    }

    private Boolean getBoolean(String str, Boolean dft) {
        try {
            return Boolean.parseBoolean(str.trim());
        } catch (Exception e) {
            LOG.error("error when parsing " + str + " to bool, use default value: " + dft, e);
            return dft;
        }
    }


    public boolean write(Properties props, String path) {
        return write(props, path, false);
    }

    public boolean write(String path) {
        return write(props, path, false);
    }

    public boolean write(String path, boolean append) {
        return write(props, path, append);
    }

    public boolean write(Properties props, String path, boolean append) {
        boolean success = true;
        FileOutputStream os = null;
        try {
            //true表示追加打开
            os = new FileOutputStream(path, append);
            props.store(os, "update email password");
        } catch (Exception e) {
            LOG.error("store properties failed", e);
            success = false;
        } finally {
            IOUtils.closeQuietly(os);
        }

        return success;
    }

    public boolean writeNoEscape(String path) {
        return writeNoEscape(props, path, true);
    }


    public boolean writeNoEscape(Properties props, String path) {
        return writeNoEscape(props, path, true);
    }


    public boolean writeNoEscape(Properties props, String path, boolean sort) {
        boolean success = true;
        if (props.size() == 0) {
            return !success;
        }
        FileOutputStream os = null;
        OutputStreamWriter writer = null;
        BufferedWriter bw = null;
        try {
            os = new FileOutputStream(path);
            writer = new OutputStreamWriter(os, CommonConsts.CHARSET_UTF8);
            bw = new BufferedWriter(writer);
            bw.newLine();

            List<String> propList = new ArrayList<String>();
            for (Enumeration<?> e = props.keys(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                if (props.containsKey(key)) {
                    Object val = props.get(key);
                    String stringVal = (val == null ? CommonConsts.EMPTY : val.toString());
                    if (StringUtils.isBlank(stringVal)) {
                        propList.add(key + "=" + CommonConsts.EMPTY + LINE_RETURN);
                    } else {
                        propList.add(key + "=" + stringVal + LINE_RETURN);
                    }
                }
            }
            if (sort) {
                //排序，不然写入文件乱序
                Collections.sort(propList);
            }
            //排序后增加空行，目的是看起来像分组了一样
            PropertyKeyParser firstParser = new PropertyKeyParser(propList.get(0));
            String lastFirstSecond = firstParser.getFirstAndSecond();
            String lastFirst = firstParser.getFirst();
            int lastNum = firstParser.getNum();
            for (int i = 0; i < propList.size(); i++) {
                String keyValue = propList.get(i);
                if (keyValue.length() == 0) {
                    continue;
                }

                PropertyKeyParser current = new PropertyKeyParser(keyValue);
                String firstSecond = current.getFirstAndSecond();
                String first = current.getFirst();
                int num = current.getNum();
                if (!lastFirstSecond.equals(firstSecond)) {//不相同，则增加空行
                    if ((num == 2 && lastNum == 2) && lastFirst.equals(first)) {
                        //排除段数恰好是2， 但第一段相同的特殊情况
                    } else {
                        //属性值首字母不同，则增加换行
                        propList.add(i, LINE_RETURN);
                        i++;
                    }
                }
                lastFirstSecond = firstSecond;
                lastFirst = first;
                lastNum = num;

            }

            //补充注释： ##### -> modify at $date
            propList.add(0, "###### -> modify at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + LINE_RETURN);
            bw.write(StringUtils.join(propList, CommonConsts.EMPTY));
            bw.flush();
        } catch (Exception e) {
            LOG.error("store properties failed", e);
            success = false;
        } finally {
            IOUtils.closeQuietly(os, writer, bw);
        }
        return success;
    }


    private static class PropertyKeyParser {
        private String rawKeyValue;
        private String key;
        private int num;
        private String[] segment;

        public PropertyKeyParser(String rawKeyValue) {
            this.rawKeyValue = rawKeyValue;
            if (StringUtils.isNotBlank(rawKeyValue)) {
                parse(rawKeyValue);
            } else {
                this.num = 0;
            }
        }

        private void parse(String rawKeyValue) {
            int i = rawKeyValue.indexOf("=");
            if (i < 0) {
                this.num = 0;
                return;
            }

            this.key = rawKeyValue.substring(0, i);
            this.segment = key.split("\\.");
            this.num = this.segment.length;
        }


        public String getRawKeyValue() {
            return rawKeyValue;
        }

        public int getNum() {
            return num;
        }

        public String getFirst() {
            if (num == 0) {
                return CommonConsts.EMPTY;
            }
            return segment[0];
        }

        public String getFirstAndSecond() {
            if (num == 0) {
                return CommonConsts.EMPTY;
            }
            if (num >= 2) {
                return segment[0] + CommonConsts.DOT + segment[1];
            }
            return segment[0];
        }


        public String[] getSegment() {
            return segment;
        }

    }


}
