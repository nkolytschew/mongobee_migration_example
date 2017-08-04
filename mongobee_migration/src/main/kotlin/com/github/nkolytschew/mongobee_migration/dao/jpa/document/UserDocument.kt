package com.github.nkolytschew.mongobee_migration.dao.jpa.document

import com.github.nkolytschew.mongobee_migration.dao.jpa.document.embedded.Address
import com.github.nkolytschew.mongobee_migration.dao.jpa.document.embedded.Email
import org.springframework.data.annotation.*
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

/**
 * main document for migration example.
 * check updates and version-changes at [com.github.nkolytschew.mongodbmigration.mongobee.changelog.DatabaseChangeLog]
 */
@Document(collection = "user")
class UserDocument(@Id val id: String = UUID.randomUUID().toString(),

                   @Field @Indexed
                   /**
                    * @since V6
                    */
                   // val String email,
                   val email: Email,

                   @Field @Indexed val username: String,
                   @Field val password: String,

                   @Field
                   /**
                    * @since V7
                    */
                   // val surname:String,
                   // @NonNull
                   // @Field
                   val name: String,

                   /**
                    * @since V8
                    */
                   @CreatedDate
                   /**
                    * @since V2
                    */
                   @Field var createDate: Date = Date(),

                   /**
                    * @since V8
                    */
                   @LastModifiedDate
                   /**
                    * @since V3
                    */
                   @Field val modificationDate: Date,

                   /**
                    * @since V3
                    */
                   @Version @Field val version: Int,

                   /**
                    * @since V8
                    */
                   @CreatedBy @Field val createdBy: String,

                   /**
                    * @since V8
                    */
                   @LastModifiedBy @Field val modifiedBy: String,

                   /**
                    * @since V4
                    */
                   @Field var userAddress: MutableList<Address>
)