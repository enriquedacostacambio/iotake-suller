package com.iotake.suller.sullerj.examples.basic;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.hamcrest.CoreMatchers;

import com.iotake.suller.sullerj.binder.ExtendedDocumentObjectBinder;
import com.iotake.suller.sullerj.binder.ExtendedDocumentObjectBinderBuilder;
import com.iotake.suller.sullerj.binder.annotation.SolrDocument;
import com.iotake.suller.sullerj.binder.annotation.SolrEmbeddable;
import com.iotake.suller.sullerj.binder.annotation.SolrId;
import com.iotake.suller.sullerj.binder.annotation.SolrName;
import com.iotake.suller.sullerj.binder.server.DocumentBinderInjector;

@SolrEmbeddable
class Phone {

  String name;

  String number;

  Phone() {
  }

  public Phone(String name, String number) {
    this.name = name;
    this.number = number;
  }

  @Override
  public String toString() {
    return name + ": " + number;
  }

}

@SolrDocument
class Person {

  @SolrId
  Long id;

  String name;

  String[] aliases;
}

@SolrDocument
class User extends Person {

  @SolrName("username")
  String email;

  List<Phone> phones;

}

public class BasicExample {

  public static void main(String[] args) throws Exception {
    // create server
    String core = "suller_examples";
    String url = "http://localhost:8983/solr/" + core;
    HttpSolrServer server = new HttpSolrServer(url);

    // create binder
    ExtendedDocumentObjectBinder binder = new ExtendedDocumentObjectBinderBuilder()
        .build();

    // inject binder into server
    DocumentBinderInjector.inject(server, binder);

    // create my bean
    User user = new User();
    user.id = System.currentTimeMillis();
    user.name = "John Doe";
    user.aliases = new String[] { "Spooge", "Diesel" };
    user.email = "test@email.com";
    user.phones = Arrays.asList(new Phone("home", "415-123-1234"), new Phone(
        "mobile", "415-321-4321"));

    // store
    System.out.println("\nStoring:\n"
        + ReflectionToStringBuilder.toString(user,
            ToStringStyle.MULTI_LINE_STYLE));
    server.addBean(user);

    // commit
    server.commit();

    // query
    SolrQuery query = new SolrQuery();
    query.setQuery("Person__id:" + user.id);
    QueryResponse rsp = server.query(query);
    List<Object> storedUsers = rsp.getBeans(Object.class);

    // some assertions
    assertEquals(1, storedUsers.size());
    Object firstEntity = storedUsers.get(0);
    System.out.println("\nRetrieved:\n"
        + ReflectionToStringBuilder.toString(firstEntity,
            ToStringStyle.MULTI_LINE_STYLE));
    assertThat(firstEntity, CoreMatchers.instanceOf(User.class));
    User storedUser = (User) firstEntity;
    assertEquals(user.id, storedUser.id);
    assertEquals(user.name, storedUser.name);
    assertArrayEquals(user.aliases, storedUser.aliases);
    assertEquals(user.email, storedUser.email);
    assertEquals(user.phones.size(), storedUser.phones.size());
    for (int i = 0; i < user.phones.size(); i++) {
      assertEquals(user.phones.get(i).name, storedUser.phones.get(i).name);
    }
  }
}
