package com.iotake.suller.sullerj.binder.collection;

import java.util.HashSet;

import com.iotake.suller.sullerj.binder.collection.CollectionCreatorFactory;
import com.iotake.suller.sullerj.binder.collection.HashSetCreatorFactory;

/**
 * Test for {@link HashSetCreatorFactory}
 * 
 * @author enrique.dacostacambio
 * 
 */
public class HashSetCreatorFactoryTest extends
    CollectionCreatorFactoryAbstractTest {

  @Override
  protected CollectionCreatorFactory createInstance() {
    return new HashSetCreatorFactory();
  }

  @Override
  protected Class<?> getCollectionImplementationType() {
    return HashSet.class;
  }

  @Override
  protected Class<?> getCollectionImplementationTypeSubclass() {
    class HashSetExtension<T> extends HashSet<T> {
      private static final long serialVersionUID = 1L;
    }
    return HashSetExtension.class;
  }

}
