package com.iotake.solr.client.binder.collection;

import java.util.ArrayList;

import com.iotake.solr.client.binder.collection.ArrayListCreatorFactory.ArrayListCreator;

public class ArrayListCreatorTest extends CollectionCreatorAbstractTest {

  @Override
  protected CollectionCreator createInstance() {
    return new ArrayListCreator();
  }

  @Override
  protected Class<?> getCollectionImplementationType() {
    return ArrayList.class;
  }

}