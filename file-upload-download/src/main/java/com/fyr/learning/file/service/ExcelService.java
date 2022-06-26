package com.fyr.learning.file.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.fyr.learning.file.entity.EmployeeEntity;
import com.fyr.learning.file.excel.CellColorWriteHandler;
import com.fyr.learning.file.excel.TitleColorCellWriteHandler;
import com.fyr.learning.file.repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class ExcelService {

    @Autowired
    EmployeeRepository employeeRepository;

    public List<EmployeeEntity> find() {
        return employeeRepository.findAll();
    }

    public void generateExcelFile() {
        try {
            List<EmployeeEntity> employeeEntities = employeeRepository.findAll();

            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            // 单元格策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            // 设置背景颜色白色
            contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            // 设置垂直居中为居中对齐
            contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            // 设置左右对齐为靠左对齐
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            // 设置单元格上下左右边框为细边框
            contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
            contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
            contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
            contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
            contentWriteCellStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.index);
            // 创建字体实例
            WriteFont cellWriteFont = new WriteFont();
            // 设置字体大小
            cellWriteFont.setFontHeightInPoints((short) 12);
            cellWriteFont.setColor(IndexedColors.RED.index);
            contentWriteCellStyle.setWriteFont(cellWriteFont);
            // 初始化表格样式
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

            List<Integer> columnIndexs = Arrays.asList(0, 1, 2);
            List<Integer> rowIndexs = Arrays.asList(0);
            TitleColorCellWriteHandler titleColorSheetWriteHandler = new TitleColorCellWriteHandler(rowIndexs, columnIndexs, IndexedColors.RED.index);

            List<Integer> columnIndexs1 = Arrays.asList(0, 1);
            List<Integer> rowIndexs1 = Arrays.asList(1, 2, 3, 4);
            CellColorWriteHandler colorSheetWriteHandler = new CellColorWriteHandler(rowIndexs1, columnIndexs1, IndexedColors.RED.index);

            ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.write(new FileOutputStream(new File(System.getProperty("user.dir") + "\\" + "employee_" + System.currentTimeMillis() + ".xlsx"))).head(EmployeeEntity.class).sheet("employees").registerWriteHandler(horizontalCellStyleStrategy);
            excelWriterSheetBuilder.registerWriteHandler(titleColorSheetWriteHandler);
            // excelWriterSheetBuilder.registerWriteHandler(colorSheetWriteHandler);
            // 开始导出
            excelWriterSheetBuilder.doWrite(employeeEntities);

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
