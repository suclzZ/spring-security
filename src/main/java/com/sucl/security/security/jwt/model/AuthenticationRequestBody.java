package com.sucl.security.security.jwt.model;

/**
 * Data transfer object that carries authentication data
 * Created by SBratic on 2/19/2017.
 */
public interface AuthenticationRequestBody {

  String getLogin();

  String getPassword();

  boolean isRememberMe();
}
