package com.example.demothymeleaf.controller;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthorizationControllerTest {

    @Autowired
    private AuthorizationController controller;

    @Test
    void contextLoads() throws Exception {
        assertNotNull(controller);
        assertThat(controller).isNotNull();
    }
}
