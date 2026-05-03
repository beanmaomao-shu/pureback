package com.zhouq.common.Utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 计算机系 周启俊
 * @since 2023/7/5 14:37
 */
public class ExcelUtils<T> {

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 工作表名称
     */
    private String sheetName;
    private String filePath;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;


    /**
     * 导入导出数据列表
     */
    private List<T> list;

    /**
     * 注解列表
     */
    private List<Object[]> fields;


    public Class<T> clazz;

    public ExcelUtils(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Boolean exportExcel(List<T> list, String sheetName,String filePath) {
        init(list, sheetName,filePath);
        return exportExcel();
    }

    public void init(List<T> list, String sheetName,String fileName) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.filePath = fileName;
        createExcelField();
        createWorkbook();
    }

    /**
     * 得到所有定义字段
     */
    private void createExcelField() {
        this.fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields) {
            // 单注解
            if (field.isAnnotationPresent(Excel.class)) {
                this.fields.add(new Object[]{field, field.getAnnotation(Excel.class)});
            }
        }
        this.fields = new ArrayList<>(this.fields);

    }

    /**
     * 创建一个工作簿
     */
    public void createWorkbook() {
        this.wb = new SXSSFWorkbook(500);
    }

    private String getTargetValue(Field field, Excel excel, Object obj) {
        try {
            Method method = obj.getClass().getMethod(
                    "get" + String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1));
            method.setAccessible(false);
            Object invoke = method.invoke(obj);
            if (invoke == null) {
                return excel.defaultValue();
            } else {
                if (invoke instanceof BigDecimal) {
                    BigDecimal bigDecimal = (BigDecimal) invoke;
                    return bigDecimal.stripTrailingZeros().toPlainString();
                } else {
                    return invoke.toString();
                }
            }
        } catch (NoSuchMethodException ignored) {
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public Boolean exportExcel() {
        Integer rowNum = 1;
        this.sheet = wb.createSheet(this.sheetName);
        Row titleRow = sheet.createRow(0);
        fields.forEach(field -> {
            Excel excel = (Excel) field[1];
            titleRow.createCell(excel.index());
            titleRow.getCell(excel.index()).setCellValue(excel.name());
        });
        for (T data : list) {
            Row row = sheet.createRow(rowNum);
            for (Object[] fieldTemp : fields) {
                Field field = (Field) fieldTemp[0];
                Excel excel = (Excel) fieldTemp[1];
                Cell cell = row.createCell(excel.index());
                cell.setCellValue(getTargetValue(field, excel, data));

            }
            rowNum = rowNum + 1;
        }

        try {
            File file = new File(filePath);
            OutputStream outputStream = Files.newOutputStream(file.toPath());
            wb.write(outputStream);
            outputStream.close();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return true;
    }
}
