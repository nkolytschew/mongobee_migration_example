package com.github.nkolytschew.mongodbmigration.mongobee.changelog;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.github.nkolytschew.mongodbmigration.dao.jpa.document.UserDocument;
import com.github.nkolytschew.mongodbmigration.dao.jpa.document.embedded.Address;
import com.github.nkolytschew.mongodbmigration.dao.jpa.document.embedded.Email;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * ChangeLogs.
 * every {@link ChangeSet} has some minor impact on the database schema.
 * every update will be executed once.
 * some {@Link ChangeSet} are not valid after another update, therefore I commented them out.
 */
@ChangeLog
public class DatabaseChangeLog {

  /**
   * simple initialization example.
   * initialize database and create 5 user.
   * @since V1
   *
   * update v5: {@link UserDocument} has changed; there is no String-Field 'email' anymore, instead use a Custom-Type E-Mail
   * @since V6
   */
  // @ChangeSet(order = "001", id = "initialization", author = "admin")
  // public void initDatabase(final MongoTemplate template) {
  //   template.insert(new UserDocument("test-1@test-email.de", "test-1-awesome", "test-1", "test-1", "1-awesome"));
  //   template.insert(new UserDocument("test-2@test-email.de", "test-2-awesome", "test-2", "test-2", "2-awesome"));
  //   template.insert(new UserDocument("test-3@test-email.de", "test-3-awesome", "test-3", "test-3", "3-awesome"));
  //   template.insert(new UserDocument("test-4@test-email.de", "test-4-awesome", "test-4", "test-4", "4-awesome"));
  //   template.insert(new UserDocument("test-5@test-email.de", "test-5-awesome", "test-5", "test-5", "5-awesome"));
  // }

  /**
   * initialize database and create 5 user, with updated E-Mail Type
   * @since V6
   *
   * update v7: {@link UserDocument} has changed; there is no String-Field 'surname' anymore, instead one Field 'name' contains the total name of a user
   * @since V8
   */
  // @ChangeSet(order = "007", id = "createNewUser", author = "admin")
  // public void createNewUsers(final MongoTemplate template) {
  //   template.insert(new UserDocument(new Email("test-6@test-email.de"), "test-6-awesome", "test-6", "test-6", "6-awesome"));
  //   template.insert(new UserDocument(new Email("test-7@test-email.de"), "test-7-awesome", "test-7", "test-7", "7-awesome"));
  //   template.insert(new UserDocument(new Email("test-8@test-email.de"), "test-8-awesome", "test-8", "test-8", "8-awesome"));
  //   template.insert(new UserDocument(new Email("test-9@test-email.de"), "test-9-awesome", "test-9", "test-9", "9-awesome"));
  //   template.insert(new UserDocument(new Email("test-0@test-email.de"), "test-0-awesome", "test-0", "test-0", "0-awesome"));
  // }

  /**
   * update v1: {@link UserDocument} has changed; Date-Field 'creationDate' was added and initialized with current date.
   *
   * @since V2
   */
  @ChangeSet(order = "002", id = "updateUserAddCreationDate", author = "admin")
  public void updateUserAndSetDate(final MongoTemplate template) {
    template.findAll(UserDocument.class, "user")
        .stream()
        .map(userDocument -> {
          if (userDocument.getCreateDate() == null)
            userDocument.setCreateDate(new Date());
          return userDocument;
        })
        .forEach(template::save);
  }

  /**
   * update v2: {@link UserDocument} has changed; Date-Field 'modificationDate' was added and initialized with currentDate.
   * Also an Integer-Field 'version' was added and initialized with 2.
   * <p>
   * Keep in mind, that you have to add {@link org.springframework.data.annotation.Version} annotation, after you have created and initialized the Field.
   *
   * @since V3
   */
  @ChangeSet(order = "003", id = "updateUserAddVersionAndModificationDate", author = "admin")
  public void updateUserAndSetVersionAndModificationDate(final MongoTemplate template) {
    template.findAll(UserDocument.class, "user")
        .stream()
        .map(userDocument -> {
          userDocument.setModificationDate(new Date());
          userDocument.setVersion(2); // workaround; set version value manually; add annotation afterwards
          return userDocument;
        })
        .forEach(template::save);
  }

  /**
   * update v3: {@link UserDocument} has changed; an embedded Document 'address' was added.
   *
   * @since V4
   */
  @ChangeSet(order = "004", id = "addAddressToUser", author = "admin")
  public void updateUserAddAddress(final MongoTemplate template) {
    final List<UserDocument> userList = template.find(Query.query(Criteria.where("email").is("test-1@test-email.de")), UserDocument.class);
    userList.stream()
        .map(userDocument -> {
          userDocument.getUserAddress()
              .addAll(
                  Stream.iterate(0, n -> n + 1)
                      .limit(new Random().nextInt(5))
                      .map(it -> new Address("Hamburger City", "Random Muster Straße " + it, it + "5678"))
                      .collect(Collectors.toList()));
          return userDocument;
        })
        .forEach(template::save);
  }

  /**
   * update v4 create user without an E-Mail field.
   * @since V5
   *
   * update v7: {@link UserDocument} has changed; there is no String-Field 'surname' anymore.
   * @since V8
   */
  // @since V8
//  @ChangeSet(order = "005", id = "createUsersWithoutEmailField", author = "admin")
//  public void createUsersWithoutEmailField(final MongoTemplate template) {
//    System.out.println("test");
//    Stream.iterate(0, n -> n + 1)
//        .limit(5)
//        .map(it -> {
//          final UserDocument uDoc = new UserDocument();
//          uDoc.setUsername("awesome-" + it + "-test");
//          uDoc.setPassword("awesome-" + it);
//          uDoc.setName("awesome-" + it);
//          uDoc.setSurname(it + "-test");
//          uDoc.setUserAddress(
//              Stream.iterate(0, n -> n + 1)
//                  .limit(new Random().nextInt(5))
//                  .map(integer -> new Address("Musterstadt", "Muster Random Straße " + integer, "5678" + integer))
//                  .collect(Collectors.toList()));
//          return uDoc;
//        })
//        .forEach(template::insert);
//    System.out.println("test");
//  }

  /**
   * update v5: {@link UserDocument} has changed; String-Field 'email' was replaced with custom Type 'Email'.
   * find all user with an email String-value, replace String-Type with Custom-Type and update all UserDocuments.
   * if email-Field is Empty or null, you don't have to update user.
   *
   * @since V6
   */
  @ChangeSet(order = "006", id = "updateUserChangeEmailFromStringToCustomClass", author = "admin")
  public void updateUserChangeEmailField(final MongoTemplate template) {
    final Criteria isEmptyCriteria = new Criteria().orOperator(Criteria.where("email").is(""), Criteria.where("email").is(null));
    while (true) {
      final UserDocument result = template.findAndModify(new Query(isEmptyCriteria), Update.update("email", new Email()), UserDocument.class);
      if (result == null)
        break;
    }

    /**
     * if email not null -> Field will be cast to specific class and updates will create correct entries
     */
    // final Criteria isNotEmptyCriteria = new Criteria().orOperator(Criteria.where("email").ne(""), Criteria.where("email").ne(null));
    // final List<UserDocument> userDocumentList = template.find(new Query(isNotEmptyCriteria), UserDocument.class);

  }

  /**
   * update 6: {@link UserDocument} has changed; 'name' and 'surname' will be concat to 'name'.
   * for each every user document get 'name' and 'surname', concat them, update 'name', remove field surname and update document.
   *
   * @since V7
   */
  @ChangeSet(order = "008", id = "updateUserChangeNameAndSurnameToName", author = "admin")
  public void updateUserChangeNameAndSurnametoName(final MongoTemplate template) {
    final DBCollection userCollection = template.getCollection("user");

    final Iterator<DBObject> cursor = userCollection.find();
    while (cursor.hasNext()) {
      final DBObject current = cursor.next();

      final Object nameObj = current.get("name");
      final Object surnameObj = current.get("surname");
      final String updateName = (nameObj != null ? nameObj.toString() : "") + " " + (surnameObj != null ? surnameObj.toString() : "");

      final BasicDBObject updateQuery = new BasicDBObject();
      updateQuery.append("$set", new BasicDBObject("name", updateName));
      updateQuery.append("$unset", new BasicDBObject("surname", ""));

      final BasicDBObject searchQuery = new BasicDBObject();
      searchQuery.put("_id", current.get("_id"));

      userCollection.update(searchQuery, updateQuery);
    }
  }

  /**
   * update 8: {@Link UserDocument} has changed; add String-Fields 'createdBy' and 'modifiedBy' and initialize with String 'admin'
   *
   * @since V8
   */
  @ChangeSet(order = "009", id = "updateUserAddCreatedByAndLastModifiedBy", author = "admin")
  public void updateUserAddCreatedByAndLastModifiedBy(final MongoTemplate template) {
    final Update update = new Update();
    update.set("createdBy", "admin");
    update.set("modifiedBy", "admin");
    template.updateMulti(null, update, UserDocument.class);
  }
}
