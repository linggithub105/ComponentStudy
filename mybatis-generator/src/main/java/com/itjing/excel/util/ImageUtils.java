package com.itjing.excel.util;

import com.itjing.excel.config.ProjectConfig;
import com.itjing.excel.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

/**
 * @author lijing
 * @date 2022年06月28日 16:50
 * @description 图像处理工具类
 */
@Slf4j
public class ImageUtils {
    public static byte[] getImage(String imagePath) {
        InputStream is = getFile(imagePath);
        try {
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            log.error("图片加载异常 {}", e);
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public static InputStream getFile(String imagePath) {
        try {
            byte[] result = readFile(imagePath);
            result = Arrays.copyOf(result, result.length);
            return new ByteArrayInputStream(result);
        } catch (Exception e) {
            log.error("获取图片异常 {}", e);
        }
        return null;
    }

    /**
     * 读取文件为字节数据
     *
     * @param url 地址
     * @return 字节数据
     */
    public static byte[] readFile(String url) {
        InputStream in = null;
        ByteArrayOutputStream baos = null;
        try {
            if (url.startsWith("http")) {
                // 网络地址
                URL urlObj = new URL(url);
                URLConnection urlConnection = urlObj.openConnection();
                urlConnection.setConnectTimeout(30 * 1000);
                urlConnection.setReadTimeout(60 * 1000);
                urlConnection.setDoInput(true);
                in = urlConnection.getInputStream();
            } else {
                // 本机地址
                String localPath = ProjectConfig.getProfile();
                String downloadPath = localPath + StringUtils.substringAfter(url, Constants.RESOURCE_PREFIX);
                in = new FileInputStream(downloadPath);
            }
            return IOUtils.toByteArray(in);
        } catch (Exception e) {
            log.error("获取文件路径异常 {}", e);
            return null;
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

}
