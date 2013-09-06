package com.iotake.suller.sullerj.binder.collection;

import java.lang.reflect.Field;
import java.util.HashSet;

public class HashSetCreatorFactory extends AbstractCollectionCreatorFactory implements CollectionCreatorFactory {

  public static class HashSetCreator implements CollectionCreator {

    public HashSet<?> create(int initialCapacity) {
      return new HashSet<Object>(initialCapacity);
    }
  }

  public HashSetCreatorFactory() {
    super(HashSet.class);
  }

  public CollectionCreator doCreate(Class<?> collectionType,
      Class<?> componentType, Class<?> documentClass, Field field) {
    return new HashSetCreator();
  }

}
