package com.itjing.excel.handler;

import com.alibaba.excel.util.StringUtils;
import com.itjing.excel.annotation.ExcelValid;
import com.itjing.excel.exception.CustomException;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Description: Excel导入字段校验
 * @Author: lijing
 * @CreateTime: 2022-09-07 14:43
 */
public class ExcelImportValidHandler {

    /**
     * Excel导入字段校验
     *
     * @param object 校验的JavaBean 其属性须有自定义注解
     */
    public static void valid(Object object) throws CustomException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 设置可访问
            field.setAccessible(true);
            // 属性的值
            Object fieldValue;
            try {
                fieldValue = field.get(object);
            } catch (IllegalAccessException e) {
                throw new CustomException("excel导入参数检查失败，没有访问权限！");
            }
            // 是否包含必填校验注解
            boolean isExcelValid = field.isAnnotationPresent(ExcelValid.class);
            if (isExcelValid) {
                if (Objects.equals(field.getGenericType().toString(), "class java.lang.String")
                        && StringUtils.isEmpty((String) fieldValue)) {
                    throw new CustomException(field.getAnnotation(ExcelValid.class).message());
                } else if (Objects.isNull(fieldValue)) {
                    throw new CustomException(field.getAnnotation(ExcelValid.class).message());
                }
            }
        }
    }

}
