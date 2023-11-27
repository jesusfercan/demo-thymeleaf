package com.example.demothymeleaf.controller;

import com.example.demothymeleaf.dto.AssociateDto;
import com.example.demothymeleaf.entity.Associate;
import com.example.demothymeleaf.security.CustomSecurityConfig;
import com.example.demothymeleaf.service.AssociateService;
import com.example.demothymeleaf.service.RecaptchaService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static com.example.demothymeleaf.controller.AssociateController.ASSOCIATE_FORM_VIEW;
import static com.example.demothymeleaf.controller.AssociateController.ASSOCIATE_LIST_VIEW;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AssociateController.class)
@Import({CustomSecurityConfig.class, RecaptchaService.class}) // to configure own spring security in test
public class AssociateControllerTest {

    @MockBean
    private AssociateService service;

    @Autowired
    private MockMvc mvc;

    private static Associate associate;
    private static List<Associate> allAssociates;

    @BeforeAll
    public static void setup(){
        associate = Associate.builder()
                .id(1L)
                .name("nombre")
                .surname("apellidos")
                .email("email").build();

        allAssociates = Collections.singletonList(associate);
    }

    @Test
    public void testFindAllAssociatesWithoutLoginRedirectToLoginForm()
            throws Exception {

        when(service.getAllAssociates()).thenReturn(allAssociates);

        mvc.perform(get("/associate"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/login"));

        verify(service, times(0)).getAllAssociates();
    }


    @Test
    public void testFindAllAssociatesWithPostProccesor()
            throws Exception {

        when(service.getAllAssociates()).thenReturn(allAssociates);

        mvc.perform(get("/associate")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user","1234")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/auth/login"));
        ;
        verify(service, times(0)).getAllAssociates();
    }

    @Test
    @WithMockUser(username="user")
    public void testFindAllAssociatesWithLoggedUser()
            throws Exception {

        List<Associate> allAssociates = Collections.singletonList(associate);

        when(service.getAllAssociates()).thenReturn(allAssociates);

        mvc.perform(get("/associate"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSOCIATE_LIST_VIEW))
                .andExpect(model().attribute("associateList",allAssociates));

        verify(service, times(1)).getAllAssociates();
    }

    @Test
    @WithMockUser(username="user")
    public void testCreateAssociateForm()
            throws Exception {

        mvc.perform(get("/associate/create"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSOCIATE_FORM_VIEW))
                .andExpect(model().attribute("associate",new AssociateDto()));

    }

    @Test
    @WithMockUser(username="user")
    void testFailSaveAssociateEmptyValues() throws Exception {

        AssociateDto associateDto = new AssociateDto();

        when(service.createAssociate(any())).thenReturn(associate);

        mvc.perform(multipart("/associate"))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSOCIATE_FORM_VIEW))
                .andExpect(model().attribute("associate",associateDto))
                .andExpect(model().errorCount(3))
                .andExpect(model().attributeHasFieldErrors("associate","name"))
                .andExpect(model().attributeHasFieldErrors("associate","surname"))
                .andExpect(model().attributeHasFieldErrors("associate","email"));

        verify(service,times(0)).createAssociate(associateDto);
    }
    @Test
    @WithMockUser(username="user")
    void testSaveAssociate() throws Exception {

        when(service.createAssociate(any())).thenReturn(associate);

        associate.setEmail("email@emai.com");
        AssociateDto associateDto = new AssociateDto(associate);

        mvc.perform(multipart("/associate")
                        .file("image","content".getBytes())
                        .param("name",associateDto.getName())
                        .param("surname",associateDto.getSurname())
                        .param("email",associateDto.getEmail())
                        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/associate/"+associate.getId()+"?created"));

        verify(service,times(1)).createAssociate(associateDto);
    }


    @Test
    @WithMockUser(username="user")
    void testFindAssociateById() throws Exception {
        when(service.getAssociateById(1L)).thenReturn(associate);

        mvc.perform(get("/associate/"+associate.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(AssociateController.ASSOCIATE_FORM_VIEW))
                .andExpect(model().attribute("associate",associate));

        verify(service,times(1)).getAssociateById(associate.getId());
    }

    @Test
    @WithMockUser(username="user")
    void testFindAssociateByIdNotFound() throws Exception {
        when(service.getAssociateById(associate.getId()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"User with id "+associate.getId()+" not found!"));

        mvc.perform(get("/associate/"+associate.getId()))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("errorCode", HttpStatus.NOT_FOUND.value()))
                .andExpect(model().attribute("url", "/associate"))
                .andExpect(model().attribute("errorMessage", containsString("404")));

        verify(service,times(1)).getAssociateById(associate.getId());
    }

    @Test
    @WithMockUser(username="user")
    void testFailUpdateAssociateWithoutFields() throws Exception {


        AssociateDto associateDto = new AssociateDto();

        when(service.updateAssociate(associate.getId(),associateDto)).thenReturn(associate);

        mvc.perform(multipart("/associate/"+associate.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSOCIATE_FORM_VIEW))
                .andExpect(model().attribute("associate",associateDto))
                .andExpect(model().errorCount(3))
                .andExpect(model().attributeHasFieldErrors("associate","name"))
                .andExpect(model().attributeHasFieldErrors("associate","surname"))
                .andExpect(model().attributeHasFieldErrors("associate","email"));

        verify(service,times(0)).updateAssociate(associate.getId(),associateDto);

    }

    @Test
    @WithMockUser(username="user")
    void testFailUpdateAssociateEmptyFields() throws Exception {

        AssociateDto associateDto = new AssociateDto("","","");

        when(service.updateAssociate(associate.getId(),associateDto)).thenReturn(associate);

        mvc.perform(multipart("/associate/"+associate.getId())
                        .param("name","")
                        .param("surname","")
                        .param("email",""))
                .andExpect(status().isOk())
                .andExpect(view().name(ASSOCIATE_FORM_VIEW))
                .andExpect(model().attribute("associate",associateDto))
                .andExpect(model().errorCount(3))
                .andExpect(model().attributeHasFieldErrors("associate","name"))
                .andExpect(model().attributeHasFieldErrors("associate","surname"))
                .andExpect(model().attributeHasFieldErrors("associate","email"));

        verify(service,times(0)).updateAssociate(associate.getId(),associateDto);

    }

    @Test
    @WithMockUser(username="user")
    void testUpdateAssociate() throws Exception {

        associate.setEmail("email@emai.com");
        AssociateDto associateDto = new AssociateDto(associate);

        when(service.updateAssociate(associate.getId(),associateDto)).thenReturn(associate);


        mvc.perform(multipart("/associate/"+associate.getId())
                        .file("image","content".getBytes())
                        .param("name",associateDto.getName())
                        .param("surname",associateDto.getSurname())
                        .param("email",associateDto.getEmail())
                        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/associate/"+associate.getId()+"?updated"));

        verify(service,times(1)).updateAssociate(associate.getId(),associateDto);

    }

    @Test
    @WithMockUser(username="user")
    void testDeleteAssociateWrongAssociateId() throws Exception {

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"User with id "+associate.getId()+" not found!"))
                .when(service).deleteAssociate(associate.getId());

        mvc.perform(delete("/associate/"+associate.getId()))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string(containsString("Error")));

        verify(service,times(1)).deleteAssociate(associate.getId());

    }
    @Test
    @WithMockUser(username="user")
    void testDeleteAssociateId() throws Exception {

        doNothing().when(service).deleteAssociate(associate.getId());

        mvc.perform(delete("/associate/"+associate.getId()))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Eliminado"));

        verify(service,times(1)).deleteAssociate(associate.getId());

    }
}
