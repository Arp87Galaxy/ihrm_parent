package com.ihrm.system.controller;

import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.domain.system.Permission;
import com.ihrm.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    /**
     *保存permission接口
     */
    @PostMapping(value = "/permission")
    public Result save(@RequestBody Map<String,Object> map) throws Exception {
        System.out.println(map.get("type").getClass());
        permissionService.save(map);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }
    /**
     *根据企业id查询permission列表
     */
    @GetMapping(value = "/permission")
    public Result findAll(@RequestParam Map<String,Object> map){
        List<Permission> list = permissionService.findAll(map);
        return new Result(ResultCode.SUCCESS,list);
    }
    /**
     *根据ID查询permission
     */
    @GetMapping(value = "/permission/{id}")
    public Result findById(@PathVariable(value = "id")String id) throws Exception {
        Map map = permissionService.findById(id);
        return new Result(ResultCode.SUCCESS,map);

    }


    /**
     * 根据部门id修改部门信息
     */

    @PutMapping(value = "/permission/{id}")
    public Result update(@PathVariable(value = "id")String id,@RequestBody Map<String,Object> map) throws Exception {
        map.put("id",id);
        permissionService.update(map);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据部门id’删除部门
     */
    @DeleteMapping(value = "/permission/{id}")
    public Result delete(@PathVariable(value = "id")String id) throws CommonException {
        permissionService.deleteById(id);
        return new Result(ResultCode.SUCCESS);

    }
}
