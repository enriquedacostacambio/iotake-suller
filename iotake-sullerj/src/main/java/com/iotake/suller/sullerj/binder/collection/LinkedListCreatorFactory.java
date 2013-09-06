package com.iotake.suller.sullerj.binder.collection;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class LinkedListCreatorFactory extends AbstractCollectionCreatorFactory
    implements CollectionCreatorFactory {

  public static class LinkedListCreator implements CollectionCreator {

    public LinkedList<?> create(int initialCapacity) {
      if (initialCapacity < 0) {
        throw new IllegalArgumentException("initial capacity must be positive");
      }
      return new LinkedList<Object>();
    }
  }

  public LinkedListCreatorFactory() {
    super(LinkedList.class);
  }

  public CollectionCreator doCreate(Class<?> collectionType,
      Class<?> componentType, Class<?> documentClass, Field field) {
    return new LinkedListCreator();
  }

}
