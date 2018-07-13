package com.sucl.springsecurity.dao;

import com.sucl.springsecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

//@NoRepositoryBean
@Repository
public interface UserRepository extends JpaRepository<User,String> {

    User findByUsername(String username);

    User findByUserId(String userId);

    List<User> findAll();
}
