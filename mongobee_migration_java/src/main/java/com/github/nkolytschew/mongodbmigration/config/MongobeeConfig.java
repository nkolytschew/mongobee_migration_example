package com.github.nkolytschew.mongodbmigration.config;

import com.github.mongobee.Mongobee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * simple MongoBee configuration.
 * set Database URI for MongoDB Source.
 * set Database Name for migration operation.
 * set BasePath to ChangeLog class, for definition of changes.
 */
@Configuration
public class MongobeeConfig {

  private final String databaseName;
  private final String databaseUri;
  private final String changeLogPath;

  @Autowired
  public MongobeeConfig(@Value("${spring.data.mongodb.database}") String databaseName, @Value("${spring.data.mongodb.uri}") String databaseUri,
      @Value("${application.property.mongobee.changelog.path}") String changeLogPath) {
    this.databaseName = databaseName;
    this.databaseUri = databaseUri;
    this.changeLogPath = changeLogPath;
  }

  @Bean
  public Mongobee mongobee() {
    Mongobee runner = new Mongobee(databaseUri);
    runner.setDbName(databaseName);                 // host must be set if not set in URI
    runner.setChangeLogsScanPackage(changeLogPath); // package to scan for changesets
    runner.setEnabled(true);                        // optional: default is true

    return runner;
  }
}
