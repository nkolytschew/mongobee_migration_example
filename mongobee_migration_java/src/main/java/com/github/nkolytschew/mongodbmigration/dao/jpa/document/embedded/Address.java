package com.github.nkolytschew.mongodbmigration.dao.jpa.document.embedded;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * simple embedded document.
 * no [@link {@link org.springframework.data.mongodb.core.mapping.Document} annotation needed, because this document won't be used as a separate document
 */
// @since v4
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Address {
  @Id
  private String id = UUID.randomUUID().toString();

  @NonNull
  @Field
  private String city;
  @NonNull
  @Field
  private String street;

  @NonNull
  @Field
  private String zip;
}