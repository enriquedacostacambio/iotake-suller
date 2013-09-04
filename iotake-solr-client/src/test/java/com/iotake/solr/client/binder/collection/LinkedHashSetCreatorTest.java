package com.iotake.solr.client.binder.collection;

import java.util.LinkedHashSet;

import com.iotake.solr.client.binder.collection.LinkedHashSetCreatorFactory.LinkedHashSetCreator;

public class LinkedHashSetCreatorTest extends CollectionCreatorAbstractTest {

  @Override
  protected CollectionCreator createInstance() {
    return new LinkedHashSetCreator();
  }

  @Override
  protected Class<?> getCollectionImplementationType() {
    return LinkedHashSet.class;
  }

}