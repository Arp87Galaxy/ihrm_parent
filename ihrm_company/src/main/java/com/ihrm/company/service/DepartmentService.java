package com.ihrm.company.service;

import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
@Service
public class DepartmentService extends BaseService {
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private IdWorker idWorker;
    /**
     *保存部门
     */
    public void save(Department department){
        String id = idWorker.nextId()+"";
        department.setId(id);
        departmentDao.save(department);
    }
    /**
     * 跟新部门
     */
    public void  update(Department department){
        //先查在改
        Department dept = departmentDao.findById(department.getId()).get();
        dept.setCode(department.getCode());
        dept.setIntroduce(department.getIntroduce());
        dept.setName(department.getName());

        departmentDao.save(dept);
    }
    /**
     * 根据部门id查询部门
     */
    public Department findById(String id){
        return departmentDao.findById(id).get();
    }
    /**
     * 根据企业id查询全部部门
     */
    public List<Department> findAll(String companyId){
        /**
         * 构造查询条件对象
         *      root：包含所有的对象数据
         *      cq：一般不用
         *      cb：构造查询条件
         */
//        Specification<Department> specification = new Specification<Department>() {
//            @Override
//            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.equal(root.get("companyId").as(String.class),companyId);
//            }
//        };
        //传入查询条件
        return departmentDao.findAll(getSpec(companyId));
    }
    /**
     * 根据部门id删除部门
     */
    public void deleteById(String id){
        departmentDao.deleteById(id);
    }
}
