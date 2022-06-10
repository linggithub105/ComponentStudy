package com.itjing.config;

import com.itjing.util.AESUtil;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijing
 * @date 2021年12月20日 10:07
 * @description 配置文件加密解密配置类
 */
@Configuration
public class EncryptionPropertyConfig {

    @Bean(name = "encryptablePropertyResolver")
    public EncryptablePropertyResolver encryptablePropertyResolver() {
        return new EncryptionPropertyResolver();
    }

    class EncryptionPropertyResolver implements EncryptablePropertyResolver {

        public static final String ENCODED_PASSWORD_HINT = "cipher@";
        protected static final String DECODEKEY = "123456";

        // 自定义解密方法
        @Override
        public String resolvePropertyValue(String property) {
            try {
                if (property.startsWith(ENCODED_PASSWORD_HINT)) {
                    return AESUtil.decrypt(property.substring(ENCODED_PASSWORD_HINT.length()), DECODEKEY);
                }
                return property;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

