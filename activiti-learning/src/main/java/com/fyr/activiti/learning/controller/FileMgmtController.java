package com.fyr.activiti.learning.controller;

import com.fyr.activiti.learning.repository.FileMgmtRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileMgmtController {

    @Autowired
    FileMgmtRepository fileRepository;

    @GetMapping("/upload")
    public void upload(@RequestParam("file") MultipartFile file, @RequestParam("oprType") String oprType) {
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

    }

}
