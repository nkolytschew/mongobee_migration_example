package com.github.nkolytschew.mongobee_migration

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class MongobeeMigrationApplication

fun main(args: Array<String>) {
    SpringApplication.run(MongobeeMigrationApplication::class.java, *args)
}
