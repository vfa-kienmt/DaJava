package com.springmvc.DaJava.controllers;

import com.springmvc.DaJava.count.Const;
import com.springmvc.DaJava.count.HashedPassword;
import com.springmvc.DaJava.models.User;
import com.springmvc.DaJava.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "admin/staff")
public class StaffAdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpSession httpSession;

    public String GetStaffList(ModelMap modelMap) {
        try {
            User user = (User) httpSession.getAttribute(Const.SESSION_LOGIN_ADMIN);
            if (!user.isAdmin()) {
                modelMap.addAttribute(Const.NAME_MSG_ERROR_PERMISSION, Const.MSG_ERROR_PERMISSION);
            }
            Iterable<User> userList = userRepository.findAll();
            modelMap.addAttribute("userList", userList);
            return Const.ROUTER_GET_STAFF_LIST;
        } catch (Exception e) {
            System.out.println(Const.MSG_ERROR_CATCH);
            return Const.ROUTER_HOME_ADMIN;
        }
    }

    /**
     * @param modelMap
     * @param fullName
     * @param email
     * @return
     */
    @RequestMapping(value = "/registeradmin", method = RequestMethod.POST)
    public String RegisterStaffAdmin(
            ModelMap modelMap,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email) {
        try {
            User user = (User) httpSession.getAttribute(Const.SESSION_LOGIN_ADMIN);
            if (!user.isAdmin()) {
                modelMap.addAttribute(Const.NAME_MSG_ERROR_PERMISSION, Const.MSG_ERROR_PERMISSION);
            }
            User existUserName = userRepository.findByUser(fullName);
            if (existUserName != null) {
                modelMap.addAttribute(Const.ERROR_REGISTER, Const.FULLNAME_ERROR);
                return Const.ROUTER_REGISTER_AD;
            }
            User existUserEmail = userRepository.findByEmail(email);
            if (existUserEmail != null) {
                modelMap.addAttribute(Const.ERROR_REGISTER, Const.EMAIL_ERROR);
                return Const.ROUTER_REGISTER_AD;
            }
            String password = HashedPassword.RandomPassord();
            User saveUser = new User();
            saveUser.setFullName(fullName);
            user.setEmail(email);
            user.setPassword(password);
            user.setAdmin(true);
            userRepository.save(user);
            modelMap.addAttribute(Const.RETURN_PASSWORD, password);
            return Const.ROUTER_HOME_ADMIN;
        } catch (Exception e) {
            System.out.println(Const.MSG_ERROR_CATCH);
            return Const.ROUTER_ERROR;
        }
    }

    @RequestMapping(value = "/updatestaff", method = RequestMethod.POST)
    public String UpdateStaff(
            @RequestParam("id") Integer id,
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            ModelMap modelMap) {
        try {
            User user = (User) httpSession.getAttribute(Const.SESSION_LOGIN_ADMIN);
            if (!user.isAdmin() && user.getEmail().equals(email)) {
                modelMap.addAttribute(Const.NAME_MSG_ERROR_PERMISSION, Const.MSG_ERROR_PERMISSION);
            }
            User existUserId = userRepository.findById(id).orElse(null);
            if (existUserId == null) {
                modelMap.addAttribute(Const.NAME_ERROR_USER, Const.ERROR_USER);
                return Const.ROUTEER_UPDATE_STAFF;
            }
            User existUserName = userRepository.findByUser(fullName);
            if (existUserName != null && !existUserName.getId().equals(id)) {
                modelMap.addAttribute(Const.ERROR_REGISTER, Const.FULLNAME_ERROR);
                return Const.ROUTEER_UPDATE_STAFF;
            }
            User existUserEmail = userRepository.findByEmail(email);
            if (existUserEmail != null && !existUserName.getId().equals(id)) {
                modelMap.addAttribute(Const.ERROR_REGISTER, Const.EMAIL_ERROR);
                return Const.ROUTEER_UPDATE_STAFF;
            }
            // Update staff.
            String password = HashedPassword.RandomPassord();
            existUserId.setFullName(fullName);
            existUserId.setEmail(email);
            userRepository.save(existUserId);
            modelMap.addAttribute(Const.RETURN_PASSWORD, password);
            return Const.ROUTER_GET_STAFF_LIST;
        } catch (Exception e) {
            System.out.println(Const.MSG_ERROR_CATCH);
            return Const.ROUTER_ERROR;
        }
    }

    @RequestMapping(value = "/deletestaff/{id}", method = RequestMethod.DELETE)
    public String DeleteStaff(@PathVariable("id") Integer id, ModelMap modelMap) {
        try {
            User user = (User) httpSession.getAttribute(Const.SESSION_LOGIN_ADMIN);
            if (!user.isAdmin()) {
                modelMap.addAttribute(Const.NAME_MSG_ERROR_PERMISSION, Const.MSG_ERROR_PERMISSION);
            }
            User existUsrId = userRepository.findById(id).orElse(null);
            if (existUsrId != null && existUsrId.getId().equals(id)) {
                modelMap.addAttribute(Const.NAME_ERROR_DELETE_STAFF, Const.ERROR_DELETE_STAFF);
                return Const.ROUTER_GET_STAFF_LIST;
            }
            userRepository.deleteById(id);
            modelMap.addAttribute(Const.SUCCESSFULLY, Const.MSG_SUCCESSFULLY);
            return Const.ROUTER_GET_STAFF_LIST;
        } catch (Exception e) {
            System.out.println(Const.MSG_ERROR_CATCH);
            return Const.ROUTER_ERROR;
        }
    }
}
