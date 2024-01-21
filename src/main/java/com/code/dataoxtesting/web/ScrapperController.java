package com.code.dataoxtesting.web;

import com.code.dataoxtesting.dto.RequestFilter;
import com.code.dataoxtesting.entity.Job;
import com.code.dataoxtesting.service.ScrappingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for scrapping data from the site.
 */
@RestController
@RequiredArgsConstructor
public class ScrapperController {

  private final ScrappingService scrappingService;

  @PostMapping("/jobListing")
  public ResponseEntity<List<Job>> getJobListing(
      @RequestBody(required = false) final RequestFilter filter) {
    final var result = scrappingService.getJobListing(filter);
    return ResponseEntity.ok(result);
  }
}
