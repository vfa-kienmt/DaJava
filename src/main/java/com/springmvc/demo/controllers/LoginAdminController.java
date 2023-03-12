package com.springmvc.demo.controllers;

import com.springmvc.demo.count.Const;
import com.springmvc.demo.count.HashedPassword;
import com.springmvc.demo.models.User;
import com.springmvc.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path = "/admin")
public class LoginAdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpSession httpSession;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String LoginAdmin() {
        try {
            return Const.ROUTER_LOGIN_ADMIN;
        } catch (Exception e) {
            System.out.println(Const.MSG_ERROR_CATCH);
            return Const.ROUTER_ERROR;
        }

    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String LoginAdmin(@RequestParam("email") String email, @RequestParam("password") String password, ModelMap modelMap) {
        try {
            User existUser = userRepository.findByEmail(email);
            if (existUser != null && HashedPassword.match(existUser.getPassword(), password)) {
                httpSession.setAttribute(Const.SESSION_LOGIN_ADMIN, existUser);
                return "homeAdmin";
            } else {
                modelMap.addAttribute(Const.NAME_MSG_ERROR_LOGIN, Const.MSG_ERROR_LOGIN);
                return Const.ROUTER_LOGIN_ADMIN;
            }
        } catch (Exception e) {
            System.out.println(Const.MSG_ERROR_CATCH);
            return Const.ROUTER_ERROR;
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public String LogoutAdmin() {
        try {
            if (httpSession.getAttribute(Const.SESSION_LOGIN_ADMIN) != null) {
                httpSession.setAttribute(Const.SESSION_LOGIN_ADMIN, null);
                return Const.ROUTER_LOGIN_ADMIN;
            } else {
                return Const.ROUTER_LOGIN_ADMIN;
            }
        } catch (Exception e) {
            System.out.println(Const.MSG_ERROR_CATCH);
            return Const.ROUTER_ERROR;
        }
    }
}
