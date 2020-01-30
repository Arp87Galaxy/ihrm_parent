package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.domain.system.response.UserResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PermissionService permissionService;


    /**
     * 用户登录后，获取用户信息
     * 1.获取请求头信息authorization
     * 2.替换Bearer => 空格
     * 3.解析token
     * 4.获取clamis
     * @return
     */
    @PostMapping(value = "/profile")
    public Result profile() throws Exception {


        String userId = claims.getId();

        //获取用户信息
        User user = userService.findById(userId);
        //根据level获取权限

        ProfileResult profileResult = null;
        if ("user".equals(user.getLevel())){
            //普通用户具有分配的权限
            profileResult = new ProfileResult(user);
        }else {
            Map map = new HashMap();
            if ("coAdmin".equals(user.getLevel())){
                map.put("enVisible",1);
            }
            List<Permission> list = permissionService.findAll(map);
            profileResult = new ProfileResult(user,list);
        }
        return new Result(ResultCode.SUCCESS,profileResult);
    }
    /**
     * 用户登录
     * @param reqMap
     * @return
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody Map<String,String> reqMap){
        User user = userService.findByMobile(reqMap.get("mobile"));
        if (user == null || !user.getPassword().equals(reqMap.get("password"))){
         return new Result(ResultCode.MOBILEORPASSWORDERROR);
        }else {
            StringBuilder sb = new StringBuilder();
            for (Role role : user.getRoles()){
                for (Permission perm : role.getPermissions()){
                    if (perm.getType() == PermissionConstants.PERMISSION_API){
                        sb.append(perm.getCode()).append(",");
                    }
                }
            }

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("apis",sb.toString());//可访问api权限字符串
            map.put("companyId",user.getCompanyId());
            map.put("companyName",user.getCompanyName());
            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
            return new Result(ResultCode.SUCCESS,token);
        }
    }
    /**
     * 分配角色
     * 1.从前端传递的map获取被分配用户id : id
     * 2.从前端传递的map获取角色id列表 : roleIds
     * 3.调用service完成角色分配
     */
    @PutMapping(value = "/user/assignRoles")
    public Result save(@RequestBody Map<String,Object> map){
//1
        String userId= (String)map.get("id");
//2
        List<String> roleIds = (List<String >)map.get("roleIds");
//3
        userService.assignRoles(userId,roleIds);
        return new Result(ResultCode.SUCCESS);
    }
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
        UserResult userResult = new UserResult(user);
        return new Result(ResultCode.SUCCESS,userResult);

    }


    /**
     * 根据部门id修改部门信息
     */

    @PutMapping(value = "/user/{id}",name = "123456")
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
    @DeleteMapping(value = "/user/{id}",name = "API-USER-DELETE")
    public Result delete(@PathVariable(value = "id")String id){
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);

    }
//    @DeleteMapping(value = "/user/{userid}",name = "API-USER-DELETE2")
//    public Result delete2(@PathVariable(value = "userid")String id){
//        userService.deleteById(id);
//        return new Result(ResultCode.SUCCESS);
//
//    }
}
