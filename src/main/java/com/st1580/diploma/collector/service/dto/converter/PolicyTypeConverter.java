package com.st1580.diploma.collector.service.dto.converter;

import com.st1580.diploma.collector.service.dto.PolicyType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PolicyTypeConverter implements Converter<String, PolicyType> {
    @Override
    public PolicyType convert(String source) {
        try {
            return PolicyType.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PolicyType.START;
        }
    }
}
