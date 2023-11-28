package com.example.demothymeleaf.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.IvGenerator;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.util.text.AES256TextEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;

public class UtilsTest {

    private final static String KEY = "secretKeyToEncriptProperties";
    private final static String DECRYPT = "buxADqdNKVypTgb5FismlhqP9kUVrZPvLVbZPxUENGfxBDuzjEHRKQvYU53FBYz/bzbzuQN5a3ePzdanW8CR1gBm89Gekb0cJuqTKgKC7IA=";
    private final static String ENCRIPT_TEXT = "1234jesus";

    @Test
    void generateJasyptEncript(){
        AES256TextEncryptor  encryptor = new AES256TextEncryptor();
        AES256TextEncryptor  encryptor2 = new AES256TextEncryptor();
        encryptor.setPassword(KEY);
        encryptor2.setPassword(KEY);

        String textEncripted = encryptor.encrypt(ENCRIPT_TEXT);
        String textDencripted = encryptor2.decrypt(DECRYPT);

        System.out.println("textEncripted: "+ textEncripted);
        System.out.println("textDencripted: "+ textDencripted);
    }

}
