package com.fyr.learning.file.controller;

import com.alibaba.excel.EasyExcel;
import com.fyr.learning.file.entity.EmployeeEntity;
import com.fyr.learning.file.service.ExcelService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    ExcelService excelService;

    @PostMapping("/upload")
    public ResponseEntity<ResultSet> upload(@RequestParam("oprType") String oprType, @RequestParam("file") MultipartFile file) {
        String filename = "uf-" + oprType + "_" + System.currentTimeMillis();
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        File destFile = new File(System.getProperty("user.dir") + File.separator + filename + File.separator + suffix);
        if (!destFile.getParentFile().exists()) {
            try {
                FileUtils.forceMkdirParent(destFile);
                file.transferTo(destFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<ResultSet> download(@RequestParam("oprType") String oprType, HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String filename = "df-" + oprType + "_" + System.currentTimeMillis();
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
        try {
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8");
            EasyExcel.write(response.getOutputStream(), EmployeeEntity.class).sheet("模板").doWrite(excelService.find());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
