package com.iotake.suller.sullerj.examples.basic;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

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
    List<Object> storedEntities = rsp.getBeans(Object.class);

    // some assertions
    System.out.println("Result count: " + storedEntities.size());
    for (Object storedEntity : storedEntities) {
      System.out.println("\nRetrieved:\n"
          + ReflectionToStringBuilder.toString(storedEntity,
              ToStringStyle.MULTI_LINE_STYLE));
    }

  }
}
