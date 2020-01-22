package com.ihrm.company.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 企业控制器
 * 提供企业操作的api
 */
@RestController
@RequestMapping(value="/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    //保存企业
    @PostMapping(value="")
    public Result save(@RequestBody Company company){
        companyService.add(company);
        return  new Result(ResultCode.SUCCESS);

    }
    //根据id更新企业
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable(value = "id")String id,
                         @RequestBody Company company){
        company.setId(id);
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }
    //根据id删除企业
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable(value = "id")String id){
        companyService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
    //根据id查询企业
    @GetMapping(value = "/{id}")
    @CrossOrigin
    public Result update(@PathVariable(value = "id")String id){
        Company company = companyService.findById(id);
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(company);
        return result;
    }
    //查询企业列表
    @GetMapping(value = "")
    @CrossOrigin
    public Result findAll(HttpServletResponse httpServletResponse){
        List<Company> companies = companyService.findAll();
        Result result = new Result(ResultCode.SUCCESS);
//        httpServletResponse.setHeader("Content-type","application/json");
//       String json = JSON.toJSONString(companies);
        result.setData(companies);
        return result;
    }
}
