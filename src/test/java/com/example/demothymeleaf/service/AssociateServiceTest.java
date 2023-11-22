package com.example.demothymeleaf.service;

import com.example.demothymeleaf.entity.Associate;
import com.example.demothymeleaf.repository.AssociateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AssociateServiceTest {

    @Autowired
    private AssociateService service;

    @MockBean
    private AssociateRepository repository;


    @Test
    @DisplayName("Test findAllAssociates Success")
    void testFindAllAssociates() {

        Associate associate = Associate.builder()
                .id(1L)
                .name("nombre")
                .email("email").build();
        List<Associate> associates = Collections.singletonList(associate);
        /*Mockito.when(repository.findAll()).thenReturn(associates);

        List<Associate> expected = service.getAllAssociates();
        Assertions.assertEquals(expected.size(),associates.size());
        Mockito.verify(repository,Mockito.times(1)).findAll();*/
    }
}
