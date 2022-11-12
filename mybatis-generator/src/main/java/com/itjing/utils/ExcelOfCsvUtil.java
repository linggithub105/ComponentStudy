package com.itjing.utils;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author lijing
 * @date 2021年12月23日 16:06
 * @description csv格式文件读取
 */
@Slf4j
public class ExcelOfCsvUtil {

    public static void main(String[] args) throws FileNotFoundException {
        // csv文件地址
        /*List<String> strs = readCsv(null);
        strs.stream().forEach(str -> {
            // 一般是逗号分隔的
            String[] field = str.split(",");
            Stream.of(field).forEach(data -> {
                System.out.print(data + "\t");
            });
            System.out.println();
        });*/
        String splitStr = new String(new char[]{0x1D});
        System.out.println(splitStr);
    }

    private static List<String> readCsv(InputStream inputStream) {
        ArrayList<String> allString = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(inputStream);
             // 这里如果csv文件编码格式是utf-8,改成utf-8即可
             BufferedReader br = new BufferedReader(new InputStreamReader(dis, "GBK"))) {

            String line = "";
            String everyLine = "";

            // 读取到的内容给line变量
            while ((line = br.readLine()) != null) {
                everyLine = line;
                allString.add(everyLine);
            }
            // 此处解析的数据中包含了表头信息，因此-1
            log.info("解析到了{}条数据！", allString.size() - 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allString;
    }

    /**
     * 将数据生成csv文件
     *
     * @param exportResults
     * @param fileName
     * @param header
     * @throws IOException
     * @throws CsvDataTypeMismatchException
     * @throws CsvRequiredFieldEmptyException
     */
    public static <T> void generateCsvFile(List<T> exportResults,
                                           String fileName,
                                           String[] header) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        Writer writer = new FileWriter(fileName);

        // 设置显示的顺序
        /*String[] columnMapping = {"name", "age"};
        ColumnPositionMappingStrategy<CsvDTO> mapper = new ColumnPositionMappingStrategy<>();
        mapper.setType(CsvDTO.class);
        mapper.setColumnMapping(columnMapping);*/

        // 写表头
        CSVWriter csvWriter = new CSVWriter(
                writer,
                // 分隔符，默认为 ,
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.NO_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
        csvWriter.writeNext(header);
        // 写内容
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
//                .withMappingStrategy(mapper)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withEscapechar('\\')
                .build();
        beanToCsv.write(exportResults);
        csvWriter.close();
        writer.close();
    }

    /**
     * 读取csv文件流返回前端下载
     *
     * @param fileName
     * @param response
     * @throws UnsupportedEncodingException
     */
    public static void readCsvFileStream(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        String myFileName = new String(fileName.getBytes("utf-8"), "gbk");
        File file = new File(myFileName);
        if (file.exists()) {
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + myFileName);
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (file.delete()) {
            log.warn("The file " + file.getName() + " has been deleted！");
        } else {
            log.error("delete file fail！");
        }
    }
}
