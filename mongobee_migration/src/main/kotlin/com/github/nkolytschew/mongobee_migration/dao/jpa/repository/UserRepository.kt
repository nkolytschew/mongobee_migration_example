package com.github.nkolytschew.mongobee_migration.dao.jpa.repository

import com.github.nkolytschew.mongobee_migration.dao.jpa.document.UserDocument
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

/**
 * simple Repository.
 * Use [RepositoryRestResource] to avoid creating custom controller.
 */
@RepositoryRestResource(path = "users")
interface UserRepository : MongoRepository<UserDocument, String>