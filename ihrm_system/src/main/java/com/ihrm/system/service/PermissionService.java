package com.ihrm.system.service;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.*;
import com.ihrm.system.dao.PermissionApiDao;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.PermissionMenuDao;
import com.ihrm.system.dao.PermissionPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.management.ObjectName;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PermissionMenuDao permissionMenuDao;
    @Autowired
    private PermissionPointDao permissionPointDao;
    @Autowired
    private PermissionApiDao permissionApiDao;
    @Autowired
    private IdWorker idWorker;
    /**
     *保存权限
     */
    public void save(Map<String,Object> map) throws Exception {
        String id = idWorker.nextId()+"";
        System.out.println(map.get("type").getClass().toString());
        //通过map构造权限对象
        //添加权限
        Permission perm = BeanMapUtils.mapToBean(map,Permission.class);
        perm.setId(id);

        //添加资源
        int type = perm.getType();
        switch (type){
            case PermissionConstants.PERMISSION_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map,PermissionMenu.class);
                permissionMenu.setId(id);
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PERMISSION_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map,PermissionApi.class);
                permissionApi.setId(id);
                permissionApiDao.save(permissionApi);
                break;
            case PermissionConstants.PERMISSION_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map,PermissionPoint.class);
                permissionPoint.setId(id);
                permissionPointDao.save(permissionPoint);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        permissionDao.save(perm);
    }
    /**
     * 更新权限
     */
    public void  update(Map<String,Object> map) throws Exception {
        //先查在改
        //更新权限
        Permission perm = BeanMapUtils.mapToBean(map,Permission.class);
        Permission permission = permissionDao.findById(perm.getId()).get();
        permission.setName(perm.getName());
        permission.setCode(perm.getCode());
        permission.setDescription(perm.getDescription());
        permission.setEnVisible(perm.getEnVisible());


        //更新资源
        int type = perm.getType();
        switch (type){
            case PermissionConstants.PERMISSION_MENU:
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map,PermissionMenu.class);
                //先查在改防止修改为null
                PermissionMenu permm = permissionMenuDao.findById(perm.getId()).get();
                permm.setId(perm.getId());
                permissionMenuDao.save(permm);
                break;
            case PermissionConstants.PERMISSION_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map,PermissionApi.class);
                //先查在改防止修改为null
//                PermissionMenu permm = permissionMenuDao.findById(perm.getId()).get();
//                permm.setId(perm.getId());
                permissionApi.setId(perm.getId());
                permissionApiDao.save(permissionApi);
                break;
            case PermissionConstants.PERMISSION_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map,PermissionPoint.class);
                //先查在改防止修改为null
//                PermissionMenu permm = permissionMenuDao.findById(perm.getId()).get();
//                permm.setId(perm.getId());
                permissionPoint.setId(perm.getId());
                permissionPointDao.save(permissionPoint);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        permissionDao.save(permission);
    }
    /**
     * 根据部门id查询权限
     *      1.根据id查询权限
     *      2.根据权限的类型查询资源
     *      3.构造map集合
     */
    public Map<String, Object> findById(String id) throws Exception {
        Permission permission = permissionDao.findById(id).get();
        int type = permission.getType();

        Object object = null;

        if (type == PermissionConstants.PERMISSION_MENU){
            object = permissionMenuDao.findById(id).get();
        }else if (type == PermissionConstants.PERMISSION_POINT){
            object = permissionPointDao.findById(id).get();
        }else if (type == PermissionConstants.PERMISSION_API){
            object = permissionApiDao.findById(id).get();
        }else {
            throw new CommonException(ResultCode.FAIL);
        }

        Map<String, Object> stringObjectMap = BeanMapUtils.beanToMap(object);

        stringObjectMap.put("name",permission.getName());
        stringObjectMap.put("type",permission.getType());
        stringObjectMap.put("code",permission.getCode());
        stringObjectMap.put("description",permission.getDescription());
        stringObjectMap.put("pid",permission.getPid());
        stringObjectMap.put("enVisible",permission.getEnVisible());
        return stringObjectMap;
    }

    /**
     * 根据企业id查询全部用户
     *      参数：map集合形式
     *          type :
     *              0:菜单+按钮
     *              1:菜单
     *              2:按钮
     *              3:api接口
     *          enVisible:
     *              0:查询所有saas平台最高权限
     *              1:企业权限
     *          pid: 父id
     *
     */
    public List<Permission> findAll(Map<String, Object> map){
        Specification<Permission> specification = new Specification<Permission>() {
            /**
             *动态拼接查询条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();

                if (!StringUtils.isEmpty(map.get("pid"))){
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class),(String)map.get("pid")));
                }
                if (!StringUtils.isEmpty(map.get("enVisible"))){
                    list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class),map.get("enVisible").toString()));
                }
                if (!StringUtils.isEmpty(map.get("type"))){
                    String type = (String) map.get("type");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("type"));
                    if("0".equals(type)){
                        in.value(1).value(2);
                    }else {
                        in.value(Integer.parseInt(type));
                    }
                    list.add(in);
                }

                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return permissionDao.findAll(specification);
    }
    /**
     * 根据部门id删除权限
     *      1.删除权限
     *      2.删除权限对应的资源
     */
    public void deleteById(String id) throws CommonException {
        //先查在改
        //更新权限
        Permission permission = permissionDao.findById(id).get();
        permissionDao.delete(permission);
        //更新资源
        int type = permission.getType();
        switch (type){
            case PermissionConstants.PERMISSION_MENU:
                //先查在改防止修改为null
                PermissionMenu permm = permissionMenuDao.findById(permission.getId()).get();
                permissionMenuDao.deleteById(permm.getId());
                break;
            case PermissionConstants.PERMISSION_API:
                PermissionApi perma = permissionApiDao.findById(permission.getId()).get();
                permissionMenuDao.deleteById(perma.getId());
                break;
            case PermissionConstants.PERMISSION_POINT:
                PermissionPoint permp = permissionPointDao.findById(permission.getId()).get();
                permissionMenuDao.deleteById(permp.getId());
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
    }
}
