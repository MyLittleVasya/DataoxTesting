package com.code.dataoxtesting.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.code.dataoxtesting.dto.RequestFilter;
import com.code.dataoxtesting.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link JwtService}.
 */
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  /**
   * Convert filter(client job requirements) into JWT token.
   *
   * @param filter body to convert.
   * @return string token value.
   */
  @Override
  public String requestFilterToJwt(RequestFilter filter) {

    final var filterMapped = filter.toMap();

    var token = JWT.create()
        .withHeader(filterMapped)
        .sign(Algorithm.HMAC256("123"));

    int dotIndex = token.indexOf(".");

    token = token.substring(0, dotIndex);

    return token;
  }
}
