package com.ihrm.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.Map;

@Getter
@Setter
//@Component
@ConfigurationProperties("jwt.config")
public class JwtUtils {
    //签名（token）私钥
    private String key;
    //签名失效时间（时效）毫秒
    private Long ttl;

    /**
     * 创建token
     *      1.登录用户id
     *      2.用户名name
     *      3.其他数据
     */
    public String createJwt(String id, String name, Map<String,Object> map){
        //1.设置失效时间
        long now = System.currentTimeMillis();//当前时间
        long exp = now+this.ttl;
        //创建JwtBuilder
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(name)
                .setIssuedAt(new Date(now))//创建时间
                .signWith(SignatureAlgorithm.HS256,this.key);

        //3.根据map设置cliams
        for (Map.Entry<String,Object> entry : map.entrySet()){
            builder.claim(entry.getKey(),entry.getValue());
        }
        //4.设置时效时间
        builder.setExpiration(new Date(exp));
        //5.返回token
        return builder.compact();
    }

    /**
     * 解析token获取claims
     */
    public Claims parseJwt(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(this.key)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
}
