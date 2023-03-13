package com.springmvc.DaJava.controllers;

import com.springmvc.DaJava.count.Const;
import com.springmvc.DaJava.count.HashedPassword;
import com.springmvc.DaJava.models.User;
import com.springmvc.DaJava.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/admin")
public class LoginAdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpSession httpSession;

    /**
     *
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String LoginAdmin() {
        try {
            return Const.ROUTER_LOGIN_ADMIN;
        } catch (Exception e) {
            System.out.println(Const.MSG_ERROR_CATCH);
            return Const.ROUTER_ERROR;
        }
    }

    /**
     *
     * @param email
     * @param password
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String LoginAdmin(@RequestParam("email") String email, @RequestParam("password") String password, ModelMap modelMap) {
        try {
            User existUser = userRepository.findByEmail(email);
            if (existUser != null && HashedPassword.match(existUser.getPassword(), password) && existUser.isAdmin()) {
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

    /**
     *
     * @return
     */
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
