package com.fyr.activiti.learning.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

public class GenderConverter implements Converter<String> {

    @Override
    public String convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // return Converter.super.convertToJavaData(cellData, contentProperty, globalConfiguration);
        switch (cellData.getData().toString()){
            case "男":
                return "M";
            case "女":
                return "F";
            default:
                return null;
        }
    }

    @Override
    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // return Converter.super.convertToExcelData(value, contentProperty, globalConfiguration);
        switch (value) {
            case "M":
                return new WriteCellData<>("男");
            case "F":
                return new WriteCellData<>("女");
            default:
                return new WriteCellData<>("Undefined");
        }
    }
}
