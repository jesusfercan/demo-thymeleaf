package com.example.demothymeleaf.controller;

import static org.assertj.core.api.Assertions.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demothymeleaf.entity.Associate;
import com.example.demothymeleaf.service.AssociateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthorizationController.class)
@WithMockUser(username="user")
public class AuthorizationControllerTest {

    @MockBean
    private AssociateService service;

    @Autowired
    private MockMvc mvc;


    @Test
    public void testFindAllAssociates()
            throws Exception {

        Associate associate = Associate.builder()
                .id(1L)
                .name("nombre")
                .email("email").build();

        List<Associate> allEmployees = Collections.singletonList(associate);

        when(service.getAllAssociates()).thenReturn(allEmployees);

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(associate.getName())));
        verify(service, times(1)).getAllAssociates();
    }
}
