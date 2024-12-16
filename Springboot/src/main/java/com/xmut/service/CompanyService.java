package com.xmut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xmut.entity.Company;

public interface CompanyService extends IService<Company> {

    Company getCompanyById(Integer companyId);

    boolean updateCompany(Company company);

    boolean addCompany(Company company);
}