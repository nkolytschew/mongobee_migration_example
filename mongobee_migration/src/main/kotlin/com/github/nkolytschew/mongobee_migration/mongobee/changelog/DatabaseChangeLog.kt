package com.github.nkolytschew.mongobee_migration.mongobee.changelog

import com.github.mongobee.changeset.ChangeLog
import com.github.mongobee.changeset.ChangeSet
import com.github.nkolytschew.mongobee_migration.dao.jpa.document.UserDocument
import com.github.nkolytschew.mongobee_migration.dao.jpa.document.embedded.Address
import com.github.nkolytschew.mongobee_migration.dao.jpa.document.embedded.Email
import com.mongodb.BasicDBObject
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.util.*

/**
 * ChangeLogs.
 * every [ChangeSet] has some minor impact on the database schema.
 * every update will be executed once.
 * some [ChangeSet] are not valid after another update, therefore I commented them out.
 */
@ChangeLog
class DatabaseChangeLog {
  /**
   * simple initialization example.
   * initialize database and create 5 user.
   * @since V1
   *
   * update v5: [UserDocument] has changed; there is no String-Field 'email' anymore, instead use a Custom-Type E-Mail
   * @since V6
   */
  //  @ChangeSet(order = "001", id = "initialization", author = "admin")
//  fun initDatabase(template: MongoTemplate) {
//    template.insert(UserDocument("test-1@test-email.de", "test-1-awesome", "test-1", "test-1", "1-awesome"))
//    template.insert(UserDocument("test-2@test-email.de", "test-2-awesome", "test-2", "test-2", "2-awesome"))
//    template.insert(UserDocument("test-3@test-email.de", "test-3-awesome", "test-3", "test-3", "3-awesome"))
//    template.insert(UserDocument("test-4@test-email.de", "test-4-awesome", "test-4", "test-4", "4-awesome"))
//    template.insert(UserDocument("test-5@test-email.de", "test-5-awesome", "test-5", "test-5", "5-awesome"))
//  }

  /**
   * initialize database and create 5 user, with updated E-Mail Type
   * @since V6
   *
   * update v7: [UserDocument] has changed; there is no String-Field 'surname' anymore, instead one Field 'name' contains the total name of a user
   * @since V8
   */
  //  @ChangeSet(order = "007", id = "createNewUser", author = "admin")
//  fun createNewUsers(template: MongoTemplate) {
//    template.insert(UserDocument(Email("test-6@test-email.de"), "test-6-awesome", "test-6", "test-6", "6-awesome"))
//    template.insert(UserDocument(Email("test-7@test-email.de"), "test-7-awesome", "test-7", "test-7", "7-awesome"))
//    template.insert(UserDocument(Email("test-8@test-email.de"), "test-8-awesome", "test-8", "test-8", "8-awesome"))
//    template.insert(UserDocument(Email("test-9@test-email.de"), "test-9-awesome", "test-9", "test-9", "9-awesome"))
//    template.insert(UserDocument(Email("test-0@test-email.de"), "test-0-awesome", "test-0", "test-0", "0-awesome"))
//  }

  /**
   * update v1: [UserDocument] has changed; Date-Field 'creationDate' was added and initialized with current date.

   * @since V2
   */
  @ChangeSet(order = "002", id = "updateUserAddCreationDate", author = "admin")
  fun updateUserAndSetDate(template: MongoTemplate) {
    template.findAll(UserDocument::class.java, "user")
        .map { userDocument ->
          if (userDocument.createDate == null)
            userDocument.createDate = Date()
          userDocument
        }
        .forEach(template::save)
  }

  /**
   * update v2: [UserDocument] has changed; Date-Field 'modificationDate' was added and initialized with currentDate.
   * Also an Integer-Field 'version' was added and initialized with 2.
   *
   *
   * Keep in mind, that you have to add [org.springframework.data.annotation.Version] annotation, after you have created and initialized the Field.

   * @since V3
   */
  //  @ChangeSet(order = "003", id = "updateUserAddVersionAndModificationDate", author = "admin")
//  fun updateUserAndSetVersionAndModificationDate(template: MongoTemplate) {
//    template.findAll<UserDocument>(UserDocument::class.java!!, "user")
//        .map { userDocument ->
//          userDocument.modificationDate = Date()
//          userDocument.version = 2 // workaround; set version value manually; add annotation afterwards
//          userDocument
//        }
//        .forEach(template::save)
//  }

  /**
   * update v3: [UserDocument] has changed; an embedded Document 'address' was added.

   * @since V4
   */
  @ChangeSet(order = "004", id = "addAddressToUser", author = "admin")
  fun updateUserAddAddress(template: MongoTemplate) {
    val userList = template.find<UserDocument>(Query.query(Criteria.where("email").`is`("test-1@test-email.de")), UserDocument::class.java)
    userList
        .map { userDocument ->
          userDocument.userAddress
              .addAll(
                  (0..Random().nextInt(5).toLong())
                      .map { it -> Address(city = "Hamburger City", street = "Random Muster Straße " + it, zip = it.toString() + "5678") }
                      .toCollection(mutableListOf()))
          userDocument
        }
        .forEach(template::save)
  }

  /**
   * update v4 create user without an E-Mail field.
   * @since V5
   *
   * update v7: [UserDocument] has changed; there is no String-Field 'surname' anymore.
   * @since V8
   */
  //  @ChangeSet(order = "005", id = "createUsersWithoutEmailField", author = "admin")
//  fun createUsersWithoutEmailField(template: MongoTemplate) {
//    (0..5)
//        .map { it ->
//          UserDocument(username = "awesome-" + it + "-test",
//              password = "awesome-" + it,
//              name = "awesome-" + it,
//              surname = it + "-test",
//              userAddress = (0..Random().nextInt(5))
//                  .map { integer -> Address(city = "Musterstadt", street = "Muster Random Straße " + integer, zip = "5678" + integer)
//                  }.toCollection(mutableListOf()))
//        }
//        .forEach(template::insert)
//  }

  /**
   * update v5: [UserDocument] has changed; String-Field 'email' was replaced with custom Type 'Email'.
   * find all user with an email String-value, replace String-Type with Custom-Type and update all UserDocuments.
   * if email-Field is Empty or null, you don't have to update user.
   * if email not null -> Field will be cast to specific class and updates will create correct entries
   *
   * @since V6
   */
  @ChangeSet(order = "006", id = "updateUserChangeEmailFromStringToCustomClass", author = "admin")
  fun updateUserChangeEmailField(template: MongoTemplate) {
    val isEmptyCriteria = Criteria().orOperator(Criteria.where("email").`is`(""), Criteria.where("email").`is`(null))
    while (true) {
      val result = template.findAndModify<UserDocument>(Query(isEmptyCriteria), Update.update("email", Email(email = "")), UserDocument::class.java)
      result ?: break
    }
  }

  /**
   * update 6: [UserDocument] has changed; 'name' and 'surname' will be concat to 'name'.
   * for each every user document get 'name' and 'surname', concat them, update 'name', remove field surname and update document.

   * @since V7
   */
  @ChangeSet(order = "008", id = "updateUserChangeNameAndSurnameToName", author = "admin")
  fun updateUserChangeNameAndSurnametoName(template: MongoTemplate) {
    val userCollection = template.getCollection("user")

    val cursor = userCollection.find()
    while (cursor.hasNext()) {
      val current = cursor.next()

      val nameObj = current.get("name")
      val surnameObj = current.get("surname")
      val updateName = (nameObj?.toString() ?: "") + " " + (surnameObj?.toString() ?: "")

      val updateQuery = BasicDBObject()
      updateQuery.append("\$set", BasicDBObject("name", updateName))
      updateQuery.append("\$unset", BasicDBObject("surname", ""))

      val searchQuery = BasicDBObject()
      searchQuery.put("_id", current.get("_id"))

      userCollection.update(searchQuery, updateQuery)
    }
  }

  /**
   * update 8: [UserDocument] has changed; add String-Fields 'createdBy' and 'modifiedBy' and initialize with String 'admin'

   * @since V8
   */
  @ChangeSet(order = "009", id = "updateUserAddCreatedByAndLastModifiedBy", author = "admin")
  fun updateUserAddCreatedByAndLastModifiedBy(template: MongoTemplate) {
    val update = Update()
    update.set("createdBy", "admin")
    update.set("modifiedBy", "admin")
    template.updateMulti(null, update, UserDocument::class.java)
  }
}