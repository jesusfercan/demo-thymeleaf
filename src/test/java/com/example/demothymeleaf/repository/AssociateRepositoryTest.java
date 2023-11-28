package com.example.demothymeleaf.repository;

import com.example.demothymeleaf.entity.Associate;
import com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesConfiguration;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(EnableEncryptablePropertiesConfiguration.class)
public class AssociateRepositoryTest {

    @Autowired
    AssociateRepository repository;


    private List<Associate> listOfAssociatesImported;
    private Long associateId = 1L;
    private Associate associate;


    @Test
    @DisplayName("Test saveAssociate Launch ConstraintViolationException")
    @Order(1)
    void testValidateLaunchConstraintViolationException(){
        associate = new Associate();
        Assertions.assertThrows(ConstraintViolationException.class, () -> repository.save(associate));
    }
    @Test
    @DisplayName("Test saveAssociate Validate Empty Fields")
    @Order(2)
    void testValidateEmptyFields(){
        associate = new Associate();
        final List<String> errorEmptyFields = List.of( "name","surname","email");

        try {
            repository.save(associate);
        }catch (ConstraintViolationException ex) {

            ex.getConstraintViolations().forEach(cv -> {
               Assertions.assertTrue(errorEmptyFields.contains(cv.getPropertyPath().toString()));
            });
        }
    }
    @Test
    @DisplayName("Test saveAssociate Validate Email type")
    @Order(3)
    void testValidateEmailTypeToSave(){

        associate = Associate.builder().name("nombre").surname("apellidos").email("email").build();
        final List<String> errorEmailFields = List.of("email");

        try {
            repository.save(associate);
        }catch (ConstraintViolationException ex) {
            ex.getConstraintViolations().forEach(cv -> {
                Assertions.assertTrue(errorEmailFields.contains(cv.getPropertyPath().toString()));
            });
        }
    }

    @Test
    @DisplayName("Test Save-update Error validation")
    @Order(4)
    void updateAssociateErrorValidation(){

        // given
        associate = repository.findById(associateId).get();
        final List<String> errorFields = List.of("surname"); // rest of fields are ok in file import.sql

        // when - action or the behaviour that we are going test
        try {
            repository.saveAndFlush(associate);
        }catch (ConstraintViolationException ex) {
            ex.getConstraintViolations().forEach(cv -> {
                Assertions.assertTrue(errorFields.contains(cv.getPropertyPath().toString()));
            });
        }

    }

    @Test
    @DisplayName("Test Save-create Associate Success")
    @Order(5)
    void saveAssociateOkResult(){

        associate = Associate.builder().name("nombre").surname("appellidos").email("email@email.com").build();

        // when - action or the behaviour that we are going test
        Associate savedAssociate = repository.save(associate);

        // then - verify the output
        assertThat(savedAssociate).isNotNull();
        assertThat(savedAssociate.getId()).isGreaterThan(2); // because have 2 in import.sql
    }

    @Test
    @DisplayName("Test Save-update Associate Success")
    @Order(6)
    void updateAssociateOkResult(){

        // given - get associate from database import.sql file
        associate = repository.findById(associateId).get();
        associate.setSurname("apellidos");

        Associate savedAssociate = repository.saveAndFlush(associate);

        // then - verify the output
        assertThat(savedAssociate).isNotNull();
        assertThat(savedAssociate.getId()).isEqualTo(associateId);
    }

    @Test
    @DisplayName("Test Delete Associate Success")
    @Order(7)
    void deleteAssociateSuccess(){

        associate = repository.findById(associateId).get();
        int totalAssociatesBeforeDelete = repository.findAll().size();

        repository.delete(associate);

        int totalAssociatesAfterDelete = repository.findAll().size();
        associate = repository.findById(associateId).orElse(null);

        assertThat(totalAssociatesAfterDelete).isLessThan(totalAssociatesBeforeDelete);
        assertThat(associate).isNull();

    }
}
