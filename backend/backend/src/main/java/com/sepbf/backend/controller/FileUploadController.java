package com.sepbf.backend.controller;

import com.sepbf.backend.utils.QCloudCosUtils;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.sepbf.backend.utils.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.qcloud.cos.exception.CosClientException;

@Controller
public class FileUploadController {




    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 检查上传文件是否为空
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("上传文件不能为空");
            }

            QCloudCosUtils qCloudCosUtils = new QCloudCosUtils();
            qCloudCosUtils.upload(file);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("文件上传成功");
        } catch (CosClientException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败");
        }
    }
}
