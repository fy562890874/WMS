package com.xmut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xmut.entity.Company;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CompanyMapper extends BaseMapper<Company> {

    // 查询企业信息
    @Select("SELECT * FROM xmut_company WHERE company_id = #{companyId}")
    Company getCompanyById(@Param("companyId") Integer companyId);

    // 更新企业信息
    @Update("UPDATE xmut_company SET company_name = #{companyName}, address = #{address}, " +
            "contact_name = #{contactName}, contact_phone = #{contactPhone}, email = #{email}, " +
            "update_time = NOW() WHERE company_id = #{companyId}")
    int updateCompany(Company company);

    // 添加企业信息
    @Insert("INSERT INTO xmut_company(company_name, address, contact_name, contact_phone, email, create_time) " +
            "VALUES(#{companyName}, #{address}, #{contactName}, #{contactPhone}, #{email}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "companyId")
    int addCompany(Company company);
}