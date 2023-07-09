package com.sepbf.backend.controller;

import com.sepbf.backend.utils.QCloudCosUtils;
import com.sepbf.backend.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class FileController {


    @PostMapping("/upload")
    public Result<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("userId") Integer userId , @RequestParam("prefix") String prefix) {

            // 检查上传文件是否为空
            if (file.isEmpty()) {
                return Result.error("上传文件为空");
            }

            QCloudCosUtils qCloudCosUtils = new QCloudCosUtils();
            qCloudCosUtils.upload(file, userId.toString(), prefix);

        return Result.success();
    }


    /**
     * 下载接口
     * @param flag
     * @param response
     */

    /**
     * OSS文件删除
     * @param fileVO 文件存储路径
     * @return
     */
    }
