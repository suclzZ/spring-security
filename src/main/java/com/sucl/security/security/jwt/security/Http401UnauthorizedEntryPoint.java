package com.sucl.security.security.jwt.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementation of {@link AuthenticationEntryPoint} that responds when unauthorized is being return by security part
 */
@Slf4j
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
    log.debug("Authentication entry point access denied");
    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
  }
}
