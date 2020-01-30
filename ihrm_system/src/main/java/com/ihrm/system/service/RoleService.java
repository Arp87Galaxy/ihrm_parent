package com.ihrm.system.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class RoleService extends BaseService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private IdWorker idWorker;
    /**
     *保存角色
     */
    public void save(Role role){
        String id = idWorker.nextId()+"";
        role.setId(id);
        roleDao.save(role);
    }
    /**
     * 更新角色
     */
    public void  update(Role role){
        //先查在改
        Role roleTarget = roleDao.findById(role.getId()).get();
        roleTarget.setDescription(role.getDescription());
        roleTarget.setName(role.getName());
        roleDao.save(roleTarget);
    }
    /**
     * 根据部门id查询角色
     */
    public Role findById(String id){
        return roleDao.findById(id).get();
    }
    /**
     * 根据企业id查询全部用户
     *      参数：map集合形式
     *          hasDept
     *          departmentId
     *          companyId
     *
     */
    public Page<Role> findAll(String companyId, int page , int size){

        return roleDao.findAll(getSpec(companyId),PageRequest.of(page-1,size));
    }
    public List<Role> findAll(String companyId){

        return roleDao.findAll(getSpec(companyId));
    }
    /**
     * 根据部门id删除用户
     */
    public void deleteById(String id){
        roleDao.deleteById(id);
    }

    /**
     * 分配权限
     */
    public void assignPerms(String roleId, List<String> permIds) {
        //1.根据id查询角色对象
        Role role = roleDao.findById(roleId).get();
        //2.设置角色的权限集合
        Set<Permission> permSet = new HashSet<Permission>();
        for (String permId : permIds){
            Permission permission = permissionDao.findById(permId).get();
            //需要根据pid和类型查询api权限列表
            List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PERMISSION_API, permission.getId());
            permSet.addAll(apiList);//自动赋予api权限
            permSet.add(permission);
        }
        //3.设置角色和权限集合的关系
        role.setPermissions(permSet);
        //4.更新角色对象
        roleDao.save(role);
    }
}
