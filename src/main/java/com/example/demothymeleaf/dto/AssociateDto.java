package com.example.demothymeleaf.dto;

import com.example.demothymeleaf.entity.Associate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssociateDto {

    @NotBlank(message = "El nombre no puede ser vacío")
    private String name;

    @NotBlank(message = "El apellido no puede ser vacío")
    private String surname;

    @NotBlank(message = "El email no puede ser vacío")
    @Email(message = "Introduzca un email correcto")
    private String email;


    public Associate getAssociate(){
        return Associate.builder()
                .name(this.name)
                .surname(this.surname)
                .email(this.email).build();
    }
}
