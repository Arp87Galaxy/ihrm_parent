package com.ihrm.demo;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class CreateJwtTest {
    private String token;
    public CreateJwtTest() {
    }
    public CreateJwtTest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 通过jjwt创建token
     * @param args
     */
    public static void main(String[] args) {
        JwtBuilder builder = Jwts.builder().setId("888").setSubject("token auth").setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256,"ihrm").claim("ttl","30")
                ;
        CreateJwtTest createJwtTest = new CreateJwtTest(builder.compact());
        System.out.println(createJwtTest.getToken());;
    }
}
