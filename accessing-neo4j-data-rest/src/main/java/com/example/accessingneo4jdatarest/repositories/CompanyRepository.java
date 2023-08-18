package com.example.accessingneo4jdatarest.repositories;
import com.example.accessingneo4jdatarest.entities.Company;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends Neo4jRepository<Company, Long> {
    List<Company> findByName(String name);

}