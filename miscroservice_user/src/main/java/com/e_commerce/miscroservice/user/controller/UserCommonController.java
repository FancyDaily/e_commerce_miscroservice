package com.e_commerce.miscroservice.user.controller;

import com.e_commerce.miscroservice.commons.entity.application.TUser;
import com.e_commerce.miscroservice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserCommonController {

    @Autowired
    private UserService userService;

    public TUser getUserById(Long userId) {
        return userService.getUserbyId(userId);
    }
}
