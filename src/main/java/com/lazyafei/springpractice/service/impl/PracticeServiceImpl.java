package com.lazyafei.springpractice.service.impl;

import com.lazyafei.springpractice.model.entity.Company;
import com.lazyafei.springpractice.persistence.CompanyRepository;
import com.lazyafei.springpractice.service.IPracticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PracticeServiceImpl implements IPracticeService {
    @Autowired
    private CompanyRepository companyRepository;

    public String insert(Integer nums){
        List<Company> companies = new ArrayList<>();
        Long start = System.currentTimeMillis();
        companies = assembleData(companies,nums);

        StringBuilder sb = new StringBuilder();
        Long stag1 = System.currentTimeMillis();
        sb.append("组装数据用时：").append(stag1-start).append(" ms.");

        companyRepository.saveAll(companies);
        Long stag2 = System.currentTimeMillis();
        sb.append("插入数据用时：").append(stag2-stag1).append(" ms.");
        return sb.toString();
    }

    public String batchInsert(Integer nums){
        List<Company> companies = new ArrayList<>();
        Long start = System.currentTimeMillis();
        companies = assembleData(companies,nums);

        StringBuilder sb = new StringBuilder();
        Long stag1 = System.currentTimeMillis();
        sb.append("批量insert组装数据用时：").append(stag1-start).append(" ms.");

        companyRepository.batchSave(companies,true);
        Long stag2 = System.currentTimeMillis();
        sb.append("批量insert插入数据用时：").append(stag2-stag1).append(" ms.");
        return sb.toString();
    }

    public List<Company> assembleData(List<Company> companies,Integer nums){
        for(int i = 0 ; i < nums ; i++){
            Company company = new Company();
            company.setTenantId(1);
            company.setName("test"+i);
            company.setCode("test"+i);
            company.setIsDelete(false);
            company.setCreateUserId(1);
            company.setLastUpdateUserId(1);
            company.setCreateTime(new Date());
            company.setLastUpdateTime(new Date());
            companies.add(company);
        }
        return companies;
    }
}
