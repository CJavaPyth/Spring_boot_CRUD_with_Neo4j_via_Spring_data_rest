package com.example.accessingneo4jdatarest.services;

import com.example.accessingneo4jdatarest.entities.Company;
import com.example.accessingneo4jdatarest.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    public void companyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies(){
        return companyRepository.findAll();
    }

    public Company addCompany(Company company){
        return companyRepository.save(company);
    }

    public Company updateCompany(Long companyId, Company company)  {
        Optional<Company> companyFromDB=  companyRepository.findById(companyId);
        if(companyFromDB.isPresent()){
            Company companyFromDBVal = companyFromDB.get();
            companyFromDBVal.setEmployees(company.getEmployees());
            companyFromDBVal.setName(company.getName());
            companyRepository.save(companyFromDBVal);
        }else{
            return null;
        }
        return company;
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public List<Company> findCompanyByName(String name) {
        return companyRepository.findByName(name);
    }

}


