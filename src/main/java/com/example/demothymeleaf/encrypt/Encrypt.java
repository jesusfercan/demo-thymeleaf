package com.example.demothymeleaf.encrypt;

import jakarta.persistence.AttributeConverter;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.hibernate5.encryptor.HibernatePBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

public class Encrypt implements AttributeConverter<String,String> {

    @Autowired
    private HibernatePBEStringEncryptor encryptor;

    @Override
    public String convertToDatabaseColumn(String s) {
        return StringUtils.isNotBlank(s) ? encryptor.encrypt(s) : s;
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return StringUtils.isNotBlank(s) ? encryptor.decrypt(s) : s;
    }
}
