//package com.example.accessingneo4jdatarest;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import org.springframework.data.neo4j.core.schema.Id;
//import org.springframework.data.neo4j.core.schema.Node;
//import org.springframework.data.neo4j.core.schema.GeneratedValue;
//import org.springframework.data.neo4j.core.schema.Relationship;
//
//import java.util.List;
//
//@Node
//public class Person {
//
//    @Id @GeneratedValue
//    private Long id;
//
//    private String firstName;
//
//    private String lastName;
//
//    @Relationship(type = "FRIENDS_WITH")
//    private List<Person> friends;
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//}