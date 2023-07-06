package com.sepbf.backend.controller;

import com.sepbf.backend.utils.QCloudCosUtils;
import com.qcloud.cos.auth.BasicCOSCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.auth.COSStaticCredentialsProvider;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class FileUploadController {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${cos.bucketName}")
    private String bucketName;

    @Value("${cos.secretId}")
    private String secretId;

    @Value("${cos.secretKey}")
    private String secretKey;

    @Value("${cos.region}")
    private String region;

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
