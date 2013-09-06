package com.iotake.suller.sullerj.binder.collection;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ArrayListCreatorFactory extends AbstractCollectionCreatorFactory
    implements CollectionCreatorFactory {

  public static class ArrayListCreator implements CollectionCreator {

    public ArrayList<?> create(int initialCapacity) {
      return new ArrayList<Object>(initialCapacity);
    }
  }

  public ArrayListCreatorFactory() {
    super(ArrayList.class);
  }

  public CollectionCreator doCreate(Class<?> collectionType,
      Class<?> componentType, Class<?> documentClass, Field field) {
    return new ArrayListCreator();
  }

}
