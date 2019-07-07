package com.sucl.security.mgt.service;

import com.sucl.security.mgt.dao.UserRepository;
import com.sucl.security.mgt.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userReposroty;

    public User getUserByUsername(String username){
        return userReposroty.findByUsername(username);
    }

    public List<User> getUsers(){
        return userReposroty.findAll();
    }
}
