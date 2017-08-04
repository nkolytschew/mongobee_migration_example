package com.github.nkolytschew.mongodbmigration.dao.jpa.document;

import com.github.nkolytschew.mongodbmigration.dao.jpa.document.embedded.Address;
import com.github.nkolytschew.mongodbmigration.dao.jpa.document.embedded.Email;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * main document for migration example.
 * check updates and version-changes at {@link com.github.nkolytschew.mongodbmigration.mongobee.changelog.DatabaseChangeLog}
 */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class UserDocument {

  @Id
  private String id = UUID.randomUUID().toString();

  @NonNull
  @Field
  @Indexed
  /**
   * @since V6
   */
  // private String email;
  private Email email;
  @NonNull
  @Field
  @Indexed
  private String username;

  @NonNull
  @Field
  private String password;

  @NonNull
  @Field
  /**
   * @since V7
   */
  // private String surname;
  // @NonNull
  // @Field
  private String name;


  /**
   * @since V8
   */
  @CreatedDate
  /**
   * @since V2
   */
  @Field
  private Date createDate = new Date();

  /**
   * @since V8
   */
  @LastModifiedDate
  /**
   * @since V3
   */
  @Field
  private Date modificationDate;

  /**
   * @since V3
   */
  @Version
  @Field
  private int version;

  /**
   * @since V8
   */
  @CreatedBy
  @Field
  private String createdBy;

  /**
   * @since V8
   */
  @LastModifiedBy
  @Field
  private String modifiedBy;

  /**
   * @since V4
   */
  @Field
  private List<Address> userAddress = new ArrayList<>();
}