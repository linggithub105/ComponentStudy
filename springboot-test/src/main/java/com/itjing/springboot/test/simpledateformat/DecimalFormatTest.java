package com.itjing.springboot.test.simpledateformat;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @author lijing
 * @date 2022年07月30日 9:38
 * @description
 */
public class DecimalFormatTest {

    /**
     * 以0补位时
     * <p>
     * - 如果数字少了，就会补“0”，小数和整数都会补；
     * - 如果数字多了，就切掉，但只切小数的末尾，整数不能切；
     * - 同时被切掉的小数位会进行四舍五入处理。
     * <p>
     * 以#补位时
     * - 如果数字少了，则不处理，不会补“0”，也不会补“#”；
     * - 如果数字多了，就切掉，但只切小数的末尾，整数不能切；
     * - 同时被切掉的小数位会进行四舍五入处理。
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("DecimalFormat 中 0 和 # 补位的区别：");
        System.out.println("0 补位：");
        // 0: 比实际数字的位数多，不足的地方用0补上。
        double a = 3.1415926535898;
        String format1 = new DecimalFormat("00.00").format(a);//结果：03.14
        String format2 = new DecimalFormat("0.000").format(a);//结果：3.142
        String format3 = new DecimalFormat("00.000").format(a);//结果：03.142
        System.out.println("0补位-比实际数字的位数多[原数：3.1415926535898, 格式：00.00]：" + format1);
        System.out.println("0补位-比实际数字的位数多[原数：3.1415926535898, 格式：0.000]：" + format2);
        System.out.println("0补位-比实际数字的位数多[原数：3.1415926535898, 格式：00.000]：" + format3);
        // 0: 比实际数字的位数少：整数部分不改动，小数部分，四舍五入
        String format4 = new DecimalFormat("0.000").format(13.146);//结果：13.146
        String format5 = new DecimalFormat("00.00").format(13.146);//结果：13.15
        String format6 = new DecimalFormat("0.00").format(13.146);//结果：13.15
        System.out.println("0补位-比实际数字的位数少[原数：13.146, 格式：0.000]：" + format4);
        System.out.println("0补位-比实际数字的位数少[原数：13.146, 格式：00.00]：" + format5);
        System.out.println("0补位-比实际数字的位数少[原数：13.146, 格式：0.00]：" + format6);


        System.out.println("# 补位：");
        // #: 比实际数字的位数多，不变。
        String format7 = new DecimalFormat("##.##").format(a);//结果：03.14
        String format8 = new DecimalFormat("#.###").format(a);//结果：3.142
        String format9 = new DecimalFormat("##.###").format(a);//结果：3.142
        System.out.println("#补位-比实际数字的位数多[原数：3.1415926535898, 格式：##.##]，" + "结果：" + format7);
        System.out.println("#补位-比实际数字的位数多[原数：3.1415926535898, 格式：#.###]，" + "结果：" + format8);
        System.out.println("#补位-比实际数字的位数多[原数：3.1415926535898, 格式：##.###]，" + "结果：" + format9);

        // #:  比实际数字的位数少：整数部分不改动，小数部分，四舍五入
        String format10 = new DecimalFormat("#.###").format(13.146);//结果：13.146
        String format11 = new DecimalFormat("##.##").format(13.146);//结果：13.15
        String format12 = new DecimalFormat("#.##").format(13.146);//结果：13.15
        System.out.println("#补位-比实际数字的位数少[原数：13.146, 格式：#.###]，" + "结果：" + format10);
        System.out.println("#补位-比实际数字的位数少[原数：13.146, 格式：##.##]，" + "结果：" + format11);
        System.out.println("#补位-比实际数字的位数少[原数：13.146, 格式：#.##]，" + "结果：" + format12);

        System.out.println("DecimalFormat 其他用法：");
        DecimalFormat format13 = new DecimalFormat("###,###.0000"); // 使用系统默认的格式
        System.out.println("使用系统默认的格式：" + format13.format(111111123456.12));

        Locale.setDefault(Locale.US);
        DecimalFormat format14 = new DecimalFormat("###,###.0000"); // 使用美国的格式
        System.out.println("使用美国的格式：" + format14.format(111111123456.12));

        DecimalFormat format15 = new DecimalFormat();
        format15.applyPattern("##,###.000");
        System.out.println("##,###.000：" + format15.format(11112345.12345));

        DecimalFormat format16 = new DecimalFormat();
        format16.applyPattern("0.000E0000");
        System.out.println("控制指数输出：" + format16.format(10000));
        System.out.println("控制指数输出：" + format16.format(12345678.345));

        /** DecimalFormat是NumberFormat的一个子类,其实例被指定为特定的地区。
         * 因此，你可以使用NumberFormat.getInstance 指定一个地区，
         * 然后将结构强制转换为一个DecimalFormat对象。
         * 文档中提到这个技术可以在大多情况下适用，但是你需要用try/catch 块包围强制转换以防转换不能正常工作
         * (大概在非常不明显得情况下使用一个奇异的地区)。
         */
        DecimalFormat percentInstance = (DecimalFormat) NumberFormat.getPercentInstance();
        percentInstance.applyPattern("00.0000%");
        System.out.println("百分数：" + percentInstance.format(0.34567));
        System.out.println("百分数：" + percentInstance.format(1.34567));
    }
}
