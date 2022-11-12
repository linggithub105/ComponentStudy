package com.itjing.sensitive.config;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.itjing.sensitive.handler.SensitiveAnnotationIntrospector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @Description: 脱敏配置
 * @Author: lijing
 * @CreateTime: 2022-11-03 11:13
 */
@Configuration(
        proxyBeanMethods = false
)
public class SensitiveConfig {

    @Configuration(
            proxyBeanMethods = false
    )
    @ConditionalOnClass({Jackson2ObjectMapperBuilder.class})
    static class JacksonObjectMapperConfiguration {

        JacksonObjectMapperConfiguration() {
        }

        @Bean
        @Primary
        ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
            ObjectMapper objectMapper = builder.createXmlMapper(false).build();
            AnnotationIntrospector ai = objectMapper.getSerializationConfig().getAnnotationIntrospector();
            AnnotationIntrospector newAi = AnnotationIntrospectorPair.pair(ai, new SensitiveAnnotationIntrospector());
            objectMapper.setAnnotationIntrospector(newAi);
            return objectMapper;
        }
    }

}
