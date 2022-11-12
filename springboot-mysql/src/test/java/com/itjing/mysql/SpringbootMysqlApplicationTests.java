package com.itjing.mysql;

import com.itjing.mysql.entity.UploadFileTest;
import com.itjing.mysql.mapper.UploadFileTestMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
@Slf4j
class SpringbootMysqlApplicationTests {

    @Autowired
    private UploadFileTestMapper uploadFileTestMapper;

    /**
     * 测试将文件二进制流保存到mysql
     * @throws IOException
     */
    @Test
    public void testInsertTbBlob() throws IOException {

        File file = new File("D:\\学习重要文件\\工作\\睿希信息科技\\南京银行北京分行计财部核销系统\\系统模板\\员工信息模板.xls");
        FileInputStream fis = new FileInputStream(file);
        log.info("bytes of file: " + fis.available());
        byte[] data = new byte[fis.available()];
        fis.read(data, 0, fis.available());
        fis.close();
        UploadFileTest uploadFile = new UploadFileTest();
        uploadFile.setName(file.getName());
        uploadFile.setUploadFile(data);

        int result = uploadFileTestMapper.insert(uploadFile);

        log.info("插入成功条数：" + result);
    }

    /**
     * 测试查询文件二进制流，并输出文件
     * @throws IOException
     */
    @Test
    public void testSelectTbBlobById() throws IOException {
        UploadFileTest uploadFile = uploadFileTestMapper.selectByPrimaryKey(1);
        if (uploadFile != null) {
            log.info("number of bytes: " + uploadFile.getUploadFile().length);
            FileOutputStream fos = new FileOutputStream("src/" + uploadFile.getName());
            fos.write(uploadFile.getUploadFile(), 0, uploadFile.getUploadFile().length);
            fos.close();
        }
    }
}
