package com.example.demo.controller;

import org.bytedeco.javacv.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Controller
@RequestMapping("/video")
public class VideoController {
    @GetMapping("/getVideo")
    @ResponseBody
    public boolean getVideo() throws Exception{
        //视频地址
        String url="http://39.106.13.47:8080/live/112.live.flv";

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(url);
        try{
            //开始获取视频
            grabber.start();
        }
        catch(Exception e){
            System.out.println("视频截取失败，请检查url地址及连接状况");
            e.printStackTrace();
        }

        // 创建Java2DFrameConverter，用于图像转换
        Java2DFrameConverter converter = new Java2DFrameConverter();

        int i = 0;
        Frame frame = null;
        while ((frame = grabber.grabImage()) != null) {
            // 将获取的frame转换为BufferedImage对象
            BufferedImage bi = converter.convert(frame);

            // 保存图片到指定路径
            File outputfile = new File("image-" + (i++) + ".jpg");
            ImageIO.write(bi, "jpg", outputfile);
        }
        return true;
    }
}
