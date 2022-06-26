package com.example.tripletest.repository;


import com.example.tripletest.entity.Mileage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageRepository extends JpaRepository<Mileage, Long> {

}
