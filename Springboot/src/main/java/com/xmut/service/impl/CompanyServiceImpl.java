package com.xmut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xmut.entity.Company;
import com.xmut.mapper.CompanyMapper;
import com.xmut.service.CompanyService;
import org.springframework.stereotype.Service;

@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    @Override
    public Company getCompanyById(Integer companyId) {
        return baseMapper.getCompanyById(companyId);
    }

    @Override
    public boolean updateCompany(Company company) {
        int result = baseMapper.updateCompany(company);
        return result > 0;
    }

    @Override
    public boolean addCompany(Company company) {
        int result = baseMapper.addCompany(company);
        return result > 0;
    }
}