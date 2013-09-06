package com.iotake.suller.sullerj.binder.collection;

import java.util.LinkedHashSet;

import com.iotake.suller.sullerj.binder.collection.CollectionCreator;
import com.iotake.suller.sullerj.binder.collection.LinkedHashSetCreatorFactory.LinkedHashSetCreator;

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