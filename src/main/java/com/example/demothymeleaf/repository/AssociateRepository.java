package com.example.demothymeleaf.repository;

import com.example.demothymeleaf.entity.Associate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends JpaRepository<Associate,Long> {
}
