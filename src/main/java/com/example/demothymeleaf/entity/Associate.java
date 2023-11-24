package com.example.demothymeleaf.entity;

import com.example.demothymeleaf.dto.AssociateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "associates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Associate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank @Email
    private String email;

    public void updateAssociate(AssociateDto associateDto){
        if(Objects.isNull(associateDto))
            return;

        if(!associateDto.getName().isEmpty())
            this.name = associateDto.getName();

        if(!associateDto.getSurname().isEmpty())
            this.name = associateDto.getSurname();

        if(!associateDto.getEmail().isEmpty())
            this.name = associateDto.getName();
    }
}
