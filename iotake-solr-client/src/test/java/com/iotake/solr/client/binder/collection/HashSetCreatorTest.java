package com.iotake.solr.client.binder.collection;

import java.util.HashSet;

import com.iotake.solr.client.binder.collection.HashSetCreatorFactory.HashSetCreator;

public class HashSetCreatorTest extends CollectionCreatorAbstractTest {

  @Override
  protected CollectionCreator createInstance() {
    return new HashSetCreator();
  }

  @Override
  protected Class<?> getCollectionImplementationType() {
    return HashSet.class;
  }

}