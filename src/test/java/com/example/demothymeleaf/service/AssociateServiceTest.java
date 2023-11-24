package com.example.demothymeleaf.service;

import com.example.demothymeleaf.entity.Associate;
import com.example.demothymeleaf.repository.AssociateRepository;
import com.example.demothymeleaf.repository.AssociateRepositoryTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

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

        //when(repository.findById(userId)).thenThrow(ChangeSetPersister.NotFoundException.class);

        //Associate associate = service.
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
        Assertions.assertEquals(expected.size(),associates.size());
        verify(repository,times(1)).findAll();

    }
}
