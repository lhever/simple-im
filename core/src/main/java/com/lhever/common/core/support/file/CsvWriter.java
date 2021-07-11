/*
 * @ProjectName: 综合安防
 * @Copyright: 2018 HangZhou Hikvision System Technology Co., Ltd. All Right Reserved.
 * @address: http://www.hikvision.com
 * @date:  2018年01月16日 18:46
 * @description: 本内容仅限于杭州海康威视系统技术公有限司内部使用，禁止转发.
 */
package com.lhever.common.core.support.file;

import com.google.common.collect.Lists;
import com.lhever.common.core.annotation.FileField;
import com.lhever.common.core.utils.CollectionUtils;
import com.lhever.common.core.utils.LogUtils;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wulang
 * @version v1.0
 * @date 2018年01月16日 18:46
 * @description
 * @modified By:
 * @modifued reason:
 */
@Slf4j
public class CsvWriter extends AbstractFileOperator {

    private static Logger logger = LoggerFactory.getLogger(CsvWriter.class);
    public static final Integer DEFAULT_SIZE_ONE_THOUSAND = 1000;
    private static class CsvWriterHolder {
        private static final CsvWriter INSTANCE = new CsvWriter();
    }

    public static CsvWriter getInstance() {
        return CsvWriterHolder.INSTANCE;
    }

    private CsvWriter() {
        super();
    }

    public <T> void writeDataToCsv(OutputStream outputStream , Collection<T> collection, boolean hasTitle, Class<T> type, boolean hasTips,CSVWriter csvWriter) throws IOException {
        if (null == outputStream || null == type) {
            return;
        }
        List<FieldDescriptorBean> fieldDescriptorBeanList = fetchFieldDescriptorList(type);
        try {
            if (hasTips) {
                writeTips(csvWriter, fieldDescriptorBeanList);
            }
            if (hasTitle) {
                writeTitle(csvWriter, type, fieldDescriptorBeanList);
            }
            if (CollectionUtils.isNotEmpty(collection)) {
                List<List<T>> subListCollection = CollectionUtils.splitCollection(collection, DEFAULT_SIZE_ONE_THOUSAND);
                for (List<T> subList : subListCollection) {
                    csvWriter.writeAll(fetchAllRowData(fieldDescriptorBeanList, subList,CSVWriter.DEFAULT_SEPARATOR));
                    csvWriter.flush();
                }
            }
        } catch (IOException e) {
            csvWriter.close();
            throw e;
        }
    }
    /**
     * 获取所有数据
     *
     * @param fieldDescriptorBeanList
     * @param collection
     * @param seperator
     * @return java.util.List<java.lang.String                                                               [                                                               ]>
     * @author wulang
     * @date 2018/5/29 18:35
     * @modify by user: {修改人}  2018/5/29 18:35
     * @modify by reason:
     * @since 1.0
     */
    private <T> List<String[]> fetchAllRowData(List<FieldDescriptorBean> fieldDescriptorBeanList, Collection<T> collection, char seperator) {
        List<String[]> rowDataList = Lists.newArrayListWithExpectedSize(collection.size());
        for (T bean : collection) {
            String[] tempRowData = fetchRowData(fieldDescriptorBeanList, bean,seperator);
            rowDataList.add(tempRowData);
        }
        return rowDataList;
    }
    /**
     * 获取单行数据
     *
     * @param fieldDescriptorBeanList
     * @param bean
     * @param seperator
     * @return java.lang.String[]
     * @author wulang
     * @date 2018/5/29 18:37
     * @modify by user: {修改人}  2018/5/29 18:37
     * @modify by reason:
     * @since 1.0
     */
    private <T> String[] fetchRowData(List<FieldDescriptorBean> fieldDescriptorBeanList, T bean, char seperator) {
        String[] rowDataArr = new String[fieldDescriptorBeanList.size()];
        int index = 0;
        for (int i = 0; i < fieldDescriptorBeanList.size(); i++) {
            FieldDescriptorBean fieldDescriptorBean = fieldDescriptorBeanList.get(i);
            if(fieldDescriptorBean.getFileField()==null){
                continue;
            }
            String cellValue = fetchCellValue(fieldDescriptorBean, bean,seperator);
            rowDataArr[index] = cellValue;
            index++;
        }
        return rowDataArr;
    }
    /**
     * 获取某一个具体的属性值
     *
     * @param fieldDescriptorBean
     * @param bean
     * @param seperator
     * @author wulang
     * @date 2018/1/16 19:33
     * @modify by user: {修改人}  2018/1/16 19:33
     * @modify by reason:
     */
    private <T> String fetchCellValue(FieldDescriptorBean fieldDescriptorBean, T bean, char seperator) {
        Method getMethod = fieldDescriptorBean.getGetMethod();
        FileField fileField = fieldDescriptorBean.getFileField();
        Object sourceValue = null;
        Object applyValue = null;
        Object cellValue = null;
        try {
            sourceValue = getMethod.invoke(bean);
            if (null != sourceValue && null != fileField) {
                applyValue = sourceValue;
            }
            if (null != applyValue && !applyValue.toString().isEmpty() ) {
               // cellValue = transformValue(applyValue,seperator);
            } else {
                cellValue = applyValue;
            }
            cellValue = applyValue;
        } catch (Throwable e) {
            logger.error( "fetchCellValue","fetchCellValue failed",e);
            cellValue = applyValue;
            if (null == cellValue) {
                cellValue = sourceValue;
            }
            if (null == cellValue && null != fileField && fileField.defaultValue().length() > 0) {
                cellValue = fileField.defaultValue();
            }
        }
        return Optional.ofNullable(cellValue).map(Object::toString).orElse("");
    }

    /**
     * 写入提示信息
     *
     * @param csvWriter
     * @param fieldDescriptorBeanList
     * @param <T>
     * @throws IOException
     */
    private <T> void writeTips(CSVWriter csvWriter, List<FieldDescriptorBean> fieldDescriptorBeanList) throws IOException {
        String tip = null;
        String colName = null;
        FileField fileField = null;
        Object applyValue;
        String[] newLine = new String[1];
        for (int i = 0; i < fieldDescriptorBeanList.size(); i++) {
            FieldDescriptorBean fieldDescriptorBean = fieldDescriptorBeanList.get(i);
            fileField = fieldDescriptorBean.getFileField();
            if (Objects.nonNull(fileField)) {
                tip = fileField.tips();
                colName = fileField.name();
                try {
                   /* if (fileField.ignore()) {
                        tip = (fileField.index() + 1) + ". " + tip;
                    } else
                   if (StringUtils.isNotEmpty(tip) && StringUtils.isNotEmpty(colName)) {
                        tip = (fileField.index() + 1) + ". " + (fileField.required() ? "" : "") + colName + ": " + tip;
                    }*/
                    if (StringUtils.isNotEmpty(tip) && StringUtils.isNotEmpty(colName)) {
                        tip =  colName + ": " + tip;
                    } else if(StringUtils.isEmpty(tip)){
                        continue;
                    }
                } catch (Exception e) {
                    LogUtils.error("download  Template failed!");
                }
                newLine[0] = tip;
                csvWriter.writeNext(newLine);
            }
        }
        csvWriter.flush();
    }
    /**
     * 写出头信息
     *
     * @param csvWriter
     * @param fieldDescriptorBeanList
     * @return
     * @author wulang
     * @date 2018/1/16 19:29
     * @modify by user: {修改人}  2018/1/16 19:29
     * @modify by reason:
     * @since 1.0
     */
    private void writeTitle(CSVWriter csvWriter, Class<?> type, List<FieldDescriptorBean> fieldDescriptorBeanList) throws IOException {
        String colName = null;
        String colValue = null;
        FileField fileField = null;
        List<FieldDescriptorBean> headers = fieldDescriptorBeanList.stream().filter(p -> !p.getFileField().ignore()).collect(Collectors.toList());
        String[] rowDataArr = new String[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            FieldDescriptorBean fieldDescriptorBean = headers.get(i);
            fileField = fieldDescriptorBean.getFileField();
            //取列名-->FileField标签无或者name为null时，取字段名为列名
            colName = null != fileField ? fileField.name() : null;
            colName = StringUtils.isBlank(colName) ? fieldDescriptorBean.getField().getName() : colName;
            rowDataArr[i] = StringUtils.isNotBlank(colValue) ? colValue : colName;
        }
        csvWriter.writeNext(rowDataArr);
        csvWriter.flush();
    }

}
