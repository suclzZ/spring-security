package com.sucl.security.mgt.dao;

import com.sucl.security.mgt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByUsername(String username);

    User findByUserId(String userId);

    List<User> findAll();
}
