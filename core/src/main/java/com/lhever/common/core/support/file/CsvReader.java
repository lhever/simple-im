/*
 * @ProjectName: 综合安防
 * @Copyright: 2018 HangZhou Hikvision System Technology Co., Ltd. All Right Reserved.
 * @address: http://www.hikvision.com
 * @date:  2018年01月16日 18:45
 * @description: 本内容仅限于杭州海康威视系统技术公有限司内部使用，禁止转发.
 */
package com.lhever.common.core.support.file;

import com.google.common.collect.Lists;
import com.lhever.common.core.utils.LogUtils;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author wulang
 * @version v1.0
 * @date 2018年01月16日 18:45
 * @description
 * @modified By:
 * @modifued reason:
 */
public class CsvReader extends AbstractFileOperator {

    private static final Logger logger = LoggerFactory.getLogger(CsvReader.class);

    private static class CsvReaderHolder {
        private static final CsvReader INSTANCE = new CsvReader();
    }

    public static CsvReader getInstance() {
        return CsvReaderHolder.INSTANCE;
    }

    private CsvReader() {
        super();
    }

    /**
     * 输入流中读取数据
     *
     * @param fileInputStream
     *
     * @param type
     * @return
     * @author wulang
     * @date 2018/1/16 20:33
     * @modify by user: {修改人}  2018/1/16 20:33
     * @modify by reason:
     * @since 1.0
     */
    public <T> List<T> readDataFromCsv(FileInputStream fileInputStream,  Class<T> type, Integer skipLines) throws IOException {
        List<T> beanList = Lists.newArrayList();
        if (null != fileInputStream && null != type) {

            CSVReader csvReader = new CSVReaderBuilder(new BufferedReader(new InputStreamReader(fileInputStream, "GBK")))
                    .withCSVParser(
                            new CSVParserBuilder()
                                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                                    .build()
                    )
                    .withSkipLines(skipLines).build();
            try {
                List<FieldDescriptorBean> fieldDescriptorBeanList = fetchFieldDescriptorList(type);
                String[] rowDataArr = null;
                while (null != (rowDataArr = csvReader.readNext())) {
                    T bean = wrapBean(rowDataArr, fieldDescriptorBeanList, type);
                    if (null != bean) {
                        beanList.add(bean);
                    }
                }
            } catch (IOException e) {
                csvReader.close();
                throw e;
            }
        }
        return beanList;
    }
    /**
     * 包装bean
     *
     * @param rowDataArr
     * @param fieldDescriptorBeanList
     * @param type
     * @return
     * @author wulang
     * @date 2018/1/16 20:33
     * @modify by user: {修改人}  2018/1/16 20:33
     * @modify by reason:
     * @since 1.0
     */

    private <T> T wrapBean(String[] rowDataArr, List<FieldDescriptorBean> fieldDescriptorBeanList, Class<T> type) {
        T target = null;
        Method setMethod = null;
        try {
            target = type.newInstance();
            if (null != rowDataArr) {
                for (int i = 0; i < rowDataArr.length && i < fieldDescriptorBeanList.size(); i++) {
                    Object value = rowDataArr[i];
                    FieldDescriptorBean fieldDescriptorBean = fieldDescriptorBeanList.get(i);
                    if (fieldDescriptorBean.getFileField().ignore()) {
                        continue;
                    }
                    setMethod = fieldDescriptorBean.getSetMethod();
                    setMethod.invoke(target, value);
                }
            }
        } catch (RuntimeException e) {
            LogUtils.error(	"wrapBean failed",e.getMessage());
        } catch (Exception e) {
            LogUtils.error(	"wrapBean failed",e.getMessage());
        }
        return target;
    }

}
