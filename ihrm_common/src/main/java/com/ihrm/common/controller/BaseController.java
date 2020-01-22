package com.ihrm.common.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {
    protected HttpServletResponse response;
    protected HttpServletRequest request;
    protected String companyId;
    protected String companyName;

    @ModelAttribute
    public void setResAnReq(HttpServletRequest request,HttpServletResponse response){
        this.request = request;
        this.response = response;


        this.companyId="1";
        this.companyName="阿尔普";

    }

}
