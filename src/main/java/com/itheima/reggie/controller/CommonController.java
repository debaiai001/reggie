package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @className: CommonController
 * @description: TODO 文件上传和下载
 * @author: Figure
 * @date: 2023/02/11 14:21
 **/
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    //上传图片保存路径，可在配置文件中修改
    @Value("${reggie.path}")
    private String basePath;

    /**
     * @Author Figure
     * @Description //TODO 文件上传
     * @Date 14:25 2023/2/11
     * @Param [file]
     * @return com.itheima.reggie.common.R<java.lang.String>
     **/
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){//file与前端一致，不能改变
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();   //xxx.jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));  //.jpg
        //使用UUID重新生成文件名
        String fileName = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        if (!dir.exists()){
            //目录不存在，需要创建
            dir.mkdir();
        }

        //将临时文件转存到指定位置
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    /**
     * @Author Figure
     * @Description //TODO 文件下载
     * @Date 13:47 2023/2/12
     * @Param [name, response]
     * @return void
     **/
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流，通过输出流将文件写回浏览器,在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes  = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();;
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
