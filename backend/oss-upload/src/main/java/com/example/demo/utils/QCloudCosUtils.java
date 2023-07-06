package com.example.demo.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;


@Slf4j
@Component
public class QCloudCosUtils {

    //API密钥secretId
    private final String secretId = SecretKey.secretId;

    //API密钥secretKey
    private final String secretKey= SecretKey.secretKey;

    //存储桶所属地域
    private final String region= SecretKey.region;

    //存储桶空间名称
    private final String bucketName= SecretKey.bucketName;

    //存储桶访问域名
    private final String url = SecretKey.url;

    //上传文件前缀路径(eg:/images/) 设置自己的主目录
    private final String prefix = SecretKey.prefix;

    /**
     * 上传File类型的文件
     *
     * @param file 文件
     * @return 上传文件在存储桶的链接
     */
    public String upload(File file) {
        //生成唯一文件名
        String newFileName = generateUniqueName(file.getName());
        //文件在存储桶中的key
        String key = prefix + newFileName;
        //声明客户端
        COSClient cosClient = null;
        try {
            //初始化用户身份信息(secretId,secretKey)
            COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
            //设置bucket的区域
            ClientConfig clientConfig = new ClientConfig(new Region(region));
            //生成cos客户端
            cosClient = new COSClient(cosCredentials, clientConfig);
            //创建存储对象的请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);

            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return url + key;
        } catch (CosClientException e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端(关闭后台线程)
            cosClient.shutdown();
        }
        return null;
    }

    private String generateUniqueName(String name) {
        return name;
    }

    /**
     * upload()重载方法
     *
     * @param multipartFile 文件对象
     * @return 上传文件在存储桶的链接
     */
    public String upload(MultipartFile multipartFile) {
        //生成唯一文件名
        String newFileName = generateUniqueName(multipartFile.getOriginalFilename());

        //文件在存储桶中的key
        String key = prefix +  "/" + newFileName;
        //声明客户端
        COSClient cosClient = null;
        //准备将MultipartFile类型转为File类型
        File file = null;
        try {
            //生成临时文件
            file = File.createTempFile("temp", null);
            //将MultipartFile类型转为File类型
            multipartFile.transferTo(file);
            //初始化用户身份信息(secretId,secretKey)
            COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
            //设置bucket的区域
            ClientConfig clientConfig = new ClientConfig(new Region(region));
            //生成cos客户端
            cosClient = new COSClient(cosCredentials, clientConfig);
            //创建存储对象的请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
            //执行上传并返回结果信息
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return url + key;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端(关闭后台线程)
            cosClient.shutdown();
        }
        return null;
    }

    /**
     * upload()重载方法
     * 流方式上传
     *
     * @param multipartFile
     * @return 上传文件在存储桶的链接
     */
    public String uploadStream(MultipartFile multipartFile) {
        //生成唯一文件名
        String newFileName = generateUniqueName(multipartFile.getOriginalFilename());
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        //文件在存储桶中的key
        String key = prefix + year + "/" + month + "/" + day + "/" + newFileName;
        //声明客户端
        COSClient cosClient = null;
        try {
            //初始化用户身份信息(secretId,secretKey)
            COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
            //设置bucket的区域
            ClientConfig clientConfig = new ClientConfig(new Region(region));
            //生成cos客户端
            cosClient = new COSClient(cosCredentials, clientConfig);
            // 获取文件流
            InputStream inputStream = multipartFile.getInputStream();
            // 获取文件名
            String fileName = multipartFile.getOriginalFilename();
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //创建存储对象的请求
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
            //执行上传并返回结果信息
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            return url + key;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭客户端(关闭后台线程)
            cosClient.shutdown();
        }
        return null;
    }

    /**
     * Description: 判断Cos服务文件上传时文件的contentType
     *
     * @param filenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
                || filenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (filenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }


}

