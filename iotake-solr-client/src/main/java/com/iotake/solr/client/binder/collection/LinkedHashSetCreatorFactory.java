package com.iotake.solr.client.binder.collection;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;

public class LinkedHashSetCreatorFactory extends
    AbstractCollectionCreatorFactory implements CollectionCreatorFactory {

  public static class LinkedHashSetCreator implements CollectionCreator {

    public LinkedHashSet<?> create(int initialCapacity) {
      return new LinkedHashSet<Object>(initialCapacity);
    }
  }

  public LinkedHashSetCreatorFactory() {
    super(LinkedHashSet.class);
  }

  public CollectionCreator doCreate(Class<?> collectionType,
      Class<?> componentType, Class<?> documentClass, Field field) {
    return new LinkedHashSetCreator();
  }

}
