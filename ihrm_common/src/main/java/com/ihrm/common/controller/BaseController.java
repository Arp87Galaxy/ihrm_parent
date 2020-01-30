package com.ihrm.common.controller;

import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {
    protected HttpServletResponse response;
    protected HttpServletRequest request;
    protected String companyId;
    protected String companyName;
    protected Claims claims;
    @ModelAttribute
    public void setResAnReq(HttpServletRequest request,HttpServletResponse response){
        this.request = request;
        this.response = response;

        Object obj =  request.getAttribute("user_claims");
        if (obj != null){
            this.claims = (Claims) obj;
            this.companyId=(String) claims.get("companyId");
            this.companyName=(String) claims.get("companyName");
        }else {
            this.companyId="1";
            this.companyName="阿尔普";
        }


    }

}
