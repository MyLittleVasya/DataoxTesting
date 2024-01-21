package com.code.dataoxtesting.service;

import com.code.dataoxtesting.dto.RequestFilter;

/**
 * Service to create jwt token, which is needed for proper interaction with filter of the page.
 */
public interface JwtService {

  /**
   * Convert filter(client job requirements) into JWT token.
   *
   * @param filter body to convert.
   * @return string token value.
   */
  String requestFilterToJwt(RequestFilter filter);

}
