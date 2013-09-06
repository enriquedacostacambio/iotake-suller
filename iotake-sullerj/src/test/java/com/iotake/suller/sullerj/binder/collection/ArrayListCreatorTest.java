package com.iotake.suller.sullerj.binder.collection;

import java.util.ArrayList;

import com.iotake.suller.sullerj.binder.collection.CollectionCreator;
import com.iotake.suller.sullerj.binder.collection.ArrayListCreatorFactory.ArrayListCreator;

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