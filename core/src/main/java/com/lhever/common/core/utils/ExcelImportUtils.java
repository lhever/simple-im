package com.lhever.common.core.utils;

import com.lhever.common.core.support.logger.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * EXECL文件导入处理工具类
 * <p>
 * </p>
 *
 * @author fanxunfeng 2013-11-21 上午09:18:02
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2013-11-21
 * @modify by reason:{方法名}:{原因}
 */
@SuppressWarnings("unchecked")
public class ExcelImportUtils {

    private static Logger logger = LogFactory.getLogger(ExcelImportUtils.class);


    private static final void insertValue(Object o, String fieldName, String value) throws SecurityException,
            NoSuchFieldException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, ParseException {
        Field field = o.getClass().getDeclaredField(fieldName);
        Method method = o.getClass().getMethod(
                "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1), field.getType());
        method.invoke(o, transforObject(field.getType(), value));
    }

    @SuppressWarnings("rawtypes")
    private static final Object transforObject(Class type, String str) throws ParseException {


        if (str == null || str.trim().length() == 0) {
            return null;
        }
        Object value = str;
        if (type.equals(Integer.class) || type.equals(int.class)) {
            value = Integer.valueOf(str);
        } else if (type.equals(Timestamp.class)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(str);
            value = new Timestamp(date.getTime());
        } else if (type.equals(boolean.class)) {
            value = Boolean.valueOf(str);
        }
        return value;
    }

    /**
     * @param excel      Excel文件
     * @param className
     * @param fieldNames 导入字段数组
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static final List parseExcel(File excel, String className, String[] fieldNames) {
        List result = new ArrayList();
        Workbook wbook = null;
        FileInputStream fs = null;
        Object o = null;
        try {
            fs = new FileInputStream(excel);
            wbook = WorkbookFactory.create(fs);
            Sheet sheet = wbook.getSheetAt(0);
            int num = sheet.getLastRowNum();
            Row row = null;
            for (int r = 1; r <= num; r++) {
                row = sheet.getRow(r);
                o = Class.forName(className).newInstance();
                if (row != null) {
                    for (int i = 0; i < fieldNames.length; i++) {
                        insertValue(o, fieldNames[i], getValue(row.getCell(i)));
                    }
                }
                result.add(o);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw new UnsupportedOperationException("解析EXCEL生成对象错误,请根据Excel模板上传正确文件!");
        } finally {
            IOUtils.closeQuietly(fs);
        }
        return result;
    }

    /**
     * 解析excel
     *
     * @param excelFile 文件
     * @param sheetName Map的键值是否为sheet页的名称，true：是，false：即为"0","1"...
     * @return map key:sheet页签 value:List里面的String[]为一行的数据
     * @throws Exception 如果有异常，抛出
     * @author chenweiyf2
     */
    public static Map<String, List<String[]>> analysisExcelByFile(
            File excelFile, boolean sheetName) throws Exception {

        return analysisExcelByWorkbook(getExcelByFile(excelFile), sheetName);
    }

    /**
     * 解析excel
     *
     * @param excelFile 文件
     * @param sheetName Map的键值是否为sheet页的名称，true：是，false：即为"0","1"...
     * @return map key:sheet页签 value:List里面的String[]为一行的数据
     * @throws Exception 如果有异常，抛出
     * @author chenweiyf2
     */
    public static Map<String, List<String[]>> analysisExcelByStream(
            InputStream stream, boolean sheetName) throws Exception {

        return analysisExcelByWorkbook(getExcelByStream(stream), sheetName);
    }

    @SuppressWarnings("deprecation")
    private static Workbook getExcelByFile(File excelFile) throws Exception {
        Workbook workBook = null;
        // 文件路径
        String filePath = excelFile.getAbsolutePath();
        try {
            // 07
            workBook = new SXSSFWorkbook(new XSSFWorkbook(filePath));
        } catch (Exception e) {
            // 03
            workBook = new HSSFWorkbook(new FileInputStream(filePath));
        }
        return workBook;
    }

    private static Workbook getExcelByStream(InputStream excelFile) throws Exception {
        Workbook workBook = null;
        // 文件路径
        try {
            // 07
            workBook = new SXSSFWorkbook(new XSSFWorkbook(excelFile));
        } catch (Exception e) {
            // 03
            workBook = new HSSFWorkbook(excelFile);
        }
        return workBook;
    }

    /**
     * 解析excel
     *
     * @param workbook  excel工作薄对象
     * @param sheetName 键值是否为sheet页的名称
     * @return map key:sheet页签 value:List里面的String[]为一行的数据
     * @author chenweiyf2
     */
    private static Map<String, List<String[]>> analysisExcelByWorkbook(
            Workbook workbook, boolean sheetName) {
        // 读取工作薄对象
        Map<String, ArrayList<ArrayList<String>>> excelMap = readExcel(
                workbook, sheetName);

        // 以下为将ArrayList<String>转换为String[]
        Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
        ArrayList<ArrayList<String>> cellValueArray = null;
        List<String[]> stringArrylist = null;
        String[] cellValue = null;
        String key = null;

        // 获取所有key的集合
        Set<String> keySet = excelMap.keySet();
        // 将ArrayList<String>中的值写入String[]中
        for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
            key = it.next();
            cellValueArray = excelMap.get(key);
            stringArrylist = new ArrayList<String[]>();
            for (ArrayList<String> cell : cellValueArray) {
                cellValue = new String[cell.size()];
                for (int k = 0; k < cell.size(); k++) {
                    cellValue[k] = cell.get(k);
                }
                stringArrylist.add(cellValue);
            }
            map.put(key, stringArrylist);
        }
        return map;
    }

    /**
     * 读取excel中所有值
     *
     * @param workBook  工作薄对象
     * @param sheetName 键值是否为sheet页的名称
     * @return Map<String   ,       ArrayList   <   ArrayList   <   String>>>
     * @author chenweiyf2
     */
    private static Map<String, ArrayList<ArrayList<String>>> readExcel(
            Workbook workBook, boolean sheetName) {
        Map<String, ArrayList<ArrayList<String>>> map = new HashMap<String, ArrayList<ArrayList<String>>>();

        // 每个sheet页中所有的值list
        ArrayList<ArrayList<String>> sheetValueList = null;
        // 每个sheet页对象
        Sheet sheet = null;
        // 每个sheet页的每行对象
        Row sheetRow = null;
        // 每个sheet页的每个行的列集合
        ArrayList<String> arrCell = null;
        // 每个sheet页的每个行的列对象
        Cell cell = null;
        for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
            sheetValueList = new ArrayList<ArrayList<String>>();
            // 得到当前sheet页对象
            sheet = workBook.getSheetAt(numSheet);
            // 如果sheet页为null，则获取下一个sheet页
            if (null == sheet) {
                continue;
            }

            sheetRow = null;
            // 循环行Row
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                arrCell = new ArrayList<String>();
                cell = null;

                sheetRow = sheet.getRow(rowNum);
                // 如果当前行对象为null，则当前list.size()==0
                if (null != sheetRow) {
                    // 循环列Cell
                    for (int cellNum = 0; cellNum < sheetRow.getLastCellNum(); cellNum++) {
                        cell = sheetRow.getCell(cellNum);
                        // 如果当前列对象为null，则给此行创建列对象，并设置值为空
                        if (null == cell) {
                            cell = sheetRow.createCell(cellNum);
                            cell.setCellValue("");
                        }
                        arrCell.add(getValue(cell));
                    }
                }
                sheetValueList.add(arrCell);
            }
            // 如果最终需要根据sheet页的name来获取数据，则此处的key为各sheet页的name
            if (sheetName) {
                map.put(sheet.getSheetName(), sheetValueList);
            } else {
                // 此处直接存序号就可以了
                map.put(String.valueOf(numSheet), sheetValueList);
            }
        }

        return map;
    }

    //获取单元格内的值
    private static final String getValue(Cell cell) {
        String value = null;
        if (cell == null) {
            return value;
        }
        switch (cell.getCellType()) {
            case STRING: // 字符串
                value = cell.getStringCellValue();
                break;
            case NUMERIC: // 数字
                if (DateUtil.isCellDateFormatted(cell)) {  //判断是否包含日期
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = dff.format(date);   //日期转化
                } else {
                    cell.setCellType(CellType.STRING);//将单元格设置成字符串类型，避免整型转正浮点型
                    value = cell.getStringCellValue();
                }
                break;
            case BOOLEAN://boolean值
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA://公式
                value = cell.getCellFormula();
                break;
            case BLANK: // 空值
                break;
            default:
                break;
        }
        return value;
    }

}
