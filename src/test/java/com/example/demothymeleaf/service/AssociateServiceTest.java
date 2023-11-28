package com.example.demothymeleaf.service;

import com.example.demothymeleaf.dto.AssociateDto;
import com.example.demothymeleaf.entity.Associate;
import com.example.demothymeleaf.repository.AssociateRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//@ExtendWith(SpringExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssociateServiceTest {

    @Autowired
    private AssociateService service;

    @MockBean
    private AssociateRepository repository;

    private static Long associateId = 1L;
    private static Associate associate = Associate.builder()
                                        .name("nombre")
                                        .surname("apellidos")
                                        .email("email").build();
    private static List<Associate> associates = Collections.singletonList(associate);

    @Test
    @Order(1)
    void testFindAssociateByIdNoResultFound(){

        when(repository.findById(associateId)).thenThrow(ResponseStatusException.class);

        assertThrows(ResponseStatusException.class, () -> service.getAssociateById(associateId));
        verify(repository,times(1)).findById(associateId);
    }

    @Test
    @Order(2)
    void testFindAssociateByIdNoResultFoundVerifyHttpStatusCode(){

        when(repository.findById(associateId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,"User with id "+associateId+" not found!"));

        try {
            service.getAssociateById(associateId);
        }catch (ResponseStatusException ex){
            assertEquals(ex.getStatusCode(), HttpStatus.NOT_FOUND);
        }
        verify(repository,times(1)).findById(associateId);
    }

    @Test
    @DisplayName("Test testFindAssociateById Success")
    @Order(3)
    void testFindAssociateById(){

        when(repository.findById(associateId))
                .thenReturn(Optional.ofNullable(associate));

        Associate expected = service.getAssociateById(associateId);

        assertEquals(expected.getId(), associate.getId());
        assertEquals(expected.getName(), associate.getName());
        assertEquals(expected.getSurname(), associate.getSurname());
        assertEquals(expected.getEmail(), associate.getEmail());

        verify(repository,times(1)).findById(associateId);
    }

    @Test
    @DisplayName("Test findAllAssociates Success")
    @Order(4)
    void testFindAllAssociates() {

        when(repository.findAll()).thenReturn(associates);

        List<Associate> expected = service.getAllAssociates();
        assertEquals(expected.size(),associates.size());
        assertEquals(expected.getFirst().getId(), associate.getId());
        assertEquals(expected.getFirst().getName(), associate.getName());
        assertEquals(expected.getFirst().getSurname(), associate.getSurname());
        assertEquals(expected.getFirst().getEmail(), associate.getEmail());
        verify(repository,times(1)).findAll();
    }

    @Test
    @DisplayName("Test createAssociate Empty Dto passed")
    @Order(5)
    void testCreateAssociateEmptyDto() {


        AssociateDto associateDto = new AssociateDto();

        when(repository.save(any(Associate.class))).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> service.createAssociate(associateDto));

        verify(repository,times(1)).save(any(Associate.class));
    }

    @Test
    @DisplayName("Test createAssociate Success")
    @Order(6)
    void testCreateAssociate() {


        associate.setId(associateId);
        AssociateDto associateDto = new AssociateDto(associate);

        when(repository.save(any(Associate.class))).thenReturn(associate);

        Associate expected = service.createAssociate(associateDto);

        assertEquals(expected.getId(), associateId);
        assertEquals(expected.getName(), associate.getName());
        assertEquals(expected.getSurname(), associate.getSurname());
        assertEquals(expected.getEmail(), associate.getEmail());

        verify(repository,times(1)).save(any(Associate.class));
    }

    @Test
    @DisplayName("Test updateAssociate not found associateId")
    @Order(7)
    void testUpdateAssociateNotFound() {

        AssociateDto associateDto = new AssociateDto(associate);

        when(repository.findById(associateId)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND,"User with id "+associateId+" not found!"));

        assertThrows(ResponseStatusException.class, () -> service.updateAssociate(associateId, associateDto));

        verify(repository,times(1)).findById(associateId);
        verify(repository,times(0)).save(any(Associate.class));
    }

    @Test
    @DisplayName("Test updateAssociate Success")
    @Order(8)
    void testUpdateAssociate() {

        associate.setId(associateId);
        AssociateDto associateDto = new AssociateDto(associate);

        when(repository.findById(associateId)).thenReturn(Optional.ofNullable(associate));
        when(repository.save(any(Associate.class))).thenReturn(associate);

        Associate expected = service.updateAssociate(associateId, associateDto);

        assertEquals(expected.getId(), associateId);
        assertEquals(expected.getName(), associate.getName());
        assertEquals(expected.getSurname(), associate.getSurname());
        assertEquals(expected.getEmail(), associate.getEmail());

        verify(repository,times(1)).findById(associateId);
        verify(repository,times(1)).save(any(Associate.class));
    }

    @Test
    @DisplayName("Test deleteAssociate not found associateId")
    @Order(9)
    void testDeleteAssociateNotFound() {

        AssociateDto associateDto = new AssociateDto(associate);

        when(repository.findById(associateId)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND,"User with id "+associateId+" not found!"));

        assertThrows(ResponseStatusException.class, () -> service.deleteAssociate(associateId));

        verify(repository,times(1)).findById(associateId);
        verify(repository,times(0)).deleteById(associateId);
    }

    @Test
    @DisplayName("Test deleteAssociate success")
    @Order(10)
    void testDeleteAssociateSuccess() {

        associate.setId(associateId);

        when(repository.findById(associateId)).thenReturn(Optional.ofNullable(associate));
        doNothing().when(repository).delete(any(Associate.class));

        service.deleteAssociate(associateId);

        verify(repository,times(1)).findById(associateId);
        verify(repository,times(1)).delete(any(Associate.class));
    }
}
