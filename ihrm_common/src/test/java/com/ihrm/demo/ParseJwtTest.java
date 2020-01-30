package com.ihrm.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class ParseJwtTest {
    public static void main(String[] args) {
        Claims claims = Jwts.parser().setSigningKey("ihrm")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4" +
                        "ODgiLCJzdWIiOiJ0b2tlbiBhdXRoIiwiaWF0IjoxNTgwMjc5MDkxLC" +
                        "J0dGwiOiIzMCJ9.7x5wMW9Do5iJJiTi-rtn7z6-s30E4rK8YkN8ajFOFgg")
                .getBody();
        System.out.println(claims.getId());

        System.out.println(claims.getIssuedAt());
        System.out.println(claims.getSubject());

        System.out.println(claims.get("ttl"));
    }
}
