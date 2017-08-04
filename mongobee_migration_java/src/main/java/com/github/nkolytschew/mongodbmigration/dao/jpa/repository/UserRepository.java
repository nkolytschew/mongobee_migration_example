package com.github.nkolytschew.mongodbmigration.dao.jpa.repository;


import com.github.nkolytschew.mongodbmigration.dao.jpa.document.UserDocument;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * simple Repository.
 * Use {@link RepositoryRestResource} to avoid creating custom controller.
 */
@RepositoryRestResource(path = "users")
public interface UserRepository extends MongoRepository<UserDocument, String> {
}
