package com.github.nkolytschew.mongobee_migration.config

import com.github.mongobee.Mongobee
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * simple MongoBee configuration.
 * set Database URI for MongoDB Source.
 * set Database Name for migration operation.
 * set BasePath to ChangeLog class, for definition of changes.
 */

@Configuration
class MongobeeConfig(@Value("\${spring.data.mongodb.database}") val databaseName: String,
                     @Value("\${spring.data.mongodb.uri}") val databaseUri: String,
                     @Value("\${application.property.mongobee.changelog.path}") val changeLogPath: String) {

  @Bean
  fun mongobee(): Mongobee {
    val runner = Mongobee(databaseUri)
    runner.setDbName(databaseName)                 // host must be set if not set in URI
    runner.setChangeLogsScanPackage(changeLogPath) // package to scan for changesets
    runner.isEnabled = true                        // optional: default is true

    return runner
  }
}