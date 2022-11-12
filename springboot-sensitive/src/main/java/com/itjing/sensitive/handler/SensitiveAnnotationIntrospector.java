package com.itjing.sensitive.handler;

/**
 * @Description:
 * @Author: lijing
 * @CreateTime: 2022-11-03 11:11
 */

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.itjing.sensitive.annotation.Sensitive;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SensitiveAnnotationIntrospector extends NopAnnotationIntrospector {

    @Override
    public Object findSerializer(Annotated am) {
        Sensitive annotation = am.getAnnotation(Sensitive.class);
        if (annotation != null) {
            return new SensitiveSerializer(annotation.mask().operation(), annotation.maskChar());
        }
        return null;
    }

}

