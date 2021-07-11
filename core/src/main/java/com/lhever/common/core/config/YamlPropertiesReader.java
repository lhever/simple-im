package com.lhever.common.core.config;

import com.lhever.common.core.utils.YamlUtils;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.Properties;

public class YamlPropertiesReader extends AbstractPropertiesReader {

    public YamlPropertiesReader(String fileName) {
        this(fileName, false);
    }

    public YamlPropertiesReader(String fileName, boolean outside) {
        this.props = readProperties(fileName, outside);
    }

    public YamlPropertiesReader(Properties props) {
        super(props);
    }

    public void refresh(String fileName, boolean outsideFile) {
        this.props = readProperties(fileName, outsideFile);
    }

    public void refresh(String fileName) {
        refresh(fileName, false);
    }


    public static Properties readProperties(String fileName, boolean outsideFile) {

        YamlPropertiesFactoryBean yaml = YamlUtils.getDefaultPropertiesFactory();

        if (outsideFile) {
            //从文件系统引入
            yaml.setResources(new FileSystemResource(fileName));
        } else {
            //从classpath引入
            yaml.setResources(new ClassPathResource(fileName));
        }
        Properties props = yaml.getObject();
        return props;
    }

    public Properties readProperties(String fileName) {
        return readProperties(fileName, false);
    }


}
