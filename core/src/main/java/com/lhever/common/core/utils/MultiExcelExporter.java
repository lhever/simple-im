package com.lhever.common.core.utils;

import com.lhever.common.core.annotation.FieldExportDesc;
import com.lhever.common.core.consts.CommonConsts;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>该类是用于excel导出的专用类，该类支持导出的数据分布在同一个excel的多个sheet中的情况。
 *
 * @author lihong10 2018/12/21 16:09
 * @return
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2018/12/21 16:09
 * @modify by reason:{原因}
 */
public class MultiExcelExporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiExcelExporter.class);
    /**
     * 导出异常说明
     */
    protected static String expPrefixDesc = "";
    private XSSFWorkbook wb = null;
    private String workbookName = null;
    private Map<String, XSSFSheet> sheets = new HashMap<String, XSSFSheet>();
    private Map<String, SheetWriter> metas = new HashMap<String, SheetWriter>();


    public MultiExcelExporter() {
    }

    public MultiExcelExporter(String workbookName) {
        forWorkbook(workbookName);
    }


    /**
     * 返回该类的实例内部维护的唯一excel对象
     *
     * @param workbookName
     * @return
     * @author lihong10 2018/12/21 16:23
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/21 16:23
     * @modify by reason:{原因}
     */
    public XSSFWorkbook forWorkbook(String workbookName) {

        workbookName = StringUtils.isBlank(workbookName) ? "workbook.xlsx" : workbookName.trim();
        if (workbookName.startsWith(CommonConsts.SLASH) || workbookName.startsWith(CommonConsts.BACK_SLASH)) {
            this.workbookName = workbookName.substring(1);
        } else {
            this.workbookName = workbookName;
        }
        if (wb != null) {
            return wb;
        }
        XSSFWorkbook wb = new XSSFWorkbook();
        this.wb = wb;
        return wb;
    }


    /**
     * 返回该类的实例内部维护的唯一工作sheet
     *
     * @param sheetName 工作sheet的名称
     * @return
     * @author lihong10 2018/12/21 16:23
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/21 16:23
     * @modify by reason:{原因}
     */
    public <T> SheetWriter createSheet(String sheetName, Class<T> cls) {
        if (StringUtils.isBlank(sheetName)) {
            throw new IllegalArgumentException("sheet name empty");
        }
        if (sheets.get(sheetName) != null) {
            return metas.get(sheetName);
        } else {
            XSSFSheet sheet = wb.createSheet(sheetName);
            SheetWriter<T> meta = new SheetWriter<T>();
            meta.init(sheet, cls);
            sheets.put(sheetName, sheet);
            metas.put(sheetName, meta);
            return meta;
        }
    }

    /**
     * 将excel文件返回给浏览器
     *
     * @param os 工作sheet的名称
     * @return
     * @author lihong10 2018/12/21 16:23
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/21 16:23
     * @modify by reason:{原因}
     */
    public void write(OutputStream os) {
        if (wb == null) {
            return;
        }
        try {
            wb.write(os);
        } catch (Exception e) {
            LOGGER.error("export error", e);
        } finally {
            IOUtils.closeQuietly(wb, os);
        }
    }

    /**
     * 将excel保存到目录directory, 文件名为： {@Link workbookName}
     *
     * @param directory 保存目录
     * @return
     * @author lihong10 2018/12/21 16:23
     * @modificationHistory=========================逻辑或功能性重大变更记录
     * @modify by user: {修改人} 2018/12/21 16:23
     * @modify by reason:{原因}
     */
    public void writeToFile(String directory) {
        if (wb == null) {
            return;
        }

        if (StringUtils.isBlank(directory)) {
            throw new IllegalArgumentException("未指定导出路径");
        }


        directory = directory.trim();
        String path = null;


        FileOutputStream os = null;
        try {
            //空白符
            String whiteSpace = " ";
            //将文件名中的空白符替换为_,以免报错
            String encodedName = workbookName.replace(whiteSpace, "_");
            if (directory.endsWith(CommonConsts.SLASH) || directory.endsWith(CommonConsts.BACK_SLASH)) {
                path = directory.substring(0, directory.length() - 1);
                path = path + File.separator + encodedName;
            } else {
                path = directory + File.separator + encodedName;
            }

            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            os = new FileOutputStream(file);
            wb.write(os);
        } catch (Exception e) {
            LOGGER.error("export error", e);
        } finally {
            IOUtils.closeQuietly(wb, os);
        }
    }

    public static class SheetWriter<T> {
        private XSSFSheet sheet = null;
        private Map<Integer, Field> fieldMap = new HashMap<Integer, Field>();
        private Map.Entry<Integer, Field>[] entries = null;
        private Map<Integer, FieldExportDesc> fieldDescMap = new HashMap<Integer, FieldExportDesc>();
        private int rowStartPosition = 1;
        private String DATE_FORMAT = null;
        private String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
        private SimpleDateFormat dateFormat = null;

        private void init(XSSFSheet sheet, Class<T> clazz) {
            this.sheet = sheet;

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                FieldExportDesc fieldDesc = field.getAnnotation(FieldExportDesc.class);
                if (fieldDesc == null) {
                    continue;
                }
                field.setAccessible(true);
                fieldDescMap.put(fieldDesc.order(), fieldDesc);
                fieldMap.put(fieldDesc.order(), field);
            }
            List<Map.Entry<Integer, Field>> entryList = new ArrayList<Map.Entry<Integer, Field>>(fieldMap.entrySet());
            //转换成数组，是为了借助数组的特性，在追加数据到excel时，加快字段迭代速度
            entries = entryList.toArray(new Map.Entry[fieldMap.entrySet().size()]);

        }

        public void setDATE_FORMAT(String FORMAT) {
            this.DATE_FORMAT = StringUtils.isBlank(FORMAT) ? DEFAULT_DATE_FORMAT : FORMAT;
            this.dateFormat = new SimpleDateFormat(this.DATE_FORMAT);
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
        public String asDateString(Timestamp date) {
            if (date == null) {
                return null;
            }
            return dateFormat.format(date);
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
        public String asDateString(Date date) {
            if (date == null) {
                return null;
            }
            return dateFormat.format(date);
        }

        /**
         * 打印表头
         *
         * @param headerRow 表头所在的行号，从0开始算行号
         * @return
         * @author lihong10 2018/12/21 16:23
         * @modificationHistory=========================逻辑或功能性重大变更记录
         * @modify by user: {修改人} 2018/12/21 16:23
         * @modify by reason:{原因}
         */
        public XSSFSheet setHeader(int headerRow) {
            XSSFRow row = sheet.createRow(headerRow);
            for (Map.Entry<Integer, FieldExportDesc> header : fieldDescMap.entrySet()) {
                Integer cloumnIndex = header.getKey();
                int width = header.getValue().width();
                String cloumnName = header.getValue().display();
                sheet.setColumnWidth(cloumnIndex, width);
                XSSFCell cell = row.createCell(cloumnIndex);
                cell.setCellValue(cloumnName);
            }
            return sheet;
        }

        /**
         * 设置数据开始追加的起始行号，如果参数为1，则数据追加的起始行是第2号（从0开始计算）
         *
         * @param rowStartPosition 数据开始追加的起始行号
         * @return
         * @author lihong10 2018/12/21 16:23
         * @modificationHistory=========================逻辑或功能性重大变更记录
         * @modify by user: {修改人} 2018/12/21 16:23
         * @modify by reason:{原因}
         */
        public void setRowStartPosition(int rowStartPosition) {
            this.rowStartPosition = rowStartPosition;
        }


        /**
         * 将数据追加到excel
         *
         * @param data 待导出的数据
         * @return
         * @author lihong10 2018/12/21 16:23
         * @modificationHistory=========================逻辑或功能性重大变更记录
         * @modify by user: {修改人} 2018/12/21 16:23
         * @modify by reason:{原因}
         */
        public void appendToExcel(T data) {
            if (data == null) {
                return;
            }

            XSSFRow row = sheet.createRow(rowStartPosition);
            for (Map.Entry<Integer, Field> record : entries) {

                Integer columnIndex = record.getKey();
                Field field = record.getValue();
                XSSFCell cell = row.createCell(columnIndex);
                try {
                    Object value = field.get(data);
                    if (value != null) {
                        setCellValue(cell, value);
                    }
                } catch (Exception e) {
                    LOGGER.error(expPrefixDesc, e);
                    cell.setCellValue(expPrefixDesc);
                }
            }
            rowStartPosition++;
        }

        /**
         * 给单元格设置值
         *
         * @param cell
         * @param value
         * @author guoliang5 2018-11-23 上午15:05:56
         */
        private void setCellValue(XSSFCell cell, Object value) {
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


        public XSSFSheet getSheet() {
            return sheet;
        }

    }


}
