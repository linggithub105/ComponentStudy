package com.itjing.springboot.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @Description: 测试commons-io的相关api
 * @Author: lijing
 * @CreateTime: 2022-09-23 09:24
 */
@Slf4j
public class TestCommonsIO {

    @Test
    public void testFileUtils() throws IOException {
        LineIterator it = FileUtils.lineIterator(new File("D:\\学习重要文件\\工作\\睿希信息科技\\徐州农商行账户资料管理\\档案系统供数\\CBOD_SAACNACN.del"), "GBK");
        try {
            String splitStr = new String(new char[]{0x1D});
            while (it.hasNext()) {
                String line = it.nextLine();

            }
        } finally {
            LineIterator.closeQuietly(it);
        }
    }
}
