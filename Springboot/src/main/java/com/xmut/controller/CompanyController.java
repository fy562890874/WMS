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

    @ApiOperation("获取企业信息")
    @GetMapping("/{companyId}")
    public Company getCompanyById(@PathVariable Integer companyId) {
        return companyService.getCompanyById(companyId);
    }

    @ApiOperation("更新企业信息")
    @PutMapping
    public boolean updateCompany(@RequestBody Company company) {
        return companyService.updateCompany(company);
    }

    @ApiOperation("添加企业信息")
    @PostMapping
    public boolean addCompany(@RequestBody Company company) {
        return companyService.addCompany(company);
    }
}