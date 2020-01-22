package com.ihrm.company.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private IdWorker idWorker;
    /**
     * 保存企业
     * 1.雪花算法生成id
     */
    public void add(Company company){
        //设置id
        company.setId(idWorker.nextId()+"");
        //设置默认状态
        company.setAuditState("1");//0.未审核，1.已审核
        company.setState(1);//0.未审核,1.已审核
        Date date = new Date(new java.util.Date().getTime());
        company.setCreateTime(date);
        companyDao.save(company);
    }
    /**
     * 更新企业
     * 1.永远都是先查再更新
     * 2.设置修改的属性
     * 3.调用到完成更新
     */
    public void update(Company company){
        //查询
        Company temp = companyDao.findById(company.getId()).get();
        temp.setName(company.getName());
        companyDao.save(temp);
    }
    /**
     * 删除企业
     */
    public void deleteById(String id){
        companyDao.deleteById(id);
    }
    /**
     * 根据id查询
     */
    public Company findById(String id){
        return companyDao.findById(id).get();
    }

    /**
     * 查询企业列表
     */
    public List<Company> findAll(){
        return companyDao.findAll();
    }
}
