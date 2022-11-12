package com.itjing.utils.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author lijing
 * @date 2022年07月05日 14:05
 * @description Ftp服务
 */
public class FtpServer {

    private static final Logger log = LoggerFactory.getLogger(FtpServer.class);

    /**
     * ftp对象
     */
    private static FTPClient ftp;

    /**
     * ftp端ip
     */
    private String ip;

    /**
     * 端口，默认21
     */
    private Integer port = 21;

    /**
     * ftp用户名
     */
    private String name;

    /**
     * ftp密码
     */
    private String pwd;

    /**
     * 是否使用SSL连接
     */
    private boolean isSftp;

    private int defaultTimeoutSecond = 60;

    private int connectTimeoutSecond = 30;

    private int dataTimeoutSecond = 30;

    private int controlKeepAliveTimeout = 120;

    private int controlKeepAliveReplyTimeout = -1;

    private String tranEncoding = "GB2312";

    private boolean isTextMode = false;

    public FtpServer(String ip, String name, String pwd, boolean isSftp) throws Exception {
        this(ip, 21, name, pwd, isSftp);
    }

    public FtpServer(String ip, int port, String name, String pwd, boolean isSftp) throws Exception {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.pwd = pwd;
        this.isSftp = isSftp;
        this.init();
    }

    /**
     * 初始化ftp
     */
    private void init() throws Exception {
        if (isSftp) {
            ftp = new FTPSClient(false);
        } else {
            ftp = new FTPClient();
        }
        ftp.setControlKeepAliveTimeout(controlKeepAliveTimeout);
        ftp.setDefaultTimeout(defaultTimeoutSecond * 1000);
        // 验证登录
        try {
            if (Objects.isNull(port)) {
                ftp.connect(ip);
            } else {
                ftp.connect(ip, port);
            }
        } catch (Exception e) {
            log.error("ftp连接失败......");
            throw new RuntimeException("ftp连接失败......");
        }

        int replyCode = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            this.destroy();
            throw new Exception("FTP server refused connection." + ip);
        }

        // 开始登录
        boolean flag = ftp.login(name, pwd);
        if (flag == false) {
            this.destroy();
            throw new Exception("Invalid username/password. ");
        }

        try {
            if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {
                // 开启服务器对UTF-8的支持，如果支持就用UTF-8，否则就用本地编码
                tranEncoding = "UTF-8";
                log.info("FTP Server allow UTF8 encoding");
            }
        } catch (Exception e) {
            log.error("ftp server opts utf8 error, {}" + e.getMessage());
        }
        ftp.setControlEncoding(tranEncoding);
        ftp.setCharset(Charset.forName("UTF-8"));
        if (isTextMode) {
            ftp.setFileType(FTP.ASCII_FILE_TYPE);
        } else {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
        }
        // 开启被动模式
        ftp.enterLocalPassiveMode();
    }

    /**
     * 获取文件输入流
     *
     * @return
     */
    public InputStream getFileInputStream(String targetPath) {
        try {
            InputStream is = ftp.retrieveFileStream(targetPath);
            return is;
        } catch (IOException e) {
            log.error("获取文件输入流失败.....");
            throw new RuntimeException("获取文件输入流失败....." + e.getMessage());
        }
    }

    /**
     * 下载文件
     *
     * @param sourcePath
     * @param targetPath
     */
    public void downLoadFile(String sourcePath, String targetPath) {
        try {
            InputStream is = ftp.retrieveFileStream(targetPath);
            if (Objects.nonNull(is)) {
                File file = new File(sourcePath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = is.read(b)) != -1) {
                    fos.write(b, 0, len);
                }
                fos.close();
                is.close();
                ftp.completePendingCommand();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("DownloadFile 下载文件失败" + e.getMessage());
        }
    }

    private void destroy() {
        if (Objects.isNull(ftp)) {
            return;
        }
        try {
            ftp.logout();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ftp.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ftp = null;
    }

}
