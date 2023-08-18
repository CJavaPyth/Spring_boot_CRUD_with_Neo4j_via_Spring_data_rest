package com.example.accessingneo4jdatarest.controllers;

import com.example.accessingneo4jdatarest.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.accessingneo4jdatarest.entities.Company;

import java.util.List;

@RestController
@RequestMapping("company")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<Company> getAllCompanies(){
        return companyService.getAllCompanies();
    }

    @PostMapping
    public Company addCompany(@RequestBody Company company){
        return companyService.addCompany(company);
    }

    @PutMapping("/{companyId}")
    public Company updateCompany(@PathVariable Long companyId, @RequestBody Company company)  {
        return companyService.updateCompany(companyId, company);
    }

    @DeleteMapping("/{authorId}")
    public void deleteCompany(@PathVariable Long authorId){
        companyService.deleteCompany(authorId);
    }
    @GetMapping("/{companyName}")
    public List<Company> findCompanyByName(@PathVariable String companyName){
        return companyService.findCompanyByName(companyName);
    }
}