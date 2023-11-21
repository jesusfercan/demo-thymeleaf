package com.example.demothymeleaf.service;

import com.example.demothymeleaf.entity.Associate;
import com.example.demothymeleaf.repository.AssociateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssociateService {

    @Autowired
    private AssociateRepository repository;

    public List<Associate> getAllAssociates(){
        return repository.findAll();
    }
}
