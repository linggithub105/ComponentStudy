package com.itjing.service.ftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lijing
 * @date 2022年06月25日 10:00
 * @description
 */
@Slf4j
public class FtpServer {

    // ftp对象
    private static FTPClient ftp;

    // 需要连接到的ftp端的ip
    private String ip;

    // 连接端口，默认21
    private Integer port = 21;

    // 要连接到的ftp端的名字
    private String name;

    // 要连接到的ftp端的对应得密码
    private String pwd;

    // 是否使用SSL连接
    private boolean isSftp;

    private boolean isTextMode = false;

    private String tranEncoding = "GB2312";

    // 秒数
    private int defaultTimeoutSecond = 60;
    private int connectTimeoutSecond = 30;
    private int dataTimeoutSecond = 30;
    // 秒
    private int controlKeepAliveTimeout = 120;
    private int controlKeepAliveReplyTimeout = -1;


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

    private void init() throws Exception {
        if (isSftp) {
            ftp = new FTPSClient(false);
        } else {
            ftp = new FTPClient();
        }
        // 每大约2分钟发一次noop，防止大文件传输导致的控制连接中断
        ftp.setControlKeepAliveTimeout(controlKeepAliveTimeout);
        // 设置默认超时
        ftp.setDefaultTimeout(defaultTimeoutSecond * 1000);
        // 验证登录
        // 连接到远程的FTP服务器.
        try {
            if (port != null) {
                ftp.connect(ip, port);
            } else {
                ftp.connect(ip);
            }
        } catch (Exception e) {
            this.destory();
            log.error("连接服务器失败,host:{}, port:{}", ip, port);
            throw e;
        }
        int reply = ftp.getReplyCode(); // 获得返回的代码，来判断连接状态
        if (!FTPReply.isPositiveCompletion(reply)) {
            this.destory();
            // 连接错误[拒绝连接]
            throw new Exception("FTP server refused connection. " + ip);
        }
        ///// 开始进行登录/////
        boolean flag = ftp.login(name, pwd);
        if (flag == false) {
            this.destory();
            throw new Exception("Invalid user/password");
        }

        try {
            if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {
                // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码
                tranEncoding = "UTF-8";
                log.debug("FTP Server allow UTF8 encoding");
            }
        } catch (Exception e) {
            log.error("ftp server opts utf8 error,{}", e.getMessage());
        }
        log.info("use encoding {}", tranEncoding);
        ftp.setControlEncoding(tranEncoding); // 中文支持
        ftp.setCharset(Charset.forName("UTF-8"));
        // 设置传送模式
        if (isTextMode) {
            ftp.setFileType(FTP.ASCII_FILE_TYPE);
        } else {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
        }
        ftp.enterLocalPassiveMode();
    }

    /**
     * 上传文件
     *
     * @param SourcePath 本地文件
     * @param TargetPath FTP服务器文件
     */
    public void UploadFile(String SourcePath, String TargetPath) {
        try {
            OutputStream os = ftp.storeFileStream(TargetPath);
            FileInputStream fis = new FileInputStream(SourcePath);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = fis.read(b)) != -1) {
                os.write(b, 0, len);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("UploadFile 上传文件失败" + e.getMessage());
        }
    }

    /**
     * 重命名
     *
     * @param OldName 旧名称
     * @param NewName 新名称
     */
    public void Rename(String OldName, String NewName) {

    }

    /**
     * 下载文件
     *
     * @param SourcePath 本地文件
     * @param TargetPath FTP文件
     */
    public void DownloadFile(String SourcePath, String TargetPath) {
        try {
            InputStream is = ftp.retrieveFileStream(TargetPath);
            FileOutputStream fos = new FileOutputStream(new File(SourcePath));
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = is.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException("DownloadFile 下载文件失败" + e.getMessage());
        }
    }

    public InputStream getInputStream(String TargetPath) {
        try {
            InputStream is = ftp.retrieveFileStream(TargetPath);
            return is;
        } catch (IOException e) {
            throw new RuntimeException("获取文件输入流失败：" + e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param DelPath 删除文件路径
     */
    public void DeleteFile(String DelPath) {
        try {
            ftp.deleteFile(DelPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("删除文件异常%s", e.getMessage()));
        }
    }

    /**
     * 获取根目录全部文件目录文件名称
     */
    public void FileList() {
        FileList("/");
    }

    /**
     * 获取选中文件节点下所以文件目录名称
     *
     * @param DirName
     */
    public void FileList(String DirName) {
        FTPFile[] files = new FTPFile[0];
        try {
            files = ftp.listFiles(DirName);
            //打印出ftp里面，“Windows”文件夹里面的文件名字
            for (FTPFile file : files) {
                System.out.println(file.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("FileList错误%s", e.getMessage()));
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param FilePath
     * @return
     */
    public boolean FileExists(String FilePath) {
        try {
            FTPFile[] files = ftp.listFiles(FilePath);
            if (files != null && files.length > 0) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("FileExists 文件是否存在异常 %s", e.getMessage()));
        }
    }


    private static List<String> readDel(InputStream inputStream) throws IOException {
        List<String> allString = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(inputStream);
             BufferedReader br = new BufferedReader(new InputStreamReader(dis, "UTF-8"))) {
            String line = "";
            String everyLine = "";
            // 读取到的内容给line变量
            while ((line = br.readLine()) != null) {
                everyLine = line;
                allString.add(everyLine);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ftp.completePendingCommand();
        return allString;
    }


    private void destory() {
        if (ftp == null) {
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
