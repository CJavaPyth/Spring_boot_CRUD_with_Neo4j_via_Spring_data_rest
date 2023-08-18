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
@Node("Employee")
public class Employee {
    @Id
    @GeneratedValue
    Long id;
    String name;
//    String nric;
//    String gender;
    int age;

    String image;

//    String jobTitle;
//    String contactNum;
    String emailAddress;


//    @Relationship(type = "ALSO_WORKS_AT", direction = Relationship.Direction.OUTGOING)
//    List<Company> companies;


//    public List<Company> getCompanies() {
//        return companies;
//    }
//
//    public void setCompanies(List<Company> companies) {
//        this.companies = companies;
//    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
//    public String getNric() {
//        return nric;
//    }
//
//    public void setNric(String nric) {
//        this.nric = nric;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

//    public String getJobTitle() {
//        return jobTitle;
//    }
//
//    public void setJobTitle(String jobTitle) {
//        this.jobTitle = jobTitle;
//    }
//
//    public String getContactNum() {
//        return contactNum;
//    }
//
//    public void setContactNum(String contactNum) {
//        this.contactNum = contactNum;
//    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}