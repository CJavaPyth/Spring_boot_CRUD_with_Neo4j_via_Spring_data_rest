package com.example.accessingneo4jdatarest.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Node("Company")
public class Company {
    @Id
    @GeneratedValue
    Long id;
    String name;

    String image;
    @Relationship(type = "WORKS_AT", direction = Relationship.Direction.INCOMING)
    List<Employee> employees;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
    public void setName(String name) {
        this.name = name;
    }


}