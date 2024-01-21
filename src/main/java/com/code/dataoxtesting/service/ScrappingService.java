package com.code.dataoxtesting.service;

import com.code.dataoxtesting.dto.RequestFilter;
import com.code.dataoxtesting.entity.Job;
import java.util.List;

/**
 * Service to scrap data from the page.
 */
public interface ScrappingService {

  /**
   * Get job listing using specific requirements of the client.
   *
   * @param filter body that contains client requirements.
   * @return list of found jobs.
   */
  List<Job> getJobListing(RequestFilter filter);
}
