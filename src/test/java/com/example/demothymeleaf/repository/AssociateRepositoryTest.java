package com.example.demothymeleaf.repository;

import com.example.demothymeleaf.entity.Associate;
import static org.assertj.core.api.Assertions.*;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AssociateRepositoryTest {

    @Autowired
    AssociateRepository repository;

    @Test
    void testValidateLaunchConstraintViolationException(){
        Associate  associate = new Associate();
        Assertions.assertThrows(ConstraintViolationException.class, () -> repository.save(associate));
    }
    @Test
    void testValidateEmptyFields(){
        //System.out.println("++++ INIT TEST CAN NOT EMPTY FIELDS ++++");
        Associate  associate = new Associate();
        final List<String> errorEmptyFields = List.of( "name","email");

        try {
            repository.save(associate);
        }catch (ConstraintViolationException ex) {

            ex.getConstraintViolations().forEach(cv -> {
                //System.out.println("Evaluating field: "+ cv.getPropertyPath().toString() + (errorEmptyFields.contains(cv.getPropertyPath().toString()) ? " (OK)" : " (FAIL)"));
                Assertions.assertTrue(errorEmptyFields.contains(cv.getPropertyPath().toString()));
            });
        }
    }
    @Test
    void testValidateEmailTypeToSave(){
        Associate  associate = Associate.builder().name("Jesus").email("incorrectEmail").build();
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
    void saveAssociateOkResult(){
        Associate  associate = Associate.builder()
                .name("jesus")
                .email("ramesh@gmail,com")
                .build();

        // when - action or the behaviour that we are going test
        Associate savedAssociate = repository.save(associate);

        // then - verify the output
        assertThat(savedAssociate).isNotNull();
        assertThat(savedAssociate.getId()).isGreaterThan(0);
    }
}
