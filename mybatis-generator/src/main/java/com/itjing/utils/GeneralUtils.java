package com.itjing.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lijing
 * @date 2022年06月26日 9:55
 * @description 自制工具类
 */
@Slf4j
public class GeneralUtils {

    // 校验时间的正则表达式
    // 格式：yyyyMMdd
    private static final String CHECK_DATE_FORMAT_REGEX_1 = "^((19|20)[0-9]{2})(((0[13578]|10|12)(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|3[0]))|((02)(0[1-9]|[12][0-9])))$";
    // 格式：yyyy-MM-dd
    private static final String CHECK_DATE_FORMAT_REGEX_2 = "^((19|20)[0-9]{2})-(((0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|3[0]))|((02)-(0[1-9]|[12][0-9])))$";
    // 格式：yyyyMMddHHmmss
    private static final String CHECK_DATE_FORMAT_REGEX_3 = "^((19|20)[0-9]{2})(((0[13578]|10|12)(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|3[0]))|((02)(0[1-9]|[12][0-9])))([01][0-9]|2[0-3])([0-5][0-9])([0-5][0-9])$";
    // 格式：yyyy-MM-dd HH:mm:ss
    private static final String CHECK_DATE_FORMAT_REGEX_4 = "^((19|20)[0-9]{2})-(((0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|3[0]))|((02)-(0[1-9]|[12][0-9]))) ([01][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

    /**
     * 得到项目根路径
     *
     * @param request
     * @return
     */
    public static String getServerPath(HttpServletRequest request) {
        return GeneralUtils.getServerPath("", request);
    }

    /**
     * 得到文件在项目中的完整的路径
     *
     * @param servletPath
     * @return
     */
    public static String getServerPath(String servletPath, HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/" + servletPath;
    }

    /**
     * 返回json格式的字符串
     *
     * @param status
     * @param msg
     * @param data
     * @return
     */
    public static String returnJson(int status, String msg, String data) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", status);
        resultMap.put("msg", msg);
        resultMap.put("data", data);
        return JSON.toJSONString(resultMap);
    }

    /**
     * 便捷将Map集合中的数据封装到JavaBean中，字段必须对应！！！
     *
     * @param map
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> c) {
        try {
            // 创建字节码文件的一个实例
            T bean = c.newInstance();
            // 利用BeanUtils工具类将map集合中的数据封装到bean实例中
            BeanUtils.populate(bean, map);
            // 返回实例
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 得到HttpServletRequest类型的request中的请求参数，
     * 并存储为Map<String, Object>类型，返回结果集
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getParameterMap(ServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        // 得到存储有所有普通表单项的请求参数Map集合
        Map<String, String[]> paramsMap = request.getParameterMap();

        // 得到存储有普通表单项的请求参数的Set集合
        Set<Map.Entry<String, String[]>> paramsSet = paramsMap.entrySet();
        // 循环遍历得到每一个Entry
        for (Map.Entry<String, String[]> entry : paramsSet) {
            // 得到请求参数的参数名称
            String paramKey = entry.getKey();
            // 得到请求参数的参数值(String数组)
            String[] paramValues = entry.getValue();
            /*
                创建字符串缓冲区
                注：StringBuffer与StringBuilder功能和方法相同
                    1. StringBuffer是线程安全的，但效率相对较慢；
                    2. StringBuilder是线程不安全的，但效率相对较快。
             */
            StringBuilder sb = new StringBuilder();
            // 判断请求参数的参数值，即String数组长度是否等于1
            if (paramValues.length == 1) {
                // 保存到字符串缓冲区
                sb.append(paramValues[0]);
            } else {
                // 循环遍历
                for (int i = 0; i < paramValues.length; i++) {
                    // 保存到字符串缓冲区
                    sb.append(paramValues[i]);
                    // 如果当前循环遍历的索引号不等于数组长度-1
                    if (i != (paramValues.length - 1)) {
                        // 则说明不是最后一个参数值，用逗号隔开
                        sb.append(",");
                    }
                }
            }
            // 调用StringBuffer的toString()方法得到一串字符串
            String paramValue = sb.toString();
            // 用请求参数名称为key，请求参数的值为value保存到Map中
            resultMap.put(paramKey, paramValue);
        }

        return resultMap;
    }


    /**
     * 将Map集合中的数据保存到.properties属性文件中
     *
     * @param propertyMap        存储有属性的Map集合
     * @param propertiesFileName 属性文件的名称，不用带后缀
     * @return Map<String, Object>  存储错误信息或成功信息
     * mark标记：boolean类型，true表成功，false表失败
     * errorMsg：当且仅当mark为false时有错误信息
     * @throws Exception
     */
    public static Map<String, Object> savePropertyToPropertiesFile(Map<String, Object> propertyMap, String propertiesFileName) throws Exception {
        // 创建Map集合用于存储成功信息或错误信息
        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 判断属性Map集合是否为null
        if (propertyMap == null || propertyMap.size() <= 0) {
            log.info("GeneralUtils.java ->> savePropertyToPropertiesFile() ->> 错误操作：propertyMap属性集合不能为空！");
            resultMap.put("mark", false);
            resultMap.put("errorMsg", "propertyMap属性集合不能为空！");
            return resultMap;
        }

        // 如果属性文件名称是否为空
        if (GeneralUtils.isEmpty(propertiesFileName)) {
            log.info("GeneralUtils.java ->> savePropertyToPropertiesFile() ->> 错误操作：参数propertiesFileName不能为空！");
            resultMap.put("mark", false);
            resultMap.put("errorMsg", "参数propertiesFileName不能为空！");
            return resultMap;
        }

        // 得到类路径的URL
        URL propertiesFileUrl = GeneralUtils.class.getClassLoader().getResource("");

        if (propertiesFileUrl == null) {
            log.info("GeneralUtils.java ->> savePropertyToPropertiesFile() ->> 错误操作：获取类路径的URL对象异常！");
            resultMap.put("mark", false);
            resultMap.put("errorMsg", "错误操作：获取类路径的URL对象异常！");
            return resultMap;
        }
        // 得到类路径的绝对路径
        String classesPath = propertiesFileUrl.getPath();

        // 得到类路径下指定属性文件的绝对路径
        String propertiesFileAbsolutePath = classesPath + propertiesFileName + ".properties";

        // 得到属性文件的File对象
        File propsFile = new File(propertiesFileAbsolutePath);
        // 如果对应路径的文件不存在
        if (!propsFile.exists()) {
            // 得到文件的目录路径
            String directory = propertiesFileAbsolutePath.substring(0, propertiesFileAbsolutePath.lastIndexOf("/"));
            // 创建文件目录的File对象
            File directoryFile = new File(directory);
            // 如果目录不存在
            if (!directoryFile.exists()) {
                // 创建目录
                directoryFile.mkdirs();
            }

            // 创建文件
            propsFile.createNewFile();
        }

        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            String charset = "UTF-8";
            // 得到属性文件的资源输入流
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(propsFile), charset));
            // 创建Properties集合用于读取资源输入流中的属性
            Properties props = new Properties();
            // 加载资源输入流中的属性
            props.load(reader);

            // 得到要添加属性到属性文件中的的Set集合
            Set<Map.Entry<String, Object>> set = propertyMap.entrySet();
            // 循环遍历
            for (Map.Entry<String, Object> entry : set) {
                // 得到属性名称
                String key = entry.getKey();
                // 得到属性值
                String value = entry.getValue().toString();

                // 设置属性
                // 如果原先有相同属性名称的属性，旧值会被新值覆盖
                props.setProperty(key, value);
            }
            // 得到属性文件的资源输出流
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(propsFile)));

            // 将集合中的属性写入到属性文件中
            props.store(writer, "");

        } catch (Exception e) {
            log.info("GeneralUtils.java ->> savePropertyToPropertiesFile() ->> 异常信息：" + e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        resultMap.put("mark", true);
        return resultMap;
    }

    /**
     * 读取类路径下属性文件中所有的属性
     *
     * @param propertyNameList   属性文件中的属性名称
     * @param propertiesFileName 属性文件的名称，必须带后缀
     * @return Map<String, Object>  存储错误信息或成功信息 或 存储属性文件中所有属性名称及属性值
     * *          mark标记：boolean类型，true表成功，false表失败
     * *          errorMsg：当且仅当mark为false时有错误信息
     * @throws Exception
     */
    public static Map<String, Object> readPropertyOfPropertiesFile(List<String> propertyNameList, String propertiesFileName) throws Exception {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 判断要获取的属性值的属性名称集合是否为空
        if (propertyNameList == null || propertyNameList.size() <= 0) {
            log.info("GeneralUtils.java ->> readPropertyOfPropertiesFile() ->> 错误操作：存储有要从属性文件中读取属性值的属性名称List集合不能为空！");
            resultMap.put("mark", false);
            resultMap.put("errorMsg", "错误操作：存储有要从属性文件中读取属性值的属性名称List集合不能为空！");
            return resultMap;
        }

        // 判断指定的文件名称
        if (GeneralUtils.isEmpty(propertiesFileName)) {
            log.info("GeneralUtils.java ->> readPropertyOfPropertiesFile() ->> 错误操作：属性文件的名称不能为空！");
            resultMap.put("mark", false);
            resultMap.put("errorMsg", "错误操作：属性文件的名称不能为空！");
            return resultMap;
        }

        int index = propertiesFileName.lastIndexOf(".");
        if (index < 0) {
            log.info("GeneralUtils.java ->> readPropertyOfPropertiesFile() ->> 错误操作：参数propertiesFileName指定的值（" + propertiesFileName + "）不是文件类型！");
            resultMap.put("mark", false);
            resultMap.put("errorMsg", "错误操作：参数propertiesFileName指定的值（" + propertiesFileName + "）不是文件类型！");
            return resultMap;
        } else {
            String suffix = propertiesFileName.substring(index);
            if (!".properties".equals(suffix)) {
                log.info("GeneralUtils.java ->> readPropertyOfPropertiesFile() ->> 错误操作：参数propertiesFileName指定的值（" + propertiesFileName + "）只能.properties文件后缀！");
                resultMap.put("mark", false);
                resultMap.put("errorMsg", "错误操作：参数propertiesFileName指定的值（" + propertiesFileName + "）只能.properties文件后缀！");
                return resultMap;
            }
        }

        URL propertiesFileUrl = GeneralUtils.class.getClassLoader().getResource(propertiesFileName);
        if (propertiesFileUrl == null) {
            log.info("GeneralUtils.java ->> readPropertyOfPropertiesFile() ->> 错误操作：指定名称（" + propertiesFileName + "）的属性文件在类路径下不存在！");
            resultMap.put("mark", false);
            resultMap.put("errorMsg", "错误操作：指定名称（" + propertiesFileName + "）的属性文件在类路径下不存在！");
            return resultMap;
        }

        String propertiesFileAbsolutePath = propertiesFileUrl.getPath();

        // 得到文件的File对象
        File propsFile = new File(propertiesFileAbsolutePath);

        BufferedReader reader = null;
        try {
            String charset = "UTF-8";
            // 创建资源输入流
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(propsFile), charset));
            // 创建集合加载属性文件中的属性
            Properties props = new Properties();
            // 加载
            props.load(reader);
            // 如果长度大于0
            if (props.size() > 0) {
                // 循环遍历要获取的属性值的属性名称集合
                for (String propertyName : propertyNameList) {
                    // 得到每一个属性的值
                    String propertyValue = props.getProperty(propertyName);
                    // 保存到Map集合中
                    resultMap.put(propertyName, propertyValue);
                }
            }

        } catch (Exception e) {
            log.info("GeneralUtils.java ->> readPropertyOfPropertiesFile() ->> 异常信息：" + e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        resultMap.put("mark", true);
        return resultMap;
    }

    /**
     * 得到某个属性文件在classes目录下的绝对路径
     *
     * @param propertiesFileName
     * @return
     * @throws Exception
     */
    public static String getAbsolutePath(String propertiesFileName) throws Exception {

        if (GeneralUtils.isEmpty(propertiesFileName)) {
            log.info("GeneralUtils.java ->> getAbsolutePath() ->> 得到属性文件的绝对路径出错：参数propertiesFileName不能为空！");
            throw new RuntimeException("得到属性文件的绝对路径出错：参数propertiesFileName不能为空！");
        }

        URL url = GeneralUtils.class.getClassLoader().getResource(propertiesFileName);

        if (url == null) {
            log.info("GeneralUtils.java ->> getAbsolutePath() ->> 得到属性文件的绝对路径出错：文件不存在");
            throw new RuntimeException("得到属性文件的绝对路径出错：文件不存在");
        }

        return url.getPath();
    }

    /**
     * 生成指定长度的随机字符码
     *
     * @param length
     * @return
     */
    public static String createRandomCode(int length) {
        // 随机字符组成的字符串
        String base = "0123456789abcdefghijklmnopqrstuvwxyz";
        // 用于产生随机字符的对象
        Random random = new Random();
        // 字符串缓冲区
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            // 从随机字符串中得到某一个字符的下标
            int index = random.nextInt(base.length());
            // 得到下标对应的字符
            char c = base.charAt(index);
            // 保存到字符串缓冲区
            sb.append(c);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 按照yyyy-MM-dd HH:mm:ss的默认格式，将日期格式化为字符串
     *
     * @param date
     * @return
     */
    public static String date2Str(Date date) {
        return date2Str(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 按照参数format的格式，将日期格式化为字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String date2Str(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    /**
     * 得到32位随机字符的ID
     *
     * @return
     */
    public static String get32UUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    /**
     * 判断字符串是否为空
     *
     * @param text
     * @return
     */
    public static boolean isEmpty(String text) {
        return text == null || "".equals(text) || text.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空
     *
     * @param text
     * @return
     */
    public static boolean notEmpty(String text) {
        return text != null && !"".equals(text) && !text.trim().isEmpty();
    }


    /**
     * 除法运算得到百分比
     *
     * @param diviend        被除数
     * @param divisor        除数
     * @param reservedDigits 保留位数
     * @return
     */
    public static String divideGetPercentage(int diviend, int divisor, int reservedDigits) {
        // 将被除数转为BigDecimal类型
        BigDecimal diviend_bigDecimal = new BigDecimal(diviend);
        // 将除数转为BigDecimal类型
        BigDecimal divisor_bigDecimal = new BigDecimal(divisor);
        // 执行除法运算，由被除数调用divide()方法，方法中第一个参数为除数
        BigDecimal remainder_bigDecimal = diviend_bigDecimal.divide(divisor_bigDecimal, 4, BigDecimal.ROUND_HALF_UP);
        // 将余数乘以100，再除以1，格式化保留位数
        BigDecimal val = remainder_bigDecimal.multiply(new BigDecimal(100)).divide(new BigDecimal(1), reservedDigits, BigDecimal.ROUND_HALF_UP);
        // 得到转为字符串拼接%符号百分比
        return val.toString() + "%";
    }

    /**
     * 批量校验参数是否为空，若其中一个为空，则直接返回
     *
     * @param parametersMap
     * @return
     * @throws Exception
     */
    public static Map<String, Object> batchCheckParameterIsEmpty(Map<String, Object> parametersMap) throws Exception {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (parametersMap == null) {
            log.info("GeneralUtils.java ->> batchCheckParameterIsEmpty() ->> 批量校验参数是否为空报错：参数Map集合不能为空！");
            resultMap.put("mark", false);
            resultMap.put("errorMsg", "批量校验参数是否为空报错：参数Map集合不能为空！");
            return resultMap;
        }

        Set<Map.Entry<String, Object>> set = parametersMap.entrySet();

        for (Map.Entry<String, Object> entry : set) {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            if (GeneralUtils.isEmpty(value)) {
                log.info("GeneralUtils.java ->> batchCheckParameterIsEmpty() ->> 批量校验参数是否为空报错：参数名称为" + key + "的参数值不能为空！");
                resultMap.put("mark", false);
                resultMap.put("errorMsg", "批量校验参数是否为空报错：参数名称为" + key + "的参数值不能为空！");
                return resultMap;
            }
        }

        resultMap.put("mark", true);
        resultMap.put("errorMsg", "");

        return resultMap;
    }

    /**
     * 校验日期字符串格式
     *
     * @param date
     * @param format 只允许为：yyyyMMdd 或 yyyy-MM-dd 或 yyyyMMddHHmmss 或 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static boolean checkDateFormat(String date, String format) {

        if (GeneralUtils.isEmpty(date)) {
            log.info("GeneralUtils.java ->> checkDateFormat() ->> 错误操作：校验日期字符串格式方法的参数date不允许为空！");
            throw new RuntimeException("错误操作：校验日期字符串格式方法的参数date不允许为空！");
        }

        if (GeneralUtils.isEmpty(format)) {
            log.info("GeneralUtils.java ->> checkDateFormat() ->> 错误操作：校验日期字符串格式方法的参数format不允许为空！");
            throw new RuntimeException("错误操作：校验日期字符串格式方法的参数format不允许为空！");
        } else if (!"yyyyMMdd".equals(format) && !"yyyy-MM-dd".equals(format)
                && !"yyyyMMddHHmmss".equals(format) && !"yyyy-MM-dd HH:mm:ss".equals(format)) {
            log.info("GeneralUtils.java ->> checkDateFormat() ->> 错误操作：校验日期字符串格式方法的参数format指定的校验格式错误（只允许yyyyMMdd或yyyy-MM-dd或yyyyMMddHHmmss或yyyy-MM-dd HH:mm:ss）");
            throw new RuntimeException("错误操作：校验日期字符串格式方法的参数format指定的校验格式错误（只允许yyyyMMdd或yyyy-MM-dd或yyyyMMddHHmmss或yyyy-MM-dd HH:mm:ss）");
        }

        String regex = null;
        if ("yyyyMMdd".equals(format)) {
            regex = CHECK_DATE_FORMAT_REGEX_1;
        } else if ("yyyy-MM-dd".equals(format)) {
            regex = CHECK_DATE_FORMAT_REGEX_2;
        } else if ("yyyyMMddHHmmss".equals(format)) {
            regex = CHECK_DATE_FORMAT_REGEX_3;
        } else {
            regex = CHECK_DATE_FORMAT_REGEX_4;
        }

        return date.matches(regex);
    }

    /**
     * 得到XML格式字符串中的所有标签节点数据
     *
     * @param xmlString
     * @return
     * @throws Exception
     */
    public static Map<String, Object> parseXmlStringGetAllElements(String xmlString) throws Exception {
        // 解析Xml格式的字符串得到Dom文档对象
        Document document = DocumentHelper.parseText(xmlString);
        // 得到根节点
        Element rootElement = document.getRootElement();
        // 递归得到所有的标签节点数据
        return recursiveGetElement(rootElement);
    }

    /**
     * 递归获取节点中的节点名称以及节点的文本内容
     *
     * @param node
     * @return
     */
    public static Map<String, Object> recursiveGetElement(Node node) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        Element element = (Element) node;
        List<Element> childElementList = element.elements();
        if (childElementList.size() > 0) {
            resultMap.put(element.getName(), "");
            for (Element childElement : childElementList) {
                resultMap.putAll(recursiveGetElement(childElement));
            }
        } else {
            resultMap.put(element.getName(), element.getText());
        }

        return resultMap;
    }


    /**
     * 选择排序
     *
     * @param list
     */
    public static void selectSort(List<Integer> list) {

        if (list == null) {
            throw new RuntimeException("list不能为null");
        }

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) > list.get(j)) {
                    int temp = list.get(j);
                    list.set(j, list.get(i));
                    list.set(i, temp);
                }
            }
        }

    }

    /**
     * 冒泡排序
     *
     * @param list
     */
    public static void bubbleSort(List<Integer> list) {

        if (list == null) {
            throw new RuntimeException("list不能为null");
        }

        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    int temp = list.get(j + 1);
                    list.set(j + 1, list.get(j));
                    list.set(j, temp);
                }
            }
        }
    }

}
