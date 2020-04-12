package com.dianping.controller;


import com.dianping.common.exception.BusinessException;
import com.dianping.common.exception.Errors;
import com.dianping.model.User;
import com.dianping.service.UserService;
import com.dianping.vo.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {

    public static final String CURRENT_USER_SESSION = "currentUserSession";


    @Resource
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping("/register")
    @ResponseBody
    public User register(@Valid @RequestBody User registerReq) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        User resUserModel = userService.register(registerReq);
        return resUserModel;
    }

    @RequestMapping("/login")
    @ResponseBody
    public User login(@RequestBody @Valid UserLogin loginReq) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        User userModel = userService.login(loginReq.getTelphone(), loginReq.getPassword());
        httpServletRequest.getSession().setAttribute(CURRENT_USER_SESSION, userModel);
        return userModel;
    }

    @RequestMapping("/logout")
    @ResponseBody
    public void logout() {
        httpServletRequest.getSession().invalidate();
    }

    //获取当前用户信息
    @RequestMapping("/getCurrentUser")
    @ResponseBody
    public User getCurrentUser(){
       return (User) httpServletRequest.getSession().getAttribute(CURRENT_USER_SESSION);
    }


    @RequestMapping("/index")
    public ModelAndView index() {
        String user = "wangyingdong";
        ModelAndView modelAndView = new ModelAndView("/index.html");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<User> user() {
        return userService.selectUser();
    }

    @RequestMapping("/get")
    @ResponseBody
    public User user(@RequestParam(name = "id") Integer id) {
        User user = (userService.getUser(id));
        if (Objects.isNull(user)) {
            throw new BusinessException(Errors.NO_OBJECT_FOUND_ERROR);
        }
        return user;

    }


}
