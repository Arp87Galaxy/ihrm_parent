package com.ihrm.system.service;

import com.ihrm.common.utils.IdWorker;

import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
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
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;


    @Autowired
    private IdWorker idWorker;

    /**
     *保存用户
     */
    public void save(User user){
        String id = idWorker.nextId()+"";
        user.setId(id);
        user.setPassword("123456");
        user.setEnableState(1);
        userDao.save(user);
    }
    /**
     * 更新用户
     */
    public void  update(User user){
        //先查在改
        User target = userDao.findById(user.getId()).get();
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        userDao.save(target);
    }
    /**
     * 根据部门id查询用户
     */
    public User findById(String id){
        return userDao.findById(id).get();
    }
    /**
     * 根据企业id查询全部用户
     *      参数：map集合形式
     *          hasDept
     *          departmentId
     *          companyId
     *
     */
    public Page<User> findAll(Map<String,Object> map,int page ,int size){
//        1.构造查询条件
        Specification<User> userSpecification = new Specification<User>() {
            /**
             *动态拼接查询条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();

                if (!StringUtils.isEmpty(map.get("companyId"))){
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                if (!StringUtils.isEmpty(map.get("departmentId"))){
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),(String)map.get("departmentId")));
                }
                if (!StringUtils.isEmpty(map.get("hasDept"))){
                    if ("0".equals((String)map.get("hasDept"))){
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    }else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }

                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
//        2.分页
        Page<User> pageUser = userDao.findAll(userSpecification,PageRequest.of(page-1,size));
        return pageUser;
    }
    /**
     * 根据部门id删除用户
     */
    public void deleteById(String id){
        userDao.deleteById(id);
    }

    /**
     * 分配角色
     * @param userId
     * @param roleIds
     */
    public void assignRoles(String userId, List<String> roleIds) {
        //1.根据id查询用户
        User user = userDao.findById(userId).get();
        //2.设置用户的角色集合
        Set<Role> roleSet = new HashSet<Role>();
        for (String roleId : roleIds){
            Role role = roleDao.findById(roleId).get();
            roleSet.add(role);
        }
        //3.设置用户和角色集合的关系
        user.setRoles(roleSet);
        //4.更新用户
        userDao.save(user);
    }
}