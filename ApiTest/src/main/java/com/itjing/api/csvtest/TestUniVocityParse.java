package com.itjing.api.csvtest;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class TestUniVocityParse {

    public static void main(String[] args) {

        // 创建csv解析器settings配置对象
        CsvParserSettings settings = new CsvParserSettings();
        // 文件中使用 '\n' 作为行分隔符
        // 确保像MacOS和Windows这样的系统
        // 也可以正确处理（MacOS使用'\r'；Windows使用'\r\n'）
        settings.getFormat().setLineSeparator("\n");
        // 创建CSV解析器（将分隔符传入对象）
        CsvParser parser = new CsvParser(settings);

        // 调用beginParsing逐个读取记录，使用迭代器iterator
        parser.beginParsing(getReader("/testcsv.csv"));
        String[] row;
        while ((row = parser.parseNext()) != null) {
            System.out.println(Arrays.toString(row));
        }

        // 在读取结束时自动关闭所有资源，
        // 或者当错误发生时，可以在任何使用调用stopParsing()

        // 只有在不是读取所有内容的情况下调用下面方法
        // 但如果不调用也没有非常严重的问题
        parser.stopParsing();
    }

    public static Reader getReader(String relativePath) {

        try {
            return new InputStreamReader(TestUniVocityParse.class.getResourceAsStream(relativePath), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unable to read input", e);
        }

    }

}