package com.iotake.suller.sullerj.binder.collection;

import java.util.ArrayList;

import com.iotake.suller.sullerj.binder.collection.ArrayListCreatorFactory;
import com.iotake.suller.sullerj.binder.collection.CollectionCreatorFactory;

/**
 * Test for {@link ArrayListCreatorFactory}
 * 
 * @author enrique.dacostacambio
 * 
 */
public class ArrayListCreatorFactoryTest extends
    CollectionCreatorFactoryAbstractTest {

  @Override
  protected CollectionCreatorFactory createInstance() {
    return new ArrayListCreatorFactory();
  }

  @Override
  protected Class<?> getCollectionImplementationType() {
    return ArrayList.class;
  }

  @Override
  protected Class<?> getCollectionImplementationTypeSubclass() {
    class ArrayListExtension<T> extends ArrayList<T> {
      private static final long serialVersionUID = 1L;
    }
    return ArrayListExtension.class;
  }
}
