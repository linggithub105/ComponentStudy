package com.itjing.minio.controller;

import com.itjing.minio.config.MinioConfig;
import com.itjing.minio.entity.UploadResponse;
import com.itjing.minio.response.GeneralResult;
import com.itjing.minio.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lijing
 * @date 2022年06月14日 19:38
 * @description 测试 minio 上传
 */
@RestController
@Slf4j
public class TestController {

    @Autowired
    private MinioUtil minioUtil;

    @Autowired
    private MinioConfig minioConfig;

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public GeneralResult minioUpload(@RequestParam(value = "file") MultipartFile file) {
        UploadResponse response = null;
        try {
            response = minioUtil.uploadFile(file, minioConfig.getBucketName());
        } catch (Exception e) {
            log.error("上传失败", e);
        }
        return GeneralResult.genSuccessResult(response);
    }


    /**
     * 上传视频
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadVideo")
    public GeneralResult uploadVideo(@RequestParam(value = "file") MultipartFile file) {
        UploadResponse response = null;
        try {
            response = minioUtil.uploadVideo(file, minioConfig.getBucketName());
        } catch (Exception e) {
            log.error("上传失败", e);
        }
        return GeneralResult.genSuccessResult(response);
    }

}