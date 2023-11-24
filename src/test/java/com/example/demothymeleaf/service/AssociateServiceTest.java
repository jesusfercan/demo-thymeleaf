package com.example.demothymeleaf.service;

import com.example.demothymeleaf.entity.Associate;
import com.example.demothymeleaf.repository.AssociateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//@ExtendWith(SpringExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AssociateServiceTest {

    @Autowired
    private AssociateService service;

    @MockBean
    private AssociateRepository repository;

    @Test
    void testFindAssociateByIdNoResultFound(){
        Long associateId = 999L;

        when(repository.findById(associateId)).thenThrow(ResponseStatusException.class);

        assertThrows(ResponseStatusException.class, () -> service.getAssociateById(associateId));
        verify(repository,times(1)).findById(associateId);
    }

    @Test
    void testFindAssociateByIdNoResultFoundVerifyHttpStatusCode(){
        Long associateId = 999L;

        when(repository.findById(associateId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"User with id "+associateId+" not found!"));

        try {
            service.getAssociateById(associateId);
        }catch (ResponseStatusException ex){
            assertEquals(ex.getStatusCode(), HttpStatus.NOT_FOUND);
        }
        verify(repository,times(1)).findById(associateId);
    }

    @Test
    @DisplayName("Test findAllAssociates Success")
    void testFindAllAssociates() {

        Associate associate = Associate.builder()
                .id(1L)
                .name("nombre")
                .email("email").build();
        List<Associate> associates = Collections.singletonList(associate);
        when(repository.findAll()).thenReturn(associates);

        List<Associate> expected = service.getAllAssociates();
        assertEquals(expected.size(),associates.size());
        verify(repository,times(1)).findAll();

    }
}
