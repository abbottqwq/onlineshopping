package com.edu.neu.project.controller;

import com.edu.neu.project.entity.UserAccount;
import com.edu.neu.project.exception.NoSuchUsername;
import com.edu.neu.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/getdisplayname", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return Optional.ofNullable(userService.getDisplayNameByUsername(principal.getName())).orElse("");
    }

    @GetMapping(value = "/accountinfo")
    public ModelAndView accountInfo(ModelAndView mv, Principal principal) {
        mv.setViewName("accountinfo");
        mv.addObject("displayname", Optional.ofNullable(userService.getDisplayNameByUsername(principal.getName())).orElse(""));
        mv.addObject("username", principal.getName());
        return mv;
    }

    @GetMapping(value = "/updatedisplayname", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> updateDisplayName(Principal principal, @RequestParam("newdisplayname") String newDisplayName) {
        Map<String, Object> map = new HashMap<>();
        try {
            UserAccount ua = this.userService.updateDisplayName(principal.getName(), newDisplayName);
            map.put("success", true);
            map.put("displayname", ua.getDisplayName());
            return map;

        } catch (NoSuchUsername e) {
            e.printStackTrace();
            map.put("error", e);
            return map;
        }
    }

    @GetMapping("/changepassword")
    public String gotoPageChangePassword() {
        return "changepassword";
    }

    @PostMapping("/changepassword")
    public ModelAndView changePassword(Principal principal, ModelAndView mv, @RequestParam("oldpassword") String oldPassword
            , @RequestParam("newpassword") String newPassword, @RequestParam("cnewpassword") String cnewPassword) {
        if (!newPassword.equals(cnewPassword)) {
            mv.addObject("error3", " password doesn't match");
            mv.setViewName("changepassword");
            return mv;
        }
        if (newPassword.length() < 5) {
            mv.addObject("error2", " password too short");
            mv.setViewName("changepassword");
            return mv;
        }
        if (!this.userService.testPassword(principal.getName(), oldPassword)) {
            mv.addObject("error1", "old password error");
            mv.setViewName("changepassword");
            return mv;
        }

        Optional.ofNullable(this.userService.changePassword(principal.getName(), newPassword))
                .ifPresentOrElse(u -> {

                    mv.setViewName("redirect:/account/accountinfo?changepasswordsuccessfully=true");
                }, () -> {
                    mv.addObject("error", "other");
                    mv.setViewName("changepassword");
                });

        return mv;
    }


}
