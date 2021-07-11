package com.lhever.common.core.utils;

import com.lhever.common.core.annotation.FieldExportDesc;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportExcelUtils {
    /**
     * 查询导出最大时长
     */
    protected static long expLastTime = 1000 * 60 * 15;
    /**
     * 导出数据
     */
    protected static String sheetNamePrefix = "导出数据";
    /**
     * 导出异常说明
     */
    protected static String expPrefixDesc = "导出异常 ：";
    /**
     * 导出超时说明
     */
    protected static String outTimeDesc = "导出时长超过15分钟，请分批导出";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcelUtils.class);

    @SuppressWarnings("unchecked")
    public static void init(Class<?> clazz, Map<Integer, FieldExportDesc> fieldDescMap, Map<Integer, Field> fieldMap) {
        Field[] fields = clazz.getDeclaredFields();
        FieldExportDesc fieldDesc;
        for (Field field : fields) {
            fieldDesc = field.getAnnotation(FieldExportDesc.class);
            if (fieldDesc == null) {
                continue;
            }
            field.setAccessible(true);
            fieldDescMap.put(fieldDesc.order(), fieldDesc);
            fieldMap.put(fieldDesc.order(), field);
        }
    }

    /**
     * 导出excel模板
     *
     * @param clazz
     * @return
     * @author guoliang5
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人}
     * @modify by reason:{原因}
     */
    public static Workbook writeToExcel(List<?> data, Class<?> clazz) {
        //初始化参数
        Map<Integer, FieldExportDesc> fieldDescMap = new HashMap<Integer, FieldExportDesc>();
        Map<Integer, Field> fieldMap = new HashMap<Integer, Field>();
        init(clazz, fieldDescMap, fieldMap);
        Workbook wb = new SXSSFWorkbook();
        long startTime = System.currentTimeMillis();
        // 写excel头部信息
        Row row;// = sheet.createRow(index);
        Cell cell;
        int startLine = 1;
        Sheet sheet = createSheetAndHeader(wb, startLine, fieldDescMap);
        // row游标
        int index = 1;
        int dataSize = data.size();
        for (int i = 0; i < dataSize; i++) {
            row = sheet.createRow(index);
            for (Map.Entry<Integer, Field> record : fieldMap.entrySet()) {
                cell = row.createCell(record.getKey());
                try {
                    setCellValue(cell, record.getValue().get(data.get(i)));
                } catch (Exception e) {
                    LOGGER.error("expPrefixDesc + e.getMessage()", e);
                    cell.setCellValue(expPrefixDesc + e.getMessage());
                    return wb;
                }
            }
            index++;
            startLine++;
        }
        if (System.currentTimeMillis() - startTime > expLastTime) {
            row = sheet.createRow(index);
            cell = row.createCell(1);
            cell.setCellValue(outTimeDesc);
            return wb;
        }
        return wb;
    }

    /**
     * 创建Sheet,并填写头部信息
     *
     * @param wb
     * @return
     * @author guoliang5
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人}
     * @modify by reason:{原因}
     */
    private static Sheet createSheetAndHeader(Workbook wb, int startLine, Map<Integer, FieldExportDesc> fieldDescMap) {
        Sheet sheet = wb.createSheet(sheetNamePrefix + "-" + startLine);
        Cell cell;
        // row邮标
        int index = 0;
        // 写excel头部信息
        Row row = sheet.createRow(index);
        for (Map.Entry<Integer, FieldExportDesc> header : fieldDescMap
                .entrySet()) {
            sheet.setColumnWidth(header.getKey(), header.getValue().width());
            cell = row.createCell(header.getKey());
            cell.setCellValue(header.getValue().display());
        }
        return sheet;
    }

    /**
     * 给单元格设置值
     *
     * @param cell
     * @param value
     * @author guoliang5 2018-11-23 上午15:05:56
     */
    private static void setCellValue(Cell cell, Object value) {
        if (cell == null) {
            return;
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
            return;
        }
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
            return;
        }
        if (value instanceof Double) {
            cell.setCellValue((Double) value);
            return;
        }
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
            return;
        }
        if (value instanceof Timestamp) {
            cell.setCellValue(asDateString((Timestamp) value));
            return;
        }
        if (value instanceof Date) {
            cell.setCellValue(asDateString((Date) value));
            return;
        }
    }

    /**
     * 通过excel模板导出
     *
     * @param importFilePath
     * @param exportFilePath
     * @param row
     * @param data
     * @author guoliang5
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人}
     * @modify by reason:{原因}
     */
    public static void wirteToExcelByTemplet(String importFilePath, String exportFilePath, int row, Map<String, Object> data) {
        //excel模板路径
        ClassPathResource cpr = new ClassPathResource(importFilePath);
        InputStream in = null;
        FileOutputStream out = null;
        XSSFWorkbook wb = null;
        try {
            in = cpr.getInputStream();
            //读取excel模板
            wb = new XSSFWorkbook(in);
            //读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            //如果这行没有了，整个公式都不会有自动计算的效果的
            sheet.setForceFormulaRecalculation(true);
            //在相应的单元格进行赋值
            for (int i = 0; i < row; i++) {
                XSSFRow r = sheet.getRow(i);
                if (r == null) {
                    break;
                }
                int n = 0;
                for (; ; ) {
                    XSSFCell cell = r.getCell(n);
                    if (cell == null) {
                        break;
                    }
                    String oldValue = cell.getStringCellValue();
                    if (StringUtils.isBlank(oldValue)) {
                        n++;
                        continue;
                    }
                    if (data.get(oldValue) == null) {
                        n++;
                        continue;
                    }
                    cell.setCellValue("" + data.get(oldValue));
                    n++;
                }
            }
            //修改模板内容导出新模板
            out = new FileOutputStream(exportFilePath);
            wb.write(out);
        } catch (IOException e) {
            LOGGER.error("导出新模板失败", e);
        } finally {
            //关闭输入输出流
            IOUtils.closeQuietly(in, out, wb);
        }
    }

    private static final String EXP_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 导出时间格式转换
     *
     * @param date
     * @author guoliang5 2018-11-23 上午15:05:56
     */
    public static String asDateString(Timestamp date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(EXP_DATE_FORMAT);
        return dateFormat.format(date);
    }

    /**
     * 导出时间格式转换
     *
     * @param date
     * @author guoliang5 2018-11-23 上午15:05:56
     */
    public static String asDateString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(EXP_DATE_FORMAT);
        return dateFormat.format(date);
    }
}
