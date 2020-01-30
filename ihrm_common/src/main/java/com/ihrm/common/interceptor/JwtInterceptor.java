package com.ihrm.common.interceptor;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.hibernate.sql.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 *      继承HandlerInterceptorAdapter
 *      preHandle :进入控制器前
 *           boolean：
 *              true : 继续执行，进入controller
 *              flase : 拦截
 *
 *
 *      postHandle : 执行控制器后，页面尚未渲染
 *      afterCompletion : 渲染完成，响应结束之前
 *
 *
 *      1.简化获取token数据代码
 *          统一的用户权限校验（是否登录）
 *      2.判断当前用户是否具有当前接口的访问权
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    /**
     * 1.通过拦截器获取token
     *      1.通过request获取请求token
     *      2.通过token解析claims
     *      3.将claims绑定到request域中
     * 2.判断当前用户是否具有当前接口的访问权
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");

        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer")){
            String token = authorization.replace("Bearer","");
            Claims claims = jwtUtils.parseJwt(token);
            if (claims != null){
                //通过claims获取当前用户的api权限
                String apis= (String)claims.get("apis");
                //通过参数handler
                HandlerMethod h = (HandlerMethod) handler;
                RequestMapping annotation = h.getMethodAnnotation(RequestMapping.class);
                String name = annotation.name();

                if (apis.contains(name)){
                    request.setAttribute("user_claims",claims);
                    return true;
                }else {
                    throw new CommonException(ResultCode.UNAUTHORISE);
                }
            }

        }
        throw new CommonException(ResultCode.UNAUTHENTICATED);

    }


}
