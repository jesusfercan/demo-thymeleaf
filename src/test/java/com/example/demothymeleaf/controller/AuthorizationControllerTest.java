package com.example.demothymeleaf.controller;

import com.example.demothymeleaf.entity.Associate;
import com.example.demothymeleaf.service.AssociateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
