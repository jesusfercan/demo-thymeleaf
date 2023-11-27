package com.example.demothymeleaf.controller;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.demothymeleaf.security.CustomSecurityConfig;
import com.example.demothymeleaf.service.RecaptchaService;
import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthorizationController.class)
@Import({CustomSecurityConfig.class, RecaptchaService.class}) // to configure own spring security in test
public class AuthorizationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void testloginFormView() throws Exception {

        mvc.perform(get("/auth/login"))
                .andExpect(status().isOk());
    }

    @Test
    void testLoginFormWithViewErrorCode() throws Exception {

        mvc.perform(get("/auth/login?error=1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error1",true));
    }

    @Test
    void testSendLoginFormWithoutParams() throws Exception {

            mvc.perform(post("/auth/checkLogin"))
                    .andExpect(redirectedUrl("/auth/login?error=1"))
                    .andExpect(result ->
                        assertThat(Objects.requireNonNull(result.getRequest().getSession()).getAttribute("SPRING_SECURITY_LAST_EXCEPTION").toString(),
                                StringContains.containsString("Bad credentials"))
                    );
    }

    @Test
    void testSendLoginFormWithIncorectParams() throws Exception {

        mvc.perform(post("/auth/checkLogin")
                .param("username","user")
                .param("password","1111")
                )
                .andExpect(redirectedUrl("/auth/login?error=1"))
                .andExpect(result ->
                    assertThat(Objects.requireNonNull(result.getRequest().getSession()).getAttribute("SPRING_SECURITY_LAST_EXCEPTION").toString(),
                            StringContains.containsString("Bad credentials"))
                );
    }

    @Test
    void testSendLoginFormWithParams() throws Exception {
        UserDetails logedUser = User.withUsername("user").password("1234").build();

        mvc.perform(post("/auth/checkLogin")
                        .param("username",logedUser.getUsername())
                        .param("password", logedUser.getPassword())
                        )
                        .andExpect(redirectedUrl("/"))
                        .andExpect(request().sessionAttribute("SPRING_SECURITY_CONTEXT", Matchers.is(Matchers.notNullValue()) ))
                        .andExpect(result -> {
                            SecurityContextImpl securityContext = (SecurityContextImpl) Objects.requireNonNull(result.getRequest().getSession()).getAttribute("SPRING_SECURITY_CONTEXT");
                            User expectedUser = (User) securityContext.getAuthentication().getPrincipal();
                            assertThat(expectedUser.getUsername(),Matchers.is(logedUser.getUsername()));
                        });
    }

}
