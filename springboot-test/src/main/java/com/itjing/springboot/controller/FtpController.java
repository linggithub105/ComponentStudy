package com.itjing.springboot.controller;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;

/**
 * @author lijing
 * @date 2022年07月04日 20:25
 * @description ftp相关
 */
@RestController
@RequestMapping("/ftp")
@Slf4j
public class FtpController {

    /**
     * 测试Hutool封装的ftp工具
     */
    @GetMapping("/testFtp")
    public void testFtp() {
        FtpConfig ftpConfig = new FtpConfig("192.168.56.111", 21, "test", "test", Charset.forName("UTF-8"));
        // 为ftpConfig设置参数 TODO
        // 超时时间

        Ftp ftp = null;
        try {

            ftp = new Ftp(ftpConfig, FtpMode.Passive);

        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }
}
