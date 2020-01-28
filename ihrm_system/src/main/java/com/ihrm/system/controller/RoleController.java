package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.response.RoleResult;
import com.ihrm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class RoleController extends BaseController{
    @Autowired
    private RoleService roleService;

    /**
     * 分配权限
     * 1.从前端传递的map获取被分配用户id : id
     * 2.从前端传递的map获取权限id列表 : permIds
     * 3.调用service完成角色分配
     */
    @PostMapping(value = "/role/assignPerm")
    public Result save(@RequestBody Map<String,Object> map){
//1
        String roleId= (String)map.get("id");
//2
        List<String> permIds = (List<String >)map.get("permIds");
//3
        roleService.assignPerms(roleId,permIds);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     *保存role接口
     */
    @PostMapping(value = "/role")
    public Result save(@RequestBody Role role){
        //设置企业id为固定值1保企业数据证隔离性，以后会解决固定id的问题
//        String companyid = "1";
        role.setCompanyId(companyId);

        //调用service保存结果
        roleService.save(role);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }
    @RequestMapping(value="/role/list" ,method=RequestMethod.GET)
    public Result findAll() throws Exception {
        List<Role> roleList = roleService.findAll(companyId);
        return new Result(ResultCode.SUCCESS,roleList);
    }
    /**
     *根据企业id查询role列表
     */
    @GetMapping(value = "/role")
    public Result findAll(int page, int size){
        Page<Role> pageRole = roleService.findAll(companyId,page,size);

        PageResult<Role> pageResult =new PageResult<Role>(pageRole.getTotalElements(),pageRole.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }
    /**
     *根据ID查询role
     */
    @GetMapping(value = "/role/{id}")
    public Result findById(@PathVariable(value = "id")String id){
        Role role = roleService.findById(id);
        RoleResult roleResult = new RoleResult(role);
        return new Result(ResultCode.SUCCESS,roleResult);

    }


    /**
     * 根据部门id修改角色信息
     */

    @PutMapping(value = "/role/{id}")
    public Result update(@PathVariable(value = "id")String id,@RequestBody Role role){
        //1.设置修改的角色id
        role.setId(id);
        //2.调用service跟新
        roleService.update(role);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据部门id’删除角色
     */
    @DeleteMapping(value = "/role/{id}")
    public Result delete(@PathVariable(value = "id")String id){
        roleService.deleteById(id);
        return new Result(ResultCode.SUCCESS);

    }
}