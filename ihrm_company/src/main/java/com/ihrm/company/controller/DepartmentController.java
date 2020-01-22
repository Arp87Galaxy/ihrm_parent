package com.ihrm.company.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.company.response.DeptListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/company")
public class DepartmentController extends BaseController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CompanyService companyService;
    /**
     *保存department接口
     */
    @PostMapping(value = "/department")
    public Result save(@RequestBody Department department){
        //设置企业id为固定值1保企业数据证隔离性，以后会解决固定id的问题
        String companyid = "1";
        department.setCompanyId(companyid);
        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        department.setCreateTime(date);
        //调用service保存结果
        departmentService.save(department);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }
    /**
     *根据企业id查询department列表
     */
    @GetMapping(value = "/department")
    public Result findAll(){
        Company company  = companyService.findById(companyId);
        List<Department> departments = departmentService.findAll(companyId);

        DeptListResult deptListResult = new DeptListResult(company,departments);
        return new Result(ResultCode.SUCCESS,deptListResult);
    }
    /**
     *根据ID查询department
     */
    @GetMapping(value = "/department/{id}")
    public Result findById(@PathVariable(value = "id")String id){
        Department department = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS,department);

    }


    /**
     * 根据部门id修改部门信息
     */

    @PutMapping(value = "/department/{id}")
    public Result update(@PathVariable(value = "id")String id,@RequestBody Department department){
        //1.设置修改的部门id
        department.setId(id);
        //2.调用service跟新
        departmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据部门id’删除部门
     */
    @DeleteMapping(value = "/department/{id}")
    public Result delete(@PathVariable(value = "id")String id){
        departmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);

    }
}
