package com.github.nkolytschew.mongobee_migration.dao.jpa.document.embedded

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import java.util.*

/**
 * simple embedded document.
 * no [org.springframework.data.mongodb.core.mapping.Document] annotation needed, because this document won't be used as a separate document
 */
/**
 *@since v4
 */
class Address(@Id val id: String = UUID.randomUUID().toString(),
              @Field val city: String,
              @Field val street: String,
              @Field val zip: String)
