package com.xmut.controller;

import com.xmut.entity.Company;
import com.xmut.service.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "企业信息管理")
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @ApiOperation("Get company information")
    @GetMapping("/{companyId}")
    public Company getCompanyById(@PathVariable Integer companyId) {
        return companyService.getCompanyById(companyId);
    }

    @ApiOperation("Update company information")
    @PutMapping
    public String updateCompany(@RequestBody Company company) {
        boolean result = companyService.updateCompany(company);
        return result ? "Update successful" : "Update failed";
    }

    @ApiOperation("Add company information")
    @PostMapping
    public String addCompany(@RequestBody Company company) {
        boolean result = companyService.addCompany(company);
        return result ? "Add successful" : "Add failed";
    }
}