package com.code.dataoxtesting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Main entity that contains specific information about job.
 */
@Entity
@Table(name = "job_table")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Job {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;

  @Column(length = 4096)
  String url;

  String name;

  @Column(length = 4096)
  String organizationUrl;

  String function;

  String iconUrl;

  String organizationTitle;

  String location;

  LocalDate postedAt;

  @Column(length = 32768)
  String description;

  String tags;

}
