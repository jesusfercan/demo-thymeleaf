package com.example.demothymeleaf.service;

import com.example.demothymeleaf.dto.AssociateDto;
import com.example.demothymeleaf.entity.Associate;
import com.example.demothymeleaf.repository.AssociateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AssociateService {

    @Autowired
    private AssociateRepository repository;

    public List<Associate> getAllAssociates(){
        return repository.findAll();
    }
    public Associate getAssociateById(Long associateId){
        return repository.findById(associateId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User with id "+associateId+" not found!")
        );
    }

    public Associate createAssociate(AssociateDto associateDto){
        return repository.save(associateDto.getAssociate());
    }

    public Associate updateAssociate(Long associateId, AssociateDto associateDto){
        Associate associateToUpdate = getAssociateById(associateId);
        associateToUpdate.updateAssociate(associateDto);

        associateToUpdate.setEmailEncrypted(associateToUpdate.getEmail());// todo eliminar
        return repository.save(associateToUpdate);
    }

    public void deleteAssociate(Long associateId){
        Associate associateToDelete = getAssociateById(associateId);
        repository.delete(associateToDelete);
    }
}
