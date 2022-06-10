package com.itjing.log.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lijing
 * @date 2022年06月02日 14:42
 * @description 日志配置
 */
@Component
@ConfigurationProperties("aspectlog")
public class AspectLogProperties {

    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}