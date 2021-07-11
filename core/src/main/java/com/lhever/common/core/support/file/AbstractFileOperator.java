package com.lhever.common.core.support.file;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lhever.common.core.annotation.FileField;
import com.lhever.common.core.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections4.MapUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: wulang
 * @Date: 2017年10月14日 15:50
 * @Version: v1.0
 * @Description:
 * @Modified By:
 * @Modifued reason
 */
@Slf4j
public abstract class AbstractFileOperator {

    /**
     * 获取对象所有属性的描述类
     *
     * @param type
     * @return
     * @author: wulang
     * @date: 2017/10/14 11:36
     * @modify by user: {修改人}  2017/10/14 11:36
     * @modify by reason:
     * @since 1.0
     */
    protected <T> List<FieldDescriptorBean> fetchFieldDescriptorList(Class<T> type) {
        try {
            PropertyDescriptor propertyDescriptor = null;
            FieldDescriptorBean fieldDescriptor = null;
          //获取所有字段
            List<Field> fieldList =  Arrays.asList(type.getDeclaredFields());
            T instance = type.newInstance();
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(instance);
            Map<String, PropertyDescriptor> descriptorsMap = Maps.newHashMap();
            Arrays.asList(descriptors).forEach(p -> {
                descriptorsMap.put(p.getName(), p);
            });
            if (MapUtils.isEmpty(descriptorsMap)) {
                return Lists.newArrayList();
            }
            List<FieldDescriptorBean> fieldDescriptorBeanList = Lists.newArrayListWithCapacity(fieldList.size());
            for (Field field : fieldList) {
                try {
                    // propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    propertyDescriptor = descriptorsMap.get(field.getName());
                    fieldDescriptor = new FieldDescriptorBean();
                    fieldDescriptor.setField(field);
                    fieldDescriptor.setType(field.getType());
                    fieldDescriptor.setFileField(field.getAnnotation(FileField.class));
                    fieldDescriptor.setSetMethod(propertyDescriptor.getWriteMethod());
                    fieldDescriptor.setGetMethod(propertyDescriptor.getReadMethod());
                    fieldDescriptorBeanList.add(fieldDescriptor);
                } catch (Throwable e) {
                    LogUtils.error("fetchFieldDescriptorList failed!",e.getMessage());
                }
            }
            return fieldDescriptorBeanList;
        } catch (Throwable e) {
            LogUtils.error("fetchFieldDescriptorList failed!",e.getMessage());
        }
        return Lists.newArrayList();
    }




    /**
     * @Author: wulang
     * @Date: 2017年10月14日 15:27
     * @Version: v1.0
     * @Description:
     * @Modified By:
     * @Modifued reason
     */
    public static class FieldDescriptorBean {
        private Class<?> type;
        private FileField fileField;
        private Field field;
        private Method setMethod;
        private Method getMethod;

        public Class<?> getType() {
            return type;
        }

        public void setType(Class<?> type) {
            this.type = type;
        }

        public FileField getFileField() {
            return fileField;
        }

        public void setFileField(FileField fileField) {
            this.fileField = fileField;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public Method getSetMethod() {
            return setMethod;
        }

        public void setSetMethod(Method setMethod) {
            this.setMethod = setMethod;
        }

        public Method getGetMethod() {
            return getMethod;
        }

        public void setGetMethod(Method getMethod) {
            this.getMethod = getMethod;
        }

    }

}
