package com.code.dataoxtesting.service.impl;

import static com.code.dataoxtesting.utils.ApplicationConstants.*;
import static com.code.dataoxtesting.utils.ApplicationConstants.HtmlRelated.*;

import com.code.dataoxtesting.dto.RequestFilter;
import com.code.dataoxtesting.entity.Job;
import com.code.dataoxtesting.entity.repository.JobRepository;
import com.code.dataoxtesting.service.JwtService;
import com.code.dataoxtesting.service.ScrappingService;
import jakarta.annotation.Nonnull;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link ScrappingService}.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ScrappingServiceImpl implements ScrappingService {

  private final JobRepository jobRepository;

  private final JwtService jwtService;

  @Value("${target.url}")
  String targetUrl;

  @Value("${target.endpoint}")
  String targetEndpoint;

  /**
   * Get job listing using specific requirements of the client.
   *
   * @param filter body that contains client requirements.
   * @return list of found jobs.
   */
  @Override
  public List<Job> getJobListing(final RequestFilter filter) {

    final var filterJwtToken = jwtService.requestFilterToJwt(filter);

    String url = targetUrl + targetEndpoint + filterJwtToken;

    List<Job> jobList = new ArrayList<>();

    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(url).build();
    try (Response response = client.newCall(request).execute()) {

      if (response.isSuccessful() && response.body() != null) {
        String responseBody = response.body().string();
        Document doc = Jsoup.parse(responseBody);

        Elements jobElements =
            doc.select(JOBS_CSS_SELECTOR);
        for (Element jobElement : jobElements) {
          final var job = parseDataAndCreateJob(jobElement,
              Arrays.toString(filter.getJobFunctions()));
          jobList.add(job);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    final var sortedJobList = sortJobListByPostedAt(jobList, filter.getPostedAtSorting());
    final var uniqueList = removeExistingJobs(new ArrayList<>(sortedJobList));
    jobRepository.saveAll(uniqueList);
    return sortedJobList;
  }

  /**
   * Parse inner text from tag using attribute as a selector.
   *
   * @param attributeName  name of the attribute selector.
   * @param attributeValue value of the attribute selector.
   * @param jobElement     element to parse from.
   * @return string value of inner text.
   */
  private String parseTagTextUsingAttribute(final String attributeName, final String attributeValue,
                                            final Element jobElement) {
    final var optionalElement = Optional.ofNullable(
        jobElement.getElementsByAttributeValue(attributeName, attributeValue).first());

    return optionalElement.map(Element::text).orElse(NOT_FOUND_PLACEHOLDER);
  }

  /**
   * Filter jobs for saving. Jobs that are already in database won`t be saved.
   *
   * @param jobList list of jobs to filter.
   * @return list of unique jobs.
   */
  private List<Job> removeExistingJobs(final List<Job> jobList) {
    final var uniqueList = new ArrayList<Job>(jobList);
    for (final var job : jobList) {
      if (jobRepository.existsByUrl(job.getUrl())) {
        uniqueList.remove(job);
      }
    }
    return uniqueList;
  }

  /**
   * Sort job list by posted date. By default, it`s sorted in desc order(newer first).
   *
   * @param jobList         list to sort.
   * @param postedAtSorting order of sorting.
   * @return sorted list of jobs.
   */
  private List<Job> sortJobListByPostedAt(final List<Job> jobList, final String postedAtSorting) {
    if (postedAtSorting.equals("asc")) {
      return jobList.stream()
          .sorted(Comparator.comparing(Job::getPostedAt))
          .collect(Collectors.toList());
    }
    return jobList.stream()
        .sorted(Comparator.comparing(Job::getPostedAt).reversed())
        .collect(Collectors.toList());
  }

  /**
   * Method for creation of job object.
   *
   * @param jobElement  element to parse data from.
   * @param jobFunction job function the client wanted.
   * @return Job instance.
   * @throws IOException
   */
  private Job parseDataAndCreateJob(@Nonnull final Element jobElement, final String jobFunction)
      throws IOException {
    Job job = new Job();
    job.setName(parseTagTextUsingAttribute(ITEMPROP_ATTRIBUTE, TITLE_ATTRIBUTE, jobElement));
    job.setIconUrl(
        parseAttributeValueUsingAnotherAttribute(CONTENT_ATTRIBUTE, ITEMPROP_ATTRIBUTE,
            LOGO_ATTRIBUTE, jobElement));
    job.setUrl(
        parseAttributeValueUsingAnotherAttribute(HREF_ATTRIBUTE, DATA_TESTID_ATTRIBUTE,
            READ_MORE_ATTRIBUTE,
            jobElement));
    if (!job.getUrl().contains(HTTP)) {
      job.setUrl(new StringBuilder(job.getUrl()).insert(0, targetUrl).toString());
    }
    job.setOrganizationUrl(
        parseAttributeValueUsingAnotherAttribute(HREF_ATTRIBUTE, DATA_TESTID_ATTRIBUTE,
            LINK_ATTRIBUTE, jobElement));
    job.setOrganizationTitle(
        parseTagTextUsingAttribute(DATA_TESTID_ATTRIBUTE, LINK_ATTRIBUTE, jobElement));
    if (!job.getOrganizationUrl().contains(targetUrl)) {
      job.setOrganizationUrl(
          new StringBuilder(job.getOrganizationUrl()).insert(0, targetUrl).toString());
    }
    job.setLocation(
        parseAttributeValueUsingAnotherAttribute(CONTENT_ATTRIBUTE, ITEMPROP_ATTRIBUTE,
            ADDRESS_ATTRIBUTE,
            jobElement));
    job.setPostedAt(LocalDate.parse(
        parseAttributeValueUsingAnotherAttribute(CONTENT_ATTRIBUTE, ITEMPROP_ATTRIBUTE,
            DATE_POSTED_ATTRIBUTE,
            jobElement), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    job.setTags(parseJobTags(jobElement));
    job.setDescription(parseJobDescription(job.getUrl()));
    if (jobFunction != null) {
      job.setFunction(jobFunction);
    } else {
      job.setFunction(NOT_FOUND_PLACEHOLDER);
    }
    return job;
  }

  /**
   * Get attribute value using other attribute value as the selector.
   *
   * @param targetAttribute      attribute to get data from.
   * @param sourceAttribute      attribute name to select element.
   * @param sourceAttributeValue attribute value to select element.
   * @param jobElement           element to parse data from.
   * @return string value of needed data.
   */
  private String parseAttributeValueUsingAnotherAttribute(final String targetAttribute,
                                                          final String sourceAttribute,
                                                          final String sourceAttributeValue,
                                                          final Element jobElement) {
    Optional<Element> optionalElement = Optional.ofNullable(
        jobElement.getElementsByAttributeValue(sourceAttribute, sourceAttributeValue).first());

    String value = optionalElement.map(element -> element.attributes().get(targetAttribute))
        .orElse("NOT FOUND");

    return value;
  }

  /**
   * Parse job tags from job element.
   *
   * @param jobElement element to parse data from.
   * @return string value of tags delimited by comma.
   */
  private String parseJobTags(final Element jobElement) {
    final var elements =
        jobElement.getElementsByAttributeValue(DATA_TESTID_ATTRIBUTE, TAG_ATTRIBUTE);
    final var resultString = new StringBuilder();
    for (final var element : elements) {
      resultString.append(element.text()).append(TAGS_DELIMITER);
    }
    if (elements.size() == 0) {
      return NOT_FOUND_PLACEHOLDER;
    }
    return resultString.toString();
  }

  /**
   * Parse job description if its url is in charge of jobs.techstars.
   * <P>If url is leading to another site, the description is NOT_FOUND</P>
   *
   * @param jobDescrUrl page of job description.
   * @return string value of formatted description.
   * @throws IOException
   */
  private String parseJobDescription(final String jobDescrUrl) throws IOException {
    if (jobDescrUrl.contains(targetUrl)) {
      OkHttpClient client = new OkHttpClient();
      Request request = new Request.Builder().url(jobDescrUrl).build();
      try (Response response = client.newCall(request).execute()) {
        if (response.isSuccessful() && response.body() != null) {
          String responseBody = response.body().string();
          Document doc = Jsoup.parse(responseBody);
          return doc.getElementsByAttributeValue(DATA_TESTID_ATTRIBUTE, CAREER_ATTRIBUTE)
              .toString();
        }
      }
    }
    return NOT_FOUND_PLACEHOLDER;
  }

}
