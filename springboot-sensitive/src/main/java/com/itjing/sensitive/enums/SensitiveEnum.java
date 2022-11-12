package com.itjing.sensitive.enums;

import com.itjing.sensitive.handler.SensitiveOperation;
import org.apache.commons.lang3.StringUtils;


/**
 * @Description: 脱敏枚举相关，可以列举常用脱敏规则
 * @Author: lijing
 * @CreateTime: 2022-11-03 11:02
 */
public enum SensitiveEnum {

    /**
     * 脱敏转换器
     */
    NO_MASK((str, maskChar) -> str),
    ALL_MASK((str, maskChar) -> {
        if (StringUtils.isNotEmpty(str)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                sb.append(maskChar);
            }
            return sb.toString();
        } else {
            return str;
        }
    });

    private final SensitiveOperation operation;

    private SensitiveEnum(SensitiveOperation operation) {
        this.operation = operation;
    }

    public SensitiveOperation operation() {
        return this.operation;
    }

}
