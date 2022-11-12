package com.itjing.excel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.itjing.excel.entity.User;
import com.itjing.excel.listener.UserListener;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Excel相关控制器
 * @Author: lijing
 * @CreateTime: 2022-09-07 15:06
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @PostMapping("/import")
    public String importByExcel(MultipartFile file) throws IOException {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcelFactory.read(file.getInputStream(), User.class, new UserListener())
                .sheet()
                // 这里可以设置行头，如果行头不止一行，可以设置最后一行行头所在位置。
                // 不写 .headRowNumber(位置) 也可以，因为默认会根据 DemoData 来解析，他没有指定头，也就是默认行头是第1行
//                .headRowNumber(1)
                .doRead();
        return "success";
    }

    /**
     * EasyExcel导出动态头
     * @param response
     * @throws IOException
     */
    @GetMapping("/exportDynamicHeadByEasyExcel")
    public void exportDynamicHeadByEasyExcel(HttpServletResponse response) throws IOException {
        ServletOutputStream out = null;

        try {
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("123.xlsx", "utf-8"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 10);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 内容水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 设置垂直居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        EasyExcel.write(out)
                // excel版本，对应xls或者xlsx
                .excelType(ExcelTypeEnum.XLSX)
                // 是否自动关流
                .autoCloseStream(Boolean.TRUE)
                .registerWriteHandler(horizontalCellStyleStrategy)
                // 设置sheet名
                .sheet("123")
                // 这里放入动态头
                .head(head())
                // 当然这里数据也可以用 List<List<String>> 去传入
                .doWrite(getData());
    }

    public List<List<String>> getData() {
        List<List<String>> total = new ArrayList<>();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("我是第" + i + "列!");
        }
        total.add(list);

        return total;

    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<>();
        head0.add("Id");
        List<String> head1 = new ArrayList<>();
        head1.add("名称");
        List<String> head2 = new ArrayList<>();
        head2.add("尺寸");

        list.add(head0);
        list.add(head1);
        list.add(head2);

        List<String> productHead = new ArrayList<>();
        productHead.add("aaa");
        productHead.add("bbb");
        productHead.add("ccc");

        List<String> head3;
        for (int i = 1; i < 5; i++) {
            for (String s : productHead) {
                head3 = new ArrayList<>();
                head3.add("子产品" + i);
                head3.add(s);
                list.add(head3);
            }
        }
        return list;
    }

    /**
     * Hutool导出动态头
     * @param response
     * @throws IOException
     */
    @GetMapping("/exportDynamicHeadByHutool")
    public void exportDynamicHeadByHutool(HttpServletResponse response) throws IOException {

    }


}
