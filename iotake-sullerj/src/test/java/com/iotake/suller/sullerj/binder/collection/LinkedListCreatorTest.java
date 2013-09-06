package com.iotake.suller.sullerj.binder.collection;

import java.util.LinkedList;

import com.iotake.suller.sullerj.binder.collection.CollectionCreator;
import com.iotake.suller.sullerj.binder.collection.LinkedListCreatorFactory.LinkedListCreator;

public class LinkedListCreatorTest extends CollectionCreatorAbstractTest {

  @Override
  protected CollectionCreator createInstance() {
    return new LinkedListCreator();
  }

  @Override
  protected Class<?> getCollectionImplementationType() {
    return LinkedList.class;
  }

}