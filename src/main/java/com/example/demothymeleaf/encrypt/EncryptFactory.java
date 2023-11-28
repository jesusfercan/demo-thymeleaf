package com.example.demothymeleaf.encrypt;

import org.jasypt.hibernate5.encryptor.HibernatePBEStringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EncryptFactory {

    @Bean
    HibernatePBEStringEncryptor encryptorFactory(){
        HibernatePBEStringEncryptor encryptor = new HibernatePBEStringEncryptor();
        String password = System.getenv("JASYPT_ENCRYPTOR_PASSWORD");
        encryptor.setPassword(password);
        return encryptor;
    }
}
