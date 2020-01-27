package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.system.User;
import com.ihrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    /**
     *保存user接口
     */
    @PostMapping(value = "/user")
    public Result save(@RequestBody User user){
        //设置企业id为固定值1保企业数据证隔离性，以后会解决固定id的问题
//        String companyid = "1";
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        user.setCreateTime(date);
        //调用service保存结果
        userService.save(user);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }
    /**
     *根据企业id查询user列表
     */
    @GetMapping(value = "/user")
    public Result findAll(int page, int size, @RequestParam Map map){
        map.put("companyId",companyId);
        Page<User> pageUser = userService.findAll(map,page,size);

        PageResult<User> pageResult =new PageResult<User>(pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }
    /**
     *根据ID查询user
     */
    @GetMapping(value = "/user/{id}")
    public Result findById(@PathVariable(value = "id")String id){
        User user = userService.findById(id);
        return new Result(ResultCode.SUCCESS,user);

    }


    /**
     * 根据部门id修改部门信息
     */

    @PutMapping(value = "/user/{id}")
    public Result update(@PathVariable(value = "id")String id,@RequestBody User user){
        //1.设置修改的部门id
        user.setId(id);
        //2.调用service跟新
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据部门id’删除部门
     */
    @DeleteMapping(value = "/user/{id}")
    public Result delete(@PathVariable(value = "id")String id){
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);

    }
}
